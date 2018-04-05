package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.collectionadapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.Dice;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceType;
import com.valyakinaleksey.roleplayingsystem.utils.extensions.ContextExtensions;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DiceCollectionViewHolder extends FlexibleViewHolder {
    @BindView(R.id.card_view)
    CardView cardView;

    @BindView(R.id.dice_container)
    LinearLayout diceContainer;

    @BindView(R.id.tv_main)
    TextView tvMain;


    public DiceCollectionViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DiceCollectionViewModel diceCollectionViewModel, DicePresenter dicePresenter) {
        updateView(diceCollectionViewModel, dicePresenter);
    }

    private void updateView(DiceCollectionViewModel diceCollectionViewModel, DicePresenter dicePresenter) {
        DiceCollection diceCollection = diceCollectionViewModel.getDiceCollection();
        LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());

        if (diceCollectionViewModel.isSelected()) {
            cardView.setCardElevation(ContextExtensions.convertDpToPixel(2));
            tvMain.setText(R.string.reset);
            tvMain.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
            tvMain.setBackground(null);
        } else {
            cardView.setCardElevation(ContextExtensions.convertDpToPixel(8));
            tvMain.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.white));
            tvMain.setText(R.string.choose);
            tvMain.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        }

        cardView.setOnClickListener(v -> {
            dicePresenter.onDiceCollectionClicked(diceCollection);
        });

        Set<Map.Entry<Dice, Integer>> entrySet = diceCollection.getDices().entrySet();
        int childCount = diceContainer.getChildCount();
        int index = 0;

        for (Map.Entry<Dice, Integer> diceIntegerEntry : entrySet) {
            Integer count = diceIntegerEntry.getValue();
            Dice dice = diceIntegerEntry.getKey();
            DiceType diceType = DiceType.getDiceType(dice);
            View diceSingleItem;

            if (childCount > index) {
                diceSingleItem = diceContainer.getChildAt(index);
            } else {
                diceSingleItem = layoutInflater.inflate(R.layout.dice_collection_single_item, diceContainer, false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ContextExtensions.convertDpToPixel(40), ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                diceContainer.addView(diceSingleItem, params);
            }

            ((ImageView) diceSingleItem.findViewById(R.id.iv_dice)).setImageResource(diceType.getResId());
            ((TextView) diceSingleItem.findViewById(R.id.tv_main)).setText(String.valueOf(count));

            index++;
        }

        if (childCount > index) {
            while (index != childCount) {
                diceContainer.removeViewAt(index);
                index++;
            }
        }

        tvMain.measure(0, 0);
        diceContainer.measure(0, 0);
        int diceContainerMeasuredWidth = diceContainer.getMeasuredWidth();

        if (tvMain.getMeasuredWidth() < diceContainerMeasuredWidth) {
            ViewGroup.LayoutParams layoutParams = tvMain.getLayoutParams();
            layoutParams.width = diceContainerMeasuredWidth;
        }
    }
}
