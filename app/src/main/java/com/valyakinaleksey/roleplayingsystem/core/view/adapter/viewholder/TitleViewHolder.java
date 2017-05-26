package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;

public class TitleViewHolder extends ButterKnifeViewHolder {
    @BindView(R.id.title)
    protected TextView tvTitle;

    @BindView(R.id.divider)
    protected View divider;

    public TitleViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(CharSequence title) {
        tvTitle.setText(title);
        divider.setVisibility(getAdapterPosition() == 0 ? View.GONE : View.VISIBLE);
    }
}
      