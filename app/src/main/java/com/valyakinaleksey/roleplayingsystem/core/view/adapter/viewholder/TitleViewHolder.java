package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;

import butterknife.Bind;

public class TitleViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.title)
    protected TextView tvTitle;

    public TitleViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(CharSequence title) {
        tvTitle.setText(title);
    }
}
      