package com.waterfly.user.data.network.model.verifiedOtpResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpVerifiedResponse {

        @SerializedName("message")
        @Expose
        private List<String> message = null;
        @SerializedName("status")
        @Expose
        private int status;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public List<String> getMessage() {
            return message;
        }

        public void setMessage(List<String> message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }
    }