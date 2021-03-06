package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.adapter;

import android.view.View;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;

import butterknife.Bind;
import butterknife.BindInt;

public class DescriptionViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.description)
    protected TextView tvDescription;

    @BindInt(R.integer.description_max_lines)
    protected int maxLines;

    public DescriptionViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (tvDescription.getMaxLines() == maxLines) {
                tvDescription.setMaxLines(Integer.MAX_VALUE);
            } else {
                tvDescription.setMaxLines(maxLines);
            }
        });
    }

    public void bind(String value) {
        tvDescription.setText(value);
    }
}
      