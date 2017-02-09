package com.valyakinaleksey.roleplayingsystem.core.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.firebase.listener.AuthStateListener;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.OnToolbarChangedListener;
import java.util.ArrayList;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class AbsActivity extends AppCompatActivity {
  protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  protected FirebaseAuth.AuthStateListener mAuthListener = new AuthStateListener(this);
  private Toolbar toolbar;
  private List<OnToolbarChangedListener> onToolbarChangedListeners;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    onToolbarChangedListeners = new ArrayList<>();
    setContentView();
    setupViews();
  }

  public void setContentView() {
    super.setContentView(getLayoutId());
  }

  protected void setupViews() {
    setupToolbar();
  }

  public void setupToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      fillToolbarItems();
    }
  }

  public void setToolbarTitle(String title) {
    ActionBar supportActionBar = getSupportActionBar();
    if (onToolbarChangedListeners.isEmpty() && supportActionBar != null) {
      supportActionBar.setTitle(title);
    } else {
      for (OnToolbarChangedListener onToolbarChangedListener : onToolbarChangedListeners) {
        onToolbarChangedListener.onTitleChanged(title);
      }
    }
  }

  protected void fillToolbarItems() {

  }

  protected abstract int getLayoutId();

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public void addOnToolBarChangedListener(OnToolbarChangedListener onToolbarChangedListener) {
    if (!onToolbarChangedListeners.contains(onToolbarChangedListener)) {
      onToolbarChangedListeners.add(onToolbarChangedListener);
    }
  }

  public void removeOnToolBarChangedListener(OnToolbarChangedListener onToolbarChangedListener) {
    onToolbarChangedListeners.remove(onToolbarChangedListener);
  }

  @Override protected void onStart() {
    super.onStart();
    firebaseAuth.addAuthStateListener(mAuthListener);
  }

  @Override protected void onStop() {
    firebaseAuth.removeAuthStateListener(mAuthListener);
    super.onStop();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    onToolbarChangedListeners.clear();
  }

  public Toolbar getToolbar() {
    return toolbar;
  }
}
