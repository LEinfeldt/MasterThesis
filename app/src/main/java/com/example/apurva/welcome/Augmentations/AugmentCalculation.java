package com.example.apurva.welcome.Augmentations;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;

import com.example.apurva.welcome.Activities.CameraActivity;
import com.example.apurva.welcome.DeviceUtils.LocationUpdate;
import com.skobbler.ngx.SKCoordinate;

/**
 * Created by apurv on 12-04-2017.
 * This is to calculate the bearing, horizontal and vertical field of view of the camera and device's orientation
 */
public class AugmentCalculation {

    private final static String TAG = "AugmentCalculation";
    private LocationUpdate locationUpdate;

    public AugmentCalculation(Context context){
        locationUpdate = new LocationUpdate(context);
    }

    public float[] deviceBearing(float[] accelValue, float[] compassValue){
        //calculates the orientation of the camera (azimuth, roll and pitch values)
        float rotation[] = new float[16];
        float identity[] = new float[16];
        float orientation[] = new float[3];

        if(accelValue!=null && compassValue!= null) {
            boolean gotRotation = SensorManager.getRotationMatrix(rotation, identity, accelValue, compassValue);

            if (gotRotation) {
                float cameraRotation[] = new float[16];
                // remap such that the camera is pointing straight down the Y axis
                SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, cameraRotation);
                // remap such that the camera is pointing along the positive direction of the Y axis
                SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, cameraRotation);
                SensorManager.getOrientation(cameraRotation, orientation);
            }
        }
        return orientation;

    }

    public static double horizontalFOV(){
        //calculates the horizontal Field of View of the Camera
        double hAngle;
        hAngle = Math.toDegrees(2 * (Math.atan(CameraActivity.cameraWidth / (2 * CameraActivity.focalLength ))));
        return hAngle;
    }

    public static double verticalFOV(){
        //calculates the vertical Field of View of the Camera
        double vAngle;
        vAngle = Math.toDegrees(2 * (Math.atan(CameraActivity.cameraHeight / (2 * CameraActivity.focalLength ))));
        return vAngle;
    }

    public double calculateBearing(Location location){
        //calculates the bearing of the POI of the targeted route
        double bearing;
        Location myLocation = new Location("manual");
        SKCoordinate tLocation = locationUpdate.currentPosition.getCoordinate();
        myLocation.setLatitude(tLocation.getLatitude());
        myLocation.setLongitude(tLocation.getLongitude());
        bearing = myLocation.bearingTo(location);
        return bearing;
    }
}
