package com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

public class GameListAdapter extends FirebaseRecyclerAdapter<GameModel, GameViewHolder> {
    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public GameListAdapter(Class<GameModel> modelClass, int modelLayout, Class<GameViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(GameViewHolder viewHolder, GameModel model, int position) {
        viewHolder.bind(model);
    }
}
      