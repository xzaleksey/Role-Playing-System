package com.valyakinaleksey.roleplayingsystem.modules.userprofile.view

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.afollestad.materialdialogs.MaterialDialog
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentView
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileComponent
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.di.UserProfileModule
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel
import com.valyakinaleksey.roleplayingsystem.utils.showPasswordDialog
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class UserProfileFragment : AbsButterLceFragment<UserProfileComponent, UserProfileViewModel, UserProfileView>(), UserProfileView {

    override fun createComponent(fragmentId: String): UserProfileComponent {
        return ((parentFragment as ComponentManagerFragment<*, *>).component as ParentFragmentComponent).getUserProfileComponent(
                UserProfileModule(fragmentId))
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
        (activity as AbsActivity).setToolbarTitle(data.toolbarTitle)
        flexibleAdapter.updateDataSet(data.items, true)
        if (data.passwordDialogViewModel != null && dialogIsNotShowing()) {
            showPasswordDialog()
        }
    }

    override fun onDestroyView() {
        (parentFragment as? ParentView)?.showAppBar()
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

    companion object {
        val TAG = UserProfileFragment::class.java.simpleName!!
        fun newInstance(): UserProfileFragment = UserProfileFragment()
    }
}
