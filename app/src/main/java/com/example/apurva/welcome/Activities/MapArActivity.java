package com.example.apurva.welcome.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.apurva.welcome.DecisionPoints.Geofencing;
import com.example.apurva.welcome.DecisionPoints.JsonParser;
import com.example.apurva.welcome.DeviceUtils.LocationUpdate;
import com.example.apurva.welcome.DeviceUtils.SensorUpdate;
import com.example.apurva.welcome.Geocoding.Constants;
import com.example.apurva.welcome.Geocoding.FetchLocationIntentService;
import com.example.apurva.welcome.R;
import com.google.android.gms.maps.model.LatLng;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.SKAnimationSettings;
import com.skobbler.ngx.map.SKAnnotation;
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

import org.json.JSONException;

import java.util.Map;


/*
This activity is responsible for the search of destination, route calculation and navigation. This also starts the geofencing services
if AR View is enabled
 */
public class MapArActivity extends AppCompatActivity implements SKMapSurfaceListener, SKRouteListener, SKNavigationListener, SensorUpdate.AccelMagnoListener {

    //Holder to hold the mapView.
    private SKMapViewHolder mapHolder;
    //PositionMe Button
    private ImageButton locateButtonAR;
    //receiver to receive the output of Geocoding
    private AddressResultReceiver mResultReceiver;
    //Edit Text widgets to edit the origin and destination addresses
    private EditText originAddress;
    private EditText destinationAddress;
    //resulted coordinates for the origin and destination points
    private SKCoordinate originPoint;
    private SKCoordinate destinationPoint;
    //Tag for printing Logs
    private static final String TAG = "MapArActivity";
    //Progress bar during the time of addresses search
    private ProgressBar progressBarAR;
    //Button for calculateroute, start navigation and stop navigation
    private Button startButton;
    //to determine if navigation is in process.
    private boolean navigationInProgress = false;
    //Switch between MapView and Map+AR View
    private Switch aSwitch;
    //Map View
    private SKMapSurfaceView mapView;
    //for showing the compass on the map view
    private boolean headingOn;
    //objects for different classes
    private LocationUpdate mLocation;
    private Geofencing geofencing;
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
        setContentView(R.layout.activity_mapar);
        //Initializing Objects
        mapHolder = (SKMapViewHolder) findViewById(R.id.view_group_mapAR);
        mapHolder.setMapSurfaceListener(this);
        //drawCircle();//TODO: this method can be used to draw the  upcoming geofence circle
        mResultReceiver = new AddressResultReceiver(null);
        progressBarAR = (ProgressBar) findViewById(R.id.progressBarAR);
        destinationAddress = (EditText) findViewById(R.id.destinationAR);
        originAddress = (EditText) findViewById(R.id.originAR);
        startButton = (Button) findViewById(R.id.buttonAR);
        mLocation = new LocationUpdate(this);
        sensorUpdate = new SensorUpdate(this);
        //registering the sensor update listener
        sensorUpdate.setListener(this);
        try {
            jsonParser = new JsonParser(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            geofencing = new Geofencing(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Map/Map+AR View Switch
        aSwitch = (Switch) findViewById(R.id.switchAR);
        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //if the switch is checked geofences would be added
                    geofencing.addGeofence();
                    //TODO: This is for testing, remove this two lines in the final run
                    Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                    startActivity(intent);
                }
                else{
                    geofencing.removeGeofence();
                }

            }
        });

        //Position Me Button
        locateButtonAR = (ImageButton) findViewById(R.id.locateButtonAR);
        locateButtonAR.setOnClickListener(new View.OnClickListener() {
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

    public void onButtonClicked(View view) {
        //If the central button is clicked while the text reads calculate route
        if(startButton.getText().equals(getResources().getString(R.string.calculate_route))) {

            //Opens the Geocoding Intent Service
            Intent intent = new Intent(this, FetchLocationIntentService.class);
            //put the value of the result receiver from the constants class
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            //put the name of the destination
            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, destinationAddress.getText().toString());
            //puts the name of the origin point
            intent.putExtra(Constants.LOCATION_NAME_DATA_ORIGIN, originAddress.getText().toString());
            //progress bar to show the backprocess
            progressBarAR.setVisibility(View.VISIBLE);
            //starting the intent service
            startService(intent);
        }
        else if(startButton.getText().equals(getResources().getString(R.string.start_navigation))){
            //if the button reads start navigation: start navigation
            startButton.setText(R.string.stop_navigation);
            launchNavigation();
        }

        else{
            //when the button reads stop navigation
            startButton.setText(R.string.calculate_route);
            //clear the cached route and annotations and stop navigation.
            clearMap();
            clearRouteFromCache();
            mapView.deleteAllAnnotationsAndCustomPOIs();
            stopNavigation();
        }

    }

    private void launchNavigation() {

        SKNavigationSettings navigationSettings = new SKNavigationSettings();
        // set the desired navigation settings
        navigationSettings.setNavigationType(SKNavigationSettings.SKNavigationType.REAL);
        navigationSettings.setPositionerVerticalAlignment(-0.25f);
        navigationSettings.setShowRealGPSPositions(true);
        // get the navigation manager object
        SKNavigationManager navigationManager = SKNavigationManager.getInstance();
        navigationManager.setMapView(mapView);
        //enable panning while navigating
        mapView.getMapSettings().setMapPanningEnabled(true);
        // set listener for navigation events
        navigationManager.setNavigationListener(this);
        // start navigating using the settings
        navigationManager.startNavigation(navigationSettings);
        //set navigation in process = true;
        navigationInProgress = true;

    }

    private void clearMap(){
        //remove the displayed calculted route
        SKRouteManager.getInstance().clearCurrentRoute();
        if(navigationInProgress){
            stopNavigation();
        }

    }

    private void stopNavigation(){
        navigationInProgress = false;
        SKNavigationManager.getInstance().stopNavigation();
    }

    private void drawCircle(){
        //TODO: to use it to draw the geofence.
        if(mapView!=null) {
            for (Map.Entry<String, LatLng> entry : jsonParser.getDPCoordinates().entrySet()) {
                Log.i(TAG, "Lat " + entry.getValue().latitude + "long " + entry.getValue().longitude);
                SKCircle fence = new SKCircle();
                fence.setCircleCenter(new SKCoordinate(entry.getValue().latitude, entry.getValue().longitude));
                fence.setRadius(Constants.GEOFENCE_RADIUS_IN_METERS);
                mapView.addCircle(fence);
            }
        }
    }


    @Override
    public void onBackPressed() {
        //double click to exit the application
        if (exit) {
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
        geofencing.apiConnect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        geofencing.apiDisconnect();
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


    private void calculateRoute(SKCoordinate startPoint, SKCoordinate destinationPoint){
        clearRouteFromCache();
        //set a destination marking object, here it is a flag
        SKAnnotation annotationDestination = new SKAnnotation(10);
        annotationDestination.setLocation(destinationPoint);
        annotationDestination.setAnnotationType(SKAnnotation.SK_ANNOTATION_TYPE_DESTINATION_FLAG);
        mapView.addAnnotation(annotationDestination, SKAnimationSettings.ANIMATION_NONE);
        // get a route object and populate it with the desired properties
        SKRouteSettings route = new SKRouteSettings();
        // set start and destination points
        //startPoint = this.currentPosition.getCoordinate();
        route.setStartCoordinate(startPoint);
        route.setDestinationCoordinate(destinationPoint);
        // set the number of routes to be calculated
        route.setMaximumReturnedRoutes(1);
        // set the route mode
        route.setRouteMode(SKRouteSettings.SKRouteMode.PEDESTRIAN);
        // set whether the route should be shown on the map after it's computed
        route.setRouteExposed(true);
        // set the route listener to be notified of route calculation
        // events
        SKRouteManager.getInstance().setRouteListener(this);
        // pass the route to the calculation routine
        SKRouteManager.getInstance().calculateRoute(route);

    }

    public void clearRouteFromCache() {
        //clears all the cached routes
        SKRouteManager.getInstance().clearAllRoutesFromCache();
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
    public void onRouteCalculationCompleted(SKRouteInfo skRouteInfo) {
        // select the current route (on which navigation will run)
        SKRouteManager.getInstance().setCurrentRouteByUniqueId(skRouteInfo.getRouteID());
        // zoom to the current route
        SKRouteManager.getInstance().zoomToRoute(1, 1, 8, 8, 8, 8, 0);
        startButton.setText(getResources().getString(R.string.start_navigation));

    }

    @Override
    public void onRouteCalculationFailed(SKRoutingErrorCode skRoutingErrorCode) {

    }

    @Override
    public void onAllRoutesCompleted() {

    }

    @Override
    public void onServerLikeRouteCalculationCompleted(SKRouteJsonAnswer skRouteJsonAnswer) {

    }

    @Override
    public void onOnlineRouteComputationHanging(int i) {

    }

    @Override
    public void onDestinationReached() {
        Toast.makeText(MapArActivity.this, R.string.destination_reached, Toast.LENGTH_SHORT).show();
        // clear the map when reaching destination
        clearMap();
    }

    @Override
    public void onSignalNewAdviceWithInstruction(String s) {

    }

    @Override
    public void onSignalNewAdviceWithAudioFiles(String[] strings, boolean b) {

    }

    @Override
    public void onSpeedExceededWithAudioFiles(String[] strings, boolean b) {

    }

    @Override
    public void onSpeedExceededWithInstruction(String s, boolean b) {

    }

    @Override
    public void onUpdateNavigationState(SKNavigationState skNavigationState) {

    }

    @Override
    public void onReRoutingStarted() {

    }

    @Override
    public void onFreeDriveUpdated(String s, String s1, String s2, SKNavigationState.SKStreetType skStreetType, double v, double v1) {

    }

    @Override
    public void onViaPointReached(int i) {

    }

    @Override
    public void onVisualAdviceChanged(boolean b, boolean b1, SKNavigationState skNavigationState) {

    }

    @Override
    public void onTunnelEvent(boolean b) {

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

    /*
    private double applySmoothAlgorithm(float newCompassValue) {//TODO: Test the Smoothing Algo
        if (Math.abs(newCompassValue - currentCompassValue) < 180) {
            currentCompassValue = currentCompassValue + SMOOTH_FACTOR_COMPASS * (newCompassValue - currentCompassValue);
        } else {
            if (currentCompassValue > newCompassValue) {
                currentCompassValue = (currentCompassValue + SMOOTH_FACTOR_COMPASS * ((360 + newCompassValue - currentCompassValue) % 360) + 360) % 360;
            } else {
                currentCompassValue = (currentCompassValue - SMOOTH_FACTOR_COMPASS * ((360 - newCompassValue + currentCompassValue) % 360) + 360) % 360;
            }
        }
        return currentCompassValue;
    }*/

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        /*
        private class the handle the results of the geocoding
         */
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {

            if (resultCode == Constants.SUCCESS_RESULT) {
                // If the calculation is successfull, assigns values to the origin and destination addresses
                final Address address = resultData.getParcelable(Constants.RESULT_D_ADDRESS);
                final Address oAddress = resultData.getParcelable(Constants.RESULT_O_ADDRESS);

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      progressBarAR.setVisibility(View.GONE);

                                      if (address != null && oAddress != null) {
                                          //assign values to the destination and origin coordinates
                                          //to be used later for calculating route
                                          destinationPoint = new SKCoordinate(address.getLatitude(), address.getLongitude());
                                          originPoint = new SKCoordinate(oAddress.getLatitude(), oAddress.getLongitude());
                                          calculateRoute(originPoint, destinationPoint);
                                          originAddress.getText().clear();
                                          destinationAddress.getText().clear();
                                          Log.i(TAG, "here");
                                      }
                                  }
                              }
                );
            } else {
                runOnUiThread(new Runnable() {
                    //In case it fails to geocode
                                  @Override
                                  public void run() {
                                      progressBarAR.setVisibility(View.GONE);
                                      originAddress.getText().clear();
                                      destinationAddress.getText().clear();
                                      Toast.makeText(getApplicationContext(), getResources().getString(R.string.geocode_failure),
                                              Toast.LENGTH_LONG).show();
                                  }
                              }
                );
            }
        }

    }
}