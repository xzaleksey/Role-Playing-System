package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<InfoSection> infoSections;

    public SectionsAdapter() {
        infoSections = new ArrayList<>();
    }

    public void update(List<InfoSection> infoSections) {
        this.infoSections = infoSections;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        for (InfoSection infoSection : infoSections) {
            int itemCount = infoSection.getItemCount();
            if (itemCount > position) {
                return infoSection.getItemViewType(position);
            } else {
                position -= itemCount;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (InfoSection infoSection : infoSections) {
            RecyclerView.ViewHolder holder = infoSection.onCreateViewHolder(parent, viewType);
            if (holder != null) {
                return holder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        for (InfoSection infoSection : infoSections) {
            int itemCount = infoSection.getItemCount();
            if (itemCount > position) {
                infoSection.onBindViewHolder(holder, position, this);
                break;
            } else {
                position -= itemCount;
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (InfoSection infoSection : infoSections) {
            count += infoSection.getItemCount();
        }
        return count;
    }
}
