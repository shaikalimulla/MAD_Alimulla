package com.example.alimu.restaurantsearch;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* References
* 1. https://www.google.com/search?q=find+places+nearby+images&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjlnozkxM_QAhVE_4MKHYydB7IQ_AUICSgC&biw=1366&bih=662#tbm=isch&q=google+maps&imgrc=N5te1LV3wbMciM%3A
* 2. https://developers.google.com/maps/documentation/android-api/location
* 3. https://developers.google.com/places/android-api/
*/

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        AsyncTaskResponse {

    private GoogleMap currentMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;
    GoogleApiClient apiClient;
    Location lastLocation;
    Marker currLocMarker;
    LocationRequest currLocReq;
    private static final String GOOGLE_API_KEY = "AIzaSyAuHkdPZEaXha9eusWWoifLkiB9bKFNiCo";
    EditText searchText;
    Button searchButton;
    EditText zipText;
    Button zipButton;
    TextView showList;
    static String nearbyPlace;
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> vicinityList = new ArrayList<String>();
    ArrayList<String> distanceList = new ArrayList<String>();
    ArrayList<String> availabilityList = new ArrayList<String>();
    ArrayList<String> iconList = new ArrayList<String>();
    CustomList adapter;
    LinearLayout listViewLayout;
    LinearLayout toggleLayout;
    ListView resultList;
    public ToggleButton toggle;
    public Switch locationSwitch;
    public Location updatedLocation;
    public String markerTitle = "";
    public boolean changeLocationSelected = false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        System.out.println("Maps onCreate called:");

        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        searchText = (EditText) findViewById(R.id.searchText);
        searchButton = (Button) findViewById(R.id.searchButton);
        zipText = (EditText) findViewById(R.id.zipText);
        zipButton = (Button) findViewById(R.id.zipButton);
        showList = (TextView) findViewById(R.id.showList);

        showList.setVisibility(View.INVISIBLE);
        System.out.println("nearbyPlace" + nearbyPlace);

        zipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCode = zipText.getText().toString();
                if (zipCode.matches("")) {
                    Toast.makeText(MapsActivity.this, "Please enter zipcode to update location", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideKeyboard();

                updateLocation(zipCode);
                toggle.setVisibility(View.INVISIBLE);
                locationSwitch.setVisibility(View.INVISIBLE);
                showList.setVisibility(View.INVISIBLE);
            }
        });


        locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        locationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocationSelected = locationSwitch.isChecked();
                if(changeLocationSelected){
                    toggle.setVisibility(View.INVISIBLE);
                    zipText.setVisibility(View.VISIBLE);
                    zipText.setText("");
                    zipButton.setVisibility(View.VISIBLE);
                    showList.setVisibility(View.INVISIBLE);
                } else {
                    toggle.setVisibility(View.VISIBLE);
                    zipText.setVisibility(View.INVISIBLE);
                    zipButton.setVisibility(View.INVISIBLE);
                    showList.setVisibility(View.VISIBLE);
                }
            }
        });

        toggleLayout = (LinearLayout) findViewById(R.id.toggleButtonLayout);
        toggle = (ToggleButton) toggleLayout.findViewById(R.id.toggleListView);
        boolean showListView = toggle.isChecked();
        System.out.println("processFinish showListView:" + showListView);
        toggle.setVisibility(View.INVISIBLE);
        locationSwitch.setVisibility(View.INVISIBLE);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean showListView = toggle.isChecked();
                System.out.println("setOnClickListener showListView:" + showListView);
                listViewLayout = (LinearLayout) findViewById(R.id.mapListViewSlider);
                resultList = (ListView) listViewLayout.findViewById(R.id.resultListView);

                if (showListView) {
                    resultList.setVisibility(View.VISIBLE);
                    listViewLayout.setVisibility(View.VISIBLE);
                    //System.out.println("setOnClickListener nameList:"+ nameList);
                    adapter = new CustomList(MapsActivity.this, nameList, vicinityList, distanceList, availabilityList, iconList);
                    resultList.setAdapter(adapter);
                    locationSwitch.setVisibility(View.INVISIBLE);
                    showList.setVisibility(View.INVISIBLE);
                    toggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_down));
                } else {
                    hideListView();
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void hideListView(){
        resultList.setEmptyView(findViewById(android.R.id.empty));
        resultList.setAdapter(null);
        locationSwitch.setVisibility(View.VISIBLE);
        resultList.setVisibility(View.INVISIBLE);
        listViewLayout.setVisibility(View.INVISIBLE);
        showList.setVisibility(View.VISIBLE);
        toggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_up));
    }

    public void updateLocation(String zipCode){
        final Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                if (ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();

                    double lat = address.getLatitude();
                    double lng = address.getLongitude();

                    updatedLocation = new Location("current position");
                    updatedLocation.setLatitude(lat);
                    updatedLocation.setLongitude(lng);
                }
            } else {
                Toast.makeText(MapsActivity.this, "Geocode unable to convert entered zipcode to Latlang", Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
        }
    }

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    public boolean validateSearchString(String input){
        String sentences[] = input.split(" ");
        boolean flag = true;
        String supportedTypeList[] = {"accounting", "airport", "amusement_park", "aquarium", "art_gallery", "atm", "bakery",
                "bank", "bar", "beauty_salon", "bicycle_store", "book_store", "bowling_alley", "bus_station", "cafe", "campground",
                "car_dealer", "car_rental", "car_repair", "car_wash", "casino", "cemetery", "church", "city_hall", "clothing_store",
                "convenience_store", "courthouse", "dentist", "department_store", "doctor", "electrician", "electronics_store",
                "embassy", "establishment", "finance", "fire_station", "florist", "food", "funeral_home", "furniture_store",
                "gas_station", "general_contractor", "grocery_or_supermarket", "gym", "hair_care", "hardware_store", "health",
                "hindu_temple", "home_goods_store", "hospital", "insurance_agency", "jewelry_store", "laundry", "lawyer", "library",
                "liquor_store", "local_government_office", "locksmith", "lodging", "meal_delivery", "meal_takeaway", "mosque",
                "movie_rental", "movie_theater", "moving_company", "museum", "night_club", "painter", "park", "parking", "pet_store",
                "pharmacy", "physiotherapist", "place_of_worship", "plumber", "police", "post_office", "real_estate_agency",
                "restaurant", "roofing_contractor", "rv_park", "school", "shoe_store", "shopping_mall", "spa", "stadium", "storage",
                "store", "subway_station", "synagogue", "taxi_stand", "train_station", "transit_station", "travel_agency", "university",
                "veterinary_care", "zoo"};

        if (sentences.length > 1) {
            Toast.makeText(MapsActivity.this, "Please enter single word to search", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i = 0; i< supportedTypeList.length;i++){
            if (input.equals(supportedTypeList[i])) {
                flag = false;
            }
        }
        if(flag){
            Toast.makeText(MapsActivity.this, "No results found for "+input, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        currentMap = googleMap;
        currentMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                currentMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            currentMap.setMyLocationEnabled(true);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearbyPlace = searchText.getText().toString();
                if (nearbyPlace.matches("")) {
                    Toast.makeText(MapsActivity.this, "Please enter places to search", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideKeyboard();
                if(!validateSearchString(nearbyPlace)){
                    return;
                }
                Log.d("onClick", "Button is Clicked");
                currentMap.clear();

                if(resultList !=null){
                    hideListView();
                }

                String url = getUrl(latitude, longitude, nearbyPlace);
                Location currentLocation = new Location("current position");
                currentLocation.setLatitude(latitude);
                currentLocation.setLongitude(longitude);

                Object[] DataTransfer = new Object[3];
                DataTransfer[0] = currentMap;
                DataTransfer[1] = url;
                DataTransfer[2] = currentLocation;

                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.delegate = MapsActivity.this;
                getNearbyPlacesData.execute(DataTransfer);
            }
        });
    }

    public void swapLists(int i, int j, ArrayList<String> currList){
        String tempVal = "";
        tempVal = currList.get(j);
        currList.set(j, currList.get(i));
        currList.set(i, tempVal);
    }
    public void sortLists(){
        int i, j;
        double value1, value2;
        for(i=0;i<distanceList.size();i++){
            for(j=i+1; j<distanceList.size();j++){
                value1 = Double.valueOf(distanceList.get(i));
                value2 = Double.valueOf(distanceList.get(j));
                if(value1 > value2){
                    swapLists(i, j, distanceList);
                    swapLists(i, j, nameList);
                    swapLists(i, j, availabilityList);
                    swapLists(i, j, vicinityList);
                    swapLists(i, j, iconList);
                }
            }
        }
    }

    @Override
    public void processFinish(List<HashMap<String, String>> nearbyPlacesList) {
        MarkerOptions markerOptions;
        HashMap<String, String> googlePlace;
        double lat, lng;
        String placeName, vicinity, availability, distance, icon;
        LatLng latLng;

        nameList.clear();
        vicinityList.clear();
        distanceList.clear();
        availabilityList.clear();
        iconList.clear();

        if(nearbyPlacesList.size() == 0){
            Log.d("processFinish", "processFinish list 0");
            toggle.setVisibility(View.INVISIBLE);
            locationSwitch.setVisibility(View.INVISIBLE);
            showList.setVisibility(View.INVISIBLE);
            zipText.setVisibility(View.VISIBLE);
            zipButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No results found for "+nearbyPlace, Toast.LENGTH_SHORT).show();
        } else {
            toggle.setVisibility(View.VISIBLE);
            toggle.setChecked(false);
            locationSwitch.setChecked(false);
            locationSwitch.setVisibility(View.VISIBLE);
            showList.setVisibility(View.VISIBLE);
            zipText.setVisibility(View.INVISIBLE);
            zipButton.setVisibility(View.INVISIBLE);
            Toast.makeText(MapsActivity.this, "Places Nearby " + nearbyPlace, Toast.LENGTH_LONG).show();
        }
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("processFinish", "processFinish");
            markerOptions = new MarkerOptions();
            googlePlace = nearbyPlacesList.get(i);

            lat = Double.parseDouble(googlePlace.get("lat"));
            lng = Double.parseDouble(googlePlace.get("lng"));
            placeName = googlePlace.get("place_name");
            vicinity = googlePlace.get("vicinity");
            availability = googlePlace.get("availability");
            distance = googlePlace.get("distance");
            icon = googlePlace.get("icon");
            latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            Log.d("placeName", placeName);
            Log.d("availability", availability);
            nameList.add(placeName);
            vicinityList.add(vicinity);
            distanceList.add(distance);
            availabilityList.add(availability);
            iconList.add(icon);
        }
        System.out.println("before lists");
        System.out.println(distanceList);
        sortLists();
        System.out.println("lists");
        System.out.println(distanceList);
    }

    protected synchronized void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        apiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("onConnected called");
        currLocReq = new LocationRequest();
        currLocReq.setInterval(1000);
        currLocReq.setFastestInterval(1000);
        currLocReq.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, currLocReq, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResume() {
        System.out.println("on Resume ");
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        lastLocation = location;
        if (currLocMarker != null) {
            currLocMarker.remove();
        }

        if (updatedLocation != null) {
            currentMap.clear();
            markerTitle = "Selected Position";
            latitude = updatedLocation.getLatitude();
            longitude = updatedLocation.getLongitude();
        }
        else {
            markerTitle = "Current Position";
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(markerTitle);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currLocMarker = currentMap.addMarker(markerOptions);
        currLocMarker.showInfoWindow();

        currentMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        currentMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(MapsActivity.this, "Your "+markerTitle, Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        if (apiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (apiClient == null) {
                            buildGoogleApiClient();
                        }
                        currentMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}