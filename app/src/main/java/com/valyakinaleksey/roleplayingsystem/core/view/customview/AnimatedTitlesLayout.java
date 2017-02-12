package com.valyakinaleksey.roleplayingsystem.core.view.customview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

public class AnimatedTitlesLayout extends RelativeLayout {
  private LinearLayout linearLayout;
  private List<TitleModel> titleModels;
  private int whiteColor;
  private int selectedPosition;
  private int secondaryTextcolor;
  private int primaryTextcolor;
  private View selector;

  public AnimatedTitlesLayout(Context context) {
    super(context);
    init();
  }

  public AnimatedTitlesLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public AnimatedTitlesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    whiteColor = ContextCompat.getColor(getContext(), R.color.md_white_1000);
    primaryTextcolor = ContextCompat.getColor(getContext(), R.color.colorTextPrimary);
    secondaryTextcolor = ContextCompat.getColor(getContext(), R.color.colorTextSecondary);
    titleModels = new ArrayList<>();
    linearLayout = new LinearLayout(getContext());
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    initSelector();
    addView(linearLayout,
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public List<TitleModel> getTitleModels() {
    return titleModels;
  }

  private void initSelector() {
    selector = new View(getContext());
    addView(selector,
        new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    GradientDrawable shape = new GradientDrawable();
    shape.setShape(GradientDrawable.RECTANGLE);
    shape.setColor(whiteColor);
    shape.setCornerRadius(ScreenUtils.convertDIPToPixels(getContext(), 2));
    selector.setBackground(shape);
  }

  public void updateModels(List<TitleModel> titleModels, int selectedPosition) {
    this.selectedPosition = selectedPosition;
    linearLayout.removeAllViews();
    for (int i = 0; i < titleModels.size(); i++) {
      TitleModel titleModel = titleModels.get(i);
      TextView child = new TextView(getContext());
      child.setGravity(Gravity.CENTER);
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.f);
      linearLayout.addView(child, params);

      child.setTextColor(i == selectedPosition ? primaryTextcolor : secondaryTextcolor);
      child.setText(titleModel.getTitle());
      int finalI = i;
      child.setOnClickListener(v -> {
        selectTitleModel(finalI);
        OnClickListener onClickListener = titleModel.getOnClickListener();
        if (onClickListener != null) {
          onClickListener.onClick(v);
        }
      });
    }
    selector.post(() -> {
      selector.setX(selectedPosition * selector.getMeasuredWidth());
    });
  }

  private void animateSelector(int selectedPosition) {
    selector.animate().translationX(selector.getMeasuredWidth() * selectedPosition);
  }

  private void selectTitleModel(int finalI) {
    this.selectedPosition = finalI;
    for (int i = 0; i < linearLayout.getChildCount(); i++) {
      ((TextView) linearLayout.getChildAt(i)).setTextColor(
          i == selectedPosition ? primaryTextcolor : secondaryTextcolor);
    }
    animateSelector(selectedPosition);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    View childAt = linearLayout.getChildAt(0);
    if (childAt != null) {
      selector.getLayoutParams().width = childAt.getMeasuredWidth();
    }
  }

  public static class TitleModel {
    private OnClickListener onClickListener;
    private String title;

    public OnClickListener getOnClickListener() {
      return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
      this.onClickListener = onClickListener;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }
}
      