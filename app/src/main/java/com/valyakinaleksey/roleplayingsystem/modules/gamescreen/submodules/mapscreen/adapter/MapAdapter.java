package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.ScreenUtils;
import java.io.File;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

public class MapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    private Uri uri;

    public MapViewHolder(View itemView) {
      super(itemView);
      Context context = itemView.getContext();
      int param =
          (ScreenUtils.getDisplayWidth(context) - (ScreenUtils.getDimensionSizeFromResources(
              context, R.dimen.common_margin_between_elements) * 6)) / 2;
      itemView.getLayoutParams().width = param;
      ivMap.getLayoutParams().height = param;
    }

    public void bind(MapModel mapModel, boolean isMaster, MapsPresenter mapsPresenter) {
      if (isMaster) {
        switchCompat.setEnabled(true);
        boolean visible = mapModel.isVisible();
        ivDelete.setVisibility(View.VISIBLE);
        switchCompat.setOnCheckedChangeListener(null);
        switchCompat.setChecked(visible);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
          mapsPresenter.changeMapVisibility(mapModel, isChecked);
        });
        ivDelete.setOnClickListener(v -> {

        });
      } else {
        switchCompat.setEnabled(false);
      }
      tvName.setText(mapModel.getFileName());
      if (subscription != null) {
        subscription.unsubscribe();
      }
      File localFile = mapModel.getLocalFile();
      if (localFile.exists() && Uri.fromFile(localFile).equals(this.uri)) {
        return;
      }
      subscription = Observable.just(localFile.exists()).switchMap(exists -> {
        if (exists) { // load from local storage
          return loadLocalFile(localFile);
        } else { // try to load from internet
          return loadFileFromInternet(mapModel);
        }
      }).subscribe();
    }

    @NonNull private Observable<?> loadLocalFile(File localFile) {
      Uri uri = Uri.fromFile(localFile);
      if (uri.equals(this.uri)) {
        return Observable.just(true);
      }
      this.uri = uri;
      loadImage(uri);
      return Observable.just(true);
    }

    @NonNull private Observable<?> loadFileFromInternet(MapModel mapModel) {
      if (mapModel.getStatus() == FireBaseUtils.SUCCESS) { // map was uploaded by master
        return mapModel.getDownloadUrlObservable().map(uri -> {
          if (uri.equals(this.uri)) {
            return Observable.just(true);
          }
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
      Glide.with(ivMap.getContext())
          .load(uri)
          .dontAnimate()
          .into(new SimpleTarget<GlideDrawable>() {
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
      