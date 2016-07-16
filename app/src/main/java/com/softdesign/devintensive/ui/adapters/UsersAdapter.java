package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.softdesign.devintensive.utils.CircleImageView;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.TransformAndCrop;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by bolshakova on 13.07.2016.
 */
public class UsersAdapter  extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    List <UserListRes.UserData> mUsers;
    Context mContext;
    UserViewHolder.CustomClickListener mCustomClickListener;

    public UsersAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener customClickListener  ) {
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
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserListRes.UserData user = mUsers.get(position);
        if (user.getPublicInfo().getPhoto() == null || user.getPublicInfo().getPhoto().isEmpty()) {
            holder.mUserPhoto.setImageResource(R.drawable.user_bg);
        }

        else {Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .resize(200,0)
                .placeholder(mContext.getResources().getDrawable(R.drawable.user_bg))
                .error(mContext.getResources().getDrawable(R.drawable.user_bg))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.mUserPhoto);}

        holder.mfullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        if (user.getPublicInfo().getBio() == null || user.getPublicInfo().getBio().isEmpty())
        holder.mBio.setText("Нет информации");
        else {holder.mBio.setText(String.valueOf(user.getPublicInfo().getBio()));}
    }


    @Override
    public int getItemCount() {
        return 33;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView mUserPhoto;
        protected TextView mfullName, mRating, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        private CustomClickListener mCustomClickListener;

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




