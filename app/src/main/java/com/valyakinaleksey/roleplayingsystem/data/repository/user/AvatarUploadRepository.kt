package com.valyakinaleksey.roleplayingsystem.data.repository.user

import android.net.Uri
import rx.Observable

interface AvatarUploadRepository {
    fun uploadAvatar(fileUri: Uri): Observable<String>
}