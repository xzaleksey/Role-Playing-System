package com.valyakinaleksey.roleplayingsystem.core.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.ComponentLifecycle;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasComponent;
import com.valyakinaleksey.roleplayingsystem.core.persistence.holder.ComponentHolder;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;

import java.util.UUID;

import javax.inject.Inject;

import timber.log.Timber;

public abstract class BaseInjectableActivity<C, P extends Presenter<V>, V extends View> extends AbsSingleFragmentActivity implements HasComponent<C>, ComponentLifecycle<C>, View {

    public static final String KEY = "key";
    private String key;
    private C mComponent;

    @Inject
    protected P presenter;

    @NonNull
    @Override
    public C getComponent() {
        return mComponent;
    }

    @Override
    public void onComponentCreated(@NonNull final C component) {
    }

    @Override
    public void onPostComponentCreated() {
    }

    /**
     * Creates component instance.
     */
    protected abstract C createComponent();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        // Deliberately create the component before the super() call so that it's initialized in
        // Fragment's onAttach()!
        if (savedInstanceState == null) {
            saveComponent();
        } else {
            key = savedInstanceState.getString(KEY);
            mComponent = ComponentHolder.getInstance().getComponent(key);
        }

        if (mComponent == null) {
            saveComponent();
            Timber.d("Component is null");
//            throw new NullPointerException("Component must not be null");
        }
        super.onCreate(savedInstanceState);
        onComponentCreated(mComponent);
        onPostComponentCreated();
    }

    private void saveComponent() {
        mComponent = createComponent();
        key = UUID.randomUUID().toString();
        ComponentHolder.getInstance().putComponent(key, mComponent);
    }

    //Временный костыль
    public void clearScope() {
        mComponent = createComponent();
        if (mComponent == null) {
            throw new NullPointerException("Component must not be null");
        }
        onComponentCreated(mComponent);
        onPostComponentCreated();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY, key);
    }

    @Override
    public void finish() {
        ComponentHolder.getInstance().remove(key);
        Timber.d("finish" + this);
        super.finish();
    }
}
      