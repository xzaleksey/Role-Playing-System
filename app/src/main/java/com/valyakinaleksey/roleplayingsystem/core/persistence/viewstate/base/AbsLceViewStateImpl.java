package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorType;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorTypes;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import timber.log.Timber;

/**
 * Base ViewState implementation for {@link LceView}
 *
 * @param <D> type of data view operates on
 * @param <V> type of view
 */
public abstract class AbsLceViewStateImpl<D extends RequestUpdateViewModel, V extends LceView<D>>
    implements LceViewState<D, V> {

  protected int currentState;
  protected BaseError error;
  protected D data;

  public AbsLceViewStateImpl() {
    currentState = STATE_UNINITIALIZED;
  }

  public void setStateShowLoading() {
    currentState = STATE_SHOW_LOADING;
    error = null;
  }

  public void setStateHideLoading() {
    if (currentState != STATE_SHOW_ERROR) {
      currentState = STATE_SHOW_CONTENT;
      error = null;
    }
  }

  public void setStateShowContent() {
    currentState = STATE_SHOW_CONTENT;
    error = null;
  }

  @Override public void setStateShowError(BaseError storeError, boolean isShown) {
    boolean isOneShot = isOneShot(storeError);
    if (isOneShot && isShown) {
      return;
    }

    error = storeError;
    currentState = STATE_SHOW_ERROR;
  }

  public void setData(D data) {
    this.data = data;
  }

  public D getData() {
    return data;
  }

  @Override public void apply(V view) {
    restoreModel(view);

    switch (currentState) {
      case STATE_SHOW_LOADING:
        view.showLoading();
        break;
      case STATE_SHOW_ERROR:
        view.showError(error);
        cleanIfOneShot();
        break;
      default:
        break;
    }
  }

  private void cleanIfOneShot() {
    if (isOneShot(error)) {
      error = null;
      currentState = STATE_SHOW_CONTENT;
    }
  }

  private void restoreModel(V view) {
    boolean shouldMakeRequest = false;
    if (data != null) {
      Timber.d(data.toString());
      if (!data.isUpdatedRequired()) {
        view.setData(data);
        view.hideLoading();
        view.showContent();
      } else {
        shouldMakeRequest = true;
      }
       if (data.isRestored()) {
        data.setRestored(false);
        shouldMakeRequest = false;
        view.loadData();
      }
    } else {
      shouldMakeRequest = true;
    }
    if (shouldMakeRequest) view.loadData();
  }

  // TODO generify things there will be new error types
  private boolean isOneShot(BaseError error) {
    boolean hasAnnotation = false;
    try {
      ErrorType type =
          error.getDeclaringClass().getField(error.name()).getAnnotation(ErrorType.class);
      if (type != null) {
        ErrorTypes type1 = type.type();
        hasAnnotation =
            type1.equals(ErrorTypes.ONE_SHOT) || (type1.equals(ErrorTypes.ONE_SHOT_OR_DEFAULT) && (
                data != null
                    && !data.isUpdatedRequired()));
      }
    } catch (NoSuchFieldException e) {
      // assume false
    }
    return hasAnnotation;
  }
}
