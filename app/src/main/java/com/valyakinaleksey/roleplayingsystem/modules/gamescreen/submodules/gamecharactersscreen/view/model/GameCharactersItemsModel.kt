package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model

import android.os.Parcel
import android.os.Parcelable
import eu.davidea.flexibleadapter.items.IFlexible
import java.io.Serializable

class GameCharactersItemsModel(@Transient val iFlexibles: List<IFlexible<*>> = emptyList(),
    val hasCharacter: Boolean = false) : Serializable, Parcelable {

  constructor(source: Parcel) : this()

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<GameCharactersItemsModel> = object : Parcelable.Creator<GameCharactersItemsModel> {
      override fun createFromParcel(
          source: Parcel): GameCharactersItemsModel = GameCharactersItemsModel(source)

      override fun newArray(size: Int): Array<GameCharactersItemsModel?> = arrayOfNulls(size)
    }
  }
}