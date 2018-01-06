package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter

import android.os.Bundle
import com.kbeanie.multipicker.api.entity.ChosenImage
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.core.view.BaseError
import com.valyakinaleksey.roleplayingsystem.core.view.BaseErrorType
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.adapter.MapFlexibleViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapsInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsView
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.MapsViewModel
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter
import com.valyakinaleksey.roleplayingsystem.modules.photo.view.ImageFragment
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils.SUCCESS
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils
import eu.davidea.flexibleadapter.items.IFlexible
import timber.log.Timber

class MapsPresenterImpl(private val mapsInteractor: MapsInteractor,
    private val parentPresenter: ParentPresenter) : BasePresenter<MapsView, MapsViewModel>(), MapsPresenter {

  override fun initNewViewModel(arguments: Bundle): MapsViewModel {
    val mapsViewModel = MapsViewModel()
    val gameModel = arguments.getParcelable<GameModel>(GameModel.KEY)
    mapsViewModel.gameModel = gameModel
    mapsViewModel.isMaster = gameModel!!.masterId == FireBaseUtils.getCurrentUserId()
    return mapsViewModel
  }

  override fun getData() {
    super.getData()
    compositeSubscription.add(mapsInteractor.observeMaps(viewModel.gameModel, this)
        .compose(RxTransformers.applySchedulers())
        .subscribe({ model ->
          viewModel.mapModel = model
          view.showContent()
        }, { this.handleThrowable(it) }))
  }

  override fun loadComplete() {
    view.hideLoading()
  }

  override fun uploadImage(chosenImage: ChosenImage) {
    mapsInteractor.createNewMap(viewModel.gameModel, chosenImage)
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe({ mapModel -> Timber.d("success map create") }) { throwable ->
          if (view != null) {
            val snack = BaseError(BaseErrorType.SNACK, StringUtils.getStringById(R.string.error))
            view.showError(snack)
          }
        }
  }

  override fun changeMapVisibility(mapModel: MapModel, isChecked: Boolean) {
    mapsInteractor.changeMapVisibility(mapModel, isChecked)
  }

  override fun deleteMap(mapModel: MapModel) {
    mapsInteractor.deleteMap(mapModel)
        .compose(RxTransformers.applySchedulers())
        .subscribe({ aVoid ->

        }, { this.handleThrowable(it) })
  }

  override fun openImage(path: String, fileName: String) {
    val args = Bundle()
    args.putString(ImageFragment.IMAGE_URL, path)
    args.putString(ImageFragment.NAME, fileName)
    args.putBoolean(NavigationUtils.ADD_BACK_STACK, true)
    parentPresenter.navigateToFragment(NavigationScreen.IMAGE_FRAGMENT, args)
  }

  override fun onItemClick(item: IFlexible<*>?): Boolean {
    if (item is MapFlexibleViewModel) {
      val mapModel = item.mapModel
      if (mapModel.localFileExists()) {
        openImage(mapModel.localFile.absolutePath, mapModel.localFile.name)
        return true
      }
      if (mapModel.status == SUCCESS) {
        openImage(mapModel.photoUrl, mapModel.fileName)
        return true
      }
    }
    return false
  }
}
