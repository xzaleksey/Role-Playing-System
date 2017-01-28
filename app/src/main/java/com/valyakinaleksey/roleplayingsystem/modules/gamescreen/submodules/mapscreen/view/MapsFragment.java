package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.Bind;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di.HasMapsPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di.MapsModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;

@AutoComponent(dependencies = { ParentGameComponent.class },
    modules = MapsModule.class,
    superinterfaces = { GlobalComponent.class, HasMapsPresenter.class }) @GameScope @AutoInjector
public class MapsFragment
    extends AbsButterLceFragment<MapsFragmentComponent, MapsViewModel, MapsView>
    implements MapsView {

  public static final String TAG = MapsFragment.class.getSimpleName();

  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  public static MapsFragment newInstance(Bundle arguments) {
    MapsFragment gamesDescriptionFragment = new MapsFragment();
    gamesDescriptionFragment.setArguments(arguments);
    return gamesDescriptionFragment;
  }

  @Override @SuppressWarnings("unchecked") protected MapsFragmentComponent createComponent() {
    return DaggerMapsFragmentComponent.builder()
        .parentGameComponent(
            ((ComponentManagerFragment<ParentGameComponent, ?>) getParentFragment()).getComponent())
        .build();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void showContent() {
    super.showContent();
  }

  @Override public void onResume() {
    super.onResume();
    initSubscriptions();
  }

  @Override protected void initSubscriptions() {

  }

  @Override protected int getContentResId() {
    return R.layout.fragment_master_log;
  }

  @Override public void updateView() {

  }
}
