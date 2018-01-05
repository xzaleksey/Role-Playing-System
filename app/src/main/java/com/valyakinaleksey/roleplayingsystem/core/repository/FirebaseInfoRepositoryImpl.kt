package com.valyakinaleksey.roleplayingsystem.core.repository

import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils

class FirebaseInfoRepositoryImpl : FirebaseInfoRepository {

    override fun getCurrentUserUid(): String = FireBaseUtils.getCurrentUserId()
}