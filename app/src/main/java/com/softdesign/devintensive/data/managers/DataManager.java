package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.network.res.UploadPhotoRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Part;
import retrofit2.http.Path;


public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private Context mContext;
    private Picasso mPicasso;
    private DaoSession mDaoSession;

    public DataManager() {

        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevintensiveApplication.getDaoSession();
    }


    public Picasso getPicasso() {
        return mPicasso;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
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
     public Call<UserListRes> getUserListFromNetwork() {
         return mRestService.getUserListFromNetwork();
     }

    public List<User> getUserListFromDb (){
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                                  .where(UserDao.Properties.CodeLines.gt(0))
                                  .orderDesc(UserDao.Properties.CodeLines)
                                  .build()
                                  .list();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<User> getUserListByName (String query) {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                       .where(UserDao.Properties.Rating.gt(0),
                        UserDao.Properties.SearchName.like("%" + query.toUpperCase()+ "%"))
                       .orderDesc(UserDao.Properties.CodeLines)
                       .build()
                       .list();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
   }
