package com.valyakinaleksey.roleplayingsystem.di.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.valyakinaleksey.roleplayingsystem.di.App;

import java.util.UUID;

/**
 * Generic base class for injectable activities.
 * <p>
 * {@link #onComponentCreated(Object)} will be called in {@link #onCreate(Bundle)}.
 *
 * @see ComponentLifecycle
 */
public abstract class AbstractInjectableActivity<C> extends AppCompatActivity
        implements HasComponent<C>, ComponentLifecycle<C> {
    public static final String KEY = "key";
    private final Logger logger = LoggerManager.getLogger();
    private String key;
    private C mComponent;

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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // Deliberately create the component before the super() call so that it's initialized in
        // Fragment's onAttach()!
        if (savedInstanceState == null) {
            mComponent = createComponent();
            key = UUID.randomUUID().toString();
            App.saveComponent(key, mComponent);
        } else {
            key = savedInstanceState.getString(KEY);
            mComponent = (C) App.restoreComponent(key);
        }

        if (mComponent == null) {
            throw new NullPointerException("Component must not be null");
        }
        super.onCreate(savedInstanceState);


        onComponentCreated(mComponent);
        onPostComponentCreated();
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
        outState.putString(KEY, key);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void finish() {
        App.removeComponent(key);
        logger.d("finish" + this);
        super.finish();
    }
}
