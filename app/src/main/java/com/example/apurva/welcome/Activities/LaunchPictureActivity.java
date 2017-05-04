package com.example.apurva.welcome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.apurva.welcome.R;
import com.skobbler.ngx.SKDeveloperKeyException;
import com.skobbler.ngx.SKMaps;
import com.skobbler.ngx.SKMapsInitializationListener;

/**
 * Created by lasse on 27.04.2017.
 */

public class LaunchPictureActivity extends AppCompatActivity implements SKMapsInitializationListener{


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
        //if library initialization is successful start MapPictureActivity
        if (isSuccessful) {
            finish();
            goToMap();
        }

    }

    public void goToMap() {
        //launching MapPictureActivity and clearing this one so that on back pressed this is not displayed
        Intent launchNextActivity;
        launchNextActivity = new Intent(this, MapPictureActivity.class);
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
