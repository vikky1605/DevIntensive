package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.button_in)Button mSignIn;
    @BindView(R.id.forget_password)TextView mRememberPassword;
    @BindView(R.id.enter_user_mail)EditText mLogin;
    @BindView(R.id.enter_password)EditText mPassword;
    @BindView(R.id.main_coordinator_container)CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        mDataManager = DataManager.getINSTANCE();

        mSignIn.setOnClickListener(this);
        mRememberPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_in:
                signIn();
                break;

            case R.id.forget_password:
                rememberPassword();
                break;
        }
    }

    public void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelRes userModel) {
        showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);

    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(),
                    mPassword.getText().toString()));

            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else {
                        if (response.code() == 404) {
                            showSnackbar("неверный логин или пароль");
                        } else showSnackbar("неизвестная ошибка");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    showSnackbar("ошибка отправки запроса серверу");
                }
            });
        } else showSnackbar("Сеть недоступна, попробуйте позже");
    }

    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()};

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);

        List<String> userFields = new ArrayList<>();
               userFields.add(userModel.getData().getUser().getContacts().getPhone());
               userFields.add(userModel.getData().getUser().getContacts().getEmail());
               userFields.add(userModel.getData().getUser().getContacts().getVk());
               userFields.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
               userFields.add(userModel.getData().getUser().getPublicInfo().getBio());

        mDataManager.getPreferencesManager().saveUserProfileDate(userFields);

        String[] userDrawerInfo = {
                userModel.getData().getUser().getFirstName(),
                userModel.getData().getUser().getSecondName(),
                userModel.getData().getUser().getContacts().getEmail()};

        mDataManager.getPreferencesManager().saveUserDrawerInfo(userDrawerInfo);

        String userPhoto = userModel.getData().getUser().getPublicInfo().getPhoto();
        String userAvatar = userModel.getData().getUser().getPublicInfo().getAvatar();

        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userPhoto));
        mDataManager.getPreferencesManager().saveUserAvatar(Uri.parse(userAvatar));
    }
}
