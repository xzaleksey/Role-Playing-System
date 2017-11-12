package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;
import org.joda.time.DateTime;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAMES;

public class CreateNewGameUseCase implements CreateNewGameInteractor {

    private SimpleCrypto simpleCrypto;
    private UserRepository userRepository;

    public CreateNewGameUseCase(SimpleCrypto simpleCrypto, UserRepository userRepository) {
        this.simpleCrypto = simpleCrypto;
        this.userRepository = userRepository;
    }

    @Override
    public Observable<String> createNewGame(GameModel gameModel) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tableReference = FireBaseUtils.getTableReference(GAMES);
        DatabaseReference push = tableReference.push();
        String key = push.getKey();
        gameModel.setId(key);
        String currentUserId = FireBaseUtils.getCurrentUserId();
        gameModel.setMasterId(currentUserId);
        return userRepository.getUserByUid(currentUserId)
                .doOnNext(user -> {
                    gameModel.setMasterName(user.getName());
                    gameModel.setTempDateCreate(DateTime.now().getMillis());
                    if (!TextUtils.isEmpty(gameModel.getPassword())) { //user set password, try to encrypt it
                        try {
                            String password = simpleCrypto.encrypt(gameModel.getPassword());
                            gameModel.setPassword(password);
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                        }
                    }
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(getGameReference(key), gameModel.toGameModelMap());
                    childUpdates.put(getGameInUserReference(key, currentUserId), gameModel.toGameInUserMap());
                    databaseReference.updateChildren(childUpdates);
                    push.child(FireBaseUtils.TEMP_DATE_CREATE).setValue(null);
                }).map(user -> key);
    }

    private String getGameInUserReference(String key, String currentUserId) {
        return String.format(FireBaseUtils.FORMAT_SLASHES, FireBaseUtils.GAMES_IN_USERS)
                + currentUserId
                + "/"
                + key;
    }

    @NonNull
    private String getGameReference(String key) {
        return String.format(FireBaseUtils.FORMAT_SLASHES, FireBaseUtils.GAMES) + key;
    }
}
      