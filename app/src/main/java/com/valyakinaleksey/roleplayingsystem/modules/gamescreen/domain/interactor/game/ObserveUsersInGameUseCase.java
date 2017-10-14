package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import rx.Observable;

public class ObserveUsersInGameUseCase implements ObserveUsersInGameInteractor {
    UserRepository userRepository;

    public ObserveUsersInGameUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Observable<UserInGameModel> observeUserJoinedToGame(String id) {
        return userRepository
                .observeUsersInGame(id)
                .filter(firebaseChildEvent -> {
                    RxFirebaseChildEvent.EventType eventType = firebaseChildEvent.getEventType();
                    return eventType == RxFirebaseChildEvent.EventType.ADDED || eventType == RxFirebaseChildEvent.EventType.REMOVED;
                }).concatMap(firebaseChildEvent -> {
                    DataSnapshot dataSnapshot = firebaseChildEvent.getValue();
                    UserInGameModel value = dataSnapshot.getValue(UserInGameModel.class);
                    value.setEventType(firebaseChildEvent.getEventType());
                    return userRepository.getUserByUid(value.getUid())
                            .map(user -> {
                                value.setUser(user);
                                return value;
                            });
                });
    }
}
      