package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter;

import android.view.View;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessageViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.TimeFormatUtils;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class MasterLogItemViewHolder extends FlexibleViewHolder {
    @BindView(R.id.tv_time)
    protected TextView tvTime;
    @BindView(R.id.tv_text)
    protected TextView tvText;

    public MasterLogItemViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        ButterKnife.bind(this, view);
    }

    public void bind(MasterLogMessageViewModel model) {
        MasterLogMessage masterLogMessage = model.getMasterLogMessage();
        tvText.setText(masterLogMessage.getText());
        setTime(masterLogMessage.getDateCreateLong());
    }

    private void setTime(long dateCreate) {
        tvTime.setText(TimeFormatUtils.getShortTime(new DateTime(dateCreate)));
    }
}
      