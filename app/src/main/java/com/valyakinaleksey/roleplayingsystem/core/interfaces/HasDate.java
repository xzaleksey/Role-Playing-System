package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import java.io.Serializable;

public interface HasDate extends Serializable {
  long getDate();

  void setDate(long date);
}
      