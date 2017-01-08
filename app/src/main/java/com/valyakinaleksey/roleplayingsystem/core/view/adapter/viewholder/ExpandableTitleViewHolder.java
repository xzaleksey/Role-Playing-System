package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ExpandableSection;

import butterknife.Bind;

public class ExpandableTitleViewHolder extends TitleViewHolder {
    @Bind(R.id.iv_expand)
    AppCompatImageView appCompatImageView;
    @Bind(R.id.divider)
    View divider;
    @Bind(R.id.container)
    View container;

    public ExpandableTitleViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(CharSequence title, ExpandableSection expandableSection, RecyclerView.Adapter adapter) {
        bind(title);
        updateExpandImage(expandableSection, adapter);
        container.setOnClickListener(v -> {
            int size = expandableSection.getData().size();
            int positionStart = getAdapterPosition() + 1;
            if (expandableSection.isExpanded()) {
                expandableSection.setExpanded(false);
                adapter.notifyItemRangeRemoved(positionStart, size);
            } else {
                expandableSection.setExpanded(true);
                adapter.notifyItemRangeInserted(positionStart, size);
            }
            updateExpandImage(expandableSection, adapter);
        });
    }

    private void updateExpandImage(ExpandableSection expandableSection, RecyclerView.Adapter adapter) {
        appCompatImageView.setImageResource(expandableSection.isExpanded() ? R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp);
        if (getAdapterPosition() == 0) {
            divider.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
        }
    }
}
      