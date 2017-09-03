package com.example.apurva.welcome.DecisionPoints;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.apurva.welcome.Geocoding.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apurv on 21-04-2017.
 */
public class Geofencing implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    protected ArrayList<Geofence> mGeofenceList;
    protected GoogleApiClient mGoogleApiClient;

    private JsonParser jsonParser;
    private Context mContext;
    private static final String TAG = "Geofencing";
    private String mode;
    private Intent mIntent;
    private boolean called = false;

    public Geofencing(Context context, int route, String mode) throws JSONException {
        jsonParser = new JsonParser(context);
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Get the geofences used.
        populateGeofenceList(route);

        //get the mode of the context
        this.mode = mode;

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient(context);
        mContext = context;
    }

    public Geofencing(Context context, int route, String mode, Intent aIntent) throws JSONException {
        jsonParser = new JsonParser(context);
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Get the geofences used.
        populateGeofenceList(route);

        //get the mode of the context
        this.mode = mode;

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient(context);
        mContext = context;
        mIntent = aIntent;
    }

    public void populateGeofenceList(int route) {
        //change back to getDPCoordiantes for testing why it crashed
        Log.i("Geofence", "Populated geofence list");
        for (Map.Entry<String, LatLng> entry : jsonParser.getDecisionpoints(route).entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    public void apiConnect(){
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    public void apiDisconnect(){
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void addGeofence() {
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent(mContext)
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    public void removeGeofence(List<String> ids) {
        if(mGoogleApiClient.isConnected()) {
            try {
                LocationServices.GeofencingApi.removeGeofences(
                        mGoogleApiClient,
                        ids
                ).setResultCallback(this); // Result processed in onResult().
            } catch (SecurityException securityException) {
                // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            }
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(Context context) {
        if (mIntent != null) {
            return PendingIntent.getService(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        intent.putExtra("mode", mode);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addGeofence()
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
      Toast.makeText(mContext, "Geofence Services Connected", Toast.LENGTH_SHORT).show();
        Log.i("Geofencing", "called: " + called);
        if(!called) {
            addGeofence();
            called = true;
        }
        Log.i("Geofencing", "Added geofences");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {

            Toast.makeText(mContext
                    ,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }
}
