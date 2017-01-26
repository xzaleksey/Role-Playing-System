package com.valyakinaleksey.roleplayingsystem.core.firebase;

import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.List;

public abstract class MyFireBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private FirebaseArray mSnapshots;

  public MyFireBaseAdapter(Query ref) {
    mSnapshots = new FirebaseArray(ref);
    mSnapshots.setOnChangedListener(new FirebaseArray.OnChangedListener() {
      @Override public void onChanged(EventType type, int index, int oldIndex) {
        switch (type) {
          case ADDED:
            notifyItemInserted(index);
            break;
          case CHANGED:
            notifyItemChanged(index);
            break;
          case REMOVED:
            notifyItemRemoved(index);
            break;
          case MOVED:
            notifyItemMoved(oldIndex, index);
            break;
          default:
            throw new IllegalStateException("Incomplete case statement");
        }
      }

      @Override public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  public void cleanup() {
    mSnapshots.cleanup();
  }

  @Override public int getItemCount() {
    return mSnapshots.getCount();
  }

  public DatabaseReference getRef(int position) {
    return mSnapshots.getItem(position).getRef();
  }

  public <T> T getItem(int position, Class<T> tClass) {
    return mSnapshots.getItem(position).getValue(tClass);
  }

  public static class FirebaseArray implements ChildEventListener {
    public interface OnChangedListener {
      enum EventType {ADDED, CHANGED, REMOVED, MOVED}

      void onChanged(EventType type, int index, int oldIndex);

      void onCancelled(DatabaseError databaseError);
    }

    private Query mQuery;
    private OnChangedListener mListener;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public FirebaseArray(Query ref) {
      mQuery = ref;
      mQuery.addChildEventListener(this);
    }

    public void cleanup() {
      mQuery.removeEventListener(this);
    }

    public int getCount() {
      return mSnapshots.size();
    }

    public DataSnapshot getItem(int index) {
      return mSnapshots.get(index);
    }

    private int getIndexForKey(String key) {
      int index = 0;
      for (DataSnapshot snapshot : mSnapshots) {
        if (snapshot.getKey().equals(key)) {
          return index;
        } else {
          index++;
        }
      }
      throw new IllegalArgumentException("Key not found");
    }

    @Override public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
      int index = 0;
      if (previousChildKey != null) {
        index = getIndexForKey(previousChildKey) + 1;
      }
      mSnapshots.add(index, snapshot);
      notifyChangedListeners(OnChangedListener.EventType.ADDED, index);
    }

    @Override public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
      int index = getIndexForKey(snapshot.getKey());
      mSnapshots.set(index, snapshot);
      notifyChangedListeners(OnChangedListener.EventType.CHANGED, index);
    }

    @Override public void onChildRemoved(DataSnapshot snapshot) {
      int index = getIndexForKey(snapshot.getKey());
      mSnapshots.remove(index);
      notifyChangedListeners(OnChangedListener.EventType.REMOVED, index);
    }

    @Override public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
      int oldIndex = getIndexForKey(snapshot.getKey());
      mSnapshots.remove(oldIndex);
      int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
      mSnapshots.add(newIndex, snapshot);
      notifyChangedListeners(OnChangedListener.EventType.MOVED, newIndex, oldIndex);
    }

    @Override public void onCancelled(DatabaseError error) {
      notifyCancelledListeners(error);
    }

    public void setOnChangedListener(OnChangedListener listener) {
      mListener = listener;
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index) {
      notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index,
        int oldIndex) {
      if (mListener != null) {
        mListener.onChanged(type, index, oldIndex);
      }
    }

    protected void notifyCancelledListeners(DatabaseError databaseError) {
      if (mListener != null) {
        mListener.onCancelled(databaseError);
      }
    }
  }
}
      