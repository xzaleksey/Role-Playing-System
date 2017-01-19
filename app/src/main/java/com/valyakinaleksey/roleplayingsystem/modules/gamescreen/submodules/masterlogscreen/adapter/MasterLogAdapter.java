package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.adapter;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;

public class MasterLogAdapter extends FirebaseRecyclerAdapter<MasterLogMessage, MasterLogItemViewHolder> {

    public MasterLogAdapter(Class<MasterLogMessage> modelClass, int modelLayout, Class<MasterLogItemViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void onCancelled(DatabaseError error) {
        Crashlytics.logException(new RuntimeException(error.getMessage() + " " + error.getCode() + " " + error.getDetails()));
    }

    @Override
    protected void populateViewHolder(MasterLogItemViewHolder viewHolder, MasterLogMessage model, int position) {
        viewHolder.bind(model);
    }
}
      