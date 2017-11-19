package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel;

public class
SubheaderViewHolder extends ButterKnifeViewHolder {
    @BindView(R.id.text)
    TextView tvTitle;
    @BindView(R.id.divider)
    View divider;

    public SubheaderViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(SubHeaderViewModel subHeaderViewModel) {
        tvTitle.setText(subHeaderViewModel.getTitle());
        if (subHeaderViewModel.getColor() == 0) {
            tvTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.primary));
        } else {
            tvTitle.setTextColor(subHeaderViewModel.getColor());
        }
        if (subHeaderViewModel.isDrawTopDivider()) {
            divider.setVisibility(View.VISIBLE);
        } else {
            divider.setVisibility(View.GONE);
        }
    }
}
      