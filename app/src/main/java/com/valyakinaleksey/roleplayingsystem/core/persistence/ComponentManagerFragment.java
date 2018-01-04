package com.valyakinaleksey.roleplayingsystem.core.persistence;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.core.persistence.holder.ComponentHelper;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.View;
import com.valyakinaleksey.roleplayingsystem.utils.LogConstants;

/**
 * Base fragment that abstracts away presenter persistence management
 * Presenter can be found in dagger component for fragment, returned by {@code getComponent()}
 * <p>
 * One thing to do is to implement {@code createComponent()} method to build object graph for
 * fragment
 */
public abstract class ComponentManagerFragment<C extends HasPresenter, V extends View>
        extends AbsFragment implements ComponentHelper.OnPresenterReady {

    /**
     * Helper object that contains all the logic to manage object graph state
     */
    private ComponentHelper<C, V> mComponentHelper = new ComponentHelper<>();

    /**
     * Factory object, provides instance of object graph for fragment
     */
    private ComponentCreator<C> mCreator = this::createComponent;
    private ComponentHelper.OnPresenterReady presenterReadyListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mComponentHelper.setOnPresenterReadyListener(this);
        Crashlytics.log(Log.DEBUG, LogConstants.FRAGMENT_LIFECYCLE, "onCreate " + this.getClass().getName());
        mComponentHelper.onCreate(savedInstanceState, getArguments(), mCreator);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupViews(android.view.View view) {
        super.setupViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Crashlytics.log(Log.DEBUG, LogConstants.FRAGMENT_LIFECYCLE, "onResume " + this.getClass().getName());
        mComponentHelper.attachView((V) this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Crashlytics.log(Log.DEBUG, LogConstants.FRAGMENT_LIFECYCLE, "onPause " + this.getClass().getName());
        mComponentHelper.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Crashlytics.log(Log.DEBUG, LogConstants.FRAGMENT_LIFECYCLE, "onSaveInstanceState " + this.getClass().getName());
        mComponentHelper.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Crashlytics.log(Log.DEBUG, LogConstants.FRAGMENT_LIFECYCLE, "onDestroyView " + this.getClass().getName());
        mComponentHelper.onDestroyView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Crashlytics.log(Log.DEBUG, LogConstants.FRAGMENT_LIFECYCLE, "onDestroy " + this.getClass().getName());
        mComponentHelper.onDestroy(this);
    }

    /**
     * Get object graph for the fragment
     *
     * @return object graph
     */
    public C getComponent() {
        return mComponentHelper.getComponent();
    }

    /**
     * Create an instance of object graph for fragment
     *
     * @param fragmentId
     * @return instance of object graph
     */
    protected abstract C createComponent(String fragmentId);

    public void setOnPresenterReadyListener(ComponentHelper.OnPresenterReady listener) {
        presenterReadyListener = listener;
    }

    @Override
    public void onPresenterReady(Presenter presenter) {
        if (presenterReadyListener != null) {
            presenterReadyListener.onPresenterReady(presenter);
        }
    }
}
