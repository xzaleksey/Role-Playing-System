package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.SectionsAdapter;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di.HasGameEditPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di.MasterGameEditModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.MasterGameEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import javax.inject.Inject;

@AutoComponent(dependencies = { ParentGameComponent.class },
    modules = MasterGameEditModule.class,
    superinterfaces = { GlobalComponent.class, HasGameEditPresenter.class }) @GameScope
@AutoInjector public class MasterGameEditFragment extends
    AbsButterLceFragment<MasterGameEditFragmentComponent, MasterGameEditModel, MasterGameEditView>
    implements MasterGameEditView {

  public static final String TAG = MasterGameEditFragment.class.getSimpleName();

  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Inject SectionsAdapter sectionsAdapter;

  public static MasterGameEditFragment newInstance(Bundle arguments) {
    MasterGameEditFragment gamesDescriptionFragment = new MasterGameEditFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked")
  protected MasterGameEditFragmentComponent createComponent() {
    return DaggerMasterGameEditFragmentComponent.builder()
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
    if (recyclerView.getAdapter() == null) {
      recyclerView.setAdapter(sectionsAdapter);
    }
    sectionsAdapter.update(data.getInfoSections());
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_master_info;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void updateView() {
    sectionsAdapter.notifyDataSetChanged();
  }
}
