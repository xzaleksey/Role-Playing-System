package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsSingleFragmentActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.ParentGameFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

public class GameActivity extends AbsSingleFragmentActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            initNavigate();
        }
    }

    @Override
    protected void fillToolbarItems() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.single_fragment_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNavigate() {
        Bundle extras = getIntent().getExtras();
        Fragment fragment = ParentGameFragment.newInstance(extras);
        setSingleFragment(fragment, ParentGameFragment.TAG);

    }

}
      