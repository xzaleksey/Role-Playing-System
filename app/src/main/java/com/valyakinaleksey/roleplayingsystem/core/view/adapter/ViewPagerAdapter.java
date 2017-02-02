package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.app.FragmentStatePagerAdapter;
import com.valyakinaleksey.roleplayingsystem.core.utils.Tuple;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogFragment;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

  private List<Tuple<Integer, String>> fragmentTitlePairs;
  private Bundle bundle;

  public ViewPagerAdapter(FragmentManager fm, List<Tuple<Integer, String>> fragmentTitlePairs,
      Bundle bundle) {
    super(fm);
    this.fragmentTitlePairs = fragmentTitlePairs;
    this.bundle = bundle;
  }

  @Override public Fragment getItem(int position) {
    switch (fragmentTitlePairs.get(position).first) {
      case NavigationUtils.GAME_MASTER_EDIT_FRAGMENT:
        return MasterGameEditFragment.newInstance(bundle);
      case NavigationUtils.GAME_MASTER_LOG_FRAGMENT:
        return MasterLogFragment.newInstance(bundle);
      case NavigationUtils.GAME_MAPS_FRAGMENT:
        return MapsFragment.newInstance(bundle);
    }
    return null;
  }

  @Override public CharSequence getPageTitle(int position) {
    return fragmentTitlePairs.get(position).second;
  }

  public void addFragment(Integer type, String title) {
    fragmentTitlePairs.add(new Tuple<>(type, title));
  }

  @Override public int getCount() {
    return fragmentTitlePairs.size();
  }
}
