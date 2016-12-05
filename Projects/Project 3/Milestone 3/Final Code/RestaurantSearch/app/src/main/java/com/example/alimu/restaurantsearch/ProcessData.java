package com.example.alimu.restaurantsearch;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alimu on 12/3/2016.
 */

public class ProcessData {
    public List<HashMap<String, String>> parseData(String jsonData, double latitude, double longitude) {
        JSONArray jsonArr = null;
        JSONObject jsonObj;

        try {
            Log.d("Places", "parse");
            jsonObj = new JSONObject((String) jsonData);
            jsonArr = jsonObj.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlacesInfo(jsonArr, latitude, longitude);
    }

    private List<HashMap<String, String>> getPlacesInfo(JSONArray jsonArr, double latitude, double longitude) {
        int placesCount = jsonArr.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlaceInfo((JSONObject) jsonArr.get(i), latitude, longitude);
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlaceInfo(JSONObject googlePlaceJson, double currentLatitude, double currentLongitude) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String availability = "";
        String icon = "";

        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            if (!googlePlaceJson.isNull("opening_hours")) {
                availability = String.valueOf(googlePlaceJson.getJSONObject("opening_hours").getBoolean("open_now"));
            }

            if (!googlePlaceJson.isNull("icon")) {
                icon = googlePlaceJson.getString("icon");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");

            Location locationA = new Location("point A");
            locationA.setLatitude(currentLatitude);
            locationA.setLongitude(currentLongitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(Double.valueOf(latitude));
            locationB.setLongitude(Double.valueOf(longitude));
            double distance = locationA.distanceTo(locationB) * 0.000621371 ;
            String distanceVal = String.format( "%.1f", distance );

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("availability", availability);
            googlePlaceMap.put("distance", distanceVal);
            googlePlaceMap.put("icon", icon);
            Log.d("availability", availability);
            System.out.println("getPlace distance"+distance);
            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
