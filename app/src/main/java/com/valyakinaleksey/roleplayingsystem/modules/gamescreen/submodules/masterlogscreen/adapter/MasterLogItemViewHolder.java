package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter;

import android.view.View;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;

import com.valyakinaleksey.roleplayingsystem.utils.TimeFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

import butterknife.Bind;

public class MasterLogItemViewHolder extends ButterKnifeViewHolder {
  @Bind(R.id.tv_date) protected TextView tvDate;
  @Bind(R.id.tv_text) protected TextView tvText;

  public MasterLogItemViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(MasterLogMessage model) {
    tvText.setText(model.getText());
    setDate(model.getDateCreate());
  }

  private void setDate(long dateCreate) {
    tvDate.setText(TimeFormatUtils.getShortTime(new DateTime(dateCreate)));
  }
}
      