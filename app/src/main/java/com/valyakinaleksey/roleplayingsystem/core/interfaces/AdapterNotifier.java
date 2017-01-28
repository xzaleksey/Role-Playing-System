package com.valyakinaleksey.roleplayingsystem.core.interfaces;

public interface AdapterNotifier {
  void notifyItemInserted(int index);

  void notifyItemChanged(int index);

  void notifyItemRemoved(int index);

  void notifyItemMoved(int oldIndex, int index);
}
      