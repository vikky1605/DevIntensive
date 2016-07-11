package com.softdesign.devintensive.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.softdesign.devintensive.R;
import com.squareup.picasso.Transformation;

/**
 * Created by bolshakova on 11.07.2016.
 */
public class TransformToCircle implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        if (source == null) {
            return null;
        }

        Context context = DevintensiveApplication.getContext();
        Resources res = context.getResources();
        int radius = res.getDimensionPixelOffset(R.dimen.size_small_24);
        int diam = radius << 1;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, diam, diam, false);
        final Shader shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        Bitmap targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawCircle(radius, radius, radius, paint);

        if (targetBitmap != source) {
            source.recycle();
        }

        return targetBitmap;

    }

    @Override
    public String key() {
        return "circle";
    }
}
