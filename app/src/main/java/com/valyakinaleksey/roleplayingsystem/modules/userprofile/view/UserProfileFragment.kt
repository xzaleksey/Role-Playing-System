package com.valyakinaleksey.roleplayingsystem.modules.userprofile.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileComponent
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileModule
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel
import com.valyakinaleksey.roleplayingsystem.utils.StorageUtils
import com.valyakinaleksey.roleplayingsystem.utils.glide.CircleTransformWithTwoBorders
import com.valyakinaleksey.roleplayingsystem.utils.showPasswordDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    public override fun setupViews(view: View) {
        super.setupViews(view)
        iv_back.setOnClickListener { activity?.onBackPressed() }
        iv_edit.setOnClickListener { component.presenter.editProfile() }
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

    override fun onStart() {
        super.onStart()
        (parentFragment as? ParentView)?.hideAppBar()
    }

    override fun loadData() {
        component.presenter.getData()
    }

    override fun showContent() {
        super.showContent()
        if (data.isCurrentUser) {
            iv_edit.visibility = View.VISIBLE
        }
        display_name.text = data.displayName
        tv_email.text = data.email
        tv_master_games_count.text = data.masterGamesCount
        tv_total_games_count.text = data.totalGamesCount
        if (data.avatarUrl.isNullOrBlank()) {
            loadImage(StorageUtils.resourceToUri(R.drawable.profile_icon))
        } else {
            loadImage(Uri.parse(data.avatarUrl))
        }
        flexibleAdapter.updateDataSet(data.items, true)
        if (data.passwordDialogViewModel != null && dialogIsNotShowing()) {
            showPasswordDialog()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = ContextCompat.getColor(activity, R.color.colorStatusBarProfile)
        }
    }

    override fun onDestroyView() {
        (parentFragment as? ParentView)?.showAppBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = ContextCompat.getColor(activity, R.color.primary_dark)
        }
        super.onDestroyView()
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
        if (avatar.drawable != null) {
            return
        }
        Glide.with(context)
                .load(uri)
                .error(R.drawable.profile_icon)
                .transform(CircleTransformWithTwoBorders(context,
                        ContextCompat.getColor(context, android.R.color.white),
                        ContextCompat.getColor(context, R.color.accent)))
                .into(avatar)
    }

    companion object {
        val TAG = UserProfileFragment::class.java.simpleName!!
        fun newInstance(): UserProfileFragment = UserProfileFragment()
    }
}
