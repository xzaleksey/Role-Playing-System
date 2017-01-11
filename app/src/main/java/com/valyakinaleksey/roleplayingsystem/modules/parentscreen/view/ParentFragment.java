package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.ParentScope;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.di.app.AppComponent;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.HasParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentModule;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.ParentModel;

import autodagger.AutoComponent;
import autodagger.AutoInjector;

@AutoComponent(dependencies = {AppComponent.class},
        modules = ParentModule.class,
        superinterfaces = {HasParentPresenter.class, GlobalComponent.class}
)
@ParentScope
@AutoInjector
public class ParentFragment extends AbsButterLceFragment<ParentFragmentComponent, ParentModel, ParentView> implements ParentView {

    public static final String TAG = ParentFragment.class.getSimpleName();

    public static ParentFragment newInstance(Bundle arguments) {
        ParentFragment gamesDescriptionFragment = new ParentFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    protected ParentFragmentComponent createComponent() {
        return DaggerParentFragmentComponent
                .builder()
                .appComponent(RpsApp.getAppComponent(getActivity()))
                .parentModule(new ParentModule())
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showContent() {
        super.showContent();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_parent_container;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void getNavigationFragment(Bundle args) {
        getComponent().getPresenter().navigateTo(this, args);
    }


}
