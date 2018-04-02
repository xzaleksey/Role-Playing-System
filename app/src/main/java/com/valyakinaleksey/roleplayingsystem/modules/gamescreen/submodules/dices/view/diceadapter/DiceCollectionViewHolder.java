package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.SingleDiceCollection;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DiceCollectionViewHolder extends FlexibleViewHolder {
    @BindView(R.id.iv_main_dice)
    ImageView ivMainDice;

    @BindView(R.id.iv_plus)
    ImageView ivPlus;

    @BindView(R.id.iv_minus)
    ImageView ivMinus;

    @BindView(R.id.iv_dice_secondary)
    ImageView ivDiceSecondary;

    @BindView(R.id.dices_actions_container)
    View dicesActionContainer;

    @BindView(R.id.tv_dice_count)
    TextView tvDiceCount;


    public DiceCollectionViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DiceCollectionViewModel diceCollectionViewModel, DicePresenter dicePresenter) {
        SingleDiceCollection diceCollection = diceCollectionViewModel.getDiceCollection();
        updateView(diceCollectionViewModel, dicePresenter);
        ivPlus.setOnClickListener(v -> {
            diceCollection.addDices(1);
            update(diceCollectionViewModel, dicePresenter);
        });

        ivMinus.setOnClickListener(v -> {
            diceCollection.removeDices(1);
            update(diceCollectionViewModel, dicePresenter);
        });

    }

    private void update(DiceCollectionViewModel diceCollectionViewModel, DicePresenter dicePresenter) {
        updateView(diceCollectionViewModel, dicePresenter);
        dicePresenter.onDicesChanged();
    }

    private void updateView(DiceCollectionViewModel diceCollectionViewModel, DicePresenter dicePresenter) {
        SingleDiceCollection diceCollection = diceCollectionViewModel.getDiceCollection();
        int mainDiceResId = diceCollectionViewModel.getMainDiceResId();

        if (diceCollection.getDiceCount() == 0) {
            ivMainDice.setImageResource(mainDiceResId);
            ivMainDice.setVisibility(View.VISIBLE);
            itemView.setSelected(false);
            ivMinus.setVisibility(View.GONE);
            ivPlus.setVisibility(View.GONE);
            tvDiceCount.setVisibility(View.GONE);
            ivDiceSecondary.setVisibility(View.GONE);
            dicesActionContainer.setOnClickListener(v -> {
                diceCollection.addDices(1);
                update(diceCollectionViewModel, dicePresenter);
            });
        } else {
            ivDiceSecondary.setImageResource(mainDiceResId);
            ivMainDice.setVisibility(View.GONE);
            itemView.setSelected(true);
            ivMinus.setVisibility(View.VISIBLE);
            ivPlus.setVisibility(View.VISIBLE);
            ivDiceSecondary.setVisibility(View.VISIBLE);
            tvDiceCount.setText(String.valueOf(diceCollection.getDiceCount()));
            tvDiceCount.setVisibility(View.VISIBLE);
            dicesActionContainer.setOnClickListener(null);
        }
    }
}
