package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapHandler;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.io.Serializable;
import java.util.List;

public class MapFlexibleViewModel extends AbstractFlexibleItem<MapViewHolder>
    implements Serializable {
  private MapModel mapModel;
  private boolean isMaster;
  private transient MapHandler mapHandler;

  public MapFlexibleViewModel(MapModel mapModel, boolean isMaster, MapHandler mapHandler) {
    this.mapModel = mapModel;
    this.isMaster = isMaster;
    this.mapHandler = mapHandler;
  }

  public MapModel getMapModel() {
    return mapModel;
  }

  public void setMapModel(MapModel mapModel) {
    this.mapModel = mapModel;
  }

  @Override public MapViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new MapViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, MapViewHolder holder, int position,
      List payloads) {
    holder.bind(mapModel, isMaster, mapHandler);
  }

  @Override public int getLayoutRes() {
    return R.layout.map_item;
  }

  @Override public boolean equals(Object o) {
    return this == o
        || (o instanceof MapFlexibleViewModel && ((MapFlexibleViewModel) o).mapModel.getId()
        .equals(mapModel.getId()))
        && ((MapFlexibleViewModel) o).mapModel.getStatus() == mapModel.getStatus()
        && ((MapFlexibleViewModel) o).mapModel.photoUrl.equals(mapModel.photoUrl);
  }

  @Override public int hashCode() {
    return mapModel.getId().hashCode();
  }
}
      