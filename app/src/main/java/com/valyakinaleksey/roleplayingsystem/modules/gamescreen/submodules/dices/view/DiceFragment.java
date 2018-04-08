package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di.DiceFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.di.DiceModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceProgressState;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.decor.ItemOffsetDecoration;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.decor.LinearOffsetItemDecortation;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
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
    @BindView(R.id.no_dices_container)
    View noDicesContainer;
    @BindView(R.id.dices_collections_container)
    View dicesCollectionContainer;
    @BindView(R.id.saved_dices_count)
    TextView tvSavedDicesCount;
    @BindView(R.id.save)
    View bntSave;
    @BindView(R.id.btn_throw)
    Button btnThrow;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.progress_bottom_container)
    View progressBottomContainer;

    @BindView(R.id.label_container)
    View progressLabelContainer;
    @BindView(R.id.progress_state_container)
    View progressStateContainer;
    @BindView(R.id.result_state_container)
    View resultStateContainer;
    @BindView(R.id.top_container)
    View topContainer;
    @BindView(R.id.iv_result_back)
    View ivResultBack;
    @BindView(R.id.recycler_view_card_view)
    CardView recyclerViewCardView;

    @BindDimen(R.dimen.dp_8)
    int dp8;
    @BindDimen(R.dimen.dp_2)
    int dp2;
    @BindDimen(R.dimen.game_characters_top_element_height)
    int progressTopElementHeight;
    @BindDimen(R.dimen.game_characters_top_element_height_result)
    int resultTopElementHeight;
    @BindColor(android.R.color.white)
    int colorWhite;
    @BindColor(R.color.backgroundColor)
    int backGroundColor;

    FlexibleAdapter<IFlexible<?>> collectionAdapter;
    FlexibleAdapter<IFlexible<?>> dicesAdapter;
    private ItemOffsetDecoration decor;
    private RecyclerView.SmoothScroller smoothScroller;

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
        recyclerViewDiceCollections.addItemDecoration(new LinearOffsetItemDecortation(LinearOffsetItemDecortation.HORIZONTAL, dp8));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_COUNT));
        bntSave.setOnClickListener(v -> getComponent().getPresenter().saveCurrentDices());
        decor = new ItemOffsetDecoration(getContext(), R.dimen.dp_8);
        bntSave.setOnClickListener(v -> getComponent().getPresenter().saveCurrentDices());
        smoothScroller = new LinearSmoothScroller(getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        btnThrow.setOnClickListener(v -> getComponent().getPresenter().throwDices());
        btnCancel.setOnClickListener(v -> getComponent().getPresenter().resetDices());
        ivResultBack.setOnClickListener(v -> getComponent().getPresenter().switchBackToProgress());
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
        } else {
            showStateShowResult();
        }

        if (dicesAdapter == null || recyclerView.getAdapter() == null) {
            dicesAdapter = new DiceAdapter(data.getDiceItems(), getComponent().getPresenter());
            recyclerView.setAdapter(dicesAdapter);
        } else {
            updateDices(false);
        }
    }

    private void showStateInProgress() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_COUNT));
        recyclerView.addItemDecoration(decor, 0);
        progressLabelContainer.setVisibility(View.VISIBLE);
        progressBottomContainer.setVisibility(View.VISIBLE);
        progressStateContainer.setVisibility(View.VISIBLE);
        resultStateContainer.setVisibility(View.GONE);
        topContainer.getLayoutParams().height = progressTopElementHeight;

        if (collectionAdapter == null || recyclerViewDiceCollections.getAdapter() == null) {
            collectionAdapter = new DiceAdapter(data.getDiceCollectionsItems(), getComponent().getPresenter());
            recyclerViewDiceCollections.setAdapter(collectionAdapter);
        }
        recyclerViewCardView.setCardBackgroundColor(colorWhite);
        recyclerViewCardView.setRadius(dp2);
        recyclerViewCardView.setCardElevation(dp2);
        ((ViewGroup.MarginLayoutParams) recyclerViewCardView.getLayoutParams()).setMargins(dp8, dp8, dp8, dp8);
    }

    private void showStateShowResult() {
        progressLabelContainer.setVisibility(View.GONE);
        progressBottomContainer.setVisibility(View.GONE);
        progressStateContainer.setVisibility(View.GONE);
        resultStateContainer.setVisibility(View.VISIBLE);
        topContainer.getLayoutParams().height = resultTopElementHeight;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCardView.setCardBackgroundColor(backGroundColor);
        recyclerViewCardView.setRadius(0);
        recyclerViewCardView.setCardElevation(0);
        ((ViewGroup.MarginLayoutParams) recyclerViewCardView.getLayoutParams()).setMargins(0, 0, 0, 0);
    }

    @Override
    public void updateDices(boolean animate) {
        dicesAdapter.updateDataSet(data.getDiceItems(), animate);
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
    public void updateActionsThrowEnabled(boolean b) {
        btnThrow.setEnabled(b);
        btnCancel.setEnabled(b);
    }

    @Override
    public void setSaveDicesEnabled(boolean b) {
        bntSave.setEnabled(b);
    }

    @Override
    public void updateDiceCollections(boolean animate) {
        List<DiceCollection> savedDiceCollections = data.getSavedDiceCollections();
        List<IFlexible<?>> diceCollectionsItems = data.getDiceCollectionsItems();

        if (savedDiceCollections.isEmpty()) {
            dicesCollectionContainer.setVisibility(View.GONE);
            noDicesContainer.setVisibility(View.VISIBLE);
        } else {
            String collectionCount = getString(R.string.saved_collections) + " (" + savedDiceCollections.size() + ")";
            tvSavedDicesCount.setText(collectionCount);
            dicesCollectionContainer.setVisibility(View.VISIBLE);
            noDicesContainer.setVisibility(View.GONE);
            collectionAdapter.updateDataSet(diceCollectionsItems, animate);
        }
    }

    @Override
    public void scrollDiceCollectionsToStart() {
        recyclerViewDiceCollections.post(() -> {
            if (recyclerViewDiceCollections != null) {
                if (recyclerViewDiceCollections.getAdapter().getItemCount() > 0) {
                    smoothScroller.setTargetPosition(0);
                    recyclerViewDiceCollections.getLayoutManager().startSmoothScroll(smoothScroller);
                }
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        boolean handleBackPressed = data != null && data.getDiceProgressState() == DiceProgressState.SHOW_RESULT;
        if (handleBackPressed) {
            getComponent().getPresenter().switchBackToProgress();
        }
        return handleBackPressed;
    }
}
