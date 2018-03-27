package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di.DiceFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di.DiceModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;

import java.util.ArrayList;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class DiceFragment extends AbsButterLceFragment<DiceFragmentComponent, DiceViewModel, DiceView>
        implements DiceView {

    public static final String TAG = DiceFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    FlexibleAdapter<IFlexible<?>> adapter = new FlexibleAdapter<>(new ArrayList<>());

    public static DiceFragment newInstance(Bundle arguments) {
        DiceFragment gamesDescriptionFragment = new DiceFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DiceFragmentComponent createComponent(
            String fragmentId) {
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
        adapter.updateDataSet(data.getDiceCollectionsItems(), true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_dices;
    }
}
