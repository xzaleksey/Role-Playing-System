package com.valyakinaleksey.roleplayingsystem.core.persistence.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentCreator;
import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.view.View;

import java.util.UUID;

/**
 * Helper class that contains all the logic to manage object graph state during lifecycle of a
 * fragment
 */
public class ComponentHelper<C extends HasPresenter, V extends View> {

    /**
     * Screen id for object graph
     */
    private String mFragmentId;

    /**
     * Flag - was onSaveInstanceState(...) called during fragment lifecycle
     * <p>
     * Used to determine if the host fragment is about to be destroyed (so presenter in this case is
     * also destroyed)
     */
    private boolean mOnSaveInstanceCalled;

    /**
     * Object graph
     */
    private C component;

    private OnPresenterReady onPresenterReadyListener;

    /**
     * Method to be called in onCreate(..) of fragment
     *
     * @param savedInstanceState previously saved state
     * @param arguments          input data
     * @param creator            factory object to provide object graph instance
     */
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable Bundle arguments,
                         ComponentCreator<C> creator) {
        if (savedInstanceState == null) {
            mFragmentId = UUID.randomUUID().toString();
        } else {
            mFragmentId = savedInstanceState.getString("identifier");
            mOnSaveInstanceCalled = false;
        }
        component = ComponentHolder.getInstance().getComponent(mFragmentId);
        if (component == null) {
            component = creator.create(mFragmentId);
            ComponentHolder.getInstance().putComponent(mFragmentId, component);
            component.getPresenter().onCreate(arguments, savedInstanceState);
        }
        if (onPresenterReadyListener != null) {
            onPresenterReadyListener.onPresenterReady(component.getPresenter());
        }
    }

    /**
     * Attach view to presenter
     *
     * @param view view
     */
    public void attachView(@NonNull V view) {
        if (component == null) {
            return;
        }
        component.getPresenter().attachView(view);
    }

    /**
     * Detach view from presenter
     */
    public void detachView() {
        if (component == null) {
            return;
        }
        component.getPresenter().detachView();
    }

    /**
     * Method to be called in onSaveInstanceState(..) of fragment
     *
     * @param bundle bundle
     */
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putString("identifier", mFragmentId);
        if (component != null) {
            component.getPresenter().onSaveInstanceState(bundle);
            mOnSaveInstanceCalled = true;
        }
    }

    /**
     * Method to be called in onDestroyView(..) of fragment
     *
     * @param fragment fragment
     */
    public void onDestroyView(@NonNull Fragment fragment) {
        if (component == null) {
            return;
        }
        if (fragment.getActivity() != null && fragment.getActivity().isFinishing()) {
            removeComponent();
        }
    }

    /**
     * Method to be called in onDestroy(..) of fragment
     *
     * @param fragment fragment
     */
    public void onDestroy(@NonNull Fragment fragment) {
        if (component == null) {
            return;
        }
        if (fragment.getActivity().isFinishing() || !mOnSaveInstanceCalled) {
            component.getPresenter().onDestroy();
            removeComponent();
        }
    }

    /**
     * Remove instance of object graph
     */
    private void removeComponent() {
        ComponentHolder.getInstance().remove(mFragmentId);
    }

    /**
     * Get object graph
     *
     * @return object graph
     */
    public C getComponent() {
        return component;
    }

    public <C extends HasPresenter, V extends View> void setOnPresenterReadyListener(
            OnPresenterReady onPresenterReadyListener) {
        this.onPresenterReadyListener = onPresenterReadyListener;
    }

    public interface OnPresenterReady {
        void onPresenterReady(Presenter presenter);
    }
}
