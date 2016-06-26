package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base.PendingStateChange;
import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.SerializableTransactionAction2;

import java.io.Serializable;

/**
 * Implementation of pending view-based navigation with ability to be serialized with param
 * Param should also be serializable
 */
public class SerializablePendingStateChangeWithDataImpl<V, D extends Serializable> implements PendingStateChange<V>, Serializable{

    private D data;
    private SerializableTransactionAction2<V, D> op;

    public SerializablePendingStateChangeWithDataImpl() {
    }

    public void setData(D data) {
        this.data = data;
    }

    public void setOperation(SerializableTransactionAction2<V, D> op) {
        this.op = op;
    }

    @Override
    public void apply(V view) {
        op.invoke(view, data);
    }

}
