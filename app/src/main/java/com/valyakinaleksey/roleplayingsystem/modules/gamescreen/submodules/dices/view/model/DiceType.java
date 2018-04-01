package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model;

import com.valyakinaleksey.roleplayingsystem.R;

public enum DiceType {
    D4(R.drawable.dice_d4, 4),
    D6(R.drawable.dice_d6, 6),
    D8(R.drawable.dice_d8, 8),
    D10(R.drawable.dice_d10, 10),
    D12(R.drawable.dice_d12, 12),
    D20(R.drawable.dice_d20, 20);

    private int resId;
    private int maxValue;

    DiceType(int resId, int maxValue) {
        this.resId = resId;
        this.maxValue = maxValue;
    }

    public int getResId() {
        return resId;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public Dice createDice() {
        return new Dice(maxValue);
    }

    public SingleDiceCollection createSingleDiceCollection() {
        return SingleDiceCollection.createSingleDiceCollectionFromDice(createDice());
    }

    public DiceCollection createDiceCollection() {
        return DiceCollection.createDiceCollectionFromDice(createDice());
    }
}
