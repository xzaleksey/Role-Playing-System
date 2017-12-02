package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import com.valyakinaleksey.roleplayingsystem.core.model.ResponseModel;
import rx.Observable;

public interface CurrentUserRepository {

    Observable<ResponseModel> updateDisplayName(String displayName);

}
      