package com.example.apurva.welcome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.apurva.welcome.R;

/**
 * Created by lasse on 27.04.2017.
 */

/**
 * @desc Select the mode for the navigation (Map, Map + AR, Map + Picture)
 */
public class ModeSelectionActivity extends AppCompatActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        /*
        //onClick Map + AR
        if(/*clicked on the button for the map + ar view) {
            Intent launchNextActivity;
            launchNextActivity = new Intent(this, LaunchArActivity.class);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        //onClick Map + Picture
        else if(/*clicked on the button for map + picture view) {
            Intent launchNextActivity;
            //kann man dem Launch mitgeben, woher er kam? Dann kann man die Launchactivity für
            //alle Karten interaktionen benutzen und dann darin unterscheiden woher man kommt
            //und wohin man dann weiterleiten will
            launchNextActivity = new Intent(this, LaunchPictureActivity.class);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        //onClick on the Map button
        else if(/*Clicked on the map button) {
            Intent launchNextActivity;
            launchNextActivity = new Intent(this, LaunchMapActivity.class);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }*/
    }

    /**
     * @desc open the map view when clicked on button
     */
    public void onMapClick(View v) {

        Intent launchNextActivity;
        launchNextActivity = new Intent(this, LaunchMapActivity.class);

        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(launchNextActivity);
    }

    /**
     * @desc open the map + ar view when clicked on button
     */
    public void onMapArClick(View v) {

        Intent launchNextActivity;
        launchNextActivity = new Intent(this, LaunchArActivity.class);

        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(launchNextActivity);
    }

    /**
     * @desc open the map + picture view when click on the button
     */
    public void onMapPictureClick(View v) {
        Intent launchNextActivity;
        launchNextActivity = new Intent(this, LaunchPictureActivity.class);

        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(launchNextActivity);
    }
}
