package com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.valyakinaleksey.roleplayingsystem.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class UserProfileGameViewHolder extends FlexibleViewHolder {
    @BindView(R.id.primary_line)
    protected TextView tvPrimaryLine;
    @BindView(R.id.secondary_line)
    protected TextView tvSecondaryLine;
    @BindView(R.id.icon)
    protected ImageView icon;


    public UserProfileGameViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UserProfileGameViewModel userProfileGameViewModel) {
        tvPrimaryLine.setText(userProfileGameViewModel.getTitle());
        tvSecondaryLine.setText(userProfileGameViewModel.getDescription());
        if (userProfileGameViewModel.isShowMasterIcon()) {
            icon.setVisibility(View.VISIBLE);
        } else {
            icon.setVisibility(View.GONE);
        }
    }
}
      