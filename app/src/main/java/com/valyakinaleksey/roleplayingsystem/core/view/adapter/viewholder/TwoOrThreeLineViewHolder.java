package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.TwoOrThreeLineTextModel;

import butterknife.Bind;

public class TwoOrThreeLineViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.primary_line)
    protected TextView tvPrimaryLine;
    @Bind(R.id.secondary_line)
    protected TextView tvSecondaryLine;

    public TwoOrThreeLineViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(TwoOrThreeLineTextModel twoOrThreeLineTextModel) {
        tvPrimaryLine.setText(twoOrThreeLineTextModel.getPrimaryText());
        tvSecondaryLine.setText(twoOrThreeLineTextModel.getSecondaryText());
    }
}
      