package com.example.apurva.welcome.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apurva.welcome.DecisionPoints.JsonParser;
import com.example.apurva.welcome.DecisionPoints.LayerInteraction;
import com.example.apurva.welcome.DeviceUtils.LocationUpdate;
import com.example.apurva.welcome.DeviceUtils.SensorUpdate;
import com.example.apurva.welcome.Geocoding.Constants;
import com.example.apurva.welcome.Logger.Logger;
import com.example.apurva.welcome.R;
import com.google.android.gms.maps.model.LatLng;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.SKAnimationSettings;
import com.skobbler.ngx.map.SKAnnotation;
import com.skobbler.ngx.map.SKAnnotationView;
import com.skobbler.ngx.map.SKCircle;
import com.skobbler.ngx.map.SKCoordinateRegion;
import com.skobbler.ngx.map.SKMapCustomPOI;
import com.skobbler.ngx.map.SKMapPOI;
import com.skobbler.ngx.map.SKMapSettings;
import com.skobbler.ngx.map.SKMapSurfaceListener;
import com.skobbler.ngx.map.SKMapSurfaceView;
import com.skobbler.ngx.map.SKMapViewHolder;
import com.skobbler.ngx.map.SKPOICluster;
import com.skobbler.ngx.map.SKScreenPoint;
import com.skobbler.ngx.navigation.SKNavigationListener;
import com.skobbler.ngx.navigation.SKNavigationManager;
import com.skobbler.ngx.navigation.SKNavigationSettings;
import com.skobbler.ngx.navigation.SKNavigationState;
import com.skobbler.ngx.positioner.SKPositionerManager;
import com.skobbler.ngx.routing.SKRouteInfo;
import com.skobbler.ngx.routing.SKRouteJsonAnswer;
import com.skobbler.ngx.routing.SKRouteListener;
import com.skobbler.ngx.routing.SKRouteManager;
import com.skobbler.ngx.routing.SKRouteSettings;

import org.json.*;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lasse on 27.04.2017.
 */

public class MapActivity extends AppCompatActivity implements SKMapSurfaceListener, SensorUpdate.AccelMagnoListener {

    //logger
    private Logger logger;
    //Timer for regular logs
    private Timer timer;
    //Holder to hold the mapView.
    private SKMapViewHolder mapHolder;
    //PositionMe Button
    private ImageButton locateButtonMap;
    //resulted coordinates for the origin and destination points
    private SKCoordinate originPoint;
    private SKCoordinate destinationPoint;
    //Tag for printing Logs
    private static final String TAG = "MapActivity";
    //to determine if navigation is in process.
    private boolean navigationInProgress = false;
    //Map View
    private SKMapSurfaceView mapView;
    private LayerInteraction layerInteraction;

    //drawer item
    private DrawerLayout mDrawerLayout;
    //list of items in the drawer
    private ListView mDrawerList;
    //String array with the names of the layers
    private String[] layerNames;
    //for showing the compass on the map view
    private boolean headingOn;
    //objects for different classes
    private LocationUpdate mLocation;
    private SensorUpdate sensorUpdate;
    private JsonParser jsonParser;
    //sensor value of accelerometer
    private float[] accelValue = new float[3];
    //initial compass value
    /**
     * defines how smooth the compass movement will be (1 is no smoothing and 0 is never
     * updating).
     */
    private static final float SMOOTH_FACTOR_COMPASS = 0.1f;
    //to handle app exit events
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_map);
        //Initializing Objects
        mapHolder = (SKMapViewHolder) findViewById(R.id.view_group_map);
        mapHolder.setMapSurfaceListener(this);
        //drawCircle();//TODO: this method can be used to draw the  upcoming geofence circle
        mLocation = new LocationUpdate(this);
        //Initialize the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Initialize the drawer list
        mDrawerList = (ListView) findViewById(R.id.left_drawer_map);
        //get the Strings from resources
        layerNames = getResources().getStringArray(R.array.layerNames);
        //set an adapter for the drawer list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.txtTitle, layerNames));

        layerInteraction = new LayerInteraction(this);

        sensorUpdate = new SensorUpdate(this);
        //registering the sensor update listener
        sensorUpdate.setListener(this);
        try {
            jsonParser = new JsonParser(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //initialize the logger
        this.logger = new Logger();
        try {
            logger.setupLogging("Map", this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        // set a listener to the list
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //toggle the visibility of the selected layer
                layerInteraction.toggleDataLayer(position, mapView, logger);
            }
        });

        //setup the regular logging
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //log the current position
                logger.logLocation(mLocation.currentPosition.getCoordinate());
            }
        }, 0, 1000);

        //Position Me Button
        locateButtonMap = (ImageButton) findViewById(R.id.locateButtonMap);
        locateButtonMap.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {if(!headingOn){
                                                 //display compass
                                                 setHeading(true);
                                             }
                                                 if (mapView != null && mLocation.currentPosition != null) {
                                                     //centers the map on the current position
                                                     mapView.centerOnCurrentPosition(17, true, 500);
                                                 } else {
                                                     Toast.makeText(getApplicationContext(),
                                                             getResources().getString(R.string.no_position_available), Toast.LENGTH_SHORT)
                                                             .show();
                                                 }
                                             }
                                         }
        );
    }

    private void setHeading(boolean enabled) {
        //enable compass
        if (enabled) {
            headingOn = true;
            //means the compass shows the direction by rotating
            mapView.getMapSettings().setHeadingMode(SKMapSettings.SKHeadingMode.ROTATING_HEADING);
            //register the sensorupdate listener
            sensorUpdate.register();
        } else {
            headingOn = false;
            mapView.getMapSettings().setHeadingMode(SKMapSettings.SKHeadingMode.NONE);
            //unregister the sensorupdate listener
            sensorUpdate.unregister();
        }
    }


    @Override
    public void onBackPressed() {
        //double click to exit the application
        if (exit) {
            try {
                logger.stopLoggingAndWriteFile();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapHolder.onResume();
        if (headingOn) {
            sensorUpdate.register();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapHolder.onPause();
        if(headingOn){
            sensorUpdate.unregister();
        }
    }

    @Override
    public void onActionPan() {

    }

    @Override
    public void onActionZoom() {

    }

    @Override
    public void onSurfaceCreated(SKMapViewHolder skMapViewHolder) {
        //inflate the mapView when the surface is created
        mapView = mapHolder.getMapSurfaceView();
        //apply different settings like panning, zooming, compass shown and scale shown to the map
        applysettings();
        if (mLocation.currentPosition != null) {
            //update the current location
            SKPositionerManager.getInstance().reportNewGPSPosition(mLocation.currentPosition);
            if (mapView != null) {
                //centres the map
                mapView.centerOnCurrentPosition(17, true, 500);
            }
        }
        // drawCircle();
        //enable the compass
        setHeading(true);
        layerInteraction.addDataToMap(mapView);
    }

    private void applysettings() {
        //apply different settings like panning, zooming, compass shown and scale shown to the map
        mapView.getMapSettings().setMapRotationEnabled(true);
        mapView.getMapSettings().setMapZoomingEnabled(true);
        mapView.getMapSettings().setMapPanningEnabled(true);
        mapView.getMapSettings().setZoomWithAnchorEnabled(true);
        mapView.getMapSettings().setInertiaRotatingEnabled(true);
        mapView.getMapSettings().setInertiaZoomingEnabled(true);
        mapView.getMapSettings().setInertiaPanningEnabled(true);
        mapView.getMapSettings().setCompassPosition(new SKScreenPoint(10, 50));
        mapView.getMapSettings().setCompassShown(true);
        mapHolder.setScaleViewEnabled(true);
        mapHolder.setScaleViewPosition(-10, 200);
    }

    @Override
    public void onMapRegionChanged(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeStarted(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeEnded(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onDoubleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onSingleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onRotateMap() {

    }

    @Override
    public void onLongPress(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onInternetConnectionNeeded() {

    }

    @Override
    public void onMapActionDown(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onMapActionUp(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onPOIClusterSelected(SKPOICluster skpoiCluster) {

    }

    @Override
    public void onMapPOISelected(SKMapPOI skMapPOI) {

    }

    @Override
    public void onAnnotationSelected(SKAnnotation skAnnotation) {

    }

    @Override
    public void onCustomPOISelected(SKMapCustomPOI skMapCustomPOI) {

    }

    @Override
    public void onCompassSelected() {

    }

    @Override
    public void onCurrentPositionSelected() {

    }

    @Override
    public void onObjectSelected(int i) {

    }

    @Override
    public void onInternationalisationCalled(int i) {

    }

    @Override
    public void onBoundingBoxImageRendered(int i) {

    }

    @Override
    public void onGLInitializationError(String s) {

    }

    @Override
    public void onScreenshotReady(Bitmap bitmap) {

    }

    @Override
    public void onAccelSensorChanged(float[] accelValue) {
        //gets the updated accelerometer values
        this.accelValue = accelValue.clone();
    }

    @Override
    public void onMagnoSensorChanged(float[] compassValue) {
        //gets the updated compass values
        float rotation[] = new float[16];
        float identity[] = new float[16];
        float orientation[] = new float[3];
        double orientInDegrees;
        if (accelValue != null && compassValue != null) {
            //calculates the orientation matrix for defining the orientation for the compass
            boolean gotRotation = SensorManager.getRotationMatrix(rotation, identity, accelValue, compassValue);

            if (gotRotation) {
                SensorManager.getOrientation(rotation, orientation);
            }
        }

        //orientInDegrees = applySmoothAlgorithm((float)(Math.toDegrees(orientation[0])));
        orientInDegrees = Math.toDegrees(orientation[0]);

        if (orientInDegrees < 0) {
            mapView.reportNewHeading(360+orientInDegrees);
        }
        if (orientInDegrees > 0) {
            mapView.reportNewHeading(orientInDegrees);
        }

    }
}
