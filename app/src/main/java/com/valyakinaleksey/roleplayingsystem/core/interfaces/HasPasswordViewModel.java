package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel;

public interface HasPasswordViewModel {
  PasswordDialogViewModel getPasswordDialogViewModel();

  void setPasswordDialogViewModel(PasswordDialogViewModel passwordDialogViewModel);
}
