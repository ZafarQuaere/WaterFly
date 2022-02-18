package com.waterfly.user.data.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOtpResponse {

    @Expose
    @SerializedName("message")
    private List<String> message = null;

    @Expose
    @SerializedName("status")
    private int status;

    @Expose
    @SerializedName("data")
    private List<Object> data = null;

    public List<String> getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public List<Object> getData() {
        return data;
    }

}