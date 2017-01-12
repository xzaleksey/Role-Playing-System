package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.DaggerMasterComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.MasterComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ChildGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragmentComponent;

public class ParentGameFragment extends AbsButterLceFragment<MasterComponent, ParentGameModel, ParentView> implements ParentView {

    public static final String TAG = ParentGameFragment.class.getSimpleName();

    public static ParentGameFragment newInstance(Bundle arguments) {
        ParentGameFragment gamesDescriptionFragment = new ParentGameFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MasterComponent createComponent() {
        return DaggerMasterComponent
                .builder()
                .parentFragmentComponent(((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent())
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showContent() {
        super.showContent();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_container;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void navigate() {
        String navigationTag = data.getNavigationTag();
        ComponentManagerFragment fragment = null;
//        Bundle arguments = new Bundle();
//        arguments.putParcelable(GameModel.KEY, data.getGameModel());
        if (ParentGameModel.USER_SCREEN.equals(navigationTag)) {
            showSnackbarString("user");
        } else if (ParentGameModel.MASTER_SCREEN.equals(navigationTag)) {
            showSnackbarString("master");
        }
//        getChildFragmentManager()
//                .beginTransaction()
//                .replace(R.id.inner_fragment_container, fragment)
//                .commit();
    }
}
