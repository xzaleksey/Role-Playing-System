package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.photo.view.ImageFragment;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.ScreenUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StorageUtils;
import java.io.File;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

public class MapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int COLUMNS_COUNT = 2;
  public static final int COLUMSN_COUNT_LANDSCAPE = 3;
  private List<MapModel> mapModels;
  private boolean isMaster = false;
  private MapsPresenter mapsPresenter;

  public MapAdapter(List<MapModel> mapModels, boolean isMaster, MapsPresenter mapsPresenter) {
    this.mapModels = mapModels;
    this.isMaster = isMaster;
    this.mapsPresenter = mapsPresenter;
  }

  public void setIsMaster(boolean isMaster) {
    this.isMaster = isMaster;
  }

  public void update(List<MapModel> mapModels) {
    this.mapModels = mapModels;
    notifyDataSetChanged();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_item, parent, false);
    return new MapViewHolder(view);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ((MapViewHolder) holder).bind(mapModels.get(position), isMaster, mapsPresenter);
  }

  @Override public int getItemCount() {
    return mapModels.size();
  }

  public static class MapViewHolder extends ButterKnifeViewHolder {
    protected Subscription subscription;
    @Bind(R.id.icon) ImageView ivMap;
    @Bind(R.id.switcher) SwitchCompat switchCompat;
    @Bind(R.id.tv_name) TextView tvName;
    @Bind(R.id.iv_delete) ImageView ivDelete;
    @Bind(R.id.divider) View divider;
    @Bind(R.id.bottom_container) View bottomContainer;
    private Uri uri;

    public MapViewHolder(View itemView) {
      super(itemView);
      Context context = itemView.getContext();
      int countOfPaddings = 2 + 2 * COLUMNS_COUNT;
      int countOfColumns = COLUMNS_COUNT;
      if (ScreenUtils.getScreenOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
        countOfPaddings = 2 + 2 * COLUMSN_COUNT_LANDSCAPE;
        countOfColumns = COLUMSN_COUNT_LANDSCAPE;
      }
      int param =
          (ScreenUtils.getDisplayWidth(context) - (ScreenUtils.getDimensionSizeFromResources(
              context, R.dimen.common_margin_between_elements) * countOfPaddings)) / countOfColumns;
      itemView.getLayoutParams().width = param;
      ivMap.getLayoutParams().height = param;
    }

    public void bind(MapModel mapModel, boolean isMaster, MapsPresenter mapsPresenter) {
      clearSubscription();
      initView(mapModel, isMaster, mapsPresenter);
      File localFile = mapModel.getLocalFile();
      boolean fileExists = localFile.exists();
      if (fileExists) {
        initClickListener(localFile.getAbsolutePath());
      }
      if (!fileExists || !Uri.fromFile(localFile).equals(this.uri)) {
        ivMap.setImageDrawable(null);
      } else {
        return;
      }
      subscription = Observable.just(fileExists).switchMap(exists -> {
        if (exists) { // load from local storage
          return loadLocalFile(localFile);
        } else { // try to load from internet
          return loadFileFromInternet(mapModel);
        }
      }).subscribe();
    }

    private void initClickListener(String url) {
      itemView.setOnClickListener(v -> {
        ImageFragment.newInstance(url)
            .show(((FragmentActivity) itemView.getContext()).getSupportFragmentManager(),
                ImageFragment.TAG);
      });
    }

    private void initView(MapModel mapModel, boolean isMaster, MapsPresenter mapsPresenter) {
      if (isMaster) {
        initMasterView(mapModel, mapsPresenter);
      } else {
        initUserView();
      }
      tvName.setText(mapModel.getFileName());
    }

    private void clearSubscription() {
      if (subscription != null) {
        subscription.unsubscribe();
      }
    }

    private void initUserView() {
      ivDelete.setVisibility(View.GONE);
      bottomContainer.setVisibility(View.GONE);
      divider.setVisibility(View.GONE);
    }

    private void initMasterView(MapModel mapModel, MapsPresenter mapsPresenter) {
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

    @NonNull private Observable<?> loadLocalFile(File localFile) {
      Uri uri = Uri.fromFile(localFile);
      initClickListener(localFile.getAbsolutePath());
      if (uri.equals(this.uri)) {
        return Observable.just(true);
      }
      this.uri = uri;
      loadImage(uri);
      return Observable.just(true);
    }

    @NonNull private Observable<?> loadFileFromInternet(MapModel mapModel) {
      if (mapModel.getStatus() == FireBaseUtils.SUCCESS) { // map was uploaded by master
        return mapModel.getDownloadUrlObservable()
            .doOnNext(uri1 -> initClickListener(uri1.toString()))
            .retry(2)
            .onErrorReturn(
                throwable -> StorageUtils.resourceToUri(R.drawable.common_full_open_on_phone))
            .map(uri -> {
              this.uri = uri;
              loadImage(uri);
              return Observable.just(true);
            });
      } else { // map uploading or failed, no picture to display
        //TODO load placeHolder
        return Observable.just(true);
      }
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
}
      