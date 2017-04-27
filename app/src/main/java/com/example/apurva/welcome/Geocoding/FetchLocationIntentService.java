package com.example.apurva.welcome.Geocoding;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.example.apurva.welcome.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by apurv on 12-04-2017.
 */
public class FetchLocationIntentService extends IntentService{

    protected ResultReceiver mDReceiver;

    private static final String TAG = "FetchIntentService";

    public FetchLocationIntentService() {

        super("FetchLocationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        List<Address> oAddresses = null;
        //receiver to send the results to map activity
        mDReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        //retrieving from the name of destination and origin address
        String name = intent.getStringExtra(Constants.LOCATION_NAME_DATA_EXTRA);
        String oName = intent.getStringExtra(Constants.LOCATION_NAME_DATA_ORIGIN);
        //call the method getlocationfromname() to geocode
        try {
            addresses = geocoder.getFromLocationName(name, 1);
            oAddresses = geocoder.getFromLocationName(oName, 1);
        } catch (IOException e) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, e);
        }

        if (addresses == null || addresses.size()  == 0 || oAddresses == null || oAddresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null, null);
        }

        else {
            Address address = addresses.get(0);
            Address oAddress = oAddresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();


            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments), address, oAddress);
        }

    }

    private void deliverResultToReceiver(int resultCode, String message1, Address address1, Address address2) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_D_ADDRESS, address1);
        bundle.putString(Constants.RESULT_DATA_KEY, message1);
        bundle.putParcelable(Constants.RESULT_O_ADDRESS, address2);
        mDReceiver.send(resultCode, bundle);
    }
}
