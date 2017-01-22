package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.OnItemClickListener;
import java.io.Serializable;

public class SimpleSingleValueEditModel implements Serializable {
  private String value;
  private String valueHint;
  private transient OnItemClickListener<String> saveOnclickListener;

  public SimpleSingleValueEditModel(String value, String valueHint,
      OnItemClickListener<String> saveOnclickListener) {
    this.value = value;
    this.valueHint = valueHint;
    this.saveOnclickListener = saveOnclickListener;
  }

  public OnItemClickListener<String> getSaveOnclickListener() {
    return saveOnclickListener;
  }

  public void setSaveOnclickListener(OnItemClickListener<String> saveOnclickListener) {
    this.saveOnclickListener = saveOnclickListener;
  }

  public String getValueHint() {
    return valueHint;
  }

  public void setValueHint(String valueHint) {
    this.valueHint = valueHint;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
      