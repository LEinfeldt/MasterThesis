package com.example.apurva.welcome.DecisionPoints;

import com.skobbler.ngx.SKCoordinate;

import java.util.List;

/**
 * Created by lasse on 15.06.2017.
 */

public interface ServiceCallbacks {

    void deleteGeofence(List ids);
    void setImagecounter(int number);
    int getImagecounter();
    void updateImage();
}
