package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.firebase.MyFireBaseAdapter;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.AddItemViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ExpandableTitleViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_ADD_EDIT_SECTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_EXPANDABLE_TITLE;

public class MasterLogAdapter extends MyFireBaseAdapter<MasterLogMessage> {
  public static final int TYPE_SIMPLE_LOG = 1;

  public MasterLogAdapter(Query ref) {
    super(ref, MasterLogMessage.class);
    setOnChangedListener(new FirebaseArray.OnChangedListener() {
      @Override public void onChanged(EventType type, int index, int oldIndex) {
        switch (type) {
          case REMOVED:
            if (getItemCount() > index + 1) {
              notifyItemChanged(index + 1);
            }
            break;
        }
      }

      @Override public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder viewHolder = null;
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View v;
    switch (viewType) {
      case TYPE_SIMPLE_LOG:
        v = inflater.inflate(R.layout.master_log_item, parent, false);
        viewHolder = new MasterLogItemViewHolder(v);
        break;
    }
    return viewHolder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case TYPE_SIMPLE_LOG:
        MasterLogMessage item = getItem(position);
        DateTime dateTime = new DateTime(item.getDateCreateLong()).withTimeAtStartOfDay();
        DateTime previousDateTime = null;
        if (position - 1 >= 0) {
          MasterLogMessage previousItem = getItem(position - 1);
          previousDateTime = new DateTime(previousItem.getDateCreateLong()).withTimeAtStartOfDay();
        }

        ((MasterLogItemViewHolder) holder).bind(item, !dateTime.equals(previousDateTime));
        break;
    }
  }

  @Override public int getItemViewType(int position) {
    return TYPE_SIMPLE_LOG;
  }

  static class HeaderHolder extends ButterKnifeViewHolder {
    @Bind(R.id.tv_date) public TextView header;

    public HeaderHolder(View itemView) {
      super(itemView);
    }
  }
}
      