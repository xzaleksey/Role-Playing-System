package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewholder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;

import butterknife.Bind;
import butterknife.BindColor;

public class SingleValueEditViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.title)
    TextView tvTitle;

    @Bind(R.id.value)
    EditText etValue;

    @Bind(R.id.edit_icon)
    ImageView editIcon;

    @BindColor(R.color.defaultGrayTintColor)
    int grayColor;

    @BindColor(R.color.colorAccent)
    int accentColor;

    public SingleValueEditViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(SingleValueEditModel singleValueEditModel) {
        tvTitle.setText(singleValueEditModel.getTitle());
        etValue.setHint(singleValueEditModel.getValueHint());
        etValue.setText(singleValueEditModel.getValue());
        etValue.setOnTouchListener((v, event) -> (!singleValueEditModel.isEditable()));
        initImageButton(singleValueEditModel);
    }

    private void initImageButton(SingleValueEditModel singleValueEditModel) {
        updateImage(singleValueEditModel);
        //test
        editIcon.setOnClickListener(v -> {
            if (singleValueEditModel.isEditable()) {
                singleValueEditModel.getSaveOnclickListener().onClick(editIcon);
                singleValueEditModel.setEditable(false);
                updateImage(singleValueEditModel);
            } else {
                singleValueEditModel.setEditable(true);
                updateImage(singleValueEditModel);
                etValue.requestFocus();
                KeyboardUtils.showSoftKeyboard(etValue);
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
      