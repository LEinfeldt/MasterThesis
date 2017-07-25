package com.example.apurva.welcome.DecisionPoints;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.apurva.welcome.Activities.DialogActivity;
import com.example.apurva.welcome.Augmentations.MyGLSurfaceView;
import com.example.apurva.welcome.Augmentations.PointOfInterests;
import com.example.apurva.welcome.Logger.Logger;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.skobbler.ngx.SKCoordinate;

import java.io.IOException;
import java.util.ArrayList;

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
    private String mode;
    private boolean called = false;

    private Logger logger;
    private ServiceCallbacks serviceCallbacks;
    private final IBinder binder = new LocalBinder();

    public GeofenceTransitionsIntentService() {

        super(TAG);  // use TAG to name the IntentService worker thread
        logger = Logger.getInstance();
    }

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public GeofenceTransitionsIntentService getService() {
            // Return this instance of geoService so clients can call public methods
            return GeofenceTransitionsIntentService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * Set the callback for the handled events to the corresponding activity
     * @param callbacks Activity to be called
     */
    public void setCallbacks(ServiceCallbacks callbacks) {
        if(called) return;
        serviceCallbacks = callbacks;
        called = true;
        Log.i("Setcallbacks", "Updated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("Geofence", "event reiceived");

        this.intent = new Intent(this, DialogActivity.class);
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
            Log.i("Geoservice", "Enter");
            String id = event.getTriggeringGeofences().get(0).getRequestId();
            //if a regular geofence was entered log the entering event
            if(!id.contentEquals("End")) {
                logger.logGeofence("Entered " + id,
                        new SKCoordinate(
                                event.getTriggeringLocation().getLatitude(),
                                event.getTriggeringLocation().getLongitude()
                        ));
                Log.i("Geofence", "Entered geofence: " + id);
            }

            /*if the entered geofence is the last one on the route (marked with the name "End")
            * the geofence will trigger the end of the experiment and stop logging data.
            */
            else if(id.contentEquals("End")) {
                logger.logGeofence("Reached " + id,
                        new SKCoordinate(
                                event.getTriggeringLocation().getLatitude(),
                                event.getTriggeringLocation().getLongitude()
                        ));
                try {
                    logger.stopLoggingAndWriteFile();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                //delete the geofence from the list, to prevent further triggering
                Geofence triggeredGeofence = event.getTriggeringGeofences().get(0);
                ArrayList<String> list = new ArrayList<>();
                list.add(triggeredGeofence.getRequestId());
                Log.i("Geofence", "DeleteGeofence");
                serviceCallbacks.deleteGeofence(list);
                return;
            }

            //if the event was triggered by the ar view
            if(mode.contentEquals("AR")) {
                Geofence triggeredGeofence = event.getTriggeringGeofences().get(0);
                PointOfInterests.presentGeofence = triggeredGeofence;
                MyGLSurfaceView.currentGeofence = triggeredGeofence;
                Toast.makeText(this, "Open dialogbox", Toast.LENGTH_SHORT).show();
                ArrayList<String> list = new ArrayList<>();
                list.add(triggeredGeofence.getRequestId());
                Log.i("Geofence", "DeleteGeofence");
                serviceCallbacks.deleteGeofence(list);
                //start the dialog to ask for the camera
                startActivity(this.intent);
                return;
            }
            //else if the event was triggerd by the picture view
            else if(mode.contentEquals("picture")) {
                Geofence triggeredGeofence = event.getTriggeringGeofences().get(0);
                Toast.makeText(this, "Confirmationpoint reached", Toast.LENGTH_SHORT).show();
                //call the method to update the image in the picture view
                Log.i("Geofence", "In the picture if " + serviceCallbacks);
                if (serviceCallbacks != null) {
                    Log.i("Called", "The callback");
                    ArrayList<String> list = new ArrayList<>();
                    list.add(triggeredGeofence.getRequestId());
                    serviceCallbacks.deleteGeofence(list);
                    serviceCallbacks.updateImage();
                }
                return;
            }
        }
        //if the user exists a geofence
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(this, "Navigation Resumed", Toast.LENGTH_SHORT).show();
            //log the event leaving and the coordinates
            logger.logGeofence("Left "+ event.getTriggeringGeofences().get(0).getRequestId(), new SKCoordinate(
                    event.getTriggeringLocation().getLatitude(),
                    event.getTriggeringLocation().getLongitude()
            ));
            return;
        }
    }
}