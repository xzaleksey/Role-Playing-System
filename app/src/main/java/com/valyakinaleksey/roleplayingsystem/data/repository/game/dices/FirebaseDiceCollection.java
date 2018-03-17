package com.valyakinaleksey.roleplayingsystem.data.repository.game.dices;

import com.google.firebase.database.Exclude;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.Dice;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDiceCollection implements HasId {
    private Map<String, Integer> dices = new HashMap<>();
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Integer> getDices() {
        return dices;
    }

    public void setDices(Map<String, Integer> dices) {
        this.dices = dices;
    }

    @Exclude
    public DiceCollection mapToDiceCollection() {
        DiceCollection diceCollection = new DiceCollection();
        diceCollection.setId(id);

        for (Map.Entry<String, Integer> entry : dices.entrySet()) {
            diceCollection.addDices(new Dice(Integer.parseInt(entry.getKey())), entry.getValue());
        }

        return diceCollection;
    }

    public static FirebaseDiceCollection newInstance(DiceCollection diceCollection) {
        FirebaseDiceCollection firebaseDiceCollection = new FirebaseDiceCollection();
        firebaseDiceCollection.id = diceCollection.getId();

        for (Map.Entry<Dice, Integer> diceIntegerEntry : diceCollection.getDices().entrySet()) {
            firebaseDiceCollection.dices.put(String.valueOf(diceIntegerEntry.getKey().getMaxValue()), diceIntegerEntry.getValue());
        }
        return firebaseDiceCollection;
    }
}
