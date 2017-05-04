package com.example.apurva.welcome.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.apurva.welcome.R;

//This activity is a launcher activity where you taken permission for accessing location


public class PermissionsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            askPermission();
        }
        else{
            //if permission is granted then open the modeSelection
            Intent launchNextActivity;
            launchNextActivity = new Intent(this, ModeSelectionActivity.class);

            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(launchNextActivity);
        }
    }

    public void askPermission(){

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, LaunchArActivity.class);
                    startActivity(intent);
                }
                else {
                    finish();

                }
                return;
            }

        }
    }
}
