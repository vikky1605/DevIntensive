package com.softdesign.devintensive.data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;


public class PicassoCache {
    private Context mContext;
    private Picasso mPicassoInstance;

    public PicassoCache(Context mContext) {
        this.mContext = mContext;
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(mContext, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(mContext);
        builder.downloader(okHttp3Downloader);

        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
    }

    public Picasso getPicassoInstance() {

        if (mPicassoInstance == null) {
            new PicassoCache(mContext);
            return mPicassoInstance;
        }

        return mPicassoInstance;
    }
}
