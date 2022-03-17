package com.waterfly.user.data.network.model.nearbyvendors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum
//        implements Parcelable
{

    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("selectedCard")
    @Expose
    private boolean isSelectedCard;

    @SerializedName("phone")
    @Expose
    private String phone;


    @SerializedName("called")
    @Expose
    private boolean isCalled;

//    public final static Creator<Datum> CREATOR = new Creator<Datum>() {
//
//
//        @SuppressWarnings({
//                "unchecked"
//        })
//        public Datum createFromParcel(android.os.Parcel in) {
//            return new Datum(in);
//        }
//
//        public Datum[] newArray(int size) {
//            return (new Datum[size]);
//        }
//
//    };

//    protected Datum(android.os.Parcel in) {
//        this.vendorId = ((String) in.readValue((String.class.getClassLoader())));
//        this.vendorName = ((String) in.readValue((String.class.getClassLoader())));
//        this.distance = ((String) in.readValue((String.class.getClassLoader())));
//        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
//        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
//        this.isSelectedCard = ((boolean) in.readValue((boolean.class.getClassLoader())));
//        this.isCalled = ((boolean) in.readValue((boolean.class.getClassLoader())));
//        this.phone = ((String) in.readValue((String.class.getClassLoader())));
//    }

    public Datum() {}

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isSelectedCard() {
        return isSelectedCard;
    }

    public void setSelectedCard(boolean selectedCard) {
        isSelectedCard = selectedCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isCalled() {
        return isCalled;
    }

    public void setCalled(boolean called) {
        this.isCalled = called;
    }

//    public void writeToParcel(android.os.Parcel dest, int flags) {
//        dest.writeValue(vendorId);
//        dest.writeValue(vendorName);
//        dest.writeValue(distance);
//        dest.writeValue(latitude);
//        dest.writeValue(longitude);
//        dest.writeValue(isSelectedCard);
//        dest.writeValue(phone);
//        dest.writeValue(isCalled);
//    }

//    public int describeContents() {
//        return 0;
//    }

}