package com.valyakinaleksey.roleplayingsystem.core.flexible.viewholder;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel;
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;
import rx.Subscription;

public class FlexibleAvatarTwoLineTextViewHolder extends FlexibleViewHolder {
    @BindView(R.id.avatar)
    protected ImageView ivAvatar;
    @BindView(R.id.arrow_right)
    protected ImageView ivRightArrow;
    @BindView(R.id.primary_line)
    protected TextView tvPrimaryLine;
    @BindView(R.id.secondary_line)
    protected TextView tvSecondaryLine;

    protected Subscription subscription;

    public FlexibleAvatarTwoLineTextViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(FlexibleAvatarWithTwoLineTextModel avatarWithTwoLineTextModel) {
        ivRightArrow.setVisibility(avatarWithTwoLineTextModel.isShowArrowRight() ? View.VISIBLE : View.GONE);
        tvPrimaryLine.setText(avatarWithTwoLineTextModel.getPrimaryText());
        tvSecondaryLine.setText(avatarWithTwoLineTextModel.getSecondaryText());
        if (subscription != null) {
            subscription.unsubscribe();
        }
        Drawable drawable = avatarWithTwoLineTextModel.getPlaceHolderAndErrorDrawableProvider().getDrawable();
        if (!(drawable instanceof RoundedBitmapDrawable) && !(drawable instanceof TextDrawable)) {
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(ivAvatar.getContext().getResources(), ImageUtils.drawableToBitmap(drawable));
            circularBitmapDrawable.setCircular(true);
            drawable = circularBitmapDrawable;
        }
        ivAvatar.setImageDrawable(drawable);
        String photoUrl = avatarWithTwoLineTextModel.getPhotoUrl();
        if (!StringUtils.isEmpty(photoUrl)) {
            ImageUtils.loadAvatarWithErrorDrawable(ivAvatar, Uri.parse(photoUrl), drawable);
        }
    }
}
      