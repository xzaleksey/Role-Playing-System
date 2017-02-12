package com.valyakinaleksey.roleplayingsystem.core.utils.lambda;

public interface Executor<R, D> {
  R execute(D data);
}
      