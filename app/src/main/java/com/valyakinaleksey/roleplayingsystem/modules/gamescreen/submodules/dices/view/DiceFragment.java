package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di.DiceFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di.DiceModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceProgressState;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.decor.ItemOffsetDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class DiceFragment extends AbsButterLceFragment<DiceFragmentComponent, DiceViewModel, DiceView>
        implements DiceView {

    public static final String TAG = DiceFragment.class.getSimpleName();
    private static final int COLUMN_COUNT = 3;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.recycler_view_dices_collections)
    RecyclerView recyclerViewDiceCollections;

    @BindView(R.id.save)
    View bntSave;

    @BindView(R.id.btn_throw)
    Button bntThrow;

    FlexibleAdapter<IFlexible<?>> collectionAdapter = new FlexibleAdapter<>(new ArrayList<>());
    FlexibleAdapter<IFlexible<?>> dicesAdapter;
    private ItemOffsetDecoration decor;

    public static DiceFragment newInstance(Bundle arguments) {
        DiceFragment gamesDescriptionFragment = new DiceFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DiceFragmentComponent createComponent(String fragmentId) {
        return ((ComponentManagerFragment<ParentGameComponent, ?>) getParentFragment()).getComponent()
                .getDiceFragmentComponent(new DiceModule(fragmentId));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        recyclerViewDiceCollections.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewDiceCollections.setAdapter(collectionAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_COUNT));
        bntSave.setOnClickListener(v -> getComponent().getPresenter().saveCurrentDices());
        decor = new ItemOffsetDecoration(getContext(), R.dimen.dp_8);
    }

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
        recyclerView.removeItemDecoration(decor);
        if (data.getDiceProgressState() == DiceProgressState.IN_PROGRESS) {
            showStateInProgress();
            collectionAdapter.updateDataSet(data.getDiceCollectionsItems(), true);
        } else {
            showStateShowResult();
        }
    }

    private void showStateInProgress() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_COUNT));
        recyclerView.addItemDecoration(decor, 0);

        if (dicesAdapter == null || recyclerView.getAdapter() == null) {
            dicesAdapter = new DiceAdapter(data.getDiceItems(), getComponent().getPresenter());
            recyclerView.setAdapter(dicesAdapter);
        }

    }

    private void showStateShowResult() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_dices;
    }

    @Override
    public void setThrowBtnEnabled(boolean b) {
        bntThrow.setEnabled(b);
    }

    @Override
    public void setSaveDicesEnabled(boolean b) {
        bntSave.setEnabled(b);
    }
}
