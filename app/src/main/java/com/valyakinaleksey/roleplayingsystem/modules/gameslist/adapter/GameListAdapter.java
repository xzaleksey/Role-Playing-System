package com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

public class GameListAdapter extends FirebaseRecyclerAdapter<GameModel, GameViewHolder> {
    private final UserGetInteractor userGetInteractor;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     * @param userGetInteractor
     */
    public GameListAdapter(Class<GameModel> modelClass, int modelLayout, Class<GameViewHolder> viewHolderClass, Query ref, UserGetInteractor userGetInteractor) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.userGetInteractor = userGetInteractor;
    }

    @Override
    protected void populateViewHolder(GameViewHolder viewHolder, GameModel model, int position) {
        viewHolder.bind(model, userGetInteractor);
    }
}
      