package com.example.apurva.welcome.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.google.android.gms.maps.model.LatLng;
import com.skobbler.ngx.SKCoordinate;

import org.json.JSONException;
//import de.unimuenster.ifgi.locormandemo.filter.Orientation;
//import de.unimuenster.ifgi.locormandemo.manipulations.ExperimentProvider;

/**
 * Created by sven on 02.08.16.
 * Modified to be used as a Singleton
 */
public class Logger {

    private String mLogfileName = "default";
    private boolean mAppendToLogfile;

    private String mFilePath = "";
    private File mFile;
    private CSVWriter mWriter;

    private String mExperimentID = "default";
    private int myRoute;
    private String[] fileHeaderCombined = {"ID","Experiment", "event","timestamp", "latitude", "longitude", "layerAction", "route"};

    private long mID = 0;

    //Coordiante to be logged
    private SKCoordinate mLastLocation;

    Context mContext;
    private static Logger instance = null;

    /**
     * Private Constructor to maintain the single instance of the logger
     * @param experimentID Name of the Experiment
     * @param context Context of the calling activity
     * @param route Route of the experiment
     */
    private Logger(String experimentID, Context context, int route) {
        try {
            setupLogging(experimentID, context, route);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        instance = this;
    }

    /**
     * Return the only instance of the logger
     * @param experimentID Name of the experiment
     * @param context Context of the calling Activity
     * @param route Route of the experiment
     * @return Only instance of the logger
     */
    public static Logger getInstance(String experimentID, Context context, int route) {
        if(instance !=  null) {
            return instance;
        }
        else return new Logger(experimentID, context, route);
    };

    /**
     * Return the instance of the logger that MUST already been initilized
     * @return Instance of the logger
     */
    public static Logger getInstance() {
        return instance;
    }

    public void setupLogging(String experimentID, Context context, int route) throws IOException {

        mContext = context;

        myRoute = route;
        // load logging settings from shared prefs
        loadLogfileSettingsFromSharedPrefs();

        mExperimentID = experimentID;
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath = baseDir + File.separator + mLogfileName;

        mFile = new File(mFilePath);

        // file is already there
        if (mFile.exists() && !mFile.isDirectory() && mAppendToLogfile) {
            mWriter = new CSVWriter(new FileWriter(mFilePath,true));
            // file needs to be created or I don't want appending of a file: create new file
        } else {
            mWriter = new CSVWriter(new FileWriter(mFilePath));
            mWriter.writeNext(fileHeaderCombined);
        }
    }

    private void loadLogfileSettingsFromSharedPrefs() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        mLogfileName = sharedPrefs.getString(GlobalConstants.SHARED_PREFS_LOGFILE_NAME_KEY,GlobalConstants.DEFAULT_LOGFILE_NAME);
        mAppendToLogfile = sharedPrefs.getBoolean(GlobalConstants.SHARED_PREFS_LOGFILE_APPEND_KEY, true);
    }

    public void logLocation(SKCoordinate location) {
        mLastLocation = location;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String idString = ""+mID;
        String event = "location update";
        String logTimestamp = dateFormat.format(System.currentTimeMillis());
        String originalLat = ""+mLastLocation.getLatitude();
        String originalLon = ""+mLastLocation.getLongitude();
        String activeLayer = "-";
        String routeAction = "No." + myRoute;


        String[] data = {idString, mExperimentID, event, logTimestamp, originalLat, originalLon, activeLayer, routeAction};

        mWriter.writeNext(data);

        mID += 1;
    }

    /**
     * Log a change in layer selection
     */
    public void logLayerSelection(String layerAction) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String idString = "" + mID;
        String event = "Layers changed";
        String logTimeStamp = dateFormat.format(System.currentTimeMillis());
        String lat = "-";
        String lng = "-";
        String activeLayer = layerAction;
        String routeAction = "No. " + myRoute;

        String[] data = {idString, mExperimentID, event, logTimeStamp, lat, lng, activeLayer, routeAction};

        mWriter.writeNext(data);
        mID++;
    }

    /**
     * Add the selected route to the logger
     */
    public void logRouteInformation() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String idString = "" + mID;
        String event = "Selected Route";
        String logTimeStamp = dateFormat.format(System.currentTimeMillis());
        String lat = "-";
        String lng = "-";
        String activeLayer = "-";
        String routeAction = "No. " + myRoute;

        String[] data = {idString, mExperimentID, event, logTimeStamp, lat, lng, activeLayer, routeAction};

        mWriter.writeNext(data);
        mID++;
    }

    public void logGeofence(String event, SKCoordinate pos) {

        mLastLocation = pos;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.i("Logger", "Geofence log: " + event);

        String idString = "" + mID;
        String geofence = event;
        String logTimeStamp = dateFormat.format(System.currentTimeMillis());
        String lat = "" + mLastLocation.getLatitude();
        String lng = "" + mLastLocation.getLongitude();
        String activeLayer = "-";
        String routeAction = "No. "+ myRoute;

        String[] data = {idString, mExperimentID, geofence, logTimeStamp, lat, lng, activeLayer, routeAction};

        mWriter.writeNext(data);
        mID++;
    }

    public void logDialog(String decision) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String idString = "" + mID;
        String geofence = "Dialog decision " + decision;
        String logTimeStamp = dateFormat.format(System.currentTimeMillis());
        String lat = "" + mLastLocation.getLatitude();
        String lng = "" + mLastLocation.getLongitude();
        String activeLayer = "-";
        String routeAction = "No. "+ myRoute;

        String[] data = {idString, mExperimentID, geofence, logTimeStamp, lat, lng, activeLayer, routeAction};

        mWriter.writeNext(data);
        mID++;
    }


    public void stopLoggingAndWriteFile() throws IOException {

        //mWriter.flush();
        mWriter.close();

        // trigger indexing of files (needed for some devices to appear on the sdcard)
        Uri contentUri = Uri.fromFile(mFile);
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"); mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);

    }
}
