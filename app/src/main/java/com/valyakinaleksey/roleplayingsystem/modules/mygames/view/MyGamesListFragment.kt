package com.valyakinaleksey.roleplayingsystem.modules.mygames.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.afollestad.materialdialogs.MaterialDialog
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListComponent
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListModule
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import com.valyakinaleksey.roleplayingsystem.utils.extensions.getStatusBarHeight
import com.valyakinaleksey.roleplayingsystem.utils.extensions.getToolBarHeight
import com.valyakinaleksey.roleplayingsystem.utils.extensions.showCreateGameDialog
import com.valyakinaleksey.roleplayingsystem.utils.extensions.showPasswordDialog
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll.HideFablListener
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.toolbar_with_background.*




class MyGamesListFragment : AbsButterLceFragment<MyGamesListComponent, MyGamesListViewViewModel, MyGamesListView>(), MyGamesListView {

    override fun createComponent(fragmentId: String): MyGamesListComponent {
        return ((parentFragment as ComponentManagerFragment<*, *>).component as ParentFragmentComponent).getMyGamesListComponent(
                MyGamesListModule(fragmentId))
    }

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.fab) lateinit var fab: FloatingActionButton
    private var dialog: MaterialDialog? = null
    private lateinit var flexibleAdapter: FlexibleAdapter<IFlexible<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    public override fun setupViews(view: View) {
        super.setupViews(view)
        setupFabButton()
        setupRecyclerView()
        val statusBarHeight = context.getStatusBarHeight()
        val topMargin = resources.getDimensionPixelOffset(R.dimen.common_margin_between_elements)
        toolbar_container.layoutParams?.let {
            it.height = statusBarHeight + context.getToolBarHeight() + topMargin
            toolbar_container.layoutParams = it
        }
        (top_toolbar_container.layoutParams as ViewGroup.MarginLayoutParams).let {
            it.topMargin = statusBarHeight + topMargin
            top_toolbar_container.layoutParams = it
        }
        top_toolbar_container.setOnClickListener {
            initSearch()
        }
        action_icon.setOnClickListener {
            if (search_view.visibility == View.GONE) {
                initSearch()
            } else {
                closeSearch()
            }
        }
        clear_icon.setOnClickListener {
            if (search_view.text.isEmpty()) {
                closeSearch()
            } else {
                search_view.setText(StringUtils.EMPTY_STRING)
            }
        }
        more.setOnClickListener {
            val popup = PopupMenu(context, more)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.main_menu, popup.menu)
            popup.show()
        }
    }

    private fun closeSearch() {
        KeyboardUtils.hideKeyboard(activity)
        tv_title.visibility = View.VISIBLE
        search_view.visibility = View.GONE
        clear_icon.visibility = View.GONE
        action_icon.setImageResource(R.drawable.ic_search_black_24dp)
    }

    private fun initSearch() {
        tv_title.visibility = View.GONE
        clear_icon.visibility = View.VISIBLE
        search_view.visibility = View.VISIBLE
        action_icon.setImageResource(R.drawable.ic_arrow_back)
        KeyboardUtils.showSoftKeyboard(search_view, 50)
    }

    private fun setupRecyclerView() {
        flexibleAdapter = FlexibleAdapter(data?.items ?: mutableListOf())
        flexibleAdapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { pos ->
            val item = flexibleAdapter.getItem(pos)
            component.presenter.onItemClicked(item)
        }
        recyclerView.addOnScrollListener(HideFablListener(fab))
        recyclerView.adapter = flexibleAdapter
    }

    override fun loadData() {
        component.presenter.getData()
    }

    override fun showContent() {
        super.showContent()
        flexibleAdapter.updateDataSet(data.items, true)
        if (data.createGameDialogViewModel != null && dialogIsNotShowing()) {
            showCreateGameDialog()
        }
        if (data.passwordDialogViewModel != null && dialogIsNotShowing()) {
            showPasswordDialog()
        }
    }

    private fun dialogIsNotShowing() = dialog?.isShowing != true

    override fun getContentResId(): Int = R.layout.fragment_my_games_list

    override fun onGameCreated() {
        recyclerView.smoothScrollToPosition(recyclerView.adapter.itemCount)
    }

    override fun showCreateGameDialog() {
        dialog = context?.showCreateGameDialog(data, component.presenter)
    }

    override fun showPasswordDialog() {
        dialog = context?.showPasswordDialog(data, component.presenter)
    }


    private fun setupFabButton() {
        fab.setOnClickListener { v -> component.presenter.onFabPressed() }
    }

    override fun updateList(iFlexibles: MutableList<IFlexible<*>>) {
        flexibleAdapter.updateDataSet(iFlexibles, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtils.hideKeyboard(activity)
    }

    companion object {

        val TAG = MyGamesListFragment::class.java.simpleName!!

        fun newInstance(): MyGamesListFragment = MyGamesListFragment()
    }
}
