package com.valyakinaleksey.roleplayingsystem.di.app;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import com.valyakinaleksey.roleplayingsystem.utils.SharedPreferencesHelper;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

public interface GlobalComponent extends UseCaseProvider {
    Context context();

    PathManager pathManager();

    FirebaseAuth firebaseAuth();

    SharedPreferencesHelper sharedPrefencesPreferencesHelper();

    SimpleCrypto getSimpleCrypto();

    SectionsAdapter getSectionsAdapter();
}
      