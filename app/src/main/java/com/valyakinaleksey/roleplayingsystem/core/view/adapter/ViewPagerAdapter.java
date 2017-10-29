package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.core.utils.SerializableTuple;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogFragment;
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen;
import timber.log.Timber;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

  private List<SerializableTuple<Integer, String>> fragmentTitlePairs;
  private Bundle bundle;

  public ViewPagerAdapter(FragmentManager fm,
      List<SerializableTuple<Integer, String>> fragmentTitlePairs, Bundle bundle) {
    super(fm);
    this.fragmentTitlePairs = fragmentTitlePairs;
    this.bundle = bundle;
  }

  @Override public Fragment getItem(int position) {
    switch (fragmentTitlePairs.get(position).first) {
      case NavigationScreen.GAME_MASTER_EDIT_FRAGMENT:
        return MasterGameEditFragment.newInstance(bundle);
      case NavigationScreen.GAME_MASTER_LOG_FRAGMENT:
        return MasterLogFragment.newInstance(bundle);
      case NavigationScreen.GAME_MAPS_FRAGMENT:
        return MapsFragment.newInstance(bundle);
      case NavigationScreen.GAME_CHARACTERS_FRAGMENT:
        return GamesCharactersFragment.newInstance(bundle);
    }
    return null;
  }

  @Override public CharSequence getPageTitle(int position) {
    return fragmentTitlePairs.get(position).second;
  }

  public void addFragment(Integer type, String title) {
    fragmentTitlePairs.add(new SerializableTuple<>(type, title));
  }

  public void addFragment(SerializableTuple<Integer, String> integerStringTuple) {
    fragmentTitlePairs.add(integerStringTuple);
  }

  @Override public void finishUpdate(ViewGroup container) {
    try {
      super.finishUpdate(container);
    } catch (NullPointerException nullPointerException) {
      Timber.d(nullPointerException);
    }
  }

  @Override public int getCount() {
    return fragmentTitlePairs.size();
  }

  public void clear() {
    fragmentTitlePairs.clear();
  }
}
