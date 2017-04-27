package com.example.apurva.welcome.DeviceUtils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by apurv on 12-04-2017.
 * Created listener class to separately deal with the events of accelerometer and compass values
 */
public class SensorUpdate  implements SensorEventListener{
    private float[] lastCompass = new float[3];
    private float[] lastAccel = new float[3];
    private static final float ALPHA = 0.25f;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor compass;
    private AccelMagnoListener listener;
    private final static String TAG = "SensorUpdate";

    public interface AccelMagnoListener{
        void onAccelSensorChanged(float[] accelValue);
        void onMagnoSensorChanged(float[] compassValue);
    }

    public SensorUpdate(Context context){
        mSensorManager =  (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        compass = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void setListener(AccelMagnoListener listener){
        this.listener = listener;
    }

    public void register() {
        Log.i(TAG, "sensor registered");
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (listener != null) {

            switch (sensorEvent.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    lastAccel = lowPass(sensorEvent.values.clone(), lastAccel);
                    listener.onAccelSensorChanged(lastAccel);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    lastCompass = lowPass(sensorEvent.values.clone(), lastCompass);
                    listener.onMagnoSensorChanged(lastCompass);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected float[] lowPass( float[] input, float[] output ) {
        //lowPass filter to cut down erroneous data
        if ( output == null )
        {return input;}
        for ( int i=0; i<input.length; i++ )
        { output[i] = output[i] + ALPHA * (input[i] - output[i]); }
        return output;
    }

}
