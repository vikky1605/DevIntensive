package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileUserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mProfileImage;
    private TextView mUserBio;
    private TextView mRating, mCodeLines, mProjects;
    private ListView mRepoListView;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_bg);
        mRating = (TextView) findViewById(R.id.rating_value);
        mCodeLines = (TextView) findViewById(R.id.code_lines_value);
        mProjects = (TextView) findViewById(R.id.projects_value);
        mUserBio = (TextView) findViewById(R.id.user_info);
        mCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);

        mRepoListView = (ListView) findViewById(R.id.repositories_list);

        setupToolbar();
        initProfileData();

    }

    public void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar !=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void initProfileData() {
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARSELABLE_KEY);
        final List<String> repositories = userDTO.getmRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepoListView.setAdapter(repositoriesAdapter);

        mUserBio.setText(userDTO.getmBio());
        mCodeLines.setText(userDTO.getmCodeLines());
        mProjects.setText(userDTO.getmProjects());
        mRating.setText(userDTO.getmRating());

        mCollapsingToolbar.setTitle(userDTO.getmFullName());

        Picasso.with(this)
                .load(userDTO.getmPhoto())
                .resize(200,0)
                .placeholder(this.getResources().getDrawable(R.drawable.user_bg))
                .error(this.getResources().getDrawable(R.drawable.user_bg))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(mProfileImage);

        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(mCollapsingToolbar, "Репозиторий " + repositories.get(position), Snackbar.LENGTH_LONG).show();

            }
        });

    }
}
