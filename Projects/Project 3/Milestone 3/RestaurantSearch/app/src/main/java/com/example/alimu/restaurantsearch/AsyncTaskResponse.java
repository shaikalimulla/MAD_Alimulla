package com.example.alimu.restaurantsearch;

import java.util.HashMap;
import java.util.List;

/**
 * Created by alimu on 11/27/2016.
 */

public interface AsyncTaskResponse {
    void processFinish(List<HashMap<String, String>> nearbyPlacesList);
}
