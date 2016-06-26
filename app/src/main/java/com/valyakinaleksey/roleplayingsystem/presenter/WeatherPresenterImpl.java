package com.valyakinaleksey.roleplayingsystem.presenter;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.model.interactor.GetWeatherInMoscowInteractor;
import com.valyakinaleksey.roleplayingsystem.view.CautionDialogData;
import com.valyakinaleksey.roleplayingsystem.view.WeatherView;
import com.valyakinaleksey.roleplayingsystem.view.mapper.WeatherMapper;
import com.valyakinaleksey.roleplayingsystem.view.model.WeatherViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@PerFragment
public class WeatherPresenterImpl implements WeatherPresenter, RestorablePresenter<WeatherViewModel> {

    private WeatherMapper mMapper;
    private final GetWeatherInMoscowInteractor mGetWeather;
    private CompositeSubscription mSubscriptions;
    private WeatherView mView;

    private Runnable mShowLoading = () -> {
        mView.showLoading();
    };
    private Runnable mHideLoading = () -> {
        mView.hideLoading();
    };
    private WeatherViewModel viewModel;

    @Inject
    public WeatherPresenterImpl(GetWeatherInMoscowInteractor getWeather, WeatherMapper mapper) {
        mGetWeather = getWeather;
        mMapper = mapper;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mView.loadData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    // todo abstract away attach / detach of view
    @Override
    public void attachView(WeatherView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void loadWeather() {
        mSubscriptions.add(mGetWeather
                .get()
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())               // inject for testing
                .observeOn(AndroidSchedulers.mainThread())  // inject for testing
                .compose(RxTransformers.applyOpBeforeAndAfter(mShowLoading, mHideLoading))
                .subscribe(weather -> {
                    viewModel = mMapper.map(weather);
                    updateUi(viewModel);
                }, throwable -> {
                    showError(WeatherView.WeatherError.GENERAL);
                }));
    }

    @Override
    public void someInsaneActionClicked() {
        Observable
                .just(new CautionDialogData(101))
                .subscribeOn(Schedulers.io())               // inject for testing
                .delay(3000, TimeUnit.MILLISECONDS)         // simulate network request
                .observeOn(AndroidSchedulers.mainThread())  // inject for testing
                .subscribe(data -> {
                    mView.showCautionDialog(data);
                });
    }

    @Override
    public void onDestroy() {
        if (mSubscriptions != null && !mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
    }

    private void updateUi(WeatherViewModel weather) {
        mView.setData(weather);
        mView.showContent();
    }

    private void showError(WeatherView.WeatherError error) {
        mView.showError(error);
    }

    @Override
    public void restoreViewModel(WeatherViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
