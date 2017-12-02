package com.valyakinaleksey.roleplayingsystem.data.repository.user

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.kelvinapps.rxfirebase.RxFirebaseStorage
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.PathManager
import id.zelory.compressor.Compressor
import rx.Observable
import java.io.File

class AvatarUploadRepositoryImpl(private val pathManager: PathManager) : AvatarUploadRepository {
    private val avatars = "AVATARS"

    override fun uploadAvatar(fileUri: Uri): Observable<String> {
        val currentUserId = FireBaseUtils.getCurrentUserId()
        val reference = FirebaseStorage.getInstance().reference.child(avatars).child(currentUserId)

        return createLocalFileCopy(File(fileUri.toString())).switchMap { file ->
            return@switchMap RxFirebaseStorage.putFile(reference, Uri.fromFile(file)).map {
                return@map it.downloadUrl.toString()
            }
        }
    }

    fun createLocalFileCopy(file: File): Observable<File> {
        return Observable.just(file).compose(RxTransformers.applyIoSchedulers()).map { file1 ->
            val newDirectory = getLocalDirectory(FireBaseUtils.getCurrentUserId())
            val compressor = Compressor.Builder(RpsApp.app())
                    .setDestinationDirectoryPath(newDirectory)
                    .setQuality(100)
                    .build()
            compressor.compressToFile(file)
        }
    }

    private fun getLocalDirectory(key: String): String {
        return pathManager.avatarsDir + key
    }
}