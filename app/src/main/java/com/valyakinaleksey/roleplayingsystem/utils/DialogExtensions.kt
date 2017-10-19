@file:JvmName("DialogExtensionsKt")

package com.valyakinaleksey.roleplayingsystem.utils

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding.widget.RxTextView
import com.rengwuxian.materialedittext.MaterialEditText
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGamePresenter
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasCreateGameViewModel
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasPasswordViewModel
import rx.subscriptions.CompositeSubscription


fun Context.showPasswordDialog(data: HasPasswordViewModel,
    createGamePresenter: CreateGamePresenter): MaterialDialog {
  val passwordDialogViewModel = data.passwordDialogViewModel
  val compositeSubscription = CompositeSubscription()
  val dialog = MaterialDialog.Builder(this).title(R.string.input_password)
      .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
      .input(getString(R.string.password), passwordDialogViewModel.inputPassword, false
      ) { dialog1, input ->
        createGamePresenter
            .validatePassword(this, input.toString(),
                passwordDialogViewModel.gameModel)
      }
      .dismissListener({ dialog1 ->
        data.passwordDialogViewModel = null
        compositeSubscription.unsubscribe()
      })
      .show()
  compositeSubscription.add(
      RxTextView.textChanges(dialog.view.findViewById(android.R.id.input))
          .skip(1)
          .subscribe { charSequence ->
            passwordDialogViewModel.inputPassword = charSequence.toString()
          })
  return dialog
}


fun Context.showCreateGameDialog(data: HasCreateGameViewModel,
    createGamePresenter: CreateGamePresenter): MaterialDialog {
  val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_game_content,
      null)
  val etName = dialogView.findViewById<View>(R.id.name) as MaterialEditText
  val etDescription = dialogView.findViewById<View>(R.id.description) as MaterialEditText
  val etPassword = dialogView.findViewById<View>(R.id.password) as MaterialEditText
  val dialogData = data.createGameDialogViewModel
  val gameModel = dialogData.gameModel
  etName.setText(gameModel.name)
  etDescription.setText(gameModel.description)
  etPassword.setText(gameModel.password)
  val compositeSubscription = CompositeSubscription()
  val dialog = MaterialDialog.Builder(this).title(dialogData.title)
      .customView(dialogView, true)
      .autoDismiss(false)
      .positiveText(android.R.string.ok)
      .negativeText(android.R.string.cancel)
      .onPositive({ dialog, which ->
        gameModel.name = gameModel.name.trim({ it <= ' ' })
        gameModel.description = gameModel.description.trim({ it <= ' ' })
        createGamePresenter.createGame(dialogData.gameModel)
        dialog.dismiss()
      })
      .onNegative({ dialog, which -> dialog.dismiss() })
      .dismissListener({ dialog1 ->
        data.createGameDialogViewModel = null
        compositeSubscription.unsubscribe()
      })
      .build()
  val actionButton = dialog.getActionButton(DialogAction.POSITIVE)
  if (TextUtils.isEmpty(gameModel.name)) {
    actionButton.isEnabled = false
  }
  etName.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
      if (s.toString().startsWith(" ")) {
        s.delete(0, 1)
      }
    }
  })
  compositeSubscription.add(RxTextView.textChanges(etName).skip(1).subscribe { charSequence ->
    actionButton.isEnabled = !TextUtils.isEmpty(charSequence)
    gameModel.name = charSequence.toString()
  })
  compositeSubscription.add(RxTextView.textChanges(etDescription).subscribe { charSequence ->
    gameModel.description = charSequence.toString()
  })
  compositeSubscription.add(RxTextView.textChanges(etPassword).subscribe { charSequence ->
    gameModel.password = charSequence.toString()
  })
  dialog.show()
  etName.post {
    etName.setSelection(etName.length())
    KeyboardUtils.showSoftKeyboard(etName)
  }
  return dialog
}