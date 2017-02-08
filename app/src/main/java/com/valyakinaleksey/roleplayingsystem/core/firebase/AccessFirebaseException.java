package com.valyakinaleksey.roleplayingsystem.core.firebase;

import android.support.annotation.NonNull;
import com.google.firebase.FirebaseException;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

public class AccessFirebaseException extends FirebaseException {
  public AccessFirebaseException() {
    this(StringUtils.getStringById(R.string.access_error));
  }

  public AccessFirebaseException(@NonNull String s) {
    super(s);
  }

  public AccessFirebaseException(@NonNull String s, Throwable throwable) {
    super(s, throwable);
  }
}
      