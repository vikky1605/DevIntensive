package com.softdesign.devintensive.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.widget.ImageView;

/** класс для рисования "круглых" картинок
 *
 */
public class CircleImageView extends ImageView {


    public CircleImageView(Context context) {
        super(context);
    }


    public static Bitmap getCircleMaskedBitmap(Bitmap source, int radius) {
        if (source == null) {
            return null;
        }
        int diam = radius << 1;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, diam, diam, false);
        final Shader shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        Bitmap targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawCircle(radius, radius, radius, paint);

        return targetBitmap;
    }
}
