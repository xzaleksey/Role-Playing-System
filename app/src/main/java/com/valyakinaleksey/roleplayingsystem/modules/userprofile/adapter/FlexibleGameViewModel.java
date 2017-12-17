package com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class FlexibleGameViewModel extends AbstractFlexibleItem<UserProfileGameViewHolder> implements Serializable {

    private String id;
    private String title;
    private String description;
    private boolean showMasterIcon;
    private String payLoad = StringUtils.EMPTY_STRING;
    private boolean isGameLocked = false;

    public FlexibleGameViewModel(String id, String title, String description, boolean showMasterIcon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.showMasterIcon = showMasterIcon;
    }

    private FlexibleGameViewModel(Builder builder) {
        id = builder.id;
        title = builder.title;
        description = builder.description;
        showMasterIcon = builder.showMasterIcon;
        payLoad = builder.payLoad;
        isGameLocked = builder.isGameLocked;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isShowMasterIcon() {
        return showMasterIcon;
    }

    @Override
    public UserProfileGameViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new UserProfileGameViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, UserProfileGameViewHolder holder, int position, List payloads) {
        holder.bind(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.game_item;
    }

    public boolean isGameLocked() {
        return isGameLocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlexibleGameViewModel that = (FlexibleGameViewModel) o;

        if (showMasterIcon != that.showMasterIcon) return false;
        if (isGameLocked != that.isGameLocked) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return payLoad != null ? payLoad.equals(that.payLoad) : that.payLoad == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (showMasterIcon ? 1 : 0);
        result = 31 * result + (payLoad != null ? payLoad.hashCode() : 0);
        result = 31 * result + (isGameLocked ? 1 : 0);
        return result;
    }

    public static final class Builder {
        private String id;
        private String title;
        private String description;
        private boolean showMasterIcon;
        private String payLoad;
        private boolean isGameLocked;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder showMasterIcon(boolean val) {
            showMasterIcon = val;
            return this;
        }

        public Builder payLoad(String val) {
            payLoad = val;
            return this;
        }

        public Builder isGameLocked(boolean val) {
            isGameLocked = val;
            return this;
        }

        public FlexibleGameViewModel build() {
            return new FlexibleGameViewModel(this);
        }
    }
}
