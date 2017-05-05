package com.example.apurva.welcome.DecisionPoints;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.skobbler.ngx.SKCoordinate;

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
    private byte[] routeBuffer;
    private byte[] mapBuffer;

    public JsonParser(Context context) throws JSONException {
        InputStream is;
        try {
            is = context.getAssets().open("coordinates.json");
            int size = is.available();
            routeBuffer = new byte[size];
            is.read(routeBuffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("gievenbeck.json");
            int size = is.available();
            mapBuffer = new byte[size];
            is.read(mapBuffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset(byte[] data) {
        String json = null;

        try {
            json = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        return json;
    }

    public HashMap<String, LatLng> getDPCoordinates() {
        HashMap<String,LatLng> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(routeBuffer));
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
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(routeBuffer));
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
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(routeBuffer));
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
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(routeBuffer));
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

    /**
     * Get a hashmap with all coordinates of the busstops in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getBusstopCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("busstops");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("name") && jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = jsonInside.getString("name");
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the supermarkets in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getSupermarketCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("supermarketsGievenbeck");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("name") && jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = jsonInside.getString("name");
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the pharmacies in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getPharmacyCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("pharmacyGievenbeck");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("name") && jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = jsonInside.getString("name");
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the parks in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getParkCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("parks");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "park" + i;
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the schools in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getSchoolCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("schools");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("name") && jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = jsonInside.getString("name");
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the health centers in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getHealthcenterCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("healthCenters");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "center" + i;
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the language centers in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getLanguageCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("language");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "center" + i;
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the insurances in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getInsuranceCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("insurance");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "insurance" + i;
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the administration in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getAdministrationCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("administration");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "administration" + i;
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the libraries in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getLibraryCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("libraries");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("name") && jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = jsonInside.getString("name");
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

    /**
     * Get a hashmap with all coordinates of the sportfacilities in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getSportCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("sportFacilities");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "sport" + i;
                    latlnglist.put(name, new SKCoordinate(latitudeValue, longitudeValue));
                }
            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;
    }

}

