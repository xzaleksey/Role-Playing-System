package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.DaggerMasterGameEditFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter.MasterLogAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter.MasterLogItemViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.HasMasterLogPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.MasterLogModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.MasterLogModel;

import com.valyakinaleksey.roleplayingsystem.utils.HideKeyBoardOnScrollListener;
import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.Bind;

@AutoComponent(dependencies = { ParentGameComponent.class },
    modules = MasterLogModule.class,
    superinterfaces = { GlobalComponent.class, HasMasterLogPresenter.class }) @GameScope
@AutoInjector public class MasterLogFragment
    extends AbsButterLceFragment<MasterLogFragmentComponent, MasterLogModel, MasterLogView>
    implements MasterLogView {

  public static final String TAG = MasterLogFragment.class.getSimpleName();

  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  @Bind(R.id.icon_send) View sendIcon;

  @Bind(R.id.input) EditText etInput;

  private MasterLogAdapter masterLogAdapter;

  private StickyHeaderDecoration decor;

  public static MasterLogFragment newInstance(Bundle arguments) {
    MasterLogFragment gamesDescriptionFragment = new MasterLogFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked") protected MasterLogFragmentComponent createComponent() {
    return DaggerMasterLogFragmentComponent.builder()
        .parentGameComponent(
            ((ComponentManagerFragment<ParentGameComponent, ?>) getParentFragment()).getComponent())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getComponent().inject(this);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    layoutManager.setStackFromEnd(true);
    sendIcon.setOnClickListener(v -> {
      if (!TextUtils.isEmpty(etInput.getText().toString().trim())) {
        getComponent().getPresenter().sendMessage(etInput.getText().toString());
        etInput.setText("");
      }
    });
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
      masterLogAdapter =
          new MasterLogAdapter(MasterLogMessage.class, R.layout.master_log_item_test_constraint,
              MasterLogItemViewHolder.class, data.getDatabaseReference());
      masterLogAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
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
      decor = new StickyHeaderDecoration(masterLogAdapter);
      recyclerView.addItemDecoration(decor);
      recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
          ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
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
