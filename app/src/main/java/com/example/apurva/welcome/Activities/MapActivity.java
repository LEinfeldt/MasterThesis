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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.apurva.welcome.DecisionPoints.Geofencing;
import com.example.apurva.welcome.DecisionPoints.JsonParser;
import com.example.apurva.welcome.DeviceUtils.LocationUpdate;
import com.example.apurva.welcome.DeviceUtils.SensorUpdate;
import com.example.apurva.welcome.Geocoding.Constants;
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

import java.util.List;
import java.util.Map;

/**
 * Created by lasse on 27.04.2017.
 */

public class MapActivity extends AppCompatActivity implements SKMapSurfaceListener, SKRouteListener, SKNavigationListener, SensorUpdate.AccelMagnoListener {

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

    //layers for the data to be displayed on the map
    private SKAnnotation[] supermarkets;
    private SKAnnotation[] pharmacies;
    private SKAnnotation[] health;
    private SKAnnotation[] busstops;
    private SKAnnotation[] sports;
    private SKAnnotation[] parks;
    private SKAnnotation[] schools;
    private SKAnnotation[] libraries;
    private SKAnnotation[] language;
    private SKAnnotation[] administration;
    private SKAnnotation[] insurance;

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
        //aSwitch = (Switch) findViewById(R.id.switchLayers);
        //get the Strings from resources
        layerNames = getResources().getStringArray(R.array.layerNames);
        //set an adapter for the drawer list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.txtTitle, layerNames));

        // set a listener to the list
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //toggle the visibility of the selected layer
                toggleDataLayer(position);
            }
        });

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

    /**
     * Toggle the visibility of a given layer in the map.
     * @param position Position of the layer that shall be toggled
     */
    private void toggleDataLayer(int position) {
        Log.i("Position", "Clicked: " + position);
        //change the visibility of the selected layer
        if(mapView != null) {
            switch (position) {
                case 0:
                    if(mapView.getAllAnnotations().contains(supermarkets[0])) {
                        for(int i = 0; i < supermarkets.length; i++) {
                            mapView.deleteAnnotation(supermarkets[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < supermarkets.length; i++) {
                            mapView.addAnnotation(supermarkets[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 1:
                    if(mapView.getAllAnnotations().contains(pharmacies[0])) {
                        for(int i = 0; i < pharmacies.length; i++) {
                            mapView.deleteAnnotation(pharmacies[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < pharmacies.length; i++) {
                            mapView.addAnnotation(pharmacies[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 2:
                    if(mapView.getAllAnnotations().contains(health[0])) {
                        for(int i = 0; i < health.length; i++) {
                            mapView.deleteAnnotation(health[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < health.length; i++) {
                            mapView.addAnnotation(health[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 3:
                    if(mapView.getAllAnnotations().contains(busstops[0])) {
                        for(int i = 0; i < busstops.length; i++) {
                            mapView.deleteAnnotation(busstops[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < busstops.length; i++) {
                            mapView.addAnnotation(busstops[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 4:
                    if(mapView.getAllAnnotations().contains(sports[0])) {
                        for(int i = 0; i < sports.length; i++) {
                            mapView.deleteAnnotation(sports[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < sports.length; i++) {
                            mapView.addAnnotation(sports[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 5:
                    if(mapView.getAllAnnotations().contains(parks[0])) {
                        for(int i = 0; i < parks.length; i++) {
                            mapView.deleteAnnotation(parks[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < parks.length; i++) {
                            mapView.addAnnotation(parks[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 6:
                    if(mapView.getAllAnnotations().contains(schools[0])) {
                        for(int i = 0; i < schools.length; i++) {
                            mapView.deleteAnnotation(schools[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < schools.length; i++) {
                            mapView.addAnnotation(schools[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 7:
                    if(mapView.getAllAnnotations().contains(libraries[0])) {
                        for(int i = 0; i < libraries.length; i++) {
                            mapView.deleteAnnotation(libraries[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < libraries.length; i++) {
                            mapView.addAnnotation(libraries[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 8:
                    if(mapView.getAllAnnotations().contains(language[0])) {
                        for(int i = 0; i < language.length; i++) {
                            mapView.deleteAnnotation(language[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < language.length; i++) {
                            mapView.addAnnotation(language[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 9:
                    if(mapView.getAllAnnotations().contains(administration[0])) {
                        for(int i = 0; i < administration.length; i++) {
                            mapView.deleteAnnotation(administration[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < administration.length; i++) {
                            mapView.addAnnotation(administration[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
                case 10:
                    if(mapView.getAllAnnotations().contains(insurance[0])) {
                        for(int i = 0; i < insurance.length; i++) {
                            mapView.deleteAnnotation(insurance[i].getUniqueID());
                        }
                    }
                    else {
                        for(int i = 0; i < insurance.length; i++) {
                            mapView.addAnnotation(insurance[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                    }
                    break;
            }
        }

    };

    /**
     * Load the data from JSON file and add the markers to the map
     */
    private void addDataToMap() {
        if(mapView != null) {
            //counter for unique id for each marker
            int i = 0;
            //counter for the entries in the array
            int k = 0;
            busstops = new SKAnnotation[jsonParser.getBusstopCoordinates().size()];
            //load the busstops
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getBusstopCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_bus, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                busstops[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            pharmacies = new SKAnnotation[jsonParser.getPharmacyCoordinates().size()];
            //add pharmacy
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getPharmacyCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_pharmacy, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                pharmacies[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            supermarkets = new SKAnnotation[jsonParser.getSupermarketCoordinates().size()];
            //load supermarkets
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getSupermarketCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                //with the logo of the supermarket that is chosen
                if(entry.getKey().matches("Aldi")) {
                    View customView = ((LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_aldi, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("Lidl")) {
                    View customView = ((LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_lidl, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("Marktkauf")) {
                    View customView = ((LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_marktkauf, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("Rewe") || entry.getKey().matches("Rewe1")) {
                    View customView = ((LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_rewe, null, false);
                    annotationView.setView(customView);
                }
                else {
                    View customView = ((LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_shop, null, false);
                    annotationView.setView(customView);
                }
                annotation.setAnnotationView(annotationView);
                supermarkets[k] = annotation;
                Log.i("Supermarket" + k, "wert ist:" + supermarkets[k]);
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            parks = new SKAnnotation[jsonParser.getParkCoordinates().size()];
            //add parks
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getParkCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_park, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                parks[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            schools = new SKAnnotation[jsonParser.getSchoolCoordinates().size()];
            //add schools
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getSchoolCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_school, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                schools[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            sports = new SKAnnotation[jsonParser.getSportCoordinates().size()];
            //add sport facilities
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getSportCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_sports, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                sports[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            insurance = new SKAnnotation[jsonParser.getInsuranceCoordinates().size()];
            //add insurances
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getInsuranceCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_tk, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                insurance[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            language = new SKAnnotation[jsonParser.getLanguageCoordinates().size()];
            //add language centers
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getLanguageCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_language, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                language[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            libraries = new SKAnnotation[jsonParser.getLibraryCoordinates().size()];
            //add libraries
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getLibraryCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_library, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                libraries[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            administration = new SKAnnotation[jsonParser.getAdministrationCoordinates().size()];
            //add administration
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getAdministrationCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_town, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                administration[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            health = new SKAnnotation[jsonParser.getHealthcenterCoordinates().size()];
            //add health centers
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getHealthcenterCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_redcross, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                health[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
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
        addDataToMap();
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
        Toast.makeText(MapActivity.this, R.string.destination_reached, Toast.LENGTH_SHORT).show();
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

}
