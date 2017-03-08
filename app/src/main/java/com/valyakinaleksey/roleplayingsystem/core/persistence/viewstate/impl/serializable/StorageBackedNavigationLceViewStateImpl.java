package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.AbsSelfRestorableNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.PendingStateChange;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action1;
import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action2;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.ShouldRequestUpdateViewModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Implementation of ViewState for LceView with view-based navigation and ability to auto-save
 * itself
 * in {@link ViewStateStorage}.
 * For particular cases of saving instance use {@link StorageBackedNavigationLceViewStateImpl#save()}
 * To restore actual state from storage use {@link StorageBackedNavigationLceViewStateImpl#restore()}
 * <p>
 * When ViewState is no longer needed clean the storage with {@link StorageBackedNavigationLceViewStateImpl#clean()}:
 * - onDestroy() for example
 */
public class StorageBackedNavigationLceViewStateImpl<D extends Serializable & ShouldRequestUpdateViewModel, V extends LceView<D>>
    extends AbsSelfRestorableNavigationLceViewStateImpl<D, V, Serializable> {

  private final ViewStateStorage storage;

  public StorageBackedNavigationLceViewStateImpl(ViewStateStorage storage) {
    this.storage = storage;
  }

  @Override public void setStateShowContent() {
    super.setStateShowContent();
    save();
  }

  @Override public void setStateShowError(BaseError error, boolean isShown) {
    super.setStateShowError(error, isShown);
    save();
  }

  @Override public <S extends Serializable> void addToPending(Action2<V, S> op, S data) {
    SerializablePendingStateChangeWithDataImpl<V, S> dto =
        new SerializablePendingStateChangeWithDataImpl<>();
    dto.setData(data);
    dto.setOperation(op::invoke);
    addPendingStateChange(dto);
  }

  @Override public void addToPending(Action1<V> op) {
    SerializablePendingStateChangeImpl<V> dto = new SerializablePendingStateChangeImpl<>();
    dto.setOperation(op::apply);
    addPendingStateChange(dto);
  }

  protected void onPendingStateChangesListAdded() {
    save();
  }

  @Override public void save() {
    Observable.fromCallable(() -> {
      storage.save(objectOutputStream -> {
        try {
          save(objectOutputStream);
        } catch (IOException e) {
          Timber.d(e);
        }
      });
      return null;
    }).subscribeOn(Schedulers.io()).subscribe();
  }

  @Override
  /**
   * Executes synchronously now for ease of use
   * It is supposed to be executed when app is recreated after low-memory
   */ public void restore() {
    Observable.fromCallable(() -> {
      storage.restore(objectInputStream -> {
        try {
          restoreFromBackUp(objectInputStream);
        } catch (IOException | ClassNotFoundException e) {
          Timber.d(e);
        }
      });
      return null;
    }).subscribe();
  }

  @Override public void clean() {
    storage.cleanUp();
  }

  private void save(ObjectOutputStream stream) throws IOException {
    stream.writeObject(currentState);
    stream.writeObject(error);
    stream.writeObject(data);
    stream.writeObject(pendingStateChangesList);
  }

  public void restoreFromBackUp(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    currentState = (int) stream.readObject();
    error = (BaseError) stream.readObject();
    data = (D) stream.readObject();
    List<PendingStateChange<V>> pendingStateChangeList =
        (List<PendingStateChange<V>>) stream.readObject();
    setPendingStateChangeList(pendingStateChangeList);
  }
}
