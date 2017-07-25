package com.example.apurva.welcome.DecisionPoints;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.SKPolyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by apurv on 12-04-2017.
 */
public class JsonParser {
    private byte[] routeBuffer;
    private byte[] route1Buffer;
    private byte[] route2Buffer;
    private byte[] route3Buffer;
    private byte[] route4Buffer;
    private byte[] route5Buffer;
    private byte[] route1ARBuffer;
    private byte[] route2ARBuffer;
    private byte[] route3ARBuffer;
    private byte[] route4ARBuffer;
    private byte[] route5ARBuffer;

    private int route;

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
            is = context.getAssets().open("Route1.json");
            int size = is.available();
            route1Buffer = new byte[size];
            is.read(route1Buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route2.json");
            int size = is.available();
            route2Buffer = new byte[size];
            is.read(route2Buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route3.json");
            int size = is.available();
            route3Buffer = new byte[size];
            is.read(route3Buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route4.json");
            int size = is.available();
            route4Buffer = new byte[size];
            is.read(route4Buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route5.json");
            int size = is.available();
            route5Buffer = new byte[size];
            is.read(route5Buffer);
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
        try {
            is = context.getAssets().open("Route1AR.json");
            int size = is.available();
            route1ARBuffer = new byte[size];
            is.read(route1ARBuffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route2AR.json");
            int size = is.available();
            route2ARBuffer = new byte[size];
            is.read(route2ARBuffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route3AR.json");
            int size = is.available();
            route3ARBuffer = new byte[size];
            is.read(route3ARBuffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route4AR.json");
            int size = is.available();
            route4ARBuffer = new byte[size];
            is.read(route4ARBuffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = context.getAssets().open("Route5AR.json");
            int size = is.available();
            route5ARBuffer = new byte[size];
            is.read(route5ARBuffer);
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
     * Get the coordinats of a route and return it as a SKPolyline
     * @param route Number of the route that is considered
     * @return Polyline of all route elements in the json file
     */
    public SKPolyline getRoute(int route) {
        SKPolyline line = new SKPolyline();
        JSONObject mJsonObject;
        try {
            //select the route
            switch(route) {
                case 1: mJsonObject = new JSONObject(loadJSONFromAsset(route1Buffer));
                    break;
                case 2: mJsonObject = new JSONObject(loadJSONFromAsset(route2Buffer));
                    break;
                case 3: mJsonObject = new JSONObject(loadJSONFromAsset(route3Buffer));
                    break;
                case 4: mJsonObject = new JSONObject(loadJSONFromAsset(route4Buffer));
                    break;
                case 5: mJsonObject = new JSONObject(loadJSONFromAsset(route5Buffer));
                    break;
                default: mJsonObject = new JSONObject(loadJSONFromAsset(route1Buffer));
            }
            JSONArray mJsonArray = mJsonObject.getJSONArray("features");
            ArrayList<SKCoordinate> nodes = new ArrayList();

            //get all the data from the json file
            JSONObject jsonInside = mJsonArray.getJSONObject(0);
            JSONObject current = jsonInside.getJSONObject("geometry");
            //get the coordinates from the line
            JSONArray coords = current.getJSONArray("coordinates");
            for(int k = 0; k < coords.length(); k++) {
                JSONArray item = coords.getJSONArray(k);
                nodes.add(new SKCoordinate(item.getDouble(1), item.getDouble(0)));
            }
            line.setNodes(nodes);
        }
        catch(JSONException e) {
                throw new RuntimeException(e);
        }
        return line;
    }


    /**
     * Get the list of all decision points on the selected route
     * @param route id of the route to be used
     * @return Hashmap of all the DPs
     */
    public HashMap<String, LatLng> getDecisionpoints(int route) {
        this.route = route;
        HashMap<String, LatLng> latlnglist = new LinkedHashMap<>();
        JSONObject mJsonObject;
        try {
            //select the route
            switch(route) {
                case 1: mJsonObject = new JSONObject(loadJSONFromAsset(route1Buffer));
                    break;
                case 2: mJsonObject = new JSONObject(loadJSONFromAsset(route2Buffer));
                    break;
                case 3: mJsonObject = new JSONObject(loadJSONFromAsset(route3Buffer));
                    break;
                case 4: mJsonObject = new JSONObject(loadJSONFromAsset(route4Buffer));
                    break;
                case 5: mJsonObject = new JSONObject(loadJSONFromAsset(route5Buffer));
                    break;
                default: mJsonObject = new JSONObject(loadJSONFromAsset(route1Buffer));
            }

            JSONArray mJsonArray = mJsonObject.getJSONArray("features");

            //get all the data from the json file (0 would be the polyline --> so start at 1)
            for(int i = 1; i < mJsonArray.length(); i++) {
                Log.i("JSONPARSER", "Value of i: " + i);
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                Log.i("JSONPARSER", "Value of mJsonArray: " + jsonInside);
                JSONObject current = jsonInside.getJSONObject("geometry");
                //decision points and confirmation points
                JSONArray coords = current.getJSONArray("coordinates");
                double latitude = coords.getDouble(1);
                double longitude = coords.getDouble(0);
                LatLng location = new LatLng(latitude, longitude);
                latlnglist.put(jsonInside.getString("name"), location);
                Log.i("JSONPARSER", "Location: " + location);
                Log.i("JSONPARSER", "Name: " + jsonInside.getString("name"));

            }
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return latlnglist;

    }

    /**
     * Return the next decision point from the current one.
     * @param current Decision Point, that is active at the moment
     * @return Next geofence on the route. Or null if no more geofences are available
     */
    public HashMap<String, LatLng> getNextDecisionpoint(String current) {
        HashMap<String, LatLng> nextDP = new HashMap<>();
        boolean next = false;
        for(Map.Entry<String, LatLng> entry : getDecisionpoints(route).entrySet()) {
            Log.i("Geofencing", "CURRENT: " + entry);
            if(next) {
                nextDP.put(entry.getKey(), entry.getValue());
                return nextDP;
            }
            if(entry.getKey().contentEquals(current)) {
                next = true;
                Log.i("Geofencing", "CURRENT: " + entry);

            }
        }
        return null;
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
     * Get a hashmap with all coordinates of the sparkasse in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getSparkasseCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("sparkasse");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "sparkasse" + i;
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
     * Get a hashmap with all coordinates of the post in the json
     * @return Hashmap with the values and the names
     */
    public HashMap<String, SKCoordinate> getPostCoordinates() {
        HashMap<String, SKCoordinate> latlnglist = new HashMap<>();
        try {
            JSONObject mJsonObject = new JSONObject(loadJSONFromAsset(mapBuffer));
            JSONArray mJsonArray = mJsonObject.getJSONArray("post");

            for(int i = 0; i < mJsonArray.length(); i++) {
                JSONObject jsonInside = mJsonArray.getJSONObject(i);
                if(jsonInside.has("lat") && jsonInside.has("lng")) {
                    Double latitudeValue = jsonInside.getDouble("lat");
                    Double longitudeValue = jsonInside.getDouble("lng");
                    String name = "post" + i;
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

