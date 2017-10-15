package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import com.valyakinaleksey.roleplayingsystem.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class TwoLineWithIdViewHolder extends FlexibleViewHolder {
  TextView tvTitle;
  TextView tvSecondary;

  public TwoLineWithIdViewHolder(View itemView, FlexibleAdapter flexibleAdapter) {
    super(itemView, flexibleAdapter);
    tvTitle = itemView.findViewById(R.id.primary_line);
    tvSecondary = itemView.findViewById(R.id.secondary_line);
  }

  public void bind(String title, String secondaryText) {
    tvTitle.setText(title);
    tvSecondary.setText(secondaryText);
  }
}
      