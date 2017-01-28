package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.google.firebase.database.DataSnapshot;
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
                    FirebaseChildEvent.EventType eventType = firebaseChildEvent.getEventType();
                    return eventType == FirebaseChildEvent.EventType.ADDED || eventType == FirebaseChildEvent.EventType.REMOVED;
                }).concatMap(firebaseChildEvent -> {
                    DataSnapshot dataSnapshot = firebaseChildEvent.getDataSnapshot();
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
      