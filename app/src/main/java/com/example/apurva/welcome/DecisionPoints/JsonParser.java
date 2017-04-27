package com.example.apurva.welcome.DecisionPoints;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by apurv on 12-04-2017.
 */
public class JsonParser {
    private byte[] buffer;

    public JsonParser(Context context) throws JSONException {
        InputStream is;
        try {
            is = context.getAssets().open("coordinates.json");
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset() {
        String json = null;

        try {
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        return json;
    }

    public HashMap<String, LatLng> getDPCoordinates() {
        HashMap<String,LatLng> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray mJsonArray = mJsonObject.getJSONArray("Coordinates");

            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if (jsonInside.has("Latitude") && jsonInside.has("Longitude") && jsonInside.has("Name")){
                    Double latitudeValue = jsonInside.getDouble("Latitude");
                    Double longitudeValue = jsonInside.getDouble("Longitude");
                    String name = jsonInside.getString("Name");
                    latlnglist.put(name, new LatLng(latitudeValue, longitudeValue));
                }
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    public HashMap<String, LatLng> getDirectionCoordinates() {
        HashMap<String,LatLng> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray mJsonArray = mJsonObject.getJSONArray("Coordinates");

            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if (jsonInside.has("DirLat") && jsonInside.has("DirLong") && jsonInside.has("Name")){
                    Double latitudeValue = jsonInside.getDouble("DirLat");
                    Double longitudeValue = jsonInside.getDouble("DirLong");
                    String name = jsonInside.getString("Name");
                    latlnglist.put(name, new LatLng(latitudeValue, longitudeValue));
                }
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    public HashMap<String, LatLng> getPOI1Coordinates() {
        HashMap<String,LatLng> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray mJsonArray = mJsonObject.getJSONArray("Coordinates");

            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if (jsonInside.has("PoILat") && jsonInside.has("PoILong") && jsonInside.has("Name")){
                    Double latitudeValue = jsonInside.getDouble("PoILat");
                    Double longitudeValue = jsonInside.getDouble("PoILong");
                    String name = jsonInside.getString("Name");
                    latlnglist.put(name, new LatLng(latitudeValue, longitudeValue));
                }
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    public HashMap<String, LatLng> getPOI2Coordinates() {
        HashMap<String,LatLng> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray mJsonArray = mJsonObject.getJSONArray("Coordinates");

            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if (jsonInside.has("PoI2Lat") && jsonInside.has("PoI2Long") && jsonInside.has("Name")){
                    Double latitudeValue = jsonInside.getDouble("PoI2Lat");
                    Double longitudeValue = jsonInside.getDouble("PoI2Long");
                    String name = jsonInside.getString("Name");
                    latlnglist.put(name, new LatLng(latitudeValue, longitudeValue));
                }
            }
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }
}

