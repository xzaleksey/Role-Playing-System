package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import android.graphics.drawable.Drawable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

public class MaterialDrawableProviderImpl implements DrawableProvider {

    private String s;
    private String uid;

    public MaterialDrawableProviderImpl(String s, String uid) {
        this.s = s;
        this.uid = uid;
    }

    @Override
    public Drawable getDrawable() {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        return TextDrawable.builder().beginConfig()
                .useFont(RpsApp.getFont())
                .toUpperCase()
                .endConfig()
                .buildRound(s.substring(0, 1), generator.getColor(uid));
    }
}
      