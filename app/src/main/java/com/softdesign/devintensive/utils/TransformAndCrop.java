package com.softdesign.devintensive.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;


import com.softdesign.devintensive.R;
import com.squareup.picasso.Transformation;

/** класс для подгонки фото под размеры PlaceHolder
 * Created by bolshakova on 05.07.2016.
 */
public class TransformAndCrop implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {

       Context context = DevintensiveApplication.getContext();
       Resources res = context.getResources();
       int x = (int) res.getDimensionPixelOffset(R.dimen.size_hugest_512);
       int y = (int) res.getDimensionPixelOffset(R.dimen.size_huge_256);
        Bitmap result = Bitmap.createScaledBitmap(source, x, y, true);
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
