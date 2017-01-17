package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.utils.ActivityUtils;
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;
import com.valyakinaleksey.roleplayingsystem.utils.ViewUtils;

import butterknife.Bind;
import butterknife.BindColor;
import rx.Subscription;

public class SingleValueEditViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.title)
    TextView tvTitle;
    @Bind(R.id.value)
    EditText etValue;

    @Bind(R.id.edit_icon)
    ImageView editIcon;

    @Bind(R.id.divider)
    View divider;

    @BindColor(R.color.defaultGrayTintColor)
    int grayColor;

    @BindColor(R.color.colorAccent)
    int accentColor;
    private Subscription etSubscription;


    public SingleValueEditViewHolder(View itemView) {
        super(itemView);
        ViewUtils.increaseTouchArea(editIcon);
    }

    public void bind(SingleValueEditModel singleValueEditModel) {
        initDivider();
        initEditText(singleValueEditModel);
        initImageButton(singleValueEditModel);
    }

    private void initEditText(SingleValueEditModel singleValueEditModel) {
        if (etSubscription != null) {
            etSubscription.unsubscribe();
        }

        tvTitle.setText(singleValueEditModel.getTitle());
        etValue.setHint(singleValueEditModel.getValueHint());
        etValue.setText(singleValueEditModel.getValue());
        etSubscription = RxTextView
                .textChanges(etValue)
                .subscribe(charSequence -> {
                    singleValueEditModel.setValue(charSequence.toString());
                    if (charSequence.length() == 0 && singleValueEditModel.isEditable()) {
                        editIcon.setVisibility(View.GONE);
                    } else {
                        editIcon.setVisibility(View.VISIBLE);
                    }
                });
        etValue.setOnTouchListener((v, event) -> (!singleValueEditModel.isEditable()));
        etValue.setCursorVisible(singleValueEditModel.isEditable());
    }

    private void initDivider() {
        if (getAdapterPosition() == 0) {
            divider.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
        }
    }

    private void initImageButton(SingleValueEditModel singleValueEditModel) {
        updateImage(singleValueEditModel);
        //test
        editIcon.setOnClickListener(v -> {
            if (singleValueEditModel.isEditable()) {
                singleValueEditModel.getSaveOnclickListener().onClick(etValue.getText().toString());
                singleValueEditModel.setEditable(false);
                updateImage(singleValueEditModel);
                Activity activity = (Activity) etValue.getContext();
                etValue.setCursorVisible(false);
                ActivityUtils.clearFocus(activity);
                KeyboardUtils.hideKeyboard(activity);
            } else {
                singleValueEditModel.setEditable(true);
                updateImage(singleValueEditModel);
                etValue.setCursorVisible(true);
                etValue.requestFocus();
                KeyboardUtils.showSoftKeyboard(etValue);
                etValue.setSelection(etValue.length());
            }
        });
    }

    private void updateImage(SingleValueEditModel singleValueEditModel) {
        if (singleValueEditModel.isEditable()) {
            ImageUtils.setTintVectorImage(editIcon, R.drawable.ic_done_black_24dp, accentColor);
        } else {
            ImageUtils.setTintVectorImage(editIcon, R.drawable.ic_mode_edit_black_24dp, grayColor);
        }
    }
}
      