package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel;

import butterknife.BindView;

public class SubHeaderViewHolder extends ButterKnifeViewHolder {
    @BindView(R.id.text)
    TextView tvTitle;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.top_divider)
    View topDivider;

    public SubHeaderViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(SubHeaderViewModel subHeaderViewModel) {
        tvTitle.setText(subHeaderViewModel.getTitle());
        tvTitle.setPadding(subHeaderViewModel.getPaddingLeft(), tvTitle.getPaddingTop(), tvTitle.getPaddingRight(), tvTitle.getPaddingBottom());
        if (subHeaderViewModel.getColor() == 0) {
            tvTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
        } else {
            tvTitle.setTextColor(subHeaderViewModel.getColor());
        }
        if (subHeaderViewModel.isDrawBottomDivider()) {
            divider.setVisibility(View.VISIBLE);
        } else {
            divider.setVisibility(View.GONE);
        }

        if (subHeaderViewModel.isDrawTopDivider()) {
            topDivider.setVisibility(View.VISIBLE);
        } else {
            topDivider.setVisibility(View.GONE);
        }
    }
}
      