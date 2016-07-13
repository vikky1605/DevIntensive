package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.network.res.UploadPhotoRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by bolshakova on 27.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private Context mContext;

    public DataManager() {

        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Call<UserModelRes> loginUser (UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UploadPhotoRes> uploadPhoto (@Path("userId") String userId,
                                             @Part MultipartBody.Part file) {
        return mRestService.uploadPhoto(userId, file);
    }
}
