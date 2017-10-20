package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment;
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter.MapViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di.DaggerMapsFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di.MapsFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di.ParentGameComponent;
import com.valyakinaleksey.roleplayingsystem.utils.ScreenUtils;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.decor.ItemOffsetDecoration;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.kbeanie.multipicker.api.Picker.PICK_IMAGE_DEVICE;

public class MapsFragment
    extends AbsButterLceFragment<MapsFragmentComponent, MapsViewModel, MapsView>
    implements MapsView {

  public static final String TAG = MapsFragment.class.getSimpleName();

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.fab) FloatingActionButton fab;
  private ImagePicker imagePicker;
  private FlexibleAdapter<IFlexible<?>> flexibleAdapter;

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
    imagePicker = new ImagePicker(this);
    imagePicker.setImagePickerCallback(getImagePickerCallback());
  }

  @Override public void setupViews(View view) {
    super.setupViews(view);
    fab.setVisibility(View.GONE);
    fab.setOnClickListener(v -> imagePicker.pickImage());
    GridLayoutManager gridLayoutManager;
    if (ScreenUtils.getScreenOrientation(getActivity()) == Configuration.ORIENTATION_LANDSCAPE) {
      gridLayoutManager =
          new GridLayoutManager(getContext(), MapViewHolder.COLUMSN_COUNT_LANDSCAPE);
    } else {
      gridLayoutManager = new GridLayoutManager(getContext(), MapViewHolder.COLUMNS_COUNT);
    }
    flexibleAdapter = new FlexibleAdapter<>(Collections.emptyList());
    flexibleAdapter.mItemClickListener = position -> {
      return getComponent().getPresenter().onItemClick(flexibleAdapter.getItem(position));
    };

    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.addItemDecoration(
        new ItemOffsetDecoration(getContext(), R.dimen.common_margin_between_elements));
    recyclerView.setAdapter(flexibleAdapter);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == PICK_IMAGE_DEVICE) {
        if (imagePicker == null) {
          imagePicker = new ImagePicker(this);
          imagePicker.setImagePickerCallback(getImagePickerCallback());
        }
        imagePicker.submit(data);
      }
    }
  }

  @Override public void loadData() {
    getComponent().getPresenter().getData();
  }

  @Override public void showContent() {
    super.showContent();
    if (data.isMaster()) {
      fab.setVisibility(View.VISIBLE);
    }
    flexibleAdapter.updateDataSet(data.getMapModel().getMapsViewModel(), true);
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override protected int getContentResId() {
    return R.layout.fragment_maps;
  }

  private ImagePickerCallback getImagePickerCallback() {
    return new ImagePickerCallback() {
      @Override public void onImagesChosen(List<ChosenImage> list) {
        getComponent().getPresenter().uploadImage(list.get(0));
      }

      @Override public void onError(String s) {
        BaseError.SNACK.setValue(s);
        showError(BaseError.SNACK);
      }
    };
  }
}
