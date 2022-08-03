package com.waterfly.user.ui.banner;


import com.google.android.libraries.places.api.model.Place;
import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;

public interface BannerScreenNavigator {

    void handleError(Throwable throwable);

    void openAutoCompleteSearch();

    void handleMassages(String massage);

    void nearByVendorDetails(NearByVendorsResponse nearByVendorsResponse);

    void openFullMapView(NearByVendorsResponse nearByVendorsResponse,Place place);

    void openUserDetailsPopUp(NearByVendorsResponse nearByVendorsResponse);

}
