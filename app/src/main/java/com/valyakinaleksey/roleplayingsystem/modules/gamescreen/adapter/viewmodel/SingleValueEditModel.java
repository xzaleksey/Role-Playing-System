package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel;

import android.view.View;

import java.io.Serializable;

public class SingleValueEditModel implements Serializable {
    private String value;
    private String title;
    private String valueHint;
    private transient View.OnClickListener saveOnclickListener;
    private boolean editable = false;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValueHint() {
        return valueHint;
    }

    public void setValueHint(String valueHint) {
        this.valueHint = valueHint;
    }

    public View.OnClickListener getSaveOnclickListener() {
        return saveOnclickListener;
    }

    public void setSaveOnclickListener(View.OnClickListener saveOnclickListener) {
        this.saveOnclickListener = saveOnclickListener;
    }
}
      