package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bolshakova on 14.07.2016.
 */
 public class UserListRes {

    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public List<UserData> data = new ArrayList<UserData>();

    public List<UserData> getData() {
        return data;
    }

    public UserListRes(List<UserData> data) {
        this.data = data;
    }

    public class UserData {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("second_name")
        @Expose
        private String secondName;
        @SerializedName("__v")
        @Expose
        private int v;
        @SerializedName("repositories")
        @Expose
        private UserModelRes.Repositories repositories;
        @SerializedName("profileValues")
        @Expose
        private UserModelRes.ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        private UserModelRes.PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("updated")
        @Expose
        private String updated;

        public UserModelRes.Repositories getRepositories() {
            return repositories;
        }

        public UserModelRes.ProfileValues getProfileValues() {
            return profileValues;
        }

        public UserModelRes.PublicInfo getPublicInfo() {
            return publicInfo;
        }

        public String getId() {
            return id;
        }

        public String getFullName() {
            return firstName + " " + secondName;

        }
    }



}


