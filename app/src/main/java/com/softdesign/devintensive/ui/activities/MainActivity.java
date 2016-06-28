package com.softdesign.devintensive.ui.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.CircleImageView;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private ImageView mCallImg;
    private ImageView mAvatar;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolBar;
    private DrawerLayout mNavigationDrawer;
    private int mCurrentEditMode = 0;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;
    private List<EditText> mUserInfoViews;
    private DataManager mDataManager;
    private boolean mCheckOpenNavigationDrawer = false; // true, если NavigationDrawer открыт, false - если закрыт

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mCallImg = (ImageView)findViewById(R.id.call_img);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout)findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mUserPhone = (EditText)findViewById(R.id.user_phone);
        mUserMail = (EditText)findViewById(R.id.user_mail);
        mUserVk = (EditText)findViewById(R.id.user_profile_vk);
        mUserGit = (EditText) findViewById(R.id.user_git);
        mUserBio = (EditText) findViewById(R.id.user_info);
        mDataManager = DataManager.getINSTANCE();

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);


        mFab.setOnClickListener(this);
        setupToolBar();
        setupDrawer();


        if (savedInstanceState == null) {
                    }
        else {
            loadUserInfoValue();
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveUserInfoValue();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:

                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                }
                else { changeEditMode(0); mCurrentEditMode = 0;}
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.my_name);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        else Log.d(TAG, "акшн бар нулевой");
    }

    @Override
   public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
            mCheckOpenNavigationDrawer = true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer () {
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        Resources res = this.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.my_photo);
        bitmap = CircleImageView.getCircleMaskedBitmap(bitmap,24);
        View view = navigationView.getHeaderView(0);
        mAvatar = (ImageView)view.findViewById(R.id.avatar);
        mAvatar.setImageBitmap(bitmap);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        mCheckOpenNavigationDrawer = false;

        return false;
        }
});
}

    // переключает режим редактирования
    // mode == 1, если режим редактирования, иначе mode == 0
    public void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
        } else {
            saveUserInfoValue();
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);


            }
        }
    }

    private void loadUserInfoValue () {
        List<String> userData = mDataManager.getmPreferencesManager().loadUserProfileDate();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText useFieldView : mUserInfoViews)  {
            userData.add(useFieldView.getText().toString());
        }
        mDataManager.getmPreferencesManager().saveUserProfileDate(userData);


    }
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
   public void onBackPressed() {
        if (mCheckOpenNavigationDrawer) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
            mCheckOpenNavigationDrawer = false;
            Log.d(TAG, "нужно закрыть драйвер");
        }
        else super.onBackPressed();
    }

    /* private void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // TO DO выполнить с задержкой
                hideProgress();
            }
        }, 5000);

    } */
}






