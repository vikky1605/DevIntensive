package com.softdesign.devintensive.utils;

import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

public class LoaderDataFromDb extends Loader<List<User>> {

    private DownloadDataFromDb mDownloadDataFromDb;
    private String mCheckForLoading;

    public LoaderDataFromDb(Context context, Bundle args) {
        super(context);
        mCheckForLoading = args.getString(ConstantManager.LOADER_KEY);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (mDownloadDataFromDb != null)
            mDownloadDataFromDb.cancel(true);
        mDownloadDataFromDb = new DownloadDataFromDb();
        mDownloadDataFromDb.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCheckForLoading);

    }
    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    void getResultFromTask(List<User> users) {
        deliverResult(users);
    }

    public class DownloadDataFromDb extends AsyncTask<String, Void, List<User>> {
        private DataManager mDataManager;
        private List<User> mUsers;
        private String mSearchName;


        @Override
        protected List<User> doInBackground(String... params) {
            mDataManager = DataManager.getINSTANCE();
            mSearchName = mCheckForLoading;
            if (mSearchName.equals("null")) {
                  try {
                    mUsers = mDataManager.getUserListFromDb();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    mUsers = mDataManager.getUserListByName(mSearchName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                return mUsers;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            getResultFromTask(users);
        }
    }
}
