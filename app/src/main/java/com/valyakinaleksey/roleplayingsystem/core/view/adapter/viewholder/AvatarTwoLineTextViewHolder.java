package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.AvatarWithTwoLineTextModel;

import butterknife.Bind;
import rx.Subscription;

public class AvatarTwoLineTextViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.avatar)
    protected ImageView ivAvatar;
    @Bind(R.id.primary_line)
    protected TextView tvPrimaryLine;
    @Bind(R.id.secondary_line)
    protected TextView tvSecondaryLine;

    protected Subscription subscription;

    public AvatarTwoLineTextViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(AvatarWithTwoLineTextModel avatarWithTwoLineTextModel) {
        tvPrimaryLine.setText(avatarWithTwoLineTextModel.getPrimaryText());
        tvSecondaryLine.setText(avatarWithTwoLineTextModel.getSecondaryText());
        if (subscription != null) {
            subscription.unsubscribe();
        }
        Drawable drawable = avatarWithTwoLineTextModel.getPlaceHolderAndErrorDrawableProvider().getDrawable();
        ivAvatar.setImageDrawable(drawable);
        Glide.with(ivAvatar.getContext()).load(avatarWithTwoLineTextModel.getPhotoUrl())
                .asBitmap()
                .centerCrop()
                .placeholder(drawable)
                .error(drawable)
                .into(new BitmapImageViewTarget(ivAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(ivAvatar.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
      