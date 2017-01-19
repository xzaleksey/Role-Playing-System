package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
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

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.Bind;

@AutoComponent(dependencies = {ParentGameComponent.class},
        modules = MasterLogModule.class,
        superinterfaces = {GlobalComponent.class, HasMasterLogPresenter.class}
)
@GameScope
@AutoInjector
public class MasterLogFragment extends AbsButterLceFragment<MasterLogFragmentComponent, MasterLogModel, MasterLogView> implements MasterLogView {

    public static final String TAG = MasterLogFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.icon_send)
    View sendIcon;


    @Bind(R.id.input)
    EditText etInput;

    private MasterLogAdapter masterLogAdapter;

    public static MasterLogFragment newInstance(Bundle arguments) {
        MasterLogFragment gamesDescriptionFragment = new MasterLogFragment();
        gamesDescriptionFragment.setArguments(arguments);
        return gamesDescriptionFragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MasterLogFragmentComponent createComponent() {
        return DaggerMasterLogFragmentComponent
                .builder()
                .parentGameComponent(((ComponentManagerFragment<ParentGameComponent, ?>) getParentFragment()).getComponent())
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
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setReverseLayout(true);
        sendIcon.setOnClickListener(v -> {
            getComponent().getPresenter().sendMessage(etInput.getText().toString());
        });
    }

    @Override
    public void loadData() {
        getComponent().getPresenter().getData();
    }

    @Override
    public void showContent() {
        super.showContent();
        if (masterLogAdapter == null) {
            masterLogAdapter = new MasterLogAdapter(MasterLogMessage.class, R.layout.master_log_item, MasterLogItemViewHolder.class, data.getDatabaseReference());
            masterLogAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    if (positionStart == 0) {
                        getComponent().getPresenter().loadComplete();
                    }
                }
            });
        }
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(masterLogAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (masterLogAdapter != null) {
            masterLogAdapter.cleanup();
        }
    }


    @Override
    protected int getContentResId() {
        return R.layout.fragment_master_log;
    }

    @Override
    public void updateView() {

    }
}
