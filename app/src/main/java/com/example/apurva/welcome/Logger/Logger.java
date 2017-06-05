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

import com.skobbler.ngx.SKCoordinate;
//import de.unimuenster.ifgi.locormandemo.filter.Orientation;
//import de.unimuenster.ifgi.locormandemo.manipulations.ExperimentProvider;

/**
 * Created by sven on 02.08.16.
 */
public class Logger {

    private String mLogfileName = "default";
    private boolean mAppendToLogfile;

    private String mFilePath = "";
    private File mFile;
    private CSVWriter mWriter;

    private String mExperimentID = "default";
    private String[] fileHeaderCombined = {"ID","Experiment", "event","log_timestamp", "original_lat", "original_lon", "layers"};

    private long mID = 0;

    //Coordiante to be logged
    private SKCoordinate mLastLocation;

    Context mContext;

    public void setupLogging(String experimentID, Context context) throws IOException {

        mContext = context;

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
        Log.i("THIS THE", "NEW WriTEr:" + mWriter);
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


        String[] data = {idString, mExperimentID, event, logTimestamp, originalLat, originalLon};

        mWriter.writeNext(data);

        mID += 1;
    }

    /**
     * Log a change in layer selection
     */
    public void logLayerSelection(String layerAction) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM-dd HH:mm:ss");

        String idString = "" + mID;
        String event = "Layers changed";
        String logTimeStamp = dateFormat.format(System.currentTimeMillis());
        String lat = "";
        String lng = "";
        String activeLayer = layerAction;

        String[] data = {idString, mExperimentID, event, logTimeStamp, lat, lng, activeLayer};

        mWriter.writeNext(data);
        mID++;
    }


    //TODO: Write method to log geofence event (enter/leaving)

    public void stopLoggingAndWriteFile() throws IOException {

        //mWriter.flush();
        mWriter.close();

        // trigger indexing of files (needed for some devices to appear on the sdcard)
        Uri contentUri = Uri.fromFile(mFile);
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"); mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);

    }
}
