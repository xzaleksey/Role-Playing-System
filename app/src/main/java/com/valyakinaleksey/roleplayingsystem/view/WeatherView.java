package com.valyakinaleksey.roleplayingsystem.view;


import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorType;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.error_declaration.ErrorTypes;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewModel;

public interface WeatherView extends LceView<WeatherViewModel, WeatherView.WeatherError> {

    void showCautionDialog(CautionDialogData data);

    enum WeatherError {
        @ErrorType(type = ErrorTypes.ONE_SHOT)
        GENERAL
    }
}
