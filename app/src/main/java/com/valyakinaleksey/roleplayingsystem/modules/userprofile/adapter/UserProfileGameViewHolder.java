package com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class UserProfileGameViewHolder extends FlexibleViewHolder {
    @BindView(R.id.primary_line)
    protected TextView tvPrimaryLine;
    @BindView(R.id.secondary_line)
    protected TextView tvSecondaryLine;
    @BindView(R.id.icon)
    protected ImageView icon;
    @BindView(R.id.iv_right)
    protected ImageView iconRight;

    public UserProfileGameViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(FlexibleGameViewModel flexibleGameViewModel) {
        tvPrimaryLine.setText(flexibleGameViewModel.getTitle());
        tvSecondaryLine.setText(flexibleGameViewModel.getDescription());
        if (flexibleGameViewModel.isShowMasterIcon()) {
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
        if (flexibleGameViewModel.isGameLocked()) {
            iconRight.setImageResource(R.drawable.ic_lock_outline_black_24px);
        } else {
            iconRight.setImageResource(R.drawable.ic_keyboard_arrow_right);
        }
    }
}
      