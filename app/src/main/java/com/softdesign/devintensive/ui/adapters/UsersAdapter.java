package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;

import java.util.List;

public class UsersAdapter  extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    public static final String TAG = ConstantManager.TAG_PREFIX + "UsersAdapter";
    List<User> mUsers;
    Context mContext;
    UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<User> users, UserViewHolder.CustomClickListener customClickListener  ) {
        this.mCustomClickListener = customClickListener;
        this.mUsers = users;
    }
    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = DevintensiveApplication.getContext();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(convertView, mCustomClickListener);
    }


    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {

        final User user = mUsers.get(position);
        final String userPhoto;

        if (user.getPhoto().isEmpty()) {
            userPhoto = "null";
        Log.e(TAG, "OnBindViewHolder: user with name " + user.getFullName() + " has empty photo");}
        else userPhoto = user.getPhoto();

        DataManager.getINSTANCE().getPicasso()
                .load(userPhoto)
                .resize(512,0)
                .placeholder(holder.mDummy)
                .error(holder.mDummy)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.mUserPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, " load from cache");

                    }

                    @Override
                    public void onError() {
                        DataManager.getINSTANCE().getPicasso()
                                .load(userPhoto)
                                .resize(512,0)
                                .placeholder(holder.mDummy)
                                .error(holder.mDummy)
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(holder.mUserPhoto, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, "Could not fetch image");

                                    }
                                });

                    }
                });


        holder.mfullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProjects()));

        if (user.getBio() == null || user.getBio().isEmpty())
        holder.mBio.setText("Нет информации");
        else {holder.mBio.setText(String.valueOf(user.getBio()));}
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView mUserPhoto;
        protected TextView mfullName, mRating, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        private CustomClickListener mCustomClickListener;
        protected Drawable mDummy;

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            this.mCustomClickListener = customClickListener;
            mUserPhoto = (ImageView) itemView.findViewById(R.id.user_photo);
            mfullName = (TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.rating_txt);
            mCodeLines = (TextView)itemView.findViewById(R.id.code_lines_txt);
            mProjects = (TextView)itemView.findViewById(R.id.projects_txt);
            mBio = (TextView)itemView.findViewById(R.id.bio_txt);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_btn);
            mDummy = mUserPhoto.getContext().getResources().getDrawable(R.drawable.user_bg);

            mShowMore.setOnClickListener(this); }

        @Override
        public void onClick(View v) {
            if (mCustomClickListener !=null)
                mCustomClickListener.onUserItemClickListener(getAdapterPosition());

        }

        public interface CustomClickListener {

                public void onUserItemClickListener(int position);

            }
        }


    }




