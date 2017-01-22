package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticFieldsSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticItem;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewholder.SingleValueEditViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.EditableSingleValueEditModel;

import java.io.Serializable;
import java.util.List;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_MASTER_GAME_INFO;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_MASTER_GAME_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_MASTER_GAME_NAME;

public class MasterGameSection extends StaticFieldsSection {
    public MasterGameSection(List<StaticItem> data) {
        super(ELEMENT_TYPE_MASTER_GAME_INFO, data);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, RecyclerView.Adapter adapter) {
        Serializable value = data.get(position).getValue();
        switch (holder.getItemViewType()) {
            case TYPE_MASTER_GAME_NAME:
            case TYPE_MASTER_GAME_DESCRIPTION:
                ((SingleValueEditViewHolder) holder).bind((EditableSingleValueEditModel) value);
                break;

        }

        super.onBindViewHolder(holder, position, adapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TYPE_MASTER_GAME_NAME:
            case TYPE_MASTER_GAME_DESCRIPTION:
                v = inflater.inflate(R.layout.base_edit_item, parent, false);
                viewHolder = new SingleValueEditViewHolder(v);
                break;
        }
        if (viewHolder != null) {
            return viewHolder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }
}
      