package com.waterfly.user.data.network.model.verifiedOtpResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("details_completed")
    @Expose
    private String detailsCompleted;
    @SerializedName("JWT_Token")
    @Expose
    private String jWTToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailsCompleted() {
        return detailsCompleted;
    }

    public void setDetailsCompleted(String detailsCompleted) {
        this.detailsCompleted = detailsCompleted;
    }

    public String getJWTToken() {
        return jWTToken;
    }

    public void setJWTToken(String jWTToken) {
        this.jWTToken = jWTToken;
    }

}