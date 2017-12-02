package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.kelvinapps.rxfirebase.RxFirebaseUser;
import com.valyakinaleksey.roleplayingsystem.core.model.ResponseModel;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import rx.Observable;

public class CurrentUserRepositoryImpl implements CurrentUserRepository {

    private GameRepository gameRepository;

    public CurrentUserRepositoryImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Observable<ResponseModel> updateDisplayName(String displayName) {
        FirebaseUser currentUser = FireBaseUtils.getCurrentUser();
        String uid = currentUser.getUid();
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setDisplayName(displayName);

//        if (!StringUtils.areEqual(user.getPhotoUrl(), currentUser.getDisplayName())) {
//            builder.setPhotoUri(Uri.parse(user.getPhotoUrl()));
//            updatedProfile = true;
//                    .concatMap(aVoid -> FireBaseUtils.setData(user.getPhotoUrl(), getDatabaseReference(currentUser)));
//        }


        return RxFirebaseUser.updateProfile(currentUser, builder.build())
                .concatMap(aVoid -> FireBaseUtils.setData(displayName, getDatabaseReference(currentUser).child(User.FIELD_DISPLAY_NAME))
                        .concatMap(aVoid1 -> gameRepository.getGamesByUserId(uid)
                                .take(1)
                                .doOnNext(stringGameModelMap -> {
                                    for (GameModel gameModel : stringGameModelMap.values()) {
                                        if (gameModel.isMaster(uid)) {
                                            FireBaseUtils.getTableReference(FireBaseUtils.GAMES)
                                                    .child(gameModel.getId()).child(GameModel.FIELD_MASTER_NAME).setValue(displayName);
                                        }
                                    }
                                }))).map(stringGameModelMap -> new ResponseModel());

    }

    private DatabaseReference getDatabaseReference(FirebaseUser currentUser) {
        return FireBaseUtils.getTableReference(FireBaseUtils.USERS).child(currentUser.getUid());
    }
}
