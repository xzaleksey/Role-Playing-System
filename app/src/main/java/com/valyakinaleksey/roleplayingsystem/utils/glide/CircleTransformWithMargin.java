package com.valyakinaleksey.roleplayingsystem.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class CircleTransformWithMargin extends BitmapTransformation {
    private int margin;

    public CircleTransformWithMargin(Context context, int margin) {
        super(context);
        this.margin = margin;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform, margin);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source, int margin) {
        if (source == null) return null;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int size = Math.min(source.getWidth(), source.getHeight());
        float r = size / 2f - margin;
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        canvas.drawCircle(r + margin, r + margin, r, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
