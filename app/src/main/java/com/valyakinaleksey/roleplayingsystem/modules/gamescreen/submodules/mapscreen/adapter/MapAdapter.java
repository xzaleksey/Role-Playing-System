package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import com.bumptech.glide.Glide;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StorageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.io.File;
import java.util.List;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_MAPS;

public class MapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<MapModel> mapModels;

  public MapAdapter(List<MapModel> mapModels) {
    this.mapModels = mapModels;
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
    ((MapViewHolder) holder).bind(mapModels.get(position));
  }

  @Override public int getItemCount() {
    return mapModels.size();
  }

  public static class MapViewHolder extends ButterKnifeViewHolder {

    @Bind(R.id.icon) ImageView ivMap;

    public MapViewHolder(View itemView) {
      super(itemView);
    }

    public void bind(MapModel mapModel) {
      Timber.d(mapModel.getGameId());
      Glide.with(ivMap.getContext())
          .load(new File(StorageUtils.getCacheDirectory()
              .concat("/")
              .concat(GAME_MAPS)
              .concat(StringUtils.formatWithSlashes(
                  StringUtils.formatRightSlash(mapModel.getGameId())
                      .concat(mapModel.getId().concat("/").concat(mapModel.getFileName()))))))
          .into(ivMap);
    }
  }
}
      