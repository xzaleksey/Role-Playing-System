package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import java.io.Serializable;

public class StaticItem<T extends Serializable> implements Serializable {
    private int type;

    private T value;

    public StaticItem(int type, T value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}