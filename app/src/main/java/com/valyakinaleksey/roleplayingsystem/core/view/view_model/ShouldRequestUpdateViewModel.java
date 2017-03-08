package com.valyakinaleksey.roleplayingsystem.core.view.view_model;

/**
 * Interface to determine empty state for view model
 */
public interface ShouldRequestUpdateViewModel {
  boolean isUpdatedRequired();

  void setNeedUpdate(boolean empty);

  void setRestored(boolean restored);

  boolean isRestored();
}
