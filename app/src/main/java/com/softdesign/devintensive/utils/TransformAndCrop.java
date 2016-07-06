package com.softdesign.devintensive.utils;

import android.graphics.Bitmap;

import com.softdesign.devintensive.R;
import com.squareup.picasso.Transformation;

/** класс для подгонки фото под размеры PlaceHolder
 * Created by bolshakova on 05.07.2016.
 */
public class TransformAndCrop implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap result = Bitmap.createScaledBitmap(source, 512, 256, true);
        if (result != source) {
            source.recycle();
        }
        return result;

    }

    @Override
    public String key() {
        return "crop";
    }
}
