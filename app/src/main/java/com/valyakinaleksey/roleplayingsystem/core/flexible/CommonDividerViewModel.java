package com.valyakinaleksey.roleplayingsystem.core.flexible;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.DividerViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class CommonDividerViewModel extends AbstractFlexibleItem<DividerViewHolder> {
  private int id;

  public CommonDividerViewModel(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public DividerViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new DividerViewHolder(inflater.inflate(getLayoutRes(), parent, false));
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, DividerViewHolder holder, int position,
      List payloads) {
  }

  @Override public int getLayoutRes() {
    return R.layout.divider;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CommonDividerViewModel that = (CommonDividerViewModel) o;

    return id == that.id;
  }

  @Override public int hashCode() {
    return id;
  }
}
      