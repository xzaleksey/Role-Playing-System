@file:JvmName("FragmentExtensions")

package com.valyakinaleksey.roleplayingsystem.utils.extensions

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.view.AbsLceFragment
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType

inline fun <reified T : Fragment> createFragment(args: Bundle?): T {
    val fragment = T::class.java.newInstance()
    fragment.arguments = args
    return fragment
}

fun AbsLceFragment<*, *, *>.showExternalReadWritePermission(successCallback: () -> Unit) {
    Dexter.withActivity(getActivity())
            .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.deniedPermissionResponses.isEmpty()) {
                        successCallback.invoke()
                    } else {
                        showError(BaseError(BaseErrorType.SNACK, getString(R.string.denied_external)))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()

}