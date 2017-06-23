package com.example.apurva.welcome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.apurva.welcome.R;

/**
 * Created by lasse on 05.06.2017.
 */

public class SelectRouteActivity extends AppCompatActivity{

    Intent i;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);
        i = getIntent();
    }

    public void onRoute1Click(View view) {

        String mode = i.getStringExtra("Mode");
        Intent launchNextActivity;

        //start the map ar mode with selected route
        if(mode.contentEquals("MapAR")) {
            launchNextActivity = new Intent(this, LaunchArActivity.class);
            launchNextActivity.putExtra("Route", 1);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("MapPicture")) {
            launchNextActivity = new Intent(this, LaunchPictureActivity.class);
            launchNextActivity.putExtra("Route", 1);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("Map")) {
            launchNextActivity = new Intent(this, LaunchMapActivity.class);
            launchNextActivity.putExtra("Route", 1);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
    }

    public void onRoute2Click(View view) {

        String mode = i.getStringExtra("Mode");
        Intent launchNextActivity;

        //start the map ar mode with selected route
        if(mode.contentEquals("MapAR")) {
            launchNextActivity = new Intent(this, LaunchArActivity.class);
            launchNextActivity.putExtra("Route", 2);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("MapPicture")) {
            launchNextActivity = new Intent(this, LaunchPictureActivity.class);
            launchNextActivity.putExtra("Route", 2);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("Map")) {
            launchNextActivity = new Intent(this, LaunchMapActivity.class);
            launchNextActivity.putExtra("Route", 2);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
    }

    public void onRoute3Click(View view) {

        String mode = i.getStringExtra("Mode");
        Intent launchNextActivity;

        //start the map ar mode with selected route
        if(mode.contentEquals("MapAR")) {
            launchNextActivity = new Intent(this, LaunchArActivity.class);
            launchNextActivity.putExtra("Route", 3);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("MapPicture")) {
            launchNextActivity = new Intent(this, LaunchPictureActivity.class);
            launchNextActivity.putExtra("Route", 3);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("Map")) {
            launchNextActivity = new Intent(this, LaunchMapActivity.class);
            launchNextActivity.putExtra("Route", 3);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
    }

    public void onRoute4Click(View view) {

        String mode = i.getStringExtra("Mode");
        Intent launchNextActivity;

        //start the map ar mode with selected route
        if(mode.contentEquals("MapAR")) {
            launchNextActivity = new Intent(this, LaunchArActivity.class);
            launchNextActivity.putExtra("Route", 4);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("MapPicture")) {
            launchNextActivity = new Intent(this, LaunchPictureActivity.class);
            launchNextActivity.putExtra("Route", 4);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("Map")) {
            launchNextActivity = new Intent(this, LaunchMapActivity.class);
            launchNextActivity.putExtra("Route", 4);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
    }

    public void onRoute5Click(View view) {

        String mode = i.getStringExtra("Mode");
        Intent launchNextActivity;

        //start the map ar mode with selected route
        if(mode.contentEquals("MapAR")) {
            launchNextActivity = new Intent(this, LaunchArActivity.class);
            launchNextActivity.putExtra("Route", 5);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("MapPicture")) {
            launchNextActivity = new Intent(this, LaunchPictureActivity.class);
            launchNextActivity.putExtra("Route", 5);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
        else if(mode.contentEquals("Map")) {
            launchNextActivity = new Intent(this, LaunchMapActivity.class);
            launchNextActivity.putExtra("Route", 5);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(launchNextActivity);
        }
    }
}
