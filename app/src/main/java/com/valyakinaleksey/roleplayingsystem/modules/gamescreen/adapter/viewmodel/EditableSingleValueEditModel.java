package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.OnItemClickListener;

import java.io.Serializable;

public class EditableSingleValueEditModel implements Serializable {
  private String value;
  private String title;
  private String valueHint;
  private transient OnItemClickListener<String> saveOnclickListener;
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

  public OnItemClickListener<String> getSaveOnclickListener() {
    return saveOnclickListener;
  }

  public void setSaveOnclickListener(OnItemClickListener<String> saveOnclickListener) {
    this.saveOnclickListener = saveOnclickListener;
  }
}
      