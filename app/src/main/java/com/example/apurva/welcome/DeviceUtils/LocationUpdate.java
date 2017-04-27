package com.example.apurva.welcome.DeviceUtils;

import android.content.Context;

import com.skobbler.ngx.positioner.SKCurrentPositionListener;
import com.skobbler.ngx.positioner.SKCurrentPositionProvider;
import com.skobbler.ngx.positioner.SKPosition;
import com.skobbler.ngx.positioner.SKPositionerManager;

/**
 * Created by apurv on 11-04-2017.
 * Calculates and updates the current location
 */
public class LocationUpdate implements SKCurrentPositionListener{

    public SKPosition currentPosition;
    public SKPositionerManager position;

    public LocationUpdate(Context context){
        SKCurrentPositionProvider currentPositionProvider = new SKCurrentPositionProvider(context);
        currentPositionProvider.setCurrentPositionListener(this);
        currentPositionProvider.requestLocationUpdates(Utils.hasGpsModule(context), Utils.hasNetworkModule(context), false);
        position = new SKPositionerManager();
        currentPosition = position.getCurrentGPSPosition(false);
    }
    @Override
    public void onCurrentPositionUpdate(SKPosition skPosition) {
        this.currentPosition = skPosition;
        SKPositionerManager.getInstance().reportNewGPSPosition(currentPosition);

    }
}
