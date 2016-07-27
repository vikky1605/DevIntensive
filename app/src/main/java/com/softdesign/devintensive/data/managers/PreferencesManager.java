package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;
    public static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
                                                ConstantManager.USER_MAIL_KEY,
                                                ConstantManager.USER_VK_KEY,
                                                ConstantManager.USER_GIT_KEY,
                                                ConstantManager.USER_BIO_KEY};

    public static final String[] USER_VALUES = {ConstantManager.USER_RATING_VALUE,
                                                ConstantManager.USER_CODE_LINES_VALUE,
                                                ConstantManager.USER_PROJECTS_VALUE};

    public static final String[] USER_DRAWER_INFO = {ConstantManager.USER_FIRST_NAME_KEY,
                                                     ConstantManager.USER_SECOND_NAME_KEY,
                                                     ConstantManager.USER_MAIL_KEY};

    public PreferencesManager() {
        this.mSharedPreferences = DevintensiveApplication.getsSharedPreferences();
    }

    public void saveUserProfileDate(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileDate () {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, null));
    return userFields;
    }

    public void saveUserPhoto (Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
       String tempUri = mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                        "android.resource://com.softdesign.devintensive/drawable/header_bg");
       return Uri.parse(tempUri);
    }

    public void saveUserAvatar (Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserAvatar () {
        String tempUri = mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY,
                "android.resource://com.softdesign.devintensive/drawable/header_bg");
        return Uri.parse(tempUri);
    }

    public void saveUserProfileValues (int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < userValues.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    public List<String> loadUserProfileValue () {
        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_VALUE, null));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE, null));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECTS_VALUE, null));
        return userValues;
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        String authToken = mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, null);
        return authToken;
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        String userId = mSharedPreferences.getString(ConstantManager.USER_ID_KEY, null);
        return userId;
    }

    public void saveUserDrawerInfo (String[] userDrawerInfo) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < userDrawerInfo.length; i++) {
            editor.putString(USER_DRAWER_INFO[i], userDrawerInfo[i]);
        }
        editor.apply();
    }

        public List<String> loadUserDrawerInfo() {
            List <String> userDrawerInfo = new ArrayList<>();
            userDrawerInfo.add(mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME_KEY, null));
            userDrawerInfo.add(mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME_KEY, null));
            userDrawerInfo.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, null));
            return userDrawerInfo;

        }
    }

