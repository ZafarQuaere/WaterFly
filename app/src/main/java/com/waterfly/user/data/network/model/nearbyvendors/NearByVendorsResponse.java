package com.waterfly.user.data.network.model.nearbyvendors;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearByVendorsResponse
//        implements Parcelable
{

    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
//    public final static Creator<NearByVendorsResponse> CREATOR = new Creator<NearByVendorsResponse>() {
//
//
//        @SuppressWarnings({
//                "unchecked"
//        })
//        public NearByVendorsResponse createFromParcel(android.os.Parcel in) {
//            return new NearByVendorsResponse(in);
//        }
//
//        public NearByVendorsResponse[] newArray(int size) {
//            return (new NearByVendorsResponse[size]);
//        }
//
//    };

//    protected NearByVendorsResponse(Parcel in) {
////        in.readList(this.message, (java.lang.String.class.getClassLoader()));
//        this.status = ((int) in.readValue((int.class.getClassLoader())));
//        in.readList(this.data, (Datum.class.getClassLoader()));
//    }

    public NearByVendorsResponse() {
    }

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

//    public void writeToParcel(android.os.Parcel dest, int flags) {
////        dest.writeList(message);
//        dest.writeValue(status);
//        dest.writeList(data);
//    }

//    public int describeContents() {
//        return 0;
//    }

}