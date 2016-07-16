package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    CoordinatorLayout mCoordinatorLayout;
    Toolbar mToolbar;
    DrawerLayout mNavigationDrawer;
    RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUserAdapter;
    private List<UserListRes.UserData> mUsers;
    private static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout)findViewById(R.id.navigation_drawer);

        mDataManager = DataManager.getINSTANCE();

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        setupToolbar();
        setupDrawer();
        loadUsers();
    }

    private void loadUsers() {

        Call<UserListRes> call = mDataManager.getUserList();
        try {
            call.enqueue(new Callback<UserListRes>() {
                @Override
                public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                    try {
                        mUsers = response.body().getData();
                        mUserAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
                            @Override
                            public void onUserItemClickListener(int position) {
                                UserDTO userDTO = new UserDTO(mUsers.get(position));
                                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                profileIntent.putExtra(ConstantManager.PARSELABLE_KEY, userDTO);
                                startActivity(profileIntent);
                            }
                        });

                                mRecyclerView.setAdapter(mUserAdapter);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                        showSnackbar("Что-то пошло не так");
                    }
                }

                @Override
                public void onFailure(Call<UserListRes> call, Throwable t) {
                    // TODO обработать ошибки

                }
            });

        } catch (Exception e) {
            Log.d("метка 666", "исключение");
        }
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            mNavigationDrawer.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer () {
        //  TODO: 14.07.2016  реализовать переход в другую активити
        // при клике по элементу меню в mNavigationDrawer




    }
    public void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }


   }
