@file:JvmName("DialogExtensionsKt")

package com.valyakinaleksey.roleplayingsystem.utils.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding.widget.RxTextView
import com.rengwuxian.materialedittext.MaterialEditText
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGamePresenter
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasCreateGameViewModel
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasPasswordViewModel
import com.valyakinaleksey.roleplayingsystem.core.interfaces.PasswordPresenter
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter.UserProfilePresenter
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import rx.subscriptions.CompositeSubscription

private const val KEYBOARD_DELAY = 100L

fun Context.showPasswordDialog(data: HasPasswordViewModel,
                               createGamePresenter: PasswordPresenter): MaterialDialog {
    val passwordDialogViewModel = data.passwordDialogViewModel
    val compositeSubscription = CompositeSubscription()
    val dialog = MaterialDialog.Builder(this).title(passwordDialogViewModel.title)
            .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            .input(getString(R.string.password), passwordDialogViewModel.inputPassword, false
            ) { _, input ->
                createGamePresenter.validatePassword(input.toString(), passwordDialogViewModel.gameModel)
            }
            .onNegative({ dialog, _ -> dialog.dismiss() })
            .dismissListener({
                data.passwordDialogViewModel = null
                compositeSubscription.unsubscribe()
            })
            .show()
    compositeSubscription.add(RxTextView.textChanges(dialog.view.findViewById(android.R.id.input))
            .skip(1)
            .subscribe { charSequence ->
                passwordDialogViewModel.inputPassword = charSequence.toString()
            })
    return dialog
}

@SuppressLint("InflateParams")
fun Context.showCreateGameDialog(data: HasCreateGameViewModel,
                                 createGamePresenter: CreateGamePresenter): MaterialDialog {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_game_content,
            null)
    val etName = dialogView.findViewById<MaterialEditText>(R.id.name)
    val etDescription = dialogView.findViewById<MaterialEditText>(R.id.description)
    val etPassword = dialogView.findViewById<MaterialEditText>(R.id.password)
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
            .onPositive({ dialog, _ ->
                gameModel.name = gameModel.name.trim({ it <= ' ' })
                gameModel.description = gameModel.description.trim({ it <= ' ' })
                createGamePresenter.createGame(dialogData.gameModel)
                dialog.dismiss()
            })
            .onNegative({ dialog, _ -> dialog.dismiss() })
            .dismissListener({
                data.createGameDialogViewModel = null
                compositeSubscription.unsubscribe()
                KeyboardUtils.hideKeyboard(this as? Activity, KEYBOARD_DELAY)
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

@SuppressLint("InflateParams")
fun Context.changeUserData(userProfileGameViewModel: UserProfileViewModel,
                           userProfilePresenter: UserProfilePresenter): MaterialDialog {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_user_name_content, null)
    val etName = dialogView.findViewById<MaterialEditText>(R.id.name)

    etName.setText(userProfileGameViewModel.displayName)
    val errorEmptyField = StringUtils.getStringById(R.string.error_empty_field)
    val compositeSubscription = CompositeSubscription()
    val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            if (s.toString().startsWith(" ")) {
                s.delete(0, 1)
            }
        }
    }

    val dialog = MaterialDialog.Builder(this).title(StringUtils.getStringById(R.string.edit))
            .customView(dialogView, true)
            .autoDismiss(false)
            .positiveText(android.R.string.ok)
            .negativeText(android.R.string.cancel)
            .onNegative({ dialog, _ -> dialog.dismiss() })
            .cancelListener { dialogInterface -> dialogInterface.dismiss() }
            .onPositive({ dialog, _ ->
                val name = etName.text.toString().trim({ it <= ' ' })
                userProfilePresenter.onEditName(name)
                dialog.dismiss()
            })
            .dismissListener {
                etName.removeTextChangedListener(textWatcher)
                compositeSubscription.unsubscribe()
                KeyboardUtils.hideKeyboard(this as? Activity, KEYBOARD_DELAY)
            }
            .build()
    val actionButton = dialog.getActionButton(DialogAction.POSITIVE)
    if (TextUtils.isEmpty(etName.text)) {
        actionButton.isEnabled = false
    }
    compositeSubscription.add(RxTextView.textChanges(etName)
            .skip(1)
            .subscribe { charSequence ->
                when {
                    TextUtils.isEmpty(charSequence) -> etName.error = String.format(errorEmptyField, getString(R.string.name))
                    else -> etName.error = null
                }

                actionButton.isEnabled = !TextUtils.isEmpty(charSequence)
            })
    etName.addTextChangedListener(textWatcher)
    dialog.show()
    etName.post {
        etName.setSelection(etName.length())
        KeyboardUtils.showSoftKeyboard(etName, KEYBOARD_DELAY)
    }
    return dialog
}
