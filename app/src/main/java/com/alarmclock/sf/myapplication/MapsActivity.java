package com.alarmclock.sf.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private EditText searchLocationTxt;
    private final static int maxResults = 5;
    List<Address> locationList;
    List<String> locationNameList;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        searchLocationTxt = (EditText) findViewById(R.id.locationSearchTxt);
        searchLocationTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    searchLocation(v);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(33.1203372, -96.7881783);
        drowMarker(sydney, "State Farm");
    }

    private void drowMarker(LatLng latLng, String title){
        mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void searchLocation(TextView textView) {
        String locationName = textView.getText().toString();
        locationNameList = new ArrayList<String>();

        try {
            locationList = geocoder.getFromLocationName(locationName, 1);

            if (locationList == null) {
                Toast.makeText(getApplicationContext(),
                        "locationList == null",
                        Toast.LENGTH_LONG).show();
            } else {
                if (locationList.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "locationList is empty",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "number of result: " + locationList.size(),
                            Toast.LENGTH_LONG).show();

                    locationNameList.clear();

                    for (Address i : locationList) {
                        if (i.getFeatureName() == null) {
                            locationNameList.add("unknown");
                        } else {
                            locationNameList.add(i.getFeatureName());
                            LatLng latLng = new LatLng(i.getLatitude(), i.getLongitude());
                            drowMarker(latLng, i.getFeatureName());
                        }
                    }
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "network unavailable or any other I/O problem occurs" + locationName,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
