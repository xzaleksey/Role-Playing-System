package com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.auth.RxFirebaseAuth;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import butterknife.Bind;
import rx.Subscription;

public class GameViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.avatar)
    protected ImageView ivAvatar;
    @Bind(R.id.master_name)
    protected TextView tvMasterName;
    @Bind(R.id.name)
    protected TextView tvName;

    protected Subscription subscription;

    public GameViewHolder(View itemView) {
        super(itemView);
    }

    public void updateMasterName(String uid) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = RxFirebaseDatabase.getInstance().observeSingleValue(FirebaseDatabase.getInstance()
                .getReference()
                .child(FireBaseUtils.USERS)
                .child(uid)
        )
                .subscribe(dataSnapshot -> {
                    User user = dataSnapshot.getValue(User.class);
                    tvMasterName.setText(user.getName());
                }, Crashlytics::logException);
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void bind(GameModel model) {
        setName(model.getName());
        if (TextUtils.isEmpty(model.getMasterName())) {
            updateMasterName(model.getMasterId());
        } else {
            setMasterName(model.getMasterName());
        }
    }

    public void setMasterName(String masterName) {
        tvMasterName.setText(masterName);
    }
}
      