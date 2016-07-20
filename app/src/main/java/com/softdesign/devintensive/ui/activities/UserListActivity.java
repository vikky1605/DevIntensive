package com.softdesign.devintensive.ui.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.LoaderDataFromDb;

import java.util.List;

import retrofit2.Call;

public class UserListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<User>> {

    CoordinatorLayout mCoordinatorLayout;
    Toolbar mToolbar;
    DrawerLayout mNavigationDrawer;
    RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUserAdapter;
    private List<User> mUsers;
    private static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";
    private MenuItem mSearchItem;
    private String mQuery;
    private Handler mHandler;
    private Context mContext;
    private Loader mLoader;


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
        mHandler = new Handler();

        setupToolbar();
        setupDrawer();
        loadUsersFromDb();
    }

    private void loadUsersFromDb() {
        Bundle bndl = new Bundle();
        bndl.putString(ConstantManager.LOADER_KEY, "null");
        mLoader = getLoaderManager().initLoader(ConstantManager.LOADER_ID, bndl, this);
        Log.d(TAG, "я тут 0");
        mLoader.forceLoad();

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        mSearchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView((mSearchItem));
        searchView.setQueryHint("Введите имя пользователя");

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mLoader.cancelLoad();
                Log.d(TAG, "отказалась от поиска");
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO поиск вызывать тут
                Log.d("вы ввели", newText);
                showUserByQuery(newText);
                return false;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }
    public void showUsers (List<User> users) {
        mUsers = users;
        Log.d(TAG, "я тут 3");
        mUserAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userDTO = new UserDTO(mUsers.get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARSELABLE_KEY, userDTO);

                Log.d("TAG","пытаюсь перейти в профиль пользователя");
                startActivity(profileIntent);

            }
        });
        mRecyclerView.swapAdapter(mUserAdapter, false);
        Log.d(TAG, "я тут 4");
    }

    private void showUserByQuery (String query) {

        mQuery = query;
        Runnable searchUsers = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "ищу пользователя с именем " + mQuery);
            }
        };
        mHandler.removeCallbacks(searchUsers);
        mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);
        Bundle bndl = new Bundle();
        bndl.putString(ConstantManager.LOADER_KEY, mQuery);
        getLoaderManager().destroyLoader(ConstantManager.LOADER_ID);
        getLoaderManager().destroyLoader(ConstantManager.LOADER_SEARCH_ID);
        mLoader = getLoaderManager().initLoader(ConstantManager.LOADER_SEARCH_ID, bndl, this);
        Log.d(TAG, "я загружаю пользователя");
        mLoader.forceLoad();
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Loader <List<User>> loader = null;
        mContext = DevintensiveApplication.getContext();
        loader = new LoaderDataFromDb(mContext, args);
        Log.d(TAG, "я тут 1");
        return loader;
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> users) {
        mUsers = users;
        Log.d(TAG, "я тут");
        if (mUsers == null) {
            Log.d(TAG, "я тут 00000");
            showSnackbar("Список пользователей не может быть загружен");
        } else {
            Log.d(TAG, "я тут 2");
            showUsers(mUsers);
        }
    }
}


