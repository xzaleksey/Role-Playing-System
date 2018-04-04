package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model;

import com.valyakinaleksey.roleplayingsystem.R;

public enum DiceType {
    D4(R.drawable.dice_d4, new Dice(4)),
    D6(R.drawable.dice_d6, new Dice(6)),
    D8(R.drawable.dice_d8, new Dice(8)),
    D10(R.drawable.dice_d10, new Dice(10)),
    D12(R.drawable.dice_d12, new Dice(12)),
    D20(R.drawable.dice_d20, new Dice(20));

    private int resId;
    private Dice dice;

    DiceType(int resId, Dice dice) {
        this.resId = resId;
        this.dice = dice;
    }


    public int getResId() {
        return resId;
    }

    public int getMaxValue() {
        return dice.getMaxValue();
    }

    public Dice createDice() {
        return dice;
    }

    public SingleDiceCollection createSingleDiceCollection() {
        return SingleDiceCollection.createSingleDiceCollectionFromDice(createDice());
    }

    public DiceCollection createDiceCollection() {
        return DiceCollection.createDiceCollectionFromDice(createDice());
    }

    public static DiceType getDiceType(Dice dice) {
        for (DiceType diceType : DiceType.values()) {
            if (diceType.dice.equals(dice)) {
                return diceType;
            }
        }

        return DiceType.D4;
    }
}
