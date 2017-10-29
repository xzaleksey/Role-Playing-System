package com.valyakinaleksey.roleplayingsystem.core.di;

public class BaseFragmentModule {
  private String fragmentId;

  public BaseFragmentModule(String fragmentId) {
    this.fragmentId = fragmentId;
  }

  public String getFragmentId() {
    return fragmentId;
  }
}
