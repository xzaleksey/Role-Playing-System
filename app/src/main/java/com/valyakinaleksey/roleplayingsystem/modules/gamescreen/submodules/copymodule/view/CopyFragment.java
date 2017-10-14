package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.di.CopyFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.di.DaggerCopyFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model.CopyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import javax.inject.Inject;

public class CopyFragment extends
    AbsButterLceFragment<CopyFragmentComponent, CopyViewModel, CopyView>
    implements CopyView {

  public static final String TAG = CopyFragment.class.getSimpleName();

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Inject SectionsAdapter sectionsAdapter;

  public static CopyFragment newInstance(Bundle arguments) {
    CopyFragment gamesDescriptionFragment = new CopyFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked")
  protected CopyFragmentComponent createComponent() {
    return DaggerCopyFragmentComponent.builder()
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
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void showContent() {
    super.showContent();
    sectionsAdapter.update(data.getInfoSections());
    recyclerView.setAdapter(sectionsAdapter);
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_game_description;
  }
}
