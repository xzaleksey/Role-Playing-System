package com.valyakinaleksey.roleplayingsystem.core.view;


import android.content.Context;
import android.support.annotation.IntDef;

import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action1;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Interface for Load-Content-Error view
 */
public interface LceView<D extends RequestUpdateViewModel> extends View {

    @Retention(SOURCE)
    @IntDef({TOAST, SNACK, DIALOG})
    @interface MessageType {
    }

    int TOAST = 0;
    int SNACK = 1;
    int DIALOG = 2;

    void showLoading();

    void hideLoading();

    void setData(D data);

    void openDialog(String tag);

    void preFillModel(D data);

    void showContent();

    void showError(BaseError error);

    void loadData();

    void performAction(Action1<Context> contextAction);

    void showMessage(CharSequence message, @MessageType int type);
}
