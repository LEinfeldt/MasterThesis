package com.example.apurva.welcome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.apurva.welcome.R;
import com.skobbler.ngx.SKDeveloperKeyException;
import com.skobbler.ngx.SKMaps;
import com.skobbler.ngx.SKMapsInitializationListener;

/*This is the activity where you activate SKMaps
 For getting Skobbeler API for android gradle repositories and dependencies were changed accordingly
 //TODO: Put a logo on this activity so that it's displayed before the Map Activity begins.
 */
public class LaunchArActivity extends AppCompatActivity implements SKMapsInitializationListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //Initializing SKMaps
        try {
            Log.i("check", "two");
            SKMaps.getInstance().initializeSKMaps(getApplication(), this);
        } catch (SKDeveloperKeyException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLibraryInitialized(boolean isSuccessful) {
        //if library initialization is successful start MapArActivity
        if (isSuccessful) {
            finish();
            goToMap();
        }

    }

    private void goToMap(){
        //launching MapArActivity and clearing this one so that on back pressed this is not displayed
        Intent launchNextActivity;
        launchNextActivity = new Intent(this, MapArActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(launchNextActivity);
    }

    @Override
    protected void onResume(){
        super.onResume();
        goToMap();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

}