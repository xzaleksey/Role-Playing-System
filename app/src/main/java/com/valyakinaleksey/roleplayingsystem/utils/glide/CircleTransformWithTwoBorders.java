package com.valyakinaleksey.roleplayingsystem.utils.glide;

import android.content.Context;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.valyakinaleksey.roleplayingsystem.utils.DpUtils;

public class CircleTransformWithTwoBorders extends BitmapTransformation {
    private Context context;
    private final int firstColor;
    private final int secondColor;

    public CircleTransformWithTwoBorders(Context context, int firstColor, int secondColor) {
        super(context);
        this.context = context;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(context, pool, toTransform, firstColor, secondColor);
    }

    private static Bitmap circleCrop(Context context, BitmapPool pool, Bitmap source, int firstColor, int secondColor) {
        if (source == null) return null;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int size = Math.min(source.getWidth(), source.getHeight());
        float innerStroke = DpUtils.convertDpToPixel(2, context);
        float outerStroke = DpUtils.convertDpToPixel(1, context);
        float shift = outerStroke + innerStroke;
        float r = size / 2f - shift;
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        paint.setStrokeWidth(outerStroke);
        paint.setColor(secondColor);
        canvas.drawArc(new RectF(0, 0, size, size), 0, 360, true, paint);
        paint.setStrokeWidth(innerStroke);
        paint.setColor(firstColor);
        canvas.drawArc(new RectF(outerStroke, outerStroke, size - outerStroke, size - outerStroke), 0, 360, true, paint);

        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        canvas.drawCircle(r + shift, r + shift, r, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
