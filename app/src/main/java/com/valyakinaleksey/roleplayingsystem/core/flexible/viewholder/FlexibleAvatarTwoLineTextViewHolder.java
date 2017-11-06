package com.valyakinaleksey.roleplayingsystem.core.flexible.viewholder;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.flexible.FlexibleAvatarWithTwoLineTextModel;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;
import rx.Subscription;

public class FlexibleAvatarTwoLineTextViewHolder extends FlexibleViewHolder {
    @BindView(R.id.avatar)
    protected ImageView ivAvatar;
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
      