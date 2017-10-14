package com.valyakinaleksey.roleplayingsystem.modules.mygames.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.afollestad.materialdialogs.MaterialDialog
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.persistence.ComponentManagerFragment
import com.valyakinaleksey.roleplayingsystem.core.ui.AbsButterLceFragment
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.DaggerMyGamesListComponent
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListComponent
import com.valyakinaleksey.roleplayingsystem.modules.mygames.di.MyGamesListModule
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.MyGamesListViewViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll.HideFablListener
import com.valyakinaleksey.roleplayingsystem.utils.showCreateGameDialog
import com.valyakinaleksey.roleplayingsystem.utils.showPasswordDialog
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class MyGamesListFragment : AbsButterLceFragment<MyGamesListComponent, MyGamesListViewViewModel, MyGamesListView>(), MyGamesListView {
  override fun createComponent(): MyGamesListComponent {
    return DaggerMyGamesListComponent.builder()
        .parentFragmentComponent(
            (parentFragment as ComponentManagerFragment<*, *>).component as ParentFragmentComponent)
        .myGamesListModule(MyGamesListModule())
        .build()
  }

  @BindView(R.id.recycler_view)
  lateinit var recyclerView: RecyclerView
  @BindView(R.id.fab) lateinit var fab: FloatingActionButton
  private var dialog: MaterialDialog? = null
  lateinit var flexibleAdapter: FlexibleAdapter<IFlexible<*>>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component.inject(this)
  }

  public override fun setupViews(view: View) {
    super.setupViews(view)
    setupFabButton()
    setupRecyclerView()
  }

  private fun setupRecyclerView() {
    flexibleAdapter = FlexibleAdapter(mutableListOf())
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
    (activity as AbsActivity).setToolbarTitle(data.toolbarTitle)
    flexibleAdapter.updateDataSet(data.items, true)
    if (data.createGameDialogViewModel != null && dialogIsNotShowing()) {
      showCreateGameDialog()
    }
    if (data.passwordDialogViewModel != null && dialogIsNotShowing()) {
      showPasswordDialog()
    }
  }

  private fun dialogIsNotShowing() = dialog?.isShowing != true

  override fun getContentResId(): Int {
    return R.layout.fragment_my_games_list
  }

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

  companion object {

    val TAG = MyGamesListFragment::class.java.simpleName

    fun newInstance(): MyGamesListFragment {
      return MyGamesListFragment()
    }
  }
}
