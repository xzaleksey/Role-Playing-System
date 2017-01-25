package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import java.io.Serializable;

public interface HasDescription extends Serializable {
  String getDescription();

  void setDescription(String description);
}
      