package com.example.apurva.welcome.DecisionPoints;

import com.skobbler.ngx.SKCoordinate;

/**
 * Created by lasse on 15.06.2017.
 */

public interface ServiceCallbacks {

    void logGeofence(String geofence, SKCoordinate pos);
    void setImagecounter(int number);
    int getImagecounter();
    void updateImage();
}
