package com.valyakinaleksey.roleplayingsystem.core.utils;

import java.io.Serializable;

public class SerializebleTuple<F extends Serializable, S extends Serializable> extends Tuple<F, S>
    implements Serializable {

  public SerializebleTuple(F first, S second) {
    super(first, second);
  }
}

