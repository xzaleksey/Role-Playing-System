package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.valyakinaleksey.roleplayingsystem.R;

public class SubheaderViewHolder extends ButterKnifeViewHolder {
  @Bind(R.id.text) protected TextView tvTitle;

  public SubheaderViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(CharSequence title) {
    tvTitle.setText(title);
  }
}
      