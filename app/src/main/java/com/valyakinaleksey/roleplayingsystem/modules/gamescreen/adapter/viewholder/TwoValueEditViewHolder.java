package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.BindColor;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.EditExpandableSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SimpleSingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.TwoValueEditModel;
import com.valyakinaleksey.roleplayingsystem.utils.ActivityUtils;
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.KeyboardUtils;
import com.valyakinaleksey.roleplayingsystem.utils.ViewUtils;
import rx.subscriptions.CompositeSubscription;

public class TwoValueEditViewHolder extends ButterKnifeViewHolder {
  @Bind(R.id.value) EditText etValue;
  @Bind(R.id.value2) EditText etValue2;

  @Bind(R.id.edit_icon) ImageView editIcon;
  @Bind(R.id.edit_icon2) ImageView editIcon2;

  @Bind(R.id.divider) View divider;

  @BindColor(R.color.colorAccent) int accentColor;
  private CompositeSubscription etSubscription;

  public TwoValueEditViewHolder(View itemView) {
    super(itemView);
    ViewUtils.increaseTouchArea(editIcon);
    ViewUtils.increaseTouchArea(editIcon2);
  }

  public void bind(TwoValueEditModel twoValueEditModel, EditExpandableSection editExpandableSection,
      RecyclerView.Adapter adapter) {
    initDivider();
    if (etSubscription != null) {
      etSubscription.unsubscribe();
    }
    etSubscription = new CompositeSubscription();
    initMainValue(twoValueEditModel, editExpandableSection, adapter);
    initSecondaryValue(twoValueEditModel.getSecondaryValue());
  }

  private void initMainValue(TwoValueEditModel twoValueEditModel,
      EditExpandableSection editExpandableSection, RecyclerView.Adapter adapter) {
    SimpleSingleValueEditModel mainValue = twoValueEditModel.getMainValue();
    etValue.setHint(mainValue.getValueHint());
    etValue.setText(mainValue.getValue());
    etSubscription.add(RxTextView.textChanges(etValue).skip(1).subscribe(charSequence -> {
      mainValue.setValue(charSequence.toString());
      handleMainValue(twoValueEditModel, mainValue, editExpandableSection, adapter);
    }));
    etValue.setOnFocusChangeListener((v, hasFocus) -> {
      if (hasFocus) {
        editIcon.setVisibility(View.VISIBLE);
      } else {
        editIcon.setVisibility(View.GONE);
      }
    });
    handleMainValue(twoValueEditModel, mainValue, editExpandableSection, adapter);
    etValue.setOnTouchListener(
        ViewUtils.getFixRecyclerPositionOnTouchListener(etValue, getAdapterPosition()));
  }

  @SuppressWarnings("unchecked") private void handleMainValue(TwoValueEditModel twoValueEditModel,
      SimpleSingleValueEditModel mainValue, EditExpandableSection editExpandableSection,
      RecyclerView.Adapter adapter) {
    if (etValue.getText().length() == 0) {
      ImageUtils.setTintVectorImage(editIcon, R.drawable.ic_delete_black_24dp, accentColor);
      editIcon.setOnClickListener(
          v -> editExpandableSection.removeItem(itemView.getContext(), twoValueEditModel,
              getAdapterPosition(), adapter));
    } else {
      ImageUtils.setTintVectorImage(editIcon, R.drawable.ic_done_black_24dp, accentColor);
      editIcon.setOnClickListener(v -> {
        ActivityUtils.clearFocus((Activity) itemView.getContext());
        KeyboardUtils.hideKeyboard((Activity) itemView.getContext());
        mainValue.getSaveOnclickListener().onClick(etValue.getText().toString());
      });
    }
  }

  private void initSecondaryValue(SimpleSingleValueEditModel singleValueEditModel) {
    ImageUtils.setTintVectorImage(editIcon2, R.drawable.ic_done_black_24dp, accentColor);
    etValue2.setHint(singleValueEditModel.getValueHint());
    etValue2.setText(singleValueEditModel.getValue());
    etSubscription.add(RxTextView.textChanges(etValue2).skip(1).subscribe(charSequence -> {
      singleValueEditModel.setValue(charSequence.toString());
      handleSecondaryValueFocus(charSequence);
    }));
    etValue2.setOnFocusChangeListener((v, hasFocus) -> {
      handleSecondaryValueFocus(etValue2.getText());
    });
    etValue2.setOnTouchListener(
        ViewUtils.getFixRecyclerPositionOnTouchListener(etValue2, getAdapterPosition()));
    editIcon2.setOnClickListener(v -> {
      ActivityUtils.clearFocus((Activity) itemView.getContext());
      KeyboardUtils.hideKeyboard((Activity) itemView.getContext());
      singleValueEditModel.getSaveOnclickListener().onClick(etValue2.getText().toString());
    });
  }

  private void handleSecondaryValueFocus(CharSequence charSequence) {
    if (etValue2.hasFocus() && charSequence.length() > 0 && etValue.getText().length() > 0) {
      editIcon2.setVisibility(View.VISIBLE);
    } else {
      editIcon2.setVisibility(View.GONE);
    }
  }

  private void initDivider() {
    if (getAdapterPosition() == 0) {
      divider.setVisibility(View.GONE);
    } else {
      divider.setVisibility(View.VISIBLE);
    }
  }
}
      