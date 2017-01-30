package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import java.io.Serializable;
import java.util.ArrayList;

public interface InfoSection<T extends Serializable> extends Serializable, Parcelable {
  int getSectionType();

  boolean isActive();

  void setActive(boolean active);

  void setTitle(String title);

  ArrayList<T> getData();

  void setData(ArrayList<T> data);

  String getTitle();

  int getItemCount();

  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

  void onBindViewHolder(RecyclerView.ViewHolder holder, int position, RecyclerView.Adapter adapter);

  int getItemViewType(int position);

  void update(RecyclerView.Adapter sectionsAdapter, int sectionPosition, DataEvent dataEvent);
}
