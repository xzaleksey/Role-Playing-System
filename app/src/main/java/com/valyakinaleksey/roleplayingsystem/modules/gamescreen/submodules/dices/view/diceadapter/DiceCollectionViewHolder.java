package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter;

import android.view.View;
import android.widget.ImageView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DiceCollectionViewHolder extends FlexibleViewHolder {
    @BindView(R.id.iv_main_dice)
    ImageView ivMainDice;

    public DiceCollectionViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DiceCollectionViewModel diceCollectionViewModel, DicePresenter dicePresenter) {
        ivMainDice.setImageResource(diceCollectionViewModel.getMainDiceResId());
        ivMainDice.setSelected(false);

    }
}
