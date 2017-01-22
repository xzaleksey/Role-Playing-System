package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewholder.TwoValueEditViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.TwoValueEditModel;
import java.util.List;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_EXPANDABLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_VALUE_EDIT_SECTION;

public class TwoValueExpandableSectionImpl extends EditExpandableSection<TwoValueEditModel> {
  public TwoValueExpandableSectionImpl(String title, String addTitle, List<TwoValueEditModel> data,
      Provider<TwoValueEditModel> twoValueEditModelProvider) {
    super(ELEMENT_TYPE_EXPANDABLE, title, addTitle, data, twoValueEditModelProvider);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
    if (viewHolder != null) {
      return viewHolder;
    }
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View v;
    switch (viewType) {
      case TYPE_TWO_VALUE_EDIT_SECTION:
        v = inflater.inflate(R.layout.two_value_edit_item, parent, false);
        viewHolder = new TwoValueEditViewHolder(v);
        break;
    }
    return viewHolder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
      RecyclerView.Adapter adapter) {
    super.onBindViewHolder(holder, position, adapter);
    switch (holder.getItemViewType()) {
      case TYPE_TWO_VALUE_EDIT_SECTION:
        ((TwoValueEditViewHolder) holder).bind(data.get(position - 1), this, adapter);
        break;
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  protected TwoValueExpandableSectionImpl(Parcel in) {
    super(in);
  }

  public static final Creator<TwoValueExpandableSectionImpl> CREATOR =
      new Creator<TwoValueExpandableSectionImpl>() {
        @Override public TwoValueExpandableSectionImpl createFromParcel(Parcel source) {
          return new TwoValueExpandableSectionImpl(source);
        }

        @Override public TwoValueExpandableSectionImpl[] newArray(int size) {
          return new TwoValueExpandableSectionImpl[size];
        }
      };

  @Override protected void handleExistingModelDelete(TwoValueEditModel model, int adapterPosition,
      RecyclerView.Adapter adapter, MaterialDialog.Builder builder) {
    builder.onPositive((dialog, which) -> {
      data.remove(model);
      adapter.notifyItemRangeRemoved(adapterPosition, 1);
      model.getDeleteOnClickListener().onClick(model);
    });
  }
}
      