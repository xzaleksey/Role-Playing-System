package com.valyakinaleksey.roleplayingsystem.core.ui;

import android.os.Bundle;

import butterknife.Unbinder;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;

import butterknife.ButterKnife;

/**
 * {link AbsActivity} with ButterKnife support
 * Move outside core because one can not make lib with butterknife
 */
public abstract class AbsButterActivity extends AbsActivity {

  private Unbinder unbinder;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    unbinder = ButterKnife.bind(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }
}
