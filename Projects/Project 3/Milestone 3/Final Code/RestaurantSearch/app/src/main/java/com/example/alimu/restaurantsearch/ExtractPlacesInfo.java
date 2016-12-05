package com.example.alimu.restaurantsearch;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by alimu on 12/3/2016.
 */

public class ExtractPlacesInfo extends AsyncTask<Object, String, String>  {
    String placesData;
    GoogleMap currMap;
    String url;
    Location currentLocation;
    double latitude;
    double longitude;
    public AsyncTaskResponse delegate = null;


    @Override
    protected String doInBackground(Object... params) {
        try {
            currMap = (GoogleMap) params[0];
            url = (String) params[1];
            currentLocation = (Location) params[2];
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            ExtractUrlData extractUrl = new ExtractUrlData();
            placesData = extractUrl.processUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return placesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> placesList = null;
        ProcessData dataParser = new ProcessData();
        placesList =  dataParser.parseData(result, latitude, longitude);
        System.out.println("nearbyPlacesList"+placesList);

        if(placesList.size() > 0){
            showPlaces(placesList);
        }
        delegate.processFinish(placesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void showPlaces(List<HashMap<String, String>> placesList) {
        for (int i = 0; i < placesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = placesList.get(i);
            System.out.println("before lat"+googlePlace.get("lat"));

            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            System.out.println("lat"+lat);
            markerOptions.title(placeName);
            currMap.addMarker(markerOptions).showInfoWindow();

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            currMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }
    }
}




