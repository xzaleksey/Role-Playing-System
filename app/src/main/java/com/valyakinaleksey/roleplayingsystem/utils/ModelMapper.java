package com.valyakinaleksey.roleplayingsystem.utils;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.OnItemClickListener;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.EditableSingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SimpleSingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.TwoValueEditModel;

public class ModelMapper {
  public static EditableSingleValueEditModel getEditableSingleValueEditModel(String title,
      String value, String valueHint, OnItemClickListener<String> saveOnclickListener) {
    EditableSingleValueEditModel singleValueEditModel = new EditableSingleValueEditModel();
    singleValueEditModel.setTitle(title);
    singleValueEditModel.setValue(value);
    singleValueEditModel.setValueHint(valueHint);
    singleValueEditModel.setSaveOnclickListener(saveOnclickListener);
    return singleValueEditModel;
  }

  public static TwoValueEditModel getTwoValueEditModel(String id,
      SimpleSingleValueEditModel mainValue, SimpleSingleValueEditModel secondaryValue,
      OnItemClickListener<TwoValueEditModel> onItemClickListener) {
    return new TwoValueEditModel(mainValue, secondaryValue, onItemClickListener);
  }

  public static SimpleSingleValueEditModel getSimpleSingleValueEditModel(String title, String hint,
      OnItemClickListener<String> stringOnItemClickListener) {
    return new SimpleSingleValueEditModel(title, hint, stringOnItemClickListener);
  }
}
      