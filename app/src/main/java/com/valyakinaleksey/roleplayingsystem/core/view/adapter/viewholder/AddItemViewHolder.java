package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindColor;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.EditExpandableSection;
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils;

public class AddItemViewHolder extends TitleViewHolder {
  @BindView(R.id.iv_icon) AppCompatImageView appCompatImageView;
  @BindView(R.id.container) View container;

  @BindColor(R.color.accent) int accentColor;

  public AddItemViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(EditExpandableSection editExpandableSection, String title,
      RecyclerView.Adapter adapter) {
    bind(title);
    ImageUtils.setTintVectorImage(appCompatImageView, R.drawable.ic_add_black_24dp, accentColor);
    container.setOnClickListener(v -> editExpandableSection.addItem(getAdapterPosition(), adapter));
  }
}
      