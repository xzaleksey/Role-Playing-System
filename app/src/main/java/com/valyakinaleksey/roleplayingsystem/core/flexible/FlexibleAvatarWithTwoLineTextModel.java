package com.valyakinaleksey.roleplayingsystem.core.flexible;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.flexible.viewholder.FlexibleAvatarTwoLineTextViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.DrawableProvider;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.io.Serializable;
import java.util.List;

public class FlexibleAvatarWithTwoLineTextModel extends AbstractFlexibleItem<FlexibleAvatarTwoLineTextViewHolder> implements Serializable, Parcelable {
    private String primaryText;
    private String secondaryText;
    private DrawableProvider placeHolderAndErrorDrawableProvider;
    private String photoUrl;
    private String id;

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public DrawableProvider getPlaceHolderAndErrorDrawableProvider() {
        return placeHolderAndErrorDrawableProvider;
    }

    public void setPlaceHolderAndErrorDrawableProvider(DrawableProvider placeHolderAndErrorDrawableProvider) {
        this.placeHolderAndErrorDrawableProvider = placeHolderAndErrorDrawableProvider;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public FlexibleAvatarWithTwoLineTextModel() {
    }

    @Override
    public FlexibleAvatarTwoLineTextViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new FlexibleAvatarTwoLineTextViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, FlexibleAvatarTwoLineTextViewHolder holder, int position, List payloads) {
        holder.bind(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlexibleAvatarWithTwoLineTextModel that = (FlexibleAvatarWithTwoLineTextModel) o;

        if (primaryText != null ? !primaryText.equals(that.primaryText) : that.primaryText != null) return false;
        if (secondaryText != null ? !secondaryText.equals(that.secondaryText) : that.secondaryText != null)
            return false;
        if (photoUrl != null ? !photoUrl.equals(that.photoUrl) : that.photoUrl != null) return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = primaryText != null ? primaryText.hashCode() : 0;
        result = 31 * result + (secondaryText != null ? secondaryText.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public FlexibleAvatarWithTwoLineTextModel(String primaryText, String secondaryText, DrawableProvider placeHolderAndErrorDrawableProvider, String photoUrl, String id) {
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.placeHolderAndErrorDrawableProvider = placeHolderAndErrorDrawableProvider;
        this.photoUrl = photoUrl;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.avatar_with_two_lines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.primaryText);
        dest.writeString(this.secondaryText);
        dest.writeSerializable(this.placeHolderAndErrorDrawableProvider);
        dest.writeString(this.photoUrl);
        dest.writeString(this.id);
    }

    protected FlexibleAvatarWithTwoLineTextModel(Parcel in) {
        this.primaryText = in.readString();
        this.secondaryText = in.readString();
        this.placeHolderAndErrorDrawableProvider = (DrawableProvider) in.readSerializable();
        this.photoUrl = in.readString();
        this.id = in.readString();
    }

    public static final Creator<FlexibleAvatarWithTwoLineTextModel> CREATOR = new Creator<FlexibleAvatarWithTwoLineTextModel>() {
        @Override
        public FlexibleAvatarWithTwoLineTextModel createFromParcel(Parcel source) {
            return new FlexibleAvatarWithTwoLineTextModel(source);
        }

        @Override
        public FlexibleAvatarWithTwoLineTextModel[] newArray(int size) {
            return new FlexibleAvatarWithTwoLineTextModel[size];
        }
    };
}
      