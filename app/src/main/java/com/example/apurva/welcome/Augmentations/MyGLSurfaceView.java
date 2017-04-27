package com.example.apurva.welcome.Augmentations;

import android.content.Context;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.os.Handler;

import com.example.apurva.welcome.DecisionPoints.JsonParser;
import com.example.apurva.welcome.DeviceUtils.SensorUpdate;
import com.example.apurva.welcome.Geocoding.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by apurv on 12-04-2017.
 */
public class MyGLSurfaceView extends GLSurfaceView implements SensorUpdate.AccelMagnoListener {

    //objects for other classes
    private SensorUpdate sensorUpdate;
    private AugmentCalculation aCal;
    private JsonParser jsonParser;
    //values of the accelerometer
    private float[] accelValue = new float[3];
    //handler to delay the sensor input
    private Handler handler;
    //flag for handling delayed sensor events
    private boolean flag = false;
    private static final String TAG = "GLSurfaceView";
    //Triggered geofence
    public static volatile Geofence currentGeofence;

    public MyGLSurfaceView(Context context) throws JSONException {
        super(context);
        //Version 2: since OpenGL ES 2.0 is used
        setEGLContextClientVersion(2);

        sensorUpdate = new SensorUpdate(context);
        sensorUpdate.setListener(this);

        aCal = new AugmentCalculation(context);

        jsonParser = new JsonParser(context);

        handler = new Handler();
    }

    private final Runnable processSensors = new Runnable() {
        @Override
        public void run() {
            // work with the sensor values.
            flag = true;
            // The Runnable is posted to run again here:
            handler.postDelayed(this, Constants.SENSOR_INTERVAL);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        sensorUpdate.register();
        handler.post(processSensors);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorUpdate.unregister();
        handler.removeCallbacks(processSensors);
    }

    @Override
    public void onAccelSensorChanged(float[] accelValue) {
        if (flag) {
            this.accelValue = accelValue.clone();
        }

    }

    @Override
    public void onMagnoSensorChanged(float[] compassValue) {
        //calculates the angle by which the augmentation must be rotated according to the bearing of the direction
        if (flag) {
            MyGLRenderer.dx = (float)(Math.toDegrees(aCal.deviceBearing(accelValue, compassValue)[0]) -
                    aCal.calculateBearing(testLocation));//TODO: Replace testLocation by getDirCoordinate().
            flag = false;
            //onDraw in Renderer is called when this is called
            requestRender();
        }

    }

    private Location getDirCoordinate(){
        Location dirLocation = new Location("manual");

        for (Map.Entry<String, LatLng> entry : jsonParser.getDirectionCoordinates().entrySet()) {
            if(entry.getKey().equals(currentGeofence.getRequestId())){
                dirLocation.setLatitude(entry.getValue().latitude);
                dirLocation.setLongitude(entry.getValue().longitude);

            }
        }

        return dirLocation;
    }

    private final static Location testLocation = new Location("manual");
    static {
        testLocation.setLatitude(23.176216);
        testLocation.setLongitude(77.477498);
    }
}