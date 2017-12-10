package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model

import android.os.Bundle
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen

data class DrawerInfoModel(val name: String,
                           @NavigationScreen val navId: Int,
                           val bundle: Bundle = Bundle())