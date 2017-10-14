package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;

public class TwoLineWithIdViewHolder extends ButterKnifeViewHolder {
  @BindView(R.id.primary_line) TextView tvTitle;
  @BindView(R.id.secondary_line) TextView tvSecondary;

  public TwoLineWithIdViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(String title, String secondaryText) {
    tvTitle.setText(title);
    tvSecondary.setText(secondaryText);
  }
}
      