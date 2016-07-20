package com.softdesign.devintensive.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.LoaderDataFromDb;
import com.softdesign.devintensive.utils.UsersForLoading;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity implements android.app.LoaderManager.LoaderCallbacks<List<User>> {

    private List<User> mUsers;
    private DataManager mDataManager;
    private Loader<List<User>> mLoader;
    private Context mContext;
    private UsersForLoading mUsersForLoading;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splach_screen);
        mDataManager = DataManager.getINSTANCE();
        mUsers = new ArrayList<>();

        if (mDataManager.getPreferencesManager().getAuthToken() == null) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        } else {
            Bundle bndl = new Bundle();
            bndl.putString(ConstantManager.LOADER_KEY, "null");
            getLoaderManager().destroyLoader(ConstantManager.LOADER_ID);
            mLoader = getLoaderManager().initLoader(ConstantManager.LOADER_ID, bndl, this);
            mLoader.forceLoad();

            Intent mainIntent = new Intent(SplashActivity.this, UserListActivity.class);
            startActivity(mainIntent);
        }
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Loader <List<User>> loader = null;
        mContext = DevintensiveApplication.getContext();
        loader = new LoaderDataFromDb(mContext, args);
        return loader;
    }
    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> users) {
        mUsers.addAll(users);
        mUsersForLoading = new UsersForLoading(mUsers);
        EventBus.getDefault().post(mUsersForLoading);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {

    }
}
