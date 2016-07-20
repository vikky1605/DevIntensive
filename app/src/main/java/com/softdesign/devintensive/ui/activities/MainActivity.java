package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UploadPhotoRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.FileUtils;
import com.softdesign.devintensive.utils.MyTextWatcher;
import com.softdesign.devintensive.utils.NetworkStatusChecker;
import com.softdesign.devintensive.utils.TransformAndCrop;
import com.softdesign.devintensive.utils.TransformToCircle;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private int mCurrentEditMode = 0;
    private List<EditText> mUserInfoViews;
    private DataManager mDataManager;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private boolean mCheckOpenNavigationDrawer = false; // true, если NavigationDrawer открыт, false - если закрыт
    private ImageView mAvatar;
    private TextView mDrawerName;
    private TextView mDrawerMail;
    private MyTextWatcher mPhoneTextWatcher, mMailTextWatcher, mVkTextWatcher, mGitTextWatcher;

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_phone)
    EditText mUserPhone;
    @BindView(R.id.user_mail)
    EditText mUserMail;
    @BindView(R.id.user_profile_vk)
    EditText mUserVk;
    @BindView(R.id.user_git)
    EditText mUserGit;
    @BindView(R.id.user_info)
    EditText mUserBio;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.to_call)
    ImageView mToCallImage;
    @BindView(R.id.to_send_mail)
    ImageView mToSendMailImage;
    @BindView(R.id.to_profile_VK)
    ImageView mToProfileVkImage;
    @BindView(R.id.to_user_git)
    ImageView mToUserGitImage;
    @BindView(R.id.rating_value)
    TextView mUserValueRating;
    @BindView(R.id.code_lines_value)
    TextView mUserValueCodeLines;
    @BindView(R.id.projects_value)
    TextView mUserValueProjects;
    private List<TextView> mUserValueViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        ButterKnife.bind(this);

        mDataManager = DataManager.getINSTANCE();

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValueRating);
        mUserValueViews.add(mUserValueCodeLines);
        mUserValueViews.add(mUserValueProjects);

        setupToolBar();
        setupDrawer();
        initUserFields();
        initUserInfoValue();


        // загружаем сохраненное фото пользователя, если таковое имеется
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.user_bg)
                .transform(new TransformAndCrop())
                .into(mProfileImage);

        // устанавливаем необходимых слушателей на view
        mProfilePlaceholder.setOnClickListener(this);
        mFab.setOnClickListener(this);
        mToCallImage.setOnClickListener(this);
        mToSendMailImage.setOnClickListener(this);
        mToProfileVkImage.setOnClickListener(this);
        mToUserGitImage.setOnClickListener(this);

        // создаем объекты TextWather для валидации полей ввода
        // и устанавливаем слушателей на поля ввода информации о пользователе
        mPhoneTextWatcher = new MyTextWatcher(mUserPhone, "phone");
        mUserPhone.addTextChangedListener(mPhoneTextWatcher);
        mVkTextWatcher = new MyTextWatcher(mUserVk, "vk");
        mUserVk.addTextChangedListener(mVkTextWatcher);
        mMailTextWatcher = new MyTextWatcher(mUserMail, "mail");
        mUserMail.addTextChangedListener(mMailTextWatcher);
        mGitTextWatcher = new MyTextWatcher(mUserGit, "git");
        mUserGit.addTextChangedListener(mGitTextWatcher);


        if (savedInstanceState == null) {
        } else {
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
        saveUserFields();
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
            // при нажатии на FloatActionButton переключаемся между режимами редактирования и просмотра
            case R.id.fab:

                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;

            // при нажатии на profile_placeholder запускаем диалог для выбора способа загрузки фото
            // пользователя: с камеры или из галереи
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            // при нажатии на иконку телефонного вызова, звоним по указанному
            // пользователем телефонному номеру
            case R.id.to_call:
                makeCall();
                break;

            // запускаем отправку е-mail
            case R.id.to_send_mail:
                sendMail();
                break;

            // переходим к профилю пользователя в контакте
            case R.id.to_profile_VK:
                String profileVk = "http://" + mUserVk.getText().toString();
                moveToAdress(profileVk);
                break;

            // загружаем репозиторий пользователя на github
            case R.id.to_user_git:
                String userGit = "http://" + mUserGit.getText().toString();
                moveToAdress(userGit);
                break;
        }
    }

    @Override
    // загружаем сохраненное состояние приложения при повороте устройства
    // или при прерывании работы приложения
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    // загружаем ToolBar
    private void setupToolBar() {

        setSupportActionBar(mToolBar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    // открываем NavigationDrawer
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
            mCheckOpenNavigationDrawer = true;
        }
        return super.onOptionsItemSelected(item);
    }

    // загружаем ресурсы NavigationDrawer и скрываем его при нажатии на пункты его меню
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View view = navigationView.getHeaderView(0);
        mAvatar = (ImageView) view.findViewById(R.id.avatar);

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserAvatar())
                .placeholder(R.drawable.user_bg)
                .transform(new TransformToCircle())
                .into(mAvatar);

        mDrawerName = (TextView) view.findViewById(R.id.user_name_txt);
        mDrawerMail = (TextView) view.findViewById(R.id.user_email_txt);
        List<String> userDrawerInfo = mDataManager.getPreferencesManager().loadUserDrawerInfo();
        mDrawerMail.setText(userDrawerInfo.get(0) + " " + userDrawerInfo.get(1));
        mDrawerMail.setText(userDrawerInfo.get(2));

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

    // получаем результат от другой Активити (фото с камеры или из галереи)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:

                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;

            case ConstantManager.REQUEST_CAMERA_PICTURE:

                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
        }
    }


    // переключает режим редактирования
    // mode == 1, если режим редактирования, иначе mode == 0
    public void changeEditMode(int mode) {
        if (mode == 1) {
            showProfilePlaceholder();
            lookToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
            mUserPhone.requestFocus(0);
            mUserPhone.setSelection(0);
        } else {
            boolean check = true;
            if (!mPhoneTextWatcher.getCheckMask()) {
                showSnackBar(getString(R.string.check_phone_false));
                check = false;
            }
            if (!mMailTextWatcher.getCheckMask()) {
                showSnackBar(getString(R.string.check_mail_false));
                check = false;
            }
            if (!mVkTextWatcher.getCheckMask()) {
                showSnackBar(getString(R.string.check_vk_false));
                check = false;
            }
            if (!mGitTextWatcher.getCheckMask()) {
                showSnackBar(getString(R.string.check_git_false));
                check = false;
            }
            if (check) {
                for (EditText userValue : mUserInfoViews) {
                    userValue.setEnabled(false);
                    userValue.setFocusable(false);
                    userValue.setFocusableInTouchMode(false);
                }
                mFab.setImageResource(R.drawable.ic_create_black_24dp);
                saveUserFields();
                hideProfilePlaceholder();
                mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
                unLookToolbar();
            } else mCurrentEditMode = 1;
        }
    }

    // загружаем сохраненные данные пользователя из SharedPreferences
    private void initUserFields() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileDate();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    // сохраняем введенные данные пользователя в SharedPreferences
    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileDate(userData);
    }

    private void initUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValue();
        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }

    @Override
    // закрываем NavigationDrawer и возвращаемся в основное меню при нажатии системной кнопки back
    public void onBackPressed() {
        if (mCheckOpenNavigationDrawer) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
            mCheckOpenNavigationDrawer = false;
        } else super.onBackPressed();
    }

    // загружаем фото из галереи
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_choose_message)),
                ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    // делаем снимок и устанавливаем фото с камеры
    private void loadPhotoFromCamera() {
        // проверяем наличие необходимых разрешений
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // создаем файл для фото
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // запускаем съемку с камеры, получаем результат и сохраняем его
            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        }
        // если нужных разрешений нет, то запрашиваем их у пользователя
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемые разрешения",
                    Snackbar.LENGTH_LONG).setAction("Разрешить", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openApplicationSettings();
                }
            }).show();
        }
    }

    @Override
    // при получении необходимых разрешений от пользователя - загружаем фото из галереи, делаем снимок
    // с камеры, запускаем телефонный вызов
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadPhotoFromCamera();
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                loadPhotoFromGallery();
            }
            if (requestCode == ConstantManager.CALL_REQUEST_PERMISSION_CODE && grantResults.length == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    makeCall();
            }
        }
    }

    // закрываем placeholder
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    // показываем placeholder
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    // открываем ToolBar
    private void lookToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);

    }

    // прячем ToolBar
    private void unLookToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);

    }

    @Override
    // запускаем диалог для выбора, откуда загрузить фото
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiсeItem) {
                        switch (choiсeItem) {
                            case 0:
                                loadPhotoFromGallery();
                                break;

                            case 1:
                                loadPhotoFromCamera();
                                break;

                            case 2:
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    // создаем файл для сохранения фото
    public File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;

    }

    // загружаем выбранное фото в профиль и сохраняем его в SharedPreferences
    private void insertProfileImage(Uri selectedImage) {

        Picasso.with(this)
                .load(selectedImage)
                .transform(new TransformAndCrop())
                .into(mProfileImage);

        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
        updateUserPhoto(selectedImage);

    }

    // запускаем активность для получения от пользователя запрашиваемых разрешений
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    // запускаем телефонный вызов
    protected void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mUserPhone.getText().toString()));
        // если необходимых разрешений нет - запрашиваем их у пользователя
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    ConstantManager.CALL_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемые разрешения",
                    Snackbar.LENGTH_LONG).setAction("Разрешить", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openApplicationSettings();
                }
            }).show();

            return;
        }
        startActivity(intent);
    }

    // отправляем e-mail по указанному пользователем адресу
    protected void sendMail() {
        Intent intent = new Intent(this, SendMailActivity.class);
        intent.putExtra("E-mail", mUserMail.getText().toString());
        startActivity(intent);
    }

    // переходим по ссылке (профиль в VK или репо на github
    protected void moveToAdress(String adress) {
        Uri uriAdress = Uri.parse(adress);
        Intent intent = new Intent(Intent.ACTION_VIEW, uriAdress);
        startActivity(intent);
    }

    // метод для показа SnackBar
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    // отправляет запрос на сервер для обновления фото пользователя
    private void updateUserPhoto(Uri image) {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            File file = new File(FileUtils.getPath(image));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

            String userId = mDataManager.getPreferencesManager().getUserId();

            Call<UploadPhotoRes> uploadPhoto = mDataManager.uploadPhoto(
                    userId, body);

            uploadPhoto.enqueue(new Callback<UploadPhotoRes>() {
                @Override
                public void onResponse(Call<UploadPhotoRes> call, Response<UploadPhotoRes> response) {
                    if (response.code() == 200) {
                        showSnackBar("Фото профиля успешно обновлено");
                    } else {
                        if (response.code() == 404) {
                            showSnackBar("ошибка доступа к данным профиля");
                        } else showSnackBar("неизвестная ошибка " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UploadPhotoRes> call, Throwable t) {
                    showSnackBar("ошибка отправки запроса серверу");
                }
            });

        } else showSnackBar("Сеть недоступна, попробуйте позже");
    }
}

