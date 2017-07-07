package com.example.apurva.welcome.DecisionPoints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.apurva.welcome.Augmentations.MyGLSurfaceView;
import com.example.apurva.welcome.Augmentations.PointOfInterests;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.Geofence;
import com.skobbler.ngx.SKCoordinate;


/**
 * Created by lasse on 06.07.2017.
 */

public class GeofenceReceiver extends BroadcastReceiver {

    Context context;
    private String mode;
    private static final String TAG = "Geofencing";
    private Intent intent;
    private boolean called;

    Intent broadcastIntent = new Intent();

    private ServiceCallbacks serviceCallbacks;
    private final IBinder binder = new LocalBinder();

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public GeofenceReceiver getService() {
            // Return this instance of geoService so clients can call public methods
            return GeofenceReceiver.this;
        }
    }

    /**
     * Set the callback for the handled events to the corresponding activity
     * @param callbacks Activity to be called
     */
    public void setCallbacks(ServiceCallbacks callbacks) {
        if(called) return;
        this.serviceCallbacks = callbacks;
        called = true;
        Log.i("Setcallbacks", "Activity: " + this.serviceCallbacks);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Receiver", "Received something");
        this.context = context;
        this.mode = intent.getStringExtra("mode");

        broadcastIntent.addCategory("CATEGORY_LOCATION_SERVICES");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        if (event.hasError()) {
            return;
        } else {
            handleEnterExit(intent);
        }
    }

    private void handleEnterExit(Intent intent) {

        Log.i("Geofence", "event reiceived");
        this.intent = new Intent();

        this.intent = new Intent();
        this.intent.setClassName("com.example.apurva.welcome", "com.example.apurva.welcome.Activities.DialogActivity");
        this.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mode = intent.getStringExtra("mode");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            Log.e(TAG, "GeofencingEvent Error: " + event.getErrorCode());
            return;
        }

        int geofenceTransition = event.getGeofenceTransition();
        //if the user enters into a geofence
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Log.i("Broadcast", "Entered geofence: " + event.getTriggeringGeofences().get(0).getRequestId());
            //log the information of the geofence entered into the CSV file

            //if the event was triggered by the ar view
            if(mode.contentEquals("AR")) {
                Geofence triggeredGeofence = event.getTriggeringGeofences().get(0);
                PointOfInterests.presentGeofence = triggeredGeofence;
                MyGLSurfaceView.currentGeofence = triggeredGeofence;
                serviceCallbacks.logGeofence("Entered " + event.getTriggeringGeofences().get(0).getRequestId(),
                        new SKCoordinate(
                                event.getTriggeringLocation().getLatitude(),
                                event.getTriggeringLocation().getLongitude()
                        ));
                Toast.makeText(context, "Open dialogbox", Toast.LENGTH_SHORT).show();
                //start the dialog to ask for the camera
                context.startActivity(this.intent);
                return;
            }
            //else if the event was triggerd by the picture view
            else if(mode.contentEquals("picture")) {
                Geofence triggeredGeofence = event.getTriggeringGeofences().get(0);
                serviceCallbacks.logGeofence("Entered " + triggeredGeofence.getRequestId(),
                        new SKCoordinate(
                                event.getTriggeringLocation().getLatitude(),
                                event.getTriggeringLocation().getLongitude()
                        ));
                Toast.makeText(context, "Confirmationpoint reached", Toast.LENGTH_SHORT).show();
                //call the method to update the image in the picture view
                Log.i("Geofence", "In the picture if " + serviceCallbacks);
                serviceCallbacks.updateImage();
                return;
            }
        }
        //if the user exists a geofence
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(context, "Navigation Resumed", Toast.LENGTH_SHORT).show();
            //log the event leaving and the coordinates
            if(mode.contentEquals("picture")) {
                serviceCallbacks.logGeofence("Left "+ event.getTriggeringGeofences().get(0).getRequestId(), new SKCoordinate(
                        event.getTriggeringLocation().getLatitude(),
                        event.getTriggeringLocation().getLongitude()
                ));
            }
            else if(mode.contentEquals("AR")) {
                serviceCallbacks.logGeofence("Left "+ event.getTriggeringGeofences().get(0).getRequestId(), new SKCoordinate(
                        event.getTriggeringLocation().getLatitude(),
                        event.getTriggeringLocation().getLongitude()
                ));
            }
            return;
        }
    }
}
