package com.valyakinaleksey.roleplayingsystem.core.model;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    private boolean success = true;
    private Throwable error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public boolean hasError() {
        return error != null;
    }
}
