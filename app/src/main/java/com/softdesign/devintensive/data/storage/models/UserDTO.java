package com.softdesign.devintensive.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Parcelable {
    private String mPhoto;
    private String mFullName;
    private String mRating;
    private String mCodeLines;
    private String mProjects;
    private String mBio;
    private List<String> mRepositories;

    public UserDTO(User userData) {
        List<String> repoLink = new ArrayList<>();
        this.mPhoto = userData.getPhoto();
        this.mFullName = userData.getFullName();
        this.mRating = String.valueOf(userData.getRating());
        this.mCodeLines = String.valueOf(userData.getCodeLines());
        this.mProjects = String.valueOf(userData.getProjects());
        this.mBio = userData.getBio();

        for (Repository gitLink : userData.getRepositories())
        {repoLink.add(gitLink.getRepositoryName());}
        mRepositories = repoLink;
    }


    protected UserDTO(Parcel in) {
        mPhoto = in.readString();
        mFullName = in.readString();
        mRating = in.readString();
        mCodeLines = in.readString();
        mProjects = in.readString();
        mBio = in.readString();
        if (in.readByte() == 0x01) {
            mRepositories = new ArrayList<String>();
            in.readList(mRepositories, String.class.getClassLoader());
        } else {
            mRepositories = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPhoto);
        dest.writeString(mFullName);
        dest.writeString(mRating);
        dest.writeString(mCodeLines);
        dest.writeString(mProjects);
        dest.writeString(mBio);
        if (mRepositories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mRepositories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserDTO> CREATOR = new Parcelable.Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getmPhoto() {
        return mPhoto;
    }

    public String getmFullName() {
        return mFullName;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmCodeLines() {
        return mCodeLines;
    }

    public String getmProjects() {
        return mProjects;
    }

    public String getmBio() {
        return mBio;
    }

    public List<String> getmRepositories() {
        return mRepositories;
    }
}

