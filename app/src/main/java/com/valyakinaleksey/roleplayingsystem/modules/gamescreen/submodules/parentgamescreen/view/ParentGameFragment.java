package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.DialogProvider;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;

public class ParentGameFragment extends AbsButterLceFragment<ParentGameComponent, ParentGameModel, ParentView>
        implements ParentView, DialogProvider {
    public static final String TAG = ParentGameFragment.class.getSimpleName();
    public static final String DELETE_GAME = "delete_game";
    public static final String FINISH_GAME = "finish_game";
    public static final String LEAVE_GAME = "leave_game";
    public static final int FINISH_GAME_ID = 1;
    public static final int OPEN_GAME_ID = 2;

    private String gameId;

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
        Bundle arguments = savedInstanceState == null ? getArguments() : savedInstanceState;
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        gameId = gameModel.getId();
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showContent() {
        super.showContent();
        preFillModel(data);
        invalidateOptionsMenu();
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
        return null;
    }
}
