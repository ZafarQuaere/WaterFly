package com.waterfly.user.ui.maincallingdashboard;


import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;

public interface CallingDashboardNavigator {

    void handleError(Throwable throwable);

    void openAutoCompleteSearch();

    void handleMassages(String massage);

    void onCallClick();

    void onCallRefresh();

    void nearByVendorDetails(NearByVendorsResponse nearByVendorsResponse);

    void localDatafetch();

}
