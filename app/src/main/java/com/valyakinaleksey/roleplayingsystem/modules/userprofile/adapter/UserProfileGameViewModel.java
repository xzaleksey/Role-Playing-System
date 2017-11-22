package com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.io.Serializable;
import java.util.List;

public class UserProfileGameViewModel extends AbstractFlexibleItem<UserProfileGameViewHolder> implements Serializable {
    public static final String CHANGE_USER_NAME = "CHANGE_USER_NAME";

    private String id;
    private String title;
    private String description;
    private boolean showMasterIcon;

    public UserProfileGameViewModel(String id, String title, String description, boolean showMasterIcon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.showMasterIcon = showMasterIcon;
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
        return R.layout.user_profile_game_item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfileGameViewModel that = (UserProfileGameViewModel) o;

        if (showMasterIcon != that.showMasterIcon) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (showMasterIcon ? 1 : 0);
        return result;
    }
}
