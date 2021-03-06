package com.valyakinaleksey.roleplayingsystem.core.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.SnackbarHelper;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.subscriptions.CompositeSubscription;

import static com.valyakinaleksey.roleplayingsystem.core.view.BaseError.NO_CONNECTION;

/**
 * Base load - content - error view
 * Handles CONNECTION, NO_DATA, GENERAL errors
 * To work properly, your view should be able to work with enum <code>XXXError</code> with corresponding fields
 */
public abstract class AbsLceFragment<C extends HasPresenter, M extends EmptyViewModel, V extends LceView<M>> extends ComponentManagerFragment<C, V> implements LceView<M> {

    @Bind(R.id.progress)
    ProgressBar progress;

    @Bind(R.id.no_connection_error_layout)
    View noConnection;

    @Bind(R.id.retry_no_connection_button)
    Button noConnectionButton;

    @Bind(R.id.no_loaded_error_layout)
    View noData;

    @Bind(R.id.retry_no_loaded_button)
    Button noDataButton;

    @Bind(R.id.lce_container_le)
    View containerLe;

    protected M data;
    private View contentRoot;

    private List<View> mViews;


    protected Runnable action = () -> getComponent().getPresenter().getData();
    protected CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void preSetupViews(final View view) {
        super.preSetupViews(view);
        ViewStub stub = (ViewStub) view.findViewById(R.id.content_placeholder);
        stub.setLayoutResource(getContentResId());
        contentRoot = stub.inflate();
    }


    @Override
    public void onResume() {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        super.onPause();
    }

    protected void initSubscriptions() {

    }

    @Override
    protected void setupViews(final View view) {
        super.setupViews(view);
        mViews = new ArrayList<>();
        mViews.add(noConnection);
        mViews.add(noData);
        mViews.add(progress);
        mViews.add(containerLe);

        setupNoConnection();
        setupLoading();
        setupNoData();
        hide(containerLe);
    }

    private void setupLoading() {
        hide(progress);
    }

    private void setupNoConnection() {
        hide(noConnection);
        noConnectionButton.setOnClickListener(v -> {
            loadData();
        });
    }

    private void setupNoData() {
        hide(noData);
        noDataButton.setOnClickListener(v -> {
            loadData();
        });
    }

    protected void showFullLoading() {
        getActivity().runOnUiThread(() -> {
            hide(noConnection);
            hide(noData);
            show(progress);
            show(containerLe);
        });
    }

    protected void showNoConnection() {
        getActivity().runOnUiThread(() -> {
            hide(progress);
            hide(noData);
            show(noConnection);
            show(containerLe);
        });
    }

    protected void showNoData() {
        getActivity().runOnUiThread(() -> {
            hide(noConnection);
            hide(progress);
            show(noData);
            show(containerLe);
        });
    }

    protected void hideFullLoading() {
        getActivity().runOnUiThread(() -> {
            hide(progress);
        });
    }

    protected void hideFullNoConnection() {
        getActivity().runOnUiThread(() -> {
            hide(noConnection);
        });
    }

    protected void hideFullNoData() {
        getActivity().runOnUiThread(() -> {
            hide(noData);
        });
    }

    protected void hideLeContainer() {
        getActivity().runOnUiThread(() -> hide(containerLe));
    }

    protected abstract int getContentResId();

    @Override
    protected int getLayoutId() {
        return R.layout.lce_view;
    }

    private void show(View v) {
        for (View view : mViews) {
            if (view == v) {
                getActivity().runOnUiThread(() -> view.setVisibility(View.VISIBLE));
                break;
            }
        }
    }

    private void hide(View v) {
        for (View view : mViews) {
            if (view == v) {
                getActivity().runOnUiThread(() -> view.setVisibility(View.GONE));
                break;
            }
        }
    }

    @Override
    public void showLoading() {
        showFullLoading();
    }

    protected final boolean hasData() {
        return data != null && !data.isEmpty();
    }

    @Override
    public void hideLoading() {
        hideFullLoading();
    }

    @Override
    public void showContent() {
        hideLeContainer();
    }

    @Override
    public void showError(BaseError e) {
        getActivity().runOnUiThread(() -> {
            switch (e) {
                case NO_CONNECTION:
                    handleNoConnection();
                    break;
                case NO_DATA:
                    break;
                case SNACK:
                    showSnackbarString(e.toString());
                    break;
                case TOAST:
                    showToast(e.toString());
                    break;
                default:
                    hideLeContainer();
                    break;
            }
        });

    }

    protected void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    protected void showSnackbarString(String s) {
        SnackbarHelper.show(contentRoot, s);
    }

    private void handleNoData() {
        if (!hasData()) {
            showNoData();
        } else {
            showSnackbarNoData();
        }
    }

    private void handleNoConnection() {
        if (!hasData()) {
            showNoConnection();
        } else {
            showSnackbarNoConnection();
        }
    }

    private void showSnackbarNoConnection() {
        SnackbarHelper.show(contentRoot, getString(R.string.error_title_no_connection));
    }

    private void showSnackbarNoData() {
        SnackbarHelper.show(contentRoot, getString(R.string.error_title_no_loaded));
    }

    @Override
    public void setData(M data) {
        this.data = data;
    }

    @Override
    public void showMessage(CharSequence message, @MessageType int type) {
        switch (type) {
            case TOAST:
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                break;
            case SNACK:
                SnackbarHelper.show(contentRoot, message);
                break;
        }
    }

    @Override
    public void preFillModel(M data) {

    }
}