package com.valyakinaleksey.roleplayingsystem.data.repository.user;

import android.net.Uri;
import com.valyakinaleksey.roleplayingsystem.core.model.ResponseModel;
import rx.Observable;

public interface CurrentUserRepository {

    Observable<ResponseModel> updateDisplayName(String displayName);

    Observable<String> updatePhoto(Uri uri);
}
      