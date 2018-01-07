package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.MasterLogFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.MasterLogModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.MasterLogModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.ScrollRecyclerViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class MasterLogFragment extends AbsButterLceFragment<MasterLogFragmentComponent, MasterLogModel, MasterLogView> implements MasterLogView {

    public static final String TAG = MasterLogFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.icon_send)
    View sendIcon;
    @BindView(R.id.input)
    EditText etInput;
    @BindView(R.id.send_form)
    ViewGroup sendForm;

    private FlexibleAdapter<IFlexible<?>> masterLogAdapter;
    private PublishSubject<ScrollRecyclerViewModel> newMessages = PublishSubject.create();

    public static MasterLogFragment newInstance(Bundle arguments) {
        MasterLogFragment gamesDescriptionFragment = new MasterLogFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MasterLogFragmentComponent createComponent(
            String fragmentId) {
        return ((ComponentManagerFragment<ParentGameComponent, ?>) getParentFragment())
                .getComponent()
                .getMasterLogFragmentComponent(new MasterLogModule(fragmentId));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
        sendForm.setOnClickListener(v -> KeyboardUtils.showSoftKeyboard(etInput));
        sendIcon.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
                getComponent().getPresenter().sendMessage(etInput.getText().toString());
                etInput.setText(StringUtils.EMPTY_STRING);
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
        recyclerView.addOnLayoutChangeListener(
                (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                    if (bottom < oldBottom) {
                        recyclerView.postDelayed(() -> {
                            if (recyclerView != null) {
                                recyclerView.smoothScrollToPosition(bottom);
                            }
                        }, 100);
                    }
                });
        masterLogAdapter = new FlexibleAdapter<>(data == null ? Collections.emptyList() : data.getItems());
        recyclerView.setAdapter(masterLogAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        compositeSubscription.add(newMessages.onBackpressureLatest()
                .throttleLast(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DataObserver<ScrollRecyclerViewModel>() {
                    @Override
                    public void onData(ScrollRecyclerViewModel scrollRecyclerViewModel) {
                        if (scrollRecyclerViewModel.getSmoothScroll()) {
                            recyclerView.smoothScrollToPosition(masterLogAdapter.getItemCount() - 1);
                        } else {
                            recyclerView.scrollToPosition(masterLogAdapter.getItemCount() - 1);
                        }
                    }
                }));
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void showContent() {
        super.showContent();
        int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        boolean shouldScrollToBottom = false;
        int previousItemCount = masterLogAdapter.getItemCount();
        if (lastVisibleItemPosition == previousItemCount - 1 || lastVisibleItemPosition == -1) {
            shouldScrollToBottom = true;
        }
        masterLogAdapter.updateDataSet(data.getItems(), previousItemCount != 0);
        if (shouldScrollToBottom) {
            newMessages.onNext(new ScrollRecyclerViewModel(previousItemCount != 0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initSubscriptions();
    }

    @Override
    protected void initSubscriptions() {
        // add custom subscription when need it
        //RxTextView.textChanges(etInput).subscribe(charSequence -> {
        //  if (charSequence.length() == 0) {
        //    sendIcon.setVisibility(View.GONE);
        //  } else {
        //    sendIcon.setVisibility(View.VISIBLE);
        //  }
        //});
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_master_log;
    }

    @Override
    public void updateView() {

    }
}
