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

public class DiceSingleCollectionViewHolder extends FlexibleViewHolder {
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


    public DiceSingleCollectionViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DiceSingleCollectionViewModel diceSingleCollectionViewModel, DicePresenter dicePresenter) {
        SingleDiceCollection diceCollection = diceSingleCollectionViewModel.getDiceCollection();
        updateView(diceSingleCollectionViewModel, dicePresenter);
        ivPlus.setOnClickListener(v -> {
            diceCollection.addDices(1);
            update(diceSingleCollectionViewModel, dicePresenter);
        });

        ivMinus.setOnClickListener(v -> {
            diceCollection.removeDices(1);
            update(diceSingleCollectionViewModel, dicePresenter);
        });

    }

    private void update(DiceSingleCollectionViewModel diceSingleCollectionViewModel, DicePresenter dicePresenter) {
        updateView(diceSingleCollectionViewModel, dicePresenter);
        dicePresenter.onDicesChanged();
    }

    private void updateView(DiceSingleCollectionViewModel diceSingleCollectionViewModel, DicePresenter dicePresenter) {
        SingleDiceCollection diceCollection = diceSingleCollectionViewModel.getDiceCollection();
        int mainDiceResId = diceSingleCollectionViewModel.getMainDiceResId();

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
                update(diceSingleCollectionViewModel, dicePresenter);
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
