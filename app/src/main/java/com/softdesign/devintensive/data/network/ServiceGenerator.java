package com.softdesign.devintensive.data.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.softdesign.devintensive.data.network.interceptors.HeaderInterceptor;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Cache;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bolshakova on 10.07.2016.
 */
public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class <S> serviceClass) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(new HeaderInterceptor());
        httpClient.addInterceptor(logging);
        httpClient.cache(new okhttp3.Cache(DevintensiveApplication.getContext().getCacheDir(), Integer.MAX_VALUE));
        httpClient.addNetworkInterceptor(new StethoInterceptor());

               Retrofit retrofit = sBuilder
                       .client(httpClient.build())
                       .build();

        return retrofit.create(serviceClass);



    }
}
