package com.example.apurva.welcome.Augmentations;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.nfc.Tag;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.apurva.welcome.DecisionPoints.Geofencing;
import com.example.apurva.welcome.DecisionPoints.JsonParser;
import com.example.apurva.welcome.DeviceUtils.SensorUpdate;
import com.example.apurva.welcome.Geocoding.Constants;
import com.example.apurva.welcome.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apurv on 12-04-2017.
 */
public class PointOfInterests extends View implements SensorUpdate.AccelMagnoListener {
    private SensorUpdate sensorUpdate;
    private JsonParser jsonParser;
    private float[] accelValue = new float[3];
    private Handler handler;
    private boolean flag = false;
    //Variables to calculate the shift in POIs and rotation
    private float dx;
    private float dy;
    private float dr;
    //Shift between two POIS
    private float x;
    //size of the device screem
    private int w;
    private int h;
    //Paint to draw on Canvas
    private Paint contentPaint = new Paint();
    private final static String TAG = "PointOfInterests";
    //to draw the bitmap image in the augmented reality view
    private Resources res = getResources();
    //TODO: Create more bitmaps for more POIs
    private Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.lumaqq);
    private Bitmap marktkauf = BitmapFactory.decodeResource(res, R.drawable.marktkauf);
    private Bitmap apotheke  = BitmapFactory.decodeResource(res, R.drawable.apotheke);
    private AugmentCalculation augmentCalculation;
    //Triggered Geofence
    //TODO:Create one instance for the triggered geofence to be accessed by all classes
    public static volatile Geofence presentGeofence;

    public PointOfInterests(Context context) throws JSONException {
        super(context);
        sensorUpdate = new SensorUpdate(context);
        sensorUpdate.setListener(this);
        jsonParser = new JsonParser(context);
        handler = new Handler();
        augmentCalculation = new AugmentCalculation(context);
    }

    private final Runnable processSensors = new Runnable() {
        @Override
        public void run() {
            // Do work with the sensor values.
            flag = true;
            // The Runnable is posted to run again here:
            handler.postDelayed(this, Constants.SENSOR_INTERVAL);
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        sensorUpdate.register();
        handler.post(processSensors);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sensorUpdate.unregister();
        handler.removeCallbacks(processSensors);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "Drawing");
        canvas.rotate(dr);
        canvas.translate(0.0f, 0.0f-dy);
        canvas.translate(0.0f-dx, 0.0f);
        //to draw a point of interest use this method with the parameter of the defined image (bitmap)
        //canvas.drawBitmap(bitmap, w/2, h/4, contentPaint);
        //to draw another point of interest
        //TODO scale the icons of the places down to 32x32 pixels (the size of the lumaqq image) so they don't appear as that large
        //might be ok if they are a little bigger as well --> test that

        //check with switch case or if statements for the currently selected route and the decision point, that was triggered
        // --> like the followig
        if(MyGLSurfaceView.currentGeofence.getRequestId().equals("Decision1") && Geofencing.route == 1) {
            canvas.drawBitmap(marktkauf, w/2, h/2, contentPaint);
        }
        //insert cases for each augmentation logo that is supposed to be displayed
        else if(MyGLSurfaceView.currentGeofence.getRequestId().equals("Decision2") && Geofencing.route == 1) {
            canvas.drawBitmap(apotheke, w/2, h/2, contentPaint);
        }
        //.....
        //draw the selected bitmaps in the case, delete this line
        canvas.drawBitmap(marktkauf,(w/2) + x, h/2,contentPaint);
    }


    @Override
    public void onAccelSensorChanged(float[] accelValue) {
        if (flag) {
            this.accelValue = accelValue.clone();
        }


    }

    @Override
    public void onMagnoSensorChanged(float[] compassValue) {
        if (flag) {
            double difference = Math.toDegrees(augmentCalculation.deviceBearing(accelValue, compassValue)[0]) -
                    augmentCalculation.calculateBearing(getAugmentation(presentGeofence.getRequestId()));
            dx = (float) (w/AugmentCalculation.horizontalFOV() * difference);
            dy = (float) (h / AugmentCalculation.verticalFOV() * (Math.toDegrees(augmentCalculation.deviceBearing(accelValue, compassValue)[1])));
            dr = (float) (0.0f - Math.toDegrees(augmentCalculation.deviceBearing(accelValue, compassValue)[2]));
            flag = false;

            this.invalidate();
        }
    }


    /**
     * Get the coordinates of an augmentation that shall be displayed in the camera view
     * @param requestID ID of the decisionpoit on the route
     * @return Coordinates of the Location
     */
    private Location getAugmentation(String requestID) {
        Location augmentationLocation = new Location("manual");

        for(Map.Entry<String, LatLng> entry: jsonParser.getAugmentations(requestID).entrySet()) {
            augmentationLocation.setLatitude(entry.getValue().latitude);
            augmentationLocation.setLongitude(entry.getValue().longitude);
        }
        return augmentationLocation;
    }
}
