package com.example.apurva.welcome.DecisionPoints;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.apurva.welcome.Activities.DialogActivity;
import com.example.apurva.welcome.Augmentations.MyGLSurfaceView;
import com.example.apurva.welcome.Augmentations.PointOfInterests;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by apurv on 22-04-2017
 * This is the intent service where you would define what to do if
 * a person enters a Geofence (i.e the decision point)
 * A dialog box will pop up to seek permission from the user for
 * opening the camera for the AR View
 */
public class GeofenceTransitionsIntentService extends IntentService {
    protected static final String TAG = "GeofenceTransitionsIS";
    //Create intent to get to DialogActivity which opens up a dialog.
    private Intent intent;

    public GeofenceTransitionsIntentService() {
        super(TAG);  // use TAG to name the IntentService worker thread
        intent = new Intent(getApplicationContext(), DialogActivity.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            Log.e(TAG, "GeofencingEvent Error: " + event.getErrorCode());
            return;
        }

        int geofenceTransition = event.getGeofenceTransition();
        //if the user enters into a geofence
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Geofence triggeredGeofence = event.getTriggeringGeofences().get(0);
            PointOfInterests.presentGeofence = triggeredGeofence;
            MyGLSurfaceView.currentGeofence = triggeredGeofence;
            Toast.makeText(this, "Open dialogbox", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        //if the user exists a geofence
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(this, "Navigation Resumed", Toast.LENGTH_SHORT).show();
        }
    }



}