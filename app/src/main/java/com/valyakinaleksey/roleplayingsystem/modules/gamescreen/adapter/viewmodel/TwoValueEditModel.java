package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.OnItemClickListener;
import java.io.Serializable;

public class TwoValueEditModel implements Serializable, HasId {
  private String id;
  private SimpleSingleValueEditModel mainValue;
  private SimpleSingleValueEditModel secondaryValue;
  private transient OnItemClickListener<TwoValueEditModel> deleteOnClickListener;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TwoValueEditModel() {
  }

  public TwoValueEditModel(SimpleSingleValueEditModel mainValue,
      SimpleSingleValueEditModel secondaryValue,
      OnItemClickListener<TwoValueEditModel> deleteOnClickListener) {
    this.mainValue = mainValue;
    this.secondaryValue = secondaryValue;
    this.deleteOnClickListener = deleteOnClickListener;
  }

  public SimpleSingleValueEditModel getMainValue() {
    return mainValue;
  }

  public void setMainValue(SimpleSingleValueEditModel mainValue) {
    this.mainValue = mainValue;
  }

  public SimpleSingleValueEditModel getSecondaryValue() {
    return secondaryValue;
  }

  public void setSecondaryValue(SimpleSingleValueEditModel secondaryValue) {
    this.secondaryValue = secondaryValue;
  }

  public OnItemClickListener<TwoValueEditModel> getDeleteOnClickListener() {
    return deleteOnClickListener;
  }

  public void setDeleteOnClickListener(
      OnItemClickListener<TwoValueEditModel> deleteOnClickListener) {
    this.deleteOnClickListener = deleteOnClickListener;
  }
}
      