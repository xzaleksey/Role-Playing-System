package com.valyakinaleksey.roleplayingsystem.core.flexible;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.TwoLineWithIdViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.List;

public class TwoLineWithIdViewModel extends AbstractFlexibleItem<TwoLineWithIdViewHolder> {
  private String id;
  private String title;
  private String secondaryText;

  public TwoLineWithIdViewModel(String id, String title, String secondaryText) {
    this.id = id;
    this.title = title;
    this.secondaryText = secondaryText;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSecondaryText() {
    return secondaryText;
  }

  public void setSecondaryText(String secondaryText) {
    this.secondaryText = secondaryText;
  }

  @Override
  public TwoLineWithIdViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new TwoLineWithIdViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, TwoLineWithIdViewHolder holder, int position,
      List payloads) {
    holder.bind(title, secondaryText);
  }

  @Override public int getLayoutRes() {
    return R.layout.two_or_three_lines_item_with_arrow_right;
  }

  @Override public boolean equals(Object o) {
    return this == o || (o instanceof TwoLineWithIdViewModel
        && ((TwoLineWithIdViewModel) o).id.equals(id)
        && ((TwoLineWithIdViewModel) o).getTitle().equals(title)
        && ((TwoLineWithIdViewModel) o).getSecondaryText().equals(secondaryText));
  }

  @Override public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (secondaryText != null ? secondaryText.hashCode() : 0);
    return result;
  }
}
      