package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.utils.TimeFormatUtils;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class MasterLogItemViewHolder extends ButterKnifeViewHolder {
  @BindView(R.id.tv_date) protected TextView tvDate;
  @BindView(R.id.tv_time) protected TextView tvTime;
  @BindView(R.id.tv_text) protected TextView tvText;
  @BindView(R.id.header_container) protected View headerContainer;

  public MasterLogItemViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(MasterLogMessage model, boolean showHeader) {
    if (showHeader) {
      tvDate.setText(new DateTime(model.getDateCreateLong()).toString(
          DateTimeFormat.shortDate().withLocale(Locale.getDefault())));
      headerContainer.setVisibility(View.VISIBLE);
    } else {
      headerContainer.setVisibility(View.GONE);
    }
    tvText.setText(model.getText());
    setTime(model.getDateCreateLong());
  }

  private void setTime(long dateCreate) {
    tvTime.setText(TimeFormatUtils.getShortTime(new DateTime(dateCreate)));
  }
}
      