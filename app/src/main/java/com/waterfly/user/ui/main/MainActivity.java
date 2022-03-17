package com.waterfly.user.ui.main;

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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
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
import com.google.android.material.navigation.NavigationView;
import com.waterfly.user.BR;
import com.waterfly.user.R;
import com.waterfly.user.ViewModelFactory;
import com.waterfly.user.WaterFlyApp;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;
import com.waterfly.user.databinding.ActivityMainBinding;
import com.waterfly.user.ui.base.BaseActivity;
import com.waterfly.user.ui.maincallingdashboard.CallingDashboard;
import com.waterfly.user.utils.AppConstants;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainScreenNavigator, OnMapReadyCallback {

    private ActivityMainBinding mActivityMainBinding;
    private MainViewModel mMainViewModel;
    private GoogleMap mMap = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private static final int DEFAULT_ZOOM = 14;
    private boolean locationPermissionGranted = false;
    private Location lastKnownLocation = null;
    private LatLng defaultLocation = new LatLng(37.4629101, -122.2449094);
    private static final int AUTOCOMPLETE_REQUEST_CODE = 101;
    private boolean gpsStatus;
    private AlertDialog alertDialog=null,alertDialogPermission=null;
    private int allowLocationPermissionCount = 1;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.MainViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        ViewModelFactory factory = new ViewModelFactory(DataManager.getInstance());
        mMainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        return mMainViewModel;
    }

    @Override
    protected void onResume() {

//        // Turn on the My Location layer and the related control on the map.
//        if(!locationPermissionGranted){
//            updateLocationUI();
//        }
////        // Get the current location of the device and set the position of the map.
//        if(!gpsStatus ) {
//            CheckGpsStatus();
//        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = getViewDataBinding();
        mMainViewModel.setNavigator(this);
        createGPSDialog();
        createPermissionDialog();

        drawerLayout = findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
//        TextView txtUserEmail = (TextView)header.findViewById(R.id.txtUserEmail);
        TextView txtUserName = (TextView)header.findViewById(R.id.txtUserName);

        txtUserName.setText("Hello");
//        txtUserEmail.setText("Wasif.developer@gmail.com");

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key_place), Locale.ENGLISH);
        }

        initAutoCompleteSearch();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findViewById(R.id.r_mapLayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainViewModel.openFullMapView(null);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CheckGpsStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gpsStatus) {
            alertDialog.dismiss();
            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
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
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap != null) {
            mMap = googleMap;
//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(@NonNull LatLng latLng) {
//                    mMainViewModel.openFullMapView(null);
////                    Toast.makeText(WaterFlyApp.getInstance(), "Map Click", Toast.LENGTH_SHORT).show();
//                }
//            });
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();
            // Get the current location of the device and set the position of the map.
            CheckGpsStatus();
//            getDeviceLocation();
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



    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude())));

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                mMainViewModel.getNearByVendorDetails(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());

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
    public void handleError(Throwable throwable) {
        //Handle Error here
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


    @Override
    public void handleMassages(String massage) {}

    @Override
    public void nearByVendorDetails(NearByVendorsResponse nearByVendorsResponse) {

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_car);

        if(nearByVendorsResponse.getData() != null && nearByVendorsResponse.getData().size() > 0) {

            for(int i =0 ; i < nearByVendorsResponse.getData().size() ; i++) {
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(Double.parseDouble(nearByVendorsResponse.getData().get(i).getLatitude()),
                        Double.parseDouble(nearByVendorsResponse.getData().get(i).getLongitude()))).title(nearByVendorsResponse.getData().get(i).getVendorName())
                        .icon(icon);
                mMap.addMarker(markerOptions);
            }
        }else{
//            Toast.makeText(WaterFlyApp.getInstance(), "No Vendor Found", Toast.LENGTH_SHORT).show();
//            openDialog();
        }
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

    @Override
    public void openFullMapView(NearByVendorsResponse nearByVendorsResponse,Place place) {
        Intent intent = CallingDashboard.newIntent(MainActivity.this);
        intent.putExtra(AppConstants.PLACES_DETAILS,place);
        startActivity(intent);
    }

    @Override
    public void openUserDetailsPopUp(NearByVendorsResponse nearByVendorsResponse) {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    // Turn on the My Location layer and the related control on the map.
                    updateLocationUI();
                    CheckGpsStatus();
                }else{
                        openDialog();
                }
            }
        }
//        updateLocationUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                mMainViewModel.openFullMapView(place);
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
                CheckGpsStatus();
            }
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

}
