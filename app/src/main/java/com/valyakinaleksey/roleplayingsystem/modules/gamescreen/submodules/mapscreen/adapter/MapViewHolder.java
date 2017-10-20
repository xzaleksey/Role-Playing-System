package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapHandler;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.ScreenUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;
import rx.Subscription;
import timber.log.Timber;

public class MapViewHolder extends FlexibleViewHolder {
  public static final int COLUMNS_COUNT = 2;
  public static final int COLUMSN_COUNT_LANDSCAPE = 3;

  protected Subscription subscription;
  @BindView(R.id.icon) ImageView ivMap;
  @BindView(R.id.switcher) SwitchCompat switchCompat;
  @BindView(R.id.tv_name) TextView tvName;
  @BindView(R.id.iv_delete) ImageView ivDelete;
  @BindView(R.id.divider) View divider;
  @BindView(R.id.bottom_container) View bottomContainer;

  public MapViewHolder(View itemView, FlexibleAdapter flexibleAdapter) {
    super(itemView, flexibleAdapter);
    ButterKnife.bind(this, itemView);
    Context context = itemView.getContext();
    int countOfPaddings = 2 + 2 * COLUMNS_COUNT;
    int countOfColumns = COLUMNS_COUNT;
    if (ScreenUtils.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
      countOfPaddings = 2 + 2 * COLUMSN_COUNT_LANDSCAPE;
      countOfColumns = COLUMSN_COUNT_LANDSCAPE;
    }
    int param =
        (ScreenUtils.getDisplayWidth(context) - (ScreenUtils.getDimensionSizeFromResources(context,
            R.dimen.common_margin_between_elements) * countOfPaddings)) / countOfColumns;
    itemView.getLayoutParams().width = param;
    ivMap.getLayoutParams().height = param;
  }

  public void bind(MapModel mapModel, boolean isMaster, MapHandler mapHandler) {
    reset();
    initView(mapModel, isMaster, mapHandler);
  }

  private void initView(MapModel mapModel, boolean isMaster, MapHandler mapsPresenter) {
    if (isMaster) {
      initMasterView(mapModel, mapsPresenter);
    } else {
      initUserView();
    }
    tvName.setText(mapModel.getFileName());
    ivMap.setImageDrawable(null);
    if (mapModel.localFileExists()) {
      loadImage(Uri.fromFile(mapModel.getLocalFile()));
    } else if (mapModel.getStatus() == FireBaseUtils.SUCCESS) {
      loadImage(Uri.parse(mapModel.photoUrl));
    }
  }

  private void reset() {
    if (subscription != null) {
      subscription.unsubscribe();
    }
    ivMap.setImageDrawable(null);
  }

  private void initUserView() {
    ivDelete.setVisibility(View.GONE);
    bottomContainer.setVisibility(View.GONE);
    divider.setVisibility(View.GONE);
  }

  private void initMasterView(MapModel mapModel, MapHandler mapsPresenter) {
    switchCompat.setEnabled(true);
    boolean visible = mapModel.isVisible();
    ivDelete.setVisibility(View.VISIBLE);
    ivDelete.setOnClickListener(v -> {
      new MaterialDialog.Builder(v.getContext()).negativeText(android.R.string.cancel)
          .title(R.string.delete_map)
          .positiveText(android.R.string.ok)
          .onPositive((dialog, which) -> {
            mapsPresenter.deleteMap(mapModel);
          })
          .show();
    });
    switchCompat.setOnCheckedChangeListener(null);
    switchCompat.setChecked(visible);
    switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
      mapsPresenter.changeMapVisibility(mapModel, isChecked);
    });
  }

  private void loadImage(Uri uri) {
    Glide.with(ivMap.getContext()).load(uri).into(new SimpleTarget<GlideDrawable>() {
      @Override public void onResourceReady(GlideDrawable resource,
          GlideAnimation<? super GlideDrawable> glideAnimation) {
        ivMap.setImageDrawable(resource);
      }

      @Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
        Timber.d("Load failed");
        super.onLoadFailed(e, errorDrawable);
      }
    });
  }
}
