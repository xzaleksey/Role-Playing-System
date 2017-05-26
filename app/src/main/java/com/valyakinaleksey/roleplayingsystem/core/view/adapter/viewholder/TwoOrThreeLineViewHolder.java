package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.TwoOrThreeLineTextModel;

public class TwoOrThreeLineViewHolder extends ButterKnifeViewHolder {
    @BindView(R.id.primary_line)
    protected TextView tvPrimaryLine;
    @BindView(R.id.secondary_line)
    protected TextView tvSecondaryLine;

    public TwoOrThreeLineViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(TwoOrThreeLineTextModel twoOrThreeLineTextModel) {
        tvPrimaryLine.setText(twoOrThreeLineTextModel.getPrimaryText());
        tvSecondaryLine.setText(twoOrThreeLineTextModel.getSecondaryText());
    }
}
      