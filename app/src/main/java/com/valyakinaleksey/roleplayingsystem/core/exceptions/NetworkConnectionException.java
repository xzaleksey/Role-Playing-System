package com.valyakinaleksey.roleplayingsystem.core.exceptions;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

public class NetworkConnectionException extends Throwable {
    public NetworkConnectionException() {
        this(RpsApp.app().getString(R.string.error_network_connection));
    }

    public NetworkConnectionException(String message) {
        super(message);
    }
}
      