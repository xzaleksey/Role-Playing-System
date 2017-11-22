package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.kelvinapps.rxfirebase.RxFirebaseUser;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import rx.Observable;

public class CurrentUserRepositoryImpl implements CurrentUserRepository {

    @Override
    public Observable<Boolean> updateDisplayNameAndEmail(String displayName, String email) {
        FirebaseUser currentUser = FireBaseUtils.getCurrentUser();

        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        boolean updatedProfile = false;

        if (!displayName.equals(currentUser.getDisplayName())) {
            builder.setDisplayName(displayName);
            updatedProfile = true;
        }

//        if (!StringUtils.areEqual(user.getPhotoUrl(), currentUser.getDisplayName())) {
//            builder.setPhotoUri(Uri.parse(user.getPhotoUrl()));
//            updatedProfile = true;
//        }

        Observable<?> observable = Observable.just(true);

        if (updatedProfile) {
            observable = RxFirebaseUser.updateProfile(currentUser, builder.build())
                    .concatMap(aVoid -> FireBaseUtils.setData(displayName, getDatabaseReference(currentUser).child(User.FIELD_DISPLAY_NAME)));
//                    .concatMap(aVoid -> FireBaseUtils.setData(user.getPhotoUrl(), getDatabaseReference(currentUser)));
        }

        if (!StringUtils.areEqual(email, currentUser.getEmail())) {
            observable = observable.concatMap(o -> RxFirebaseUser.updateEmail(currentUser, email))
                    .concatMap(aVoid -> FireBaseUtils.setData(email, getDatabaseReference(currentUser).child(User.FIELD_EMAIL)));
        }

        return observable.map(o -> true);
    }

    private DatabaseReference getDatabaseReference(FirebaseUser currentUser) {
        return FireBaseUtils.getTableReference(FireBaseUtils.USERS).child(currentUser.getUid());
    }
}
