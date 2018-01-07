package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter.MasterLogItemViewHolder
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

class MasterLogMessageViewModel(val masterLogMessage: MasterLogMessage) : AbstractFlexibleItem<MasterLogItemViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater,
                                  parent: ViewGroup?): MasterLogItemViewHolder {
        return MasterLogItemViewHolder(inflater.inflate(layoutRes, parent, false), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: MasterLogItemViewHolder, position: Int, payloads: List<*>?) {
        holder.bind(this)
    }

    override fun getLayoutRes(): Int {
        return R.layout.master_log_item
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MasterLogMessageViewModel

        if (masterLogMessage != other.masterLogMessage) return false

        return true
    }

    override fun hashCode(): Int {
        return masterLogMessage.hashCode()
    }
}