package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsSingleFragmentActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListFragment;

import timber.log.Timber;

public class GameActivity extends AbsSingleFragmentActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            CheckUserJoinedGameInteractor checkUserJoinedGameInteractor = RpsApp.getAppComponent(this).getCheckUserJoinedGameInteractor();
            Bundle extras = getIntent().getExtras();
            GameModel gameModel = extras.getParcelable(GameModel.KEY);
            checkUserJoinedGameInteractor
                    .checkUserInGame(FirebaseAuth.getInstance().getCurrentUser().getUid(),gameModel )
                    .compose(RxTransformers.applySchedulers())
                    .subscribe(aBoolean -> {
                        Fragment fragment = GamesDescriptionFragment.newInstance(extras);
                        if (!aBoolean){
                            setSingleFragment(fragment, GamesListFragment.TAG);
                        } else {
                            Toast.makeText(this,"user is in game", Toast.LENGTH_SHORT).show();
                        }

                    });
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

}
      