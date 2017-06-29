package com.example.apurva.welcome.DecisionPoints;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.apurva.welcome.Logger.Logger;
import com.example.apurva.welcome.R;
import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.map.SKAnimationSettings;
import com.skobbler.ngx.map.SKAnnotation;
import com.skobbler.ngx.map.SKAnnotationView;
import com.skobbler.ngx.map.SKMapSurfaceView;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by lasse on 23.06.2017.
 */

public class LayerInteraction {

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
    private SKAnnotation[] sparkasse;
    private SKAnnotation[] post;

    private JsonParser jsonParser;
    private Context context;

    public LayerInteraction(Context context) {
        this.context = context;
        try {
            jsonParser = new JsonParser(context);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the data from JSON file and add the markers to the map
     */
    public void addDataToMap(SKMapSurfaceView mapView) {
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_aldi, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("Lidl")) {
                    View customView = ((LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_lidl, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("Marktkauf")) {
                    View customView = ((LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_marktkauf, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("Rewe") || entry.getKey().matches("Rewe1")) {
                    View customView = ((LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_rewe, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("DM")) {
                    View customView = ((LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_dm, null, false);
                    annotationView.setView(customView);
                }
                else if(entry.getKey().matches("Family")) {
                    View customView = ((LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_family, null, false);
                    annotationView.setView(customView);
                }
                else {
                    View customView = ((LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                            R.layout.layout_shop, null, false);
                    annotationView.setView(customView);
                }
                annotation.setAnnotationView(annotationView);
                supermarkets[k] = annotation;
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_park, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                parks[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            sparkasse = new SKAnnotation[jsonParser.getSparkasseCoordinates().size()];
            //add Sparkasse
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getSparkasseCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_sparkasse, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                sparkasse[k] = annotation;
                mapView.addAnnotation(annotation, SKAnimationSettings.ANIMATION_NONE);
                i++;
                k++;
            }
            k = 0;
            post = new SKAnnotation[jsonParser.getPostCoordinates().size()];
            //add Post
            for(Map.Entry<String, SKCoordinate> entry : jsonParser.getPostCoordinates().entrySet()) {
                SKAnnotation annotation = new SKAnnotation(i);
                annotation.setLocation(entry.getValue());
                annotation.setMininumZoomLevel(5);
                // add an annotation with a view
                SKAnnotationView annotationView = new SKAnnotationView();
                //get the view, containing the image, that should be displayed
                View customView = ((LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.layout_post, null, false);
                annotationView.setView(customView);
                annotation.setAnnotationView(annotationView);
                post[k] = annotation;
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
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

    /**
     * Toggle the visibility of a given layer in the map.
     * @param position Position of the layer that shall be toggled
     */
    public void toggleDataLayer(int position, SKMapSurfaceView mapView, Logger logger) {
        Log.i("Position", "Clicked: " + position);
        //change the visibility of the selected layer
        if(mapView != null) {
            switch (position) {
                case 0:
                    if(mapView.getAllAnnotations().contains(supermarkets[0])) {
                        for(int i = 0; i < supermarkets.length; i++) {
                            mapView.deleteAnnotation(supermarkets[i].getUniqueID());
                        }
                        logger.logLayerSelection("Supermarkets inactive");
                    }
                    else {
                        for(int i = 0; i < supermarkets.length; i++) {
                            mapView.addAnnotation(supermarkets[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Supermarkets active");
                    }
                    break;
                case 1:
                    if(mapView.getAllAnnotations().contains(pharmacies[0])) {
                        for(int i = 0; i < pharmacies.length; i++) {
                            mapView.deleteAnnotation(pharmacies[i].getUniqueID());
                        }
                        logger.logLayerSelection("Pharmacies inactive");
                    }
                    else {
                        for(int i = 0; i < pharmacies.length; i++) {
                            mapView.addAnnotation(pharmacies[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Pharmacies active");
                    }
                    break;
                case 2:
                    if(mapView.getAllAnnotations().contains(health[0])) {
                        for(int i = 0; i < health.length; i++) {
                            mapView.deleteAnnotation(health[i].getUniqueID());
                        }
                        logger.logLayerSelection("Health inactive");
                    }
                    else {
                        for(int i = 0; i < health.length; i++) {
                            mapView.addAnnotation(health[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Health active");
                    }
                    break;
                case 3:
                    if(mapView.getAllAnnotations().contains(busstops[0])) {
                        for(int i = 0; i < busstops.length; i++) {
                            mapView.deleteAnnotation(busstops[i].getUniqueID());
                        }
                        logger.logLayerSelection("Busstops inactive");
                    }
                    else {
                        for(int i = 0; i < busstops.length; i++) {
                            mapView.addAnnotation(busstops[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Busstops active");
                    }
                    break;
                case 4:
                    if(mapView.getAllAnnotations().contains(sports[0])) {
                        for(int i = 0; i < sports.length; i++) {
                            mapView.deleteAnnotation(sports[i].getUniqueID());
                        }
                        logger.logLayerSelection("Sports inactive");
                    }
                    else {
                        for(int i = 0; i < sports.length; i++) {
                            mapView.addAnnotation(sports[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Sports active");
                    }
                    break;
                case 5:
                    if(mapView.getAllAnnotations().contains(parks[0])) {
                        for(int i = 0; i < parks.length; i++) {
                            mapView.deleteAnnotation(parks[i].getUniqueID());
                        }
                        logger.logLayerSelection("Parks inactive");
                    }
                    else {
                        for(int i = 0; i < parks.length; i++) {
                            mapView.addAnnotation(parks[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Parks active");
                    }
                    break;
                case 6:
                    if(mapView.getAllAnnotations().contains(schools[0])) {
                        for(int i = 0; i < schools.length; i++) {
                            mapView.deleteAnnotation(schools[i].getUniqueID());
                        }
                        logger.logLayerSelection("Schools inactive");
                    }
                    else {
                        for(int i = 0; i < schools.length; i++) {
                            mapView.addAnnotation(schools[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Schools active");
                    }
                    break;
                case 7:
                    if(mapView.getAllAnnotations().contains(libraries[0])) {
                        for(int i = 0; i < libraries.length; i++) {
                            mapView.deleteAnnotation(libraries[i].getUniqueID());
                        }
                        logger.logLayerSelection("Libraries inactive");
                    }
                    else {
                        for(int i = 0; i < libraries.length; i++) {
                            mapView.addAnnotation(libraries[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Libraries active");
                    }
                    break;
                case 8:
                    if(mapView.getAllAnnotations().contains(language[0])) {
                        for(int i = 0; i < language.length; i++) {
                            mapView.deleteAnnotation(language[i].getUniqueID());
                        }
                        logger.logLayerSelection("Languagecenter inactive");
                    }
                    else {
                        for(int i = 0; i < language.length; i++) {
                            mapView.addAnnotation(language[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Languagecenter active");
                    }
                    break;
                case 9:
                    if(mapView.getAllAnnotations().contains(administration[0])) {
                        for(int i = 0; i < administration.length; i++) {
                            mapView.deleteAnnotation(administration[i].getUniqueID());
                        }
                        logger.logLayerSelection("Administration inactive");
                    }
                    else {
                        for(int i = 0; i < administration.length; i++) {
                            mapView.addAnnotation(administration[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Administration active");
                    }
                    break;
                case 10:
                    if(mapView.getAllAnnotations().contains(insurance[0])) {
                        for(int i = 0; i < insurance.length; i++) {
                            mapView.deleteAnnotation(insurance[i].getUniqueID());
                        }
                        logger.logLayerSelection("Insurance inactive");
                    }
                    else {
                        for(int i = 0; i < insurance.length; i++) {
                            mapView.addAnnotation(insurance[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Insurance active");
                    }
                    break;
                case 11:
                    if(mapView.getAllAnnotations().contains(sparkasse[0])) {
                        for(int i = 0; i < sparkasse.length; i++) {
                            mapView.deleteAnnotation(sparkasse[i].getUniqueID());
                        }
                        logger.logLayerSelection("Sparkasse inactive");
                    }
                    else {
                        for(int i = 0; i < sparkasse.length; i++) {
                            mapView.addAnnotation(sparkasse[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Sparkasse active");
                    }
                    break;
                case 12:
                    if(mapView.getAllAnnotations().contains(post[0])) {
                        for(int i = 0; i < post.length; i++) {
                            mapView.deleteAnnotation(post[i].getUniqueID());
                        }
                        logger.logLayerSelection("Post inactive");
                    }
                    else {
                        for(int i = 0; i < post.length; i++) {
                            mapView.addAnnotation(post[i], SKAnimationSettings.ANIMATION_NONE);
                        }
                        logger.logLayerSelection("Post active");
                    }
                    break;
            }
        }

    }

}
