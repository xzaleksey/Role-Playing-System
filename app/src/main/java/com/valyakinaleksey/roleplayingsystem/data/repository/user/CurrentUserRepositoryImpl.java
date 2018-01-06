package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Transaction;
import com.kelvinapps.rxfirebase.RxFirebaseUser;
import com.valyakinaleksey.roleplayingsystem.core.model.ResponseModel;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.FirebaseTable;

import rx.Observable;

public class CurrentUserRepositoryImpl implements CurrentUserRepository {

    private GameRepository gameRepository;
    private AvatarUploadRepository avatarUploadRepository;

    public CurrentUserRepositoryImpl(GameRepository gameRepository, AvatarUploadRepository avatarUploadRepository) {
        this.gameRepository = gameRepository;
        this.avatarUploadRepository = avatarUploadRepository;
    }

    @Override
    public Observable<ResponseModel> updateDisplayName(String displayName) {
        FirebaseUser currentUser = FireBaseUtils.getCurrentUser();
        String uid = currentUser.getUid();
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setDisplayName(displayName);

        return RxFirebaseUser.updateProfile(currentUser, builder.build())
                .concatMap(aVoid -> FireBaseUtils.setData(displayName, getDatabaseReference(currentUser).child(User.FIELD_DISPLAY_NAME))
                        .concatMap(aVoid1 -> gameRepository.getGamesByUserId(uid)
                                .take(1)
                                .switchMap(stringGameModelMap -> Observable.from(stringGameModelMap.values()))
                                .flatMap(gameModel -> FireBaseUtils.startTransaction(getGameReference(gameModel.getId()), data -> {
                                    GameModel gameModelInTransaction = data.getValue(GameModel.class);
                                    if (gameModelInTransaction == null) {
                                        return Transaction.success(data);
                                    }
                                    if (gameModelInTransaction.isMaster(uid)) {
                                        gameModelInTransaction.setMasterName(displayName);
                                        data.setValue(gameModelInTransaction);
                                    }
                                    return Transaction.success(data);
                                }))
                        ))
                .toList()
                .map(stringGameModelMap -> new ResponseModel());

    }

    private DatabaseReference getGameReference(String gameId) {
        return FireBaseUtils.getTableReference(FirebaseTable.GAMES).child(gameId);
    }


    @Override
    public Observable<String> updatePhoto(Uri uri) {
        FirebaseUser currentUser = FireBaseUtils.getCurrentUser();
        return avatarUploadRepository.uploadAvatar(uri)
                .concatMap(photoUrl -> {
                    UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                    builder.setPhotoUri(Uri.parse(photoUrl));
                    return RxFirebaseUser.updateProfile(currentUser, builder.build())
                            .concatMap(aVoid -> FireBaseUtils.setData(photoUrl, FireBaseUtils.getTableReference(FirebaseTable.USERS)
                                    .child(currentUser.getUid()).child(User.FIELD_PHOTO_URL))).map(aVoid -> photoUrl);
                });
    }

    private DatabaseReference getDatabaseReference(FirebaseUser currentUser) {
        return FireBaseUtils.getTableReference(FirebaseTable.USERS).child(currentUser.getUid());
    }
}
