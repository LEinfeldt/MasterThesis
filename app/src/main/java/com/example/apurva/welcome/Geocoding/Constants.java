package com.example.apurva.welcome.Geocoding;

/**
 * Created by apurv on 12-04-2017.
 * Constants class
 */
public final class Constants {
    public static final int SENSOR_INTERVAL = 500;
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 100;
    public static final String PACKAGE_NAME =
            "com.example.apurva.welcome.Geocoding";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_NAME_DATA_EXTRA";
    public static final String LOCATION_NAME_DATA_ORIGIN = PACKAGE_NAME +
            ".LOCATION_NAME_DATA_ORIGIN";
    public static final String RESULT_D_ADDRESS = PACKAGE_NAME + ".RESULT_D_ADDRESS";
    public static final String RESULT_O_ADDRESS = PACKAGE_NAME + ".RESULT_O_ADDRESS";
}