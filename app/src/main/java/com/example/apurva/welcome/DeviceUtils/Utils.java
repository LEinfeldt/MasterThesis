package com.example.apurva.welcome.DeviceUtils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by apurv on 12-04-2017.
 */
public class Utils {

    /**
     * Checks if the current device has a GPS module (hardware)
     *
     * @return true if the current device has GPS
     */
    public static boolean hasGpsModule(final Context context) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationManager.getAllProviders()) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current device has a  NETWORK module (hardware)
     *
     * @return true if the current device has NETWORK
     */
    public static boolean hasNetworkModule(final Context context) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationManager.getAllProviders()) {
            if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                return true;
            }
        }
        return false;
    }

}
