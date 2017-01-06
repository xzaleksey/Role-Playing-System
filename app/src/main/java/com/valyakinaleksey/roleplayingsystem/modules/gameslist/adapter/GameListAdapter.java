package com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter.GamesListPresenter;

public class GameListAdapter extends FirebaseRecyclerAdapter<GameModel, GameViewHolder> {
    private final GamesListPresenter gamesListPresenter;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
*                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     * @param gamesListPresenter
     */
    public GameListAdapter(Class<GameModel> modelClass, int modelLayout, Class<GameViewHolder> viewHolderClass, Query ref, GamesListPresenter gamesListPresenter) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.gamesListPresenter = gamesListPresenter;
    }

    @Override
    protected void populateViewHolder(GameViewHolder viewHolder, GameModel model, int position) {
        viewHolder.bind(model, gamesListPresenter);
    }
}
      