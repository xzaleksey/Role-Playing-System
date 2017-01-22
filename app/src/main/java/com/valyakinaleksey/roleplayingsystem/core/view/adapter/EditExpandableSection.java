package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.content.Context;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.AddItemViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ExpandableTitleViewHolder;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.util.List;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_ADD_EDIT_SECTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_VALUE_EDIT_SECTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_EXPANDABLE_TITLE;

public abstract class EditExpandableSection<T extends HasId> extends ExpandableSection<T> {
  private String addTitle;
  private transient Provider<T> tProvider;

  public EditExpandableSection(int sectionType, String title, String addTitle, List<T> data,
      Provider<T> tProvider) {
    super(sectionType, title, data);
    this.addTitle = addTitle;
    this.tProvider = tProvider;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder viewHolder = null;
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View v;
    switch (viewType) {
      case TYPE_EXPANDABLE_TITLE:
        v = inflater.inflate(R.layout.title_item_with_right_image, parent, false);
        viewHolder = new ExpandableTitleViewHolder(v);
        break;
      case TYPE_ADD_EDIT_SECTION:
        v = inflater.inflate(R.layout.title_item_with_right_image, parent, false);
        viewHolder = new AddItemViewHolder(v);
        break;
    }
    return viewHolder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
      RecyclerView.Adapter adapter) {
    switch (holder.getItemViewType()) {
      case TYPE_EXPANDABLE_TITLE:
        ((ExpandableTitleViewHolder) holder).bind(getTitle(), this, adapter);
        break;
      case TYPE_ADD_EDIT_SECTION:
        ((AddItemViewHolder) holder).bind(this, addTitle, adapter);
        break;
    }
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_EXPANDABLE_TITLE;
    }
    position--;
    if (position == data.size()) {
      return TYPE_ADD_EDIT_SECTION;
    }
    return TYPE_TWO_VALUE_EDIT_SECTION;
  }

  @Override public int getItemCount() {
    //name and add button
    return expanded ? super.getItemCount() + 1 : 1;
  }

  public void addItem(int adapterPosition, RecyclerView.Adapter adapter) {
    data.add(tProvider.getValue());
    adapter.notifyItemInserted(adapterPosition);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.addTitle);
  }

  protected EditExpandableSection(Parcel in) {
    super(in);
    this.addTitle = in.readString();
  }

  public void removeItem(Context context, T model, int adapterPosition,
      RecyclerView.Adapter adapter) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
    builder.title(StringUtils.getStringById(R.string.delete_characteristic));
    builder.negativeText(android.R.string.cancel);
    builder.positiveText(android.R.string.ok);
    if (TextUtils.isEmpty(model.getId())) {
      handleEmptyModelDelete(model, adapterPosition, adapter, builder);
    } else {
      handleExistingModelDelete(model, adapterPosition, adapter, builder);
    }
    builder.show();
  }

  protected void handleExistingModelDelete(T model, int adapterPosition,
      RecyclerView.Adapter adapter, MaterialDialog.Builder builder) {
    handleEmptyModelDelete(model, adapterPosition, adapter, builder);
  }

  protected void handleEmptyModelDelete(T model, int adapterPosition, RecyclerView.Adapter adapter,
      MaterialDialog.Builder builder) {
    builder.onPositive((dialog, which) -> {
      data.remove(model);
      adapter.notifyItemRangeRemoved(adapterPosition, 1);
    });
  }
}
      