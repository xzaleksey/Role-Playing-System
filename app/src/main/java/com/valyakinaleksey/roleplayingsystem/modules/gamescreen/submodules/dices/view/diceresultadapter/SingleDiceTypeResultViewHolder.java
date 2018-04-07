package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceResult;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class SingleDiceTypeResultViewHolder extends FlexibleViewHolder {
    @BindView(R.id.iv_main_dice)
    ImageView ivMainDice;

    @BindView(R.id.iv_expand)
    ImageView ivExpand;

    @BindView(R.id.dice_container)
    ViewGroup diceContainer;

    @BindView(R.id.btn_rethrow_dices)
    TextView btnRethrow;

    @BindView(R.id.btn_rethrow_dices_secondary)
    TextView btnRethrowSecondary;

    @BindView(R.id.tv_main_result)
    TextView tvMainResult;

    @BindView(R.id.tv_dice_count)
    TextView tvDiceCount;


    public SingleDiceTypeResultViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("SetTextI18n")
    public void bind(SingleDiceTypeResultViewModel singleDiceTypeResultViewModel, DicePresenter dicePresenter) {
        List<DiceResult> diceResults = singleDiceTypeResultViewModel.getDiceResults();
        DiceType diceType = singleDiceTypeResultViewModel.getDiceType();
        int size = diceResults.size();
        String diceCount = String.valueOf(size) + "d" + diceType.getMaxValue();

        if (size > 1) {
            btnRethrowSecondary.setVisibility(View.VISIBLE);
            btnRethrow.setVisibility(View.INVISIBLE);
            ivExpand.setVisibility(View.VISIBLE);

            if (singleDiceTypeResultViewModel.isStateExpanded()) {

                ivExpand.setImageResource(R.drawable.ic_expand_less_black_24dp);
                ivExpand.setOnClickListener(v -> {
                    singleDiceTypeResultViewModel.setStateExpanded(false);
                    bind(singleDiceTypeResultViewModel, dicePresenter);
                });
                btnRethrowSecondary.setVisibility(View.VISIBLE);

                int childCount = diceContainer.getChildCount();
                int index = 0;
                LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());

                for (DiceResult diceResult : diceResults) {
                    View container;
                    if (childCount > index) {
                        container = diceContainer.getChildAt(index);
                    } else {
                        container = layoutInflater.inflate(R.layout.dice_result_subitem, diceContainer, false);
                        diceContainer.addView(container);
                    }

                    ((ImageView) container.findViewById(R.id.iv_main_dice)).setImageResource(diceType.getResId());
                    ((TextView) container.findViewById(R.id.tv_dice_count)).setText("1d" + diceType.getMaxValue());
                    ((TextView) container.findViewById(R.id.tv_main_result)).setText(String.valueOf(diceResult.getValue()));
                    index++;
                }

                if (childCount > index) {
                    int currentIndex = index;

                    while (index != childCount) {
                        diceContainer.removeViewAt(currentIndex);
                        index++;
                    }
                }

            } else {
                ivExpand.setImageResource(R.drawable.ic_expand_more_black_24dp);
                ivExpand.setOnClickListener(v -> {
                    singleDiceTypeResultViewModel.setStateExpanded(true);
                    bind(singleDiceTypeResultViewModel, dicePresenter);
                });
                btnRethrowSecondary.setVisibility(View.GONE);
                diceContainer.removeAllViews();
            }
        } else {
            diceContainer.removeAllViews();
            btnRethrowSecondary.setVisibility(View.GONE);
            btnRethrow.setVisibility(View.VISIBLE);
            ivExpand.setVisibility(View.GONE);
        }

        tvDiceCount.setText(diceCount);
        tvMainResult.setText(String.valueOf(singleDiceTypeResultViewModel.getTotalResultValue()));
        ivMainDice.setImageResource(diceType.getResId());
    }

}
