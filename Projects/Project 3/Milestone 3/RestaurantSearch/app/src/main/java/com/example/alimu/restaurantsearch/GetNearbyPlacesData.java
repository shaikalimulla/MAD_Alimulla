package com.example.alimu.restaurantsearch;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by alimu on 11/25/2016.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap currMap;
    String url;
    Location currentLocation;
    double latitude;
    double longitude;
    public AsyncTaskResponse delegate = null;


    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            currMap = (GoogleMap) params[0];
            url = (String) params[1];
            currentLocation = (Location) params[2];
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result, latitude, longitude);
        System.out.println("nearbyPlacesList"+nearbyPlacesList);

        if(nearbyPlacesList.size() > 0){
            ShowNearbyPlaces(nearbyPlacesList);
        }
        delegate.processFinish(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
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
            //move map camera
            currMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            currMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }
    }
}
