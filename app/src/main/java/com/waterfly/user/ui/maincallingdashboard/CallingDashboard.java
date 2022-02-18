package com.waterfly.user.ui.maincallingdashboard;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.waterfly.user.BR;
import com.waterfly.user.R;
import com.waterfly.user.ViewModelFactory;
import com.waterfly.user.WaterFlyApp;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.UserDetails;
import com.waterfly.user.data.network.model.nearbyvendors.Datum;
import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;
import com.waterfly.user.databinding.ActivityFullMapBinding;
import com.waterfly.user.ui.base.BaseActivity;
import com.waterfly.user.utils.AppConstants;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class CallingDashboard extends BaseActivity<ActivityFullMapBinding, CallingDashBoardViewModel> implements UserDetailsAdapter.UserDetailsListener, CallingDashboardNavigator, OnMapReadyCallback {

    private ActivityFullMapBinding mActivityMainBinding;
    private CallingDashBoardViewModel mMainViewModel;
    private GoogleMap mMap = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE=2;
    private static final int DEFAULT_ZOOM = 14;
    private boolean locationPermissionGranted = false;
    private boolean callPermissionGranted = false;
    private Location lastKnownLocation = null;
    private LatLng defaultLocation = new LatLng(37.4629101, -122.2449094);
    private UserDetailsAdapter userDetailsAdapter;
    private Datum mUserDetails = null;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 101;
    private Place place = null;
    private AlertDialog alertDialogPermission,alertDialog;
    private boolean gpsStatus;
    private int allowLocationPermissionCount = 1;
    private NearByVendorsResponse mNearByVendorsResponse;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CallingDashboard.class);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.CallingDashBoardViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_full_map;
    }

    @Override
    public CallingDashBoardViewModel getViewModel() {
        ViewModelFactory factory = new ViewModelFactory(DataManager.getInstance());
        mMainViewModel = ViewModelProviders.of(this, factory).get(CallingDashBoardViewModel.class);
        return mMainViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = getViewDataBinding();
        mMainViewModel.setNavigator(this);
        createGPSDialog();
        createPermissionDialog();

        userDetailsAdapter = new UserDetailsAdapter(this);
        mActivityMainBinding.recyclerView.setAdapter(userDetailsAdapter);
        mActivityMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        place = getIntent().getParcelableExtra(AppConstants.PLACES_DETAILS);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key_place), Locale.ENGLISH);
        }

        initAutoCompleteSearch();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void CheckGpsStatus(Place place){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gpsStatus) {
            // Get the current location of the device and set the position of the map.
            alertDialog.dismiss();
            getDeviceLocation(place);
        }else{
            openGPSDialog();
        }
    }

    private void initAutoCompleteSearch(){
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
    }

    @Override
    public void openAutoCompleteSearch() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void setAutoCompleteSearch(){}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap != null) {
            mMap = googleMap;
            mMap.clear();
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

//            Log.i("TAG", "Place: " + place.toString());

            // Get the current location of the device and set the position of the map.
            CheckGpsStatus(place);
        }else{
            Toast.makeText(WaterFlyApp.getInstance(), "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
        }
    }



    private void getDeviceLocation(Place place) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            mMap.clear();
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                if(place == null || place.getLatLng() == null) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude())));

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                    mMainViewModel.getNearByVendorDetails(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
//                                    28.576332, 77.386383
                                } else{
                                    mMap.addMarker(new MarkerOptions()
                                            .position(place.getLatLng()));

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));

                                    mMainViewModel.getNearByVendorDetails(place.getLatLng().latitude,place.getLatLng().longitude);
                                }
                            }
                        } else {
                            mMap.addMarker(new MarkerOptions()
                                    .position(defaultLocation));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));

                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    @Override
    public void handleError(Throwable throwable) {
        //Handle Error here
    }

    @Override
    public void onUserDetailsClicked(Datum userDetails) {
        mUserDetails = userDetails;
        userDetailsAdapter.setSelectedCard(userDetails);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(userDetails.getLatitude()),Double.parseDouble(userDetails.getLongitude())), 18));

    }

    private void openDialogVendorNotFound(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("No Vendors found")
//set message
//                .setMessage("Allow WaterFly to automatically detect your current location to show your available vendors")
//set positive button
//                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        //set what would happen when positive button is clicked
//                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//                    }
//                })
//set negative button
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                });
        alert.show();
    }


    private void createPermissionDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("Location permission required")
//set message
                .setMessage("Allow WaterFly to automatically detect your current location to show your available vendors")
//set positive button
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //set what would happen when positive button is clicked
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent,0);
                    }
                }).setCancelable(false);
//set negative button
//                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //set what should happen when negative button is clicked
//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
//                    }
//                })
        alertDialogPermission= alert.create();
    }

    private void openDialog(){

        alertDialogPermission.show();
    }

    private void createGPSDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("GPS Turned off")
//set message
                .setMessage("Allow WaterFly to turn on your phone GPS for accurate vendor locations")
//set positive button
                .setPositiveButton("TURN ON GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    }
                }).setCancelable(false);
//set negative button
//                .setNegativeButton("TURN ON GPS", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//
//                    }
//                });
        alertDialog= alert.create();
    }

    private void openGPSDialog(){
        alertDialog.show();
    }


    @Override
    public void onCallClick() {
        getPhonePermission();
        if(callPermissionGranted && mUserDetails !=null) {
            mUserDetails.setCalled(true);
            mUserDetails.setSelectedCard(false);
            userDetailsAdapter.notifyDataSetChanged();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+mUserDetails.getPhone()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            startActivity(intent);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
        }else{
            Toast.makeText(WaterFlyApp.getInstance(),"Please select vendor",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCallRefresh() {
        if(place == null || place.getLatLng() == null) {
            mMainViewModel.getNearByVendorDetails(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        }else{
            mMainViewModel.getNearByVendorDetails(place.getLatLng().latitude,place.getLatLng().longitude);
              }
    }

    @Override
    public void handleMassages(String massage) {}


    @Override
    public void nearByVendorDetails(NearByVendorsResponse nearByVendorsResponse) {
        this.mNearByVendorsResponse = nearByVendorsResponse;
        if(nearByVendorsResponse == null){
            userDetailsAdapter.setItems(null);
            openDialogVendorNotFound();
            mActivityMainBinding.btnCallBtn.setBackground(getResources().getDrawable(R.drawable.call_grey_shape_bk));
            mActivityMainBinding.btnCallBtn.setClickable(false);
        }else{
            mActivityMainBinding.btnCallBtn.setBackground(getResources().getDrawable(R.drawable.call_btn__shape_bk));
            mActivityMainBinding.btnCallBtn.setClickable(true);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_car);

            userDetailsAdapter.setItems(nearByVendorsResponse.getData());

            if(nearByVendorsResponse.getData() != null && nearByVendorsResponse.getData().size() > 0) {
                for(int i =0 ; i < nearByVendorsResponse.getData().size() ; i++) {
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(nearByVendorsResponse.getData().get(i).getLatitude()),
                            Double.parseDouble(nearByVendorsResponse.getData().get(i).getLongitude()))).title(nearByVendorsResponse.getData().get(i).getVendorName())
                            .icon(icon);

                    mMap.addMarker(markerOptions);
                }
            }
        }
    }


    @Override
    public void localDatafetch() {
//      mMainViewModel.getDataManager().getAppDbHelper().getAllUsers().observe(this,new UserDetailsObserver());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        callPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    updateLocationUI();
                    CheckGpsStatus(place);
                }else{
                    openDialog();
                }
                break;
            }
            case PERMISSIONS_REQUEST_CALL_PHONE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPermissionGranted = true;
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                 place = Autocomplete.getPlaceFromIntent(data);
//                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                mActivityMainBinding.autoCompleteTextView1.setText(place.getName());
                CheckGpsStatus(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }else{
            if(allowLocationPermissionCount <=2) {
                allowLocationPermissionCount = allowLocationPermissionCount + 1;
                updateLocationUI();
            }
            CheckGpsStatus(place);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getPhonePermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            callPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

}
