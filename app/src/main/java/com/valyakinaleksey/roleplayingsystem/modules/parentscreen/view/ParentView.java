package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view;


import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;

public interface ParentView extends LceView<ParentModel> {

    void updateToolbar();

    void getNavigationFragment(Bundle args);
}
