package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceResult;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class SingleDiceTypeResultViewHolder extends FlexibleViewHolder {
    private static final int SINGLE_DIE = 1;
    private static final int MAX_VALUE = 3;
    @BindView(R.id.iv_main_dice)
    ImageView ivMainDice;

    @BindView(R.id.btn_rethrow_dices)
    TextView btnRethrow;

    @BindView(R.id.tv_main_result)
    TextView tvMainResult;

    @BindView(R.id.tv_dice_count)
    TextView tvDiceCount;

    @BindView(R.id.tv_dice_results)
    TextView tvDiceResults;

    SingleDiceTypeResultViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("SetTextI18n")
    public void bind(SingleDiceTypeResultViewModel singleDiceTypeResultViewModel, DicePresenter dicePresenter) {
        List<DiceResult> diceResults = singleDiceTypeResultViewModel.getDiceResults();
        DiceType diceType = singleDiceTypeResultViewModel.getDiceType();
        int size = diceResults.size();
        String diceMaxResult = "d" + diceType.getMaxValue();
        String diceCount = String.valueOf(size) + diceMaxResult;
        StringBuilder sb = new StringBuilder(String.valueOf(singleDiceTypeResultViewModel.getTotalResultValue()));

        if (size == SINGLE_DIE) {
            tvMainResult.setText(sb);
            tvDiceResults.setVisibility(View.GONE);
        } else if (size <= MAX_VALUE) {
            sb.append(" = ");
            for (int i = 0; i < diceResults.size(); i++) {
                DiceResult diceResult = diceResults.get(i);
                sb.append(diceResult.getValue());
                if (i < diceResults.size() - 1) {
                    sb.append(" + ");
                }
            }
            tvMainResult.setText(sb);
            tvDiceResults.setVisibility(View.GONE);
        } else {
            tvMainResult.setText(sb);
            sb.setLength(0);

            for (int i = 0; i < diceResults.size(); i++) {
                DiceResult diceResult = diceResults.get(i);
                sb.append(diceResult.getValue());
                if (i < diceResults.size() - 1) {
                    sb.append(" + ");
                }
            }
            tvDiceResults.setText(sb);
            tvDiceResults.setVisibility(View.VISIBLE);
        }

        tvDiceCount.setText(diceCount);
        ivMainDice.setImageResource(diceType.getResId());

        initThrowListener(diceResults, dicePresenter, diceType, diceMaxResult);
    }

    private void initThrowListener(List<DiceResult> diceResults, DicePresenter dicePresenter, DiceType diceType, String diceMaxResult) {
        String singleDiceMaxResult = String.valueOf(1) + diceMaxResult;

        LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
        btnRethrow.setOnClickListener(v -> {
            if (diceResults.size() == 1) {
                dicePresenter.rethrowDices(new HashSet<>(diceResults));
                return;
            }

            View dialogView = layoutInflater.inflate(R.layout.dice_result_dialog_view, null);
            Map<View, DiceResult> diceResultViewMap = new HashMap<>();
            Set<DiceResult> selectedDiceResults = new HashSet<>();

            MaterialDialog materialDialog = new MaterialDialog.Builder(v.getContext())
                    .title(R.string.rethrow)
                    .customView(dialogView, true)
                    .negativeText(android.R.string.cancel)
                    .positiveText(R.string.throw_dice)
                    .onPositive((dialog, which) -> dicePresenter.rethrowDices(selectedDiceResults))
                    .build();

            MDButton actionButton = materialDialog.getActionButton(DialogAction.POSITIVE);
            actionButton.setEnabled(false);

            ViewGroup diceContainer = dialogView.findViewById(R.id.dice_container);
            CheckBox allCheckbox = dialogView.findViewById(R.id.checkbox);
            View allContainer = dialogView.findViewById(R.id.all_container);

            for (DiceResult diceResult : diceResults) {
                View diceView = layoutInflater.inflate(R.layout.dice_result_dialog_item, diceContainer, false);
                ((ImageView) diceView.findViewById(R.id.iv_main_dice)).setImageResource(diceType.getResId());
                ((TextView) diceView.findViewById(R.id.tv_dice_count)).setText(singleDiceMaxResult);
                ((TextView) diceView.findViewById(R.id.tv_main_result)).setText(String.valueOf(diceResult.getValue()));
                CheckBox checkBox = diceView.findViewById(R.id.checkbox);
                diceView.setOnClickListener(v1 -> getOnClickListener(diceResults, allCheckbox, selectedDiceResults, diceResult, checkBox, actionButton));
                checkBox.setOnClickListener(v1 -> getOnClickListener(diceResults, allCheckbox, selectedDiceResults, diceResult, checkBox, actionButton));
                diceContainer.addView(diceView);
                diceResultViewMap.put(diceView, diceResult);
            }

            allContainer.setOnClickListener(v12 -> getAllCLickListener(diceResults, diceResultViewMap, selectedDiceResults, actionButton, allCheckbox));
            allCheckbox.setOnClickListener(v12 -> getAllCLickListener(diceResults, diceResultViewMap, selectedDiceResults, actionButton, allCheckbox));

            materialDialog.show();
        });
    }

    private void getAllCLickListener(List<DiceResult> diceResults, Map<View, DiceResult> diceResultViewMap, Set<DiceResult> selectedDiceResults, MDButton actionButton, CheckBox allCheckbox) {
        if (selectedDiceResults.size() == diceResults.size()) {
            selectedDiceResults.clear();
            for (View view : diceResultViewMap.keySet()) {
                ((CheckBox) view.findViewById(R.id.checkbox)).setChecked(false);
            }
            allCheckbox.setChecked(false);
        } else {
            selectedDiceResults.addAll(diceResults);
            for (View view : diceResultViewMap.keySet()) {
                ((CheckBox) view.findViewById(R.id.checkbox)).setChecked(true);
            }
            allCheckbox.setChecked(true);
        }
        actionButton.setEnabled(selectedDiceResults.size() > 0);
    }

    private void getOnClickListener(List<DiceResult> diceResults, CheckBox allCheckbox, Set<DiceResult> selectedDiceResults, DiceResult diceResult, CheckBox checkBox, MDButton actionButton) {
        if (selectedDiceResults.contains(diceResult)) {
            selectedDiceResults.remove(diceResult);
            checkBox.setChecked(false);
        } else {
            selectedDiceResults.add(diceResult);
            checkBox.setChecked(true);
        }
        if (selectedDiceResults.size() == diceResults.size()) {
            allCheckbox.setChecked(true);
        } else {
            allCheckbox.setChecked(false);
        }
        actionButton.setEnabled(selectedDiceResults.size() > 0);
    }

}
