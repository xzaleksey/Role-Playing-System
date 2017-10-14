package com.valyakinaleksey.roleplayingsystem.core.flexible;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.SubheaderViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class SubHeaderViewModel extends AbstractFlexibleItem<SubheaderViewHolder> {
  private String title;

  public SubHeaderViewModel() {
  }

  public SubHeaderViewModel(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public SubheaderViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new SubheaderViewHolder(inflater.inflate(getLayoutRes(), parent, false));
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, SubheaderViewHolder holder, int position,
      List payloads) {
    holder.bind(title);
  }

  @Override public int getLayoutRes() {
    return R.layout.simple_header;
  }

  @Override public boolean equals(Object o) {
    return this == o || o instanceof SubHeaderViewModel && ((SubHeaderViewModel) o).getTitle()
        .equals(title);
  }
}
      