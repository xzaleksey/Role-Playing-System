package com.valyakinaleksey.roleplayingsystem.modules.mygames.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding.widget.RxTextView
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListComponent
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListModule
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragment
import com.valyakinaleksey.roleplayingsystem.utils.HideKeyBoardOnScrollListener
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
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val DEBOUNCE_TIMEOUT = 200
private const val THROTTLE_INTERVAL = 100

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
            search_view.setText(StringUtils.EMPTY_STRING)
        }
        more.setOnClickListener {
            val popup = PopupMenu(context, more)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.main_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                (parentFragment as ParentFragment).onOptionsItemSelected(item)
            }
            popup.show()
        }
        if (data != null && !data.filterModel.isEmpty()) {
            initSearch()
        }
    }

    private fun closeSearch() {
        KeyboardUtils.hideKeyboard(activity)
        tv_title.visibility = View.VISIBLE
        search_view.visibility = View.GONE
        search_view.setText(StringUtils.EMPTY_STRING)
        action_icon.setImageResource(R.drawable.ic_search_black_24dp)
    }

    private fun initSearch() {
        tv_title.visibility = View.GONE
        search_view.visibility = View.VISIBLE
        action_icon.setImageResource(R.drawable.ic_arrow_back)
        val query = data.filterModel.getQuery()
        search_view.setText(query)
        KeyboardUtils.showSoftKeyboard(search_view, 50)
        clear_icon.visibility = if (query.isBlank()) View.GONE else View.VISIBLE
    }

    private fun setupRecyclerView() {
        flexibleAdapter = FlexibleAdapter(data?.items ?: mutableListOf())
        flexibleAdapter.mItemClickListener = FlexibleAdapter.OnItemClickListener { pos ->
            val item = flexibleAdapter.getItem(pos)
            component.presenter.onItemClicked(item)
        }
        recyclerView.addOnScrollListener(HideFablListener(fab))
        recyclerView.addOnScrollListener(HideKeyBoardOnScrollListener())
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

    override fun onStart() {
        super.onStart()
        compositeSubscription.add(RxTextView.afterTextChangeEvents(search_view)
                .doOnNext {
                    clear_icon.visibility = if (it.editable().toString().isBlank()) View.GONE else View.VISIBLE
                }
                .debounce(DEBOUNCE_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .throttleLast(THROTTLE_INTERVAL.toLong(), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text ->
                    Timber.d("new query " + text.editable().toString())
                    component.presenter.onSearchQueryChanged(text.editable().toString())
                })
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
