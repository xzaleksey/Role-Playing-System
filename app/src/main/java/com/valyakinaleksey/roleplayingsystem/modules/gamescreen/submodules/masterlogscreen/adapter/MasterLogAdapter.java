package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class MasterLogAdapter
    extends FirebaseRecyclerAdapter<MasterLogMessage, MasterLogItemViewHolder>
    implements StickyHeaderAdapter<MasterLogAdapter.HeaderHolder> {

  private List<Long> dates = new ArrayList<>();

  public MasterLogAdapter(Class<MasterLogMessage> modelClass, int modelLayout,
      Class<MasterLogItemViewHolder> viewHolderClass, Query ref) {
    super(modelClass, modelLayout, viewHolderClass, ref);
  }

  @Override protected void onCancelled(DatabaseError error) {
    Crashlytics.logException(new RuntimeException(
        error.getMessage() + " " + error.getCode() + " " + error.getDetails()));
  }

  @Override
  protected void populateViewHolder(MasterLogItemViewHolder viewHolder, MasterLogMessage model,
      int position) {
    viewHolder.bind(model);
  }

  @Override public long getHeaderId(int position) {
    long dateCreate =
        new DateTime(getItem(position).getDateCreate()).withTimeAtStartOfDay().getMillis();
    int index = dates.indexOf(dateCreate);
    if (index > 0) {
      return index;
    } else {
      if (!dates.contains(dateCreate)) {
        dates.add(0, dateCreate);
      }
      return dates.indexOf(dateCreate);
    }
  }

  @Override public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    final View view = layoutInflater.inflate(R.layout.master_log_header, parent, false);
    return new HeaderHolder(view);
  }

  @Override public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
    viewholder.header.setText(new DateTime(dates.get(position)).toString(
        DateTimeFormat.shortDate().withLocale(Locale.getDefault())));
  }

  static class HeaderHolder extends ButterKnifeViewHolder {
    @Bind(R.id.tv_date) public TextView header;

    public HeaderHolder(View itemView) {
      super(itemView);
    }
  }
}
      