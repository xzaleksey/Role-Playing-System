package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindColor;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ExpandableSection;
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;

public class ExpandableTitleViewHolder extends TitleViewHolder {
  @BindView(R.id.iv_icon) AppCompatImageView appCompatImageView;
  @BindView(R.id.divider) View divider;
  @BindView(R.id.container) View container;

  @BindColor(R.color.defaultGrayTintColor) int grayColor;

  public ExpandableTitleViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(CharSequence title, ExpandableSection expandableSection,
      RecyclerView.Adapter adapter) {
    bind(title);
    updateExpandImage(expandableSection, adapter);
    container.setOnClickListener(v -> {
      int positionStart = getAdapterPosition() + 1;
      if (expandableSection.isExpanded()) {
        int size = expandableSection.getItemCount() - 1;
        expandableSection.setExpanded(false);
        KeyboardUtils.hideKeyboard((Activity) itemView.getContext());
        adapter.notifyItemRangeRemoved(positionStart, size);
      } else {
        expandableSection.setExpanded(true);
        int size = expandableSection.getItemCount() - 1;
        adapter.notifyItemRangeInserted(positionStart, size);
      }
      updateExpandImage(expandableSection, adapter);
    });
  }

  private void updateExpandImage(ExpandableSection expandableSection,
      RecyclerView.Adapter adapter) {

    if (expandableSection.isExpanded()) {
      ImageUtils.setTintVectorImage(appCompatImageView, R.drawable.ic_expand_less_black_24dp,
          grayColor);
    } else {
      ImageUtils.setTintVectorImage(appCompatImageView, R.drawable.ic_expand_more_black_24dp,
          grayColor);
    }
    if (getAdapterPosition() == 0) {
      divider.setVisibility(View.GONE);
    } else {
      divider.setVisibility(View.VISIBLE);
    }
  }
}
      