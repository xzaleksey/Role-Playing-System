package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.bumptech.glide.Glide;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import java.io.File;
import java.util.List;
import rx.Observable;
import rx.Subscription;

import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.getStringById;

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
    @Bind(R.id.checkbox_text) TextView tvSwitch;

    public MapViewHolder(View itemView) {
      super(itemView);
    }

    public void bind(MapModel mapModel, boolean isMaster, MapsPresenter mapsPresenter) {
      if (isMaster) {
        switchCompat.setVisibility(View.VISIBLE);
        tvSwitch.setVisibility(View.VISIBLE);
        boolean visible = mapModel.isVisible();
        switchCompat.setOnCheckedChangeListener(null);
        updateSwitchText(visible);
        switchCompat.setChecked(visible);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
          updateSwitchText(isChecked);
          mapsPresenter.changeMapVisibility(mapModel, isChecked);
        });
      }
      if (subscription != null) {
        subscription.unsubscribe();
      }
      File localFile = mapModel.getLocalFile();
      subscription = Observable.just(localFile).switchMap(file -> {
        if (localFile.exists()) { // load from local storage
          loadImage(Uri.fromFile(localFile));
          return Observable.just(true);
        } else { // try to load from internet
          if (mapModel.getStatus() == FireBaseUtils.SUCCESS) { // map was uploaded by master
            return mapModel.getDownloadUrlObservable().map(uri -> {
              loadImage(uri);
              return Observable.just(true);
            });
          } else { // map uploading or failed, no picture to display
            //TODO load placeHolder
            return Observable.just(true);
          }
        }
      }).subscribe();
    }

    private void updateSwitchText(boolean visible) {
      tvSwitch.setText(visible ? getStringById(R.string.all) : getStringById(R.string.only_master));
    }

    private void loadImage(Uri uri) {
      Glide.with(ivMap.getContext()).load(uri).into(ivMap);
    }
  }
}
      