package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.GameListAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.GameViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.DaggerGamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.di.GamesListComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;

import butterknife.Bind;
import butterknife.BindString;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class GamesListFragment extends AbsButterLceFragment<GamesListComponent, GamesListViewModel, GamesListView> implements GamesListView {

    public static final String TAG = GamesListFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindString(R.string.error_empty_field)
    String errorEmptyField;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private GameListAdapter gameListAdapter;
    private MaterialDialog dialog;

    public static GamesListFragment newInstance() {
        return new GamesListFragment();
    }

    @Override
    protected GamesListComponent createComponent() {
        return DaggerGamesListComponent
                .builder()
                .appComponent(RpsApp.getAppComponent(getActivity()))
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
        setupFabButton();
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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
        ((AbsActivity) getActivity()).setToolbarTitle(data.getToolbarTitle());
        if (gameListAdapter == null) {
            gameListAdapter = new GameListAdapter(GameModel.class, R.layout.games_list_item, GameViewHolder.class, data.getReference(), getComponent().getPresenter());
            gameListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
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
            recyclerView.setAdapter(gameListAdapter);
        }
        if (data.getCreateGameDialogData() != null && (dialog == null || !dialog.isShowing())) {
            showCreateGameDialog();
        }
        if (data.getPasswordDialogViewModel() != null && (dialog == null || !dialog.isShowing())) {
            showPasswordDialog();
        }
    }

    @Override
    public void onDestroy() {
        if (gameListAdapter != null) {
            gameListAdapter.cleanup();
        }
        super.onDestroy();
    }


    @Override
    protected int getContentResId() {
        return R.layout.fragment_games_list;
    }

    @Override
    public void onGameCreated() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
    }

    @Override
    public void showCreateGameDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_game_content, null);
        MaterialEditText etName = (MaterialEditText) dialogView.findViewById(R.id.name);
        MaterialEditText etDescription = (MaterialEditText) dialogView.findViewById(R.id.description);
        MaterialEditText etPassword = (MaterialEditText) dialogView.findViewById(R.id.password);
        CreateGameDialogViewModel dialogData = data.getCreateGameDialogData();
        GameModel gameModel = dialogData.getGameModel();
        etName.setText(gameModel.getName());
        etDescription.setText(gameModel.getDescription());
        etPassword.setText(gameModel.getPassword());
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        dialog = new MaterialDialog.Builder(getContext())
                .title(dialogData.getTitle())
                .customView(dialogView, true)
                .autoDismiss(false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    getComponent().getPresenter().createGame(dialogData.getGameModel());
                })
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                })
                .dismissListener(dialog1 -> {
                    data.setCreateGameDialogData(null);
                    compositeSubscription.unsubscribe();
                })
                .build();
        MDButton actionButton = dialog.getActionButton(DialogAction.POSITIVE);
        if (TextUtils.isEmpty(gameModel.getName())) {
            actionButton.setEnabled(false);
        }
        compositeSubscription.add(RxTextView.textChanges(etName)
                .skip(1)
                .subscribe(charSequence -> {
                    if (TextUtils.isEmpty(charSequence)) {
                        actionButton.setEnabled(false);
                    } else {
                        actionButton.setEnabled(true);
                    }
                    gameModel.setName(charSequence.toString());
                }));
        compositeSubscription.add(RxTextView.textChanges(etDescription)
                .subscribe(charSequence -> {
                    gameModel.setDescription(charSequence.toString());
                }));
        compositeSubscription.add(RxTextView.textChanges(etPassword)
                .subscribe(charSequence -> {
                    gameModel.setPassword(charSequence.toString());
                }));
        dialog.show();
        etName.post(() -> {
            etName.setSelection(etName.length());
            KeyboardUtils.showSoftKeyboard(etName);
        });
    }

    @Override
    public void showPasswordDialog() {
        PasswordDialogViewModel passwordDialogViewModel = data.getPasswordDialogViewModel();
        dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.input_password)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.password), passwordDialogViewModel.getInputPassword(), false, (dialog1, input) -> {
                    getComponent().getPresenter().validatePassword(getContext(), input.toString(), passwordDialogViewModel.getGameModel());
                })
                .dismissListener(dialog1 -> data.setPasswordDialogViewModel(null))
                .show();
    }

    @Override
    public void onDestroyView() {
        dialog = null;
        super.onDestroyView();
    }

    private void setupFabButton() {
        fab.setOnClickListener(v -> {
            getComponent().getPresenter().onFabPressed();
        });
    }
}
