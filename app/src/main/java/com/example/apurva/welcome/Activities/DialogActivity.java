package com.example.apurva.welcome.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.apurva.welcome.R;

/*
Opens a dialog box to ask user whether to open camera for the AR View
 */
public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent launchNextActivity;
        launchNextActivity = new Intent(this, CameraActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Dialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_open_camera)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //If the users selects: launch the cameraActivity
                        startActivity(launchNextActivity);
                    }
                });
        builder.
                setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //if the users selects no: kill the activity
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
