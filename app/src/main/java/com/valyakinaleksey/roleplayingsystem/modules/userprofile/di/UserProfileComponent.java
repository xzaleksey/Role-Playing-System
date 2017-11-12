package com.valyakinaleksey.roleplayingsystem.modules.userprofile.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.UserProfileFragment;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter.UserProfilePresenter;
import dagger.Subcomponent;

@Subcomponent(modules = UserProfileModule.class) @PerFragmentScope
public interface UserProfileComponent extends HasPresenter<UserProfilePresenter> {

  void inject(UserProfileFragment gamesListFragment);
}
