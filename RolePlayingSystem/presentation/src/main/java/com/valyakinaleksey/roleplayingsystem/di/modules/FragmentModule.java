package com.valyakinaleksey.roleplayingsystem.di.modules;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.valyakinaleksey.roleplayingsystem.di.base.PerFragment;

import dagger.Module;
import dagger.Provides;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

@Module
public class FragmentModule {

    private final Fragment mFragment;

    public FragmentModule(@NonNull final Fragment fragment) {
        mFragment = checkNotNull(fragment);
    }

    @Provides
    @PerFragment
    Fragment provideFragment() {
        return mFragment;
    }
}
      