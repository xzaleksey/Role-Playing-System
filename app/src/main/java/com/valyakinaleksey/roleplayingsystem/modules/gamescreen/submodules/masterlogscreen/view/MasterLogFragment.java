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
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter.MasterLogAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.MasterLogFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.MasterLogModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.MasterLogModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;

public class MasterLogFragment
    extends AbsButterLceFragment<MasterLogFragmentComponent, MasterLogModel, MasterLogView>
    implements MasterLogView {

  public static final String TAG = MasterLogFragment.class.getSimpleName();

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @BindView(R.id.icon_send) View sendIcon;

  @BindView(R.id.input) EditText etInput;
  @BindView(R.id.send_form) ViewGroup sendForm;

  private MasterLogAdapter masterLogAdapter;

  public static MasterLogFragment newInstance(Bundle arguments) {
    MasterLogFragment gamesDescriptionFragment = new MasterLogFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked") protected MasterLogFragmentComponent createComponent(
      String fragmentId) {
    return ((ComponentManagerFragment<ParentGameComponent, ?>) getParentFragment()).getComponent()
        .getMasterLogFragmentComponent(new MasterLogModule(fragmentId));
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    //LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    //layoutManager.setStackFromEnd(true);
    sendForm.setOnClickListener(v -> KeyboardUtils.showSoftKeyboard(etInput));
    sendIcon.setOnClickListener(v -> {
      if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
        getComponent().getPresenter().sendMessage(etInput.getText().toString());
        etInput.setText("");
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
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void showContent() {
    super.showContent();
    if (masterLogAdapter == null) {
      masterLogAdapter = new MasterLogAdapter(data.getDatabaseReference());
      masterLogAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
          super.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override public void onItemRangeInserted(int positionStart, int itemCount) {
          super.onItemRangeInserted(positionStart, itemCount);
          if (positionStart == 0) {
            getComponent().getPresenter().loadComplete();
          }
          if (recyclerView != null && masterLogAdapter != null) {
            recyclerView.smoothScrollToPosition(masterLogAdapter.getItemCount());
          }
        }
      });
    }
    if (recyclerView.getAdapter() == null) {
      recyclerView.setAdapter(masterLogAdapter);
    }
  }

  @Override public void onResume() {
    super.onResume();
    initSubscriptions();
  }

  @Override protected void initSubscriptions() {
    // add custom subscription when need it
    //RxTextView.textChanges(etInput).subscribe(charSequence -> {
    //  if (charSequence.length() == 0) {
    //    sendIcon.setVisibility(View.GONE);
    //  } else {
    //    sendIcon.setVisibility(View.VISIBLE);
    //  }
    //});
  }

  @Override public void onDestroy() {
    if (masterLogAdapter != null) {
      masterLogAdapter.cleanup();
    }
    super.onDestroy();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_master_log;
  }

  @Override public void updateView() {

  }
}
