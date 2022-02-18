package com.waterfly.user.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginResponse {

    @Expose
    @SerializedName("token")
    private String token;

    public String getToken(){
        return token;
    }
}