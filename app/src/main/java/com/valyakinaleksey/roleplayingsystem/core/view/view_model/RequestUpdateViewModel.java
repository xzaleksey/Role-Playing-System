package com.valyakinaleksey.roleplayingsystem.core.view.view_model;

/**
 * Interface to determine empty state for view model
 */
public interface RequestUpdateViewModel {
  boolean isUpdatedRequired();

  void setNeedUpdate(boolean empty);

  void setRestored(boolean restored);

  boolean isRestored();
}
