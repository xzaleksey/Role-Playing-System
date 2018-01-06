package com.valyakinaleksey.roleplayingsystem.core.repository

import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils

class FirebaseInfoRepositoryImpl : FirebaseInfoRepository {

    override fun getCurrentUserUid(): String = FireBaseUtils.getCurrentUserId()
}