package com.valyakinaleksey.roleplayingsystem.core.utils;

import java.io.Serializable;

public class SerializableTuple<F extends Serializable, S extends Serializable> extends Tuple<F, S> {

  public SerializableTuple(F first, S second) {
    super(first, second);
  }
}

