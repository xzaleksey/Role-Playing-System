package com.valyakinaleksey.roleplayingsystem.modules.userprofile.view

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.afollestad.materialdialogs.MaterialDialog
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker.PICK_IMAGE_DEVICE
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenImage
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileComponent
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileModule
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel.CHANGE_USER_NAME
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils
import com.valyakinaleksey.roleplayingsystem.utils.StorageUtils
import com.valyakinaleksey.roleplayingsystem.utils.extensions.changeUserData
import com.valyakinaleksey.roleplayingsystem.utils.extensions.showExternalReadWritePermission
import com.valyakinaleksey.roleplayingsystem.utils.extensions.showPasswordDialog
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_my_profile.*

class UserProfileFragment : AbsButterLceFragment<UserProfileComponent, UserProfileViewModel, UserProfileView>(), UserProfileView {

    override fun createComponent(fragmentId: String): UserProfileComponent {
        return ((parentFragment as ComponentManagerFragment<*, *>).component as ParentFragmentComponent)
                .getUserProfileComponent(UserProfileModule(fragmentId))
    }

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView
    private var dialog: MaterialDialog? = null
    private lateinit var flexibleAdapter: FlexibleAdapter<IFlexible<*>>
    private var imagePicker: ImagePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        imagePicker = ImagePicker(this)
        imagePicker!!.setImagePickerCallback(getImagePickerCallback())
    }

    public override fun setupViews(view: View) {
        super.setupViews(view)
        iv_back.setOnClickListener { activity?.onBackPressed() }
        iv_edit.setOnClickListener { component.presenter.editProfile() }
        fab_image.setOnClickListener { showExternalReadWritePermission { component.presenter.onSelectAvatar() } }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        flexibleAdapter = FlexibleAdapter(data?.items ?: mutableListOf())
        flexibleAdapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { pos ->
            val item = flexibleAdapter.getItem(pos)
            component.presenter.onItemClicked(item)
        }
        recyclerView.adapter = flexibleAdapter
    }

    override fun loadData() {
        component.presenter.getData()
    }

    override fun showContent() {
        super.showContent()
        if (data.isCurrentUser) {
            iv_edit.visibility = View.VISIBLE
            fab_image.visibility = View.VISIBLE
        }
        preFillModel(data)
        flexibleAdapter.updateDataSet(data.items, true)
        if (data.passwordDialogViewModel != null && dialogIsNotShowing()) {
            showPasswordDialog()
        }
    }

    private fun dialogIsNotShowing() = dialog?.isShowing != true

    override fun getContentResId(): Int = R.layout.fragment_my_profile

    override fun showPasswordDialog() {
        dialog = context?.showPasswordDialog(data, component.presenter)
    }

    override fun updateList(iFlexibles: MutableList<IFlexible<*>>) {
        flexibleAdapter.updateDataSet(iFlexibles, true)
    }

    private fun loadImage(uri: Uri) {
        ImageUtils.loadAvatarWithErrorDrawable(avatar, uri, ContextCompat.getDrawable(context, R.drawable.profile_icon))
    }

    override fun getDialog(tag: String): Dialog {
        when {
            tag == CHANGE_USER_NAME -> {
                return context.changeUserData(data, component.presenter)
            }
        }
        throw IllegalArgumentException("Not implemented tag")
    }

    override fun preFillModel(data: UserProfileViewModel?) {
        data?.let {
            display_name.text = data.displayName
            tv_email.text = data.email
            tv_master_games_count.text = data.masterGamesCount
            tv_total_games_count.text = data.totalGamesCount

            if (data.avatarUrl.isNullOrBlank()) {
                loadImage(StorageUtils.resourceToUri(R.drawable.profile_icon))
            } else {
                loadImage(Uri.parse(data.avatarUrl))
            }
        }
    }

    override fun pickImage() {
        imagePicker!!.pickImage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = ImagePicker(this)
                    imagePicker!!.setImagePickerCallback(getImagePickerCallback())
                }
                imagePicker!!.submit(data)
            }
        }
    }

    private fun getImagePickerCallback(): ImagePickerCallback {
        return object : ImagePickerCallback {
            override fun onImagesChosen(list: List<ChosenImage>) {
                component.presenter.avatarImageChosen(list[0])
            }

            override fun onError(s: String) {
                showError(BaseError(BaseErrorType.SNACK, s))
            }
        }
    }

    companion object {
        val TAG = UserProfileFragment::class.java.simpleName!!

        fun newInstance(): UserProfileFragment = UserProfileFragment()
    }
}
