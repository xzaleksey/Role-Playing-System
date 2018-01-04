package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.DialogProvider;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen;

import butterknife.BindView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class ParentGameFragment extends AbsButterLceFragment<ParentGameComponent, ParentGameModel, ParentView> implements ParentView, DialogProvider {
    public static final String TAG = ParentGameFragment.class.getSimpleName();
    public static final String DELETE_GAME = "delete_game";
    public static final String FINISH_GAME = "finish_game";
    public static final String LEAVE_GAME = "leave_game";

    @BindView(R.id.BottomNavigation)
    BottomNavigation bottomNavigation;

    private String gameId;
    private Bundle arguments;

    public static ParentGameFragment newInstance(Bundle arguments) {
        ParentGameFragment gamesDescriptionFragment = new ParentGameFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ParentGameComponent createComponent(String fragmentId) {
        return ((ComponentManagerFragment<ParentFragmentComponent, ?>) getParentFragment()).getComponent()
                .getParentGameComponent(new ParentGameModule(gameId, fragmentId));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        arguments = savedInstanceState == null ? getArguments() : savedInstanceState;
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        gameId = gameModel.getId();
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    protected void setupViews(View view) {
        super.setupViews(view);
        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(int id, int i1, boolean b) {
                switch (id) {
                    case R.id.action_characters:
                        data.setNavigationTag(NavigationScreen.GAME_CHARACTERS_FRAGMENT);
                        break;
                }
                navigate();
            }

            @Override
            public void onMenuItemReselect(int i, int i1, boolean b) {

            }
        });
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void showContent() {
        super.showContent();
        preFillModel(data);
        navigate();
    }

    private void navigate() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fragment_container);
        int navigationTag = data.getNavigationTag();
        Class<? extends Fragment> currentFragmentClass = getFragmentClassByTag(navigationTag);
        if (fragment == null || !fragment.getClass().equals(currentFragmentClass)) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, getFragmentByTag(navigationTag))
                    .commit();
        }
    }

    private Class<? extends Fragment> getFragmentClassByTag(@NavigationScreen int navigationTag) {
        switch (navigationTag) {
            case NavigationScreen.GAME_CHARACTERS_FRAGMENT:
                return GamesCharactersFragment.class;
        }
        throw new IllegalArgumentException("unsupported navigationTag");
    }

    private Fragment getFragmentByTag(@NavigationScreen int navigationTag) {
        switch (navigationTag) {
            case NavigationScreen.GAME_CHARACTERS_FRAGMENT:
                return GamesCharactersFragment.newInstance(arguments);
        }
        throw new IllegalArgumentException("unsupported navigationTag");
    }


    @Override
    public void invalidateOptionsMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void preFillModel(ParentGameModel data) {
        super.preFillModel(data);
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_game;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public Dialog getDialog(String tag) {
        switch (tag) {
            case DELETE_GAME:
                return new MaterialDialog.Builder(getContext()).title(R.string.delete_game)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onPositive((dialog, which) -> getComponent().getPresenter().deleteGame())
                        .build();
            case FINISH_GAME:
                return new MaterialDialog.Builder(getContext()).title(R.string.finish_game)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onPositive((dialog, which) -> getComponent().getPresenter().finishGame())
                        .build();
            case LEAVE_GAME:
                return new MaterialDialog.Builder(getContext()).title(R.string.leave_game)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onPositive((dialog, which) -> getComponent().getPresenter().leaveGame())
                        .build();
        }
        throw new IllegalArgumentException("Unsupported tag");
    }
}
