package com.example.w.maps;

import java.io.IOException;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.w.maps.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.services.drive.model.Comment;

public class MapsActivity extends FragmentActivity {
    ActionBar actionBar;
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    String location,warlocation;
    //String Destination;
    //LatLng dest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        Bundle bundle=this.getIntent().getExtras();
        warlocation=bundle.getString("location");
        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();
        if (warlocation != null && !warlocation.equals("")){
        new GeocoderTask(getApplicationContext()).execute(warlocation);}
        else {
            Toast.makeText(getApplicationContext(),"null address",Toast.LENGTH_LONG).show();
        }
        // Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) findViewById(R.id.btn_find);

        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.source);
               // EditText etDest=(EditText)findViewById(R.id.destination);

                // Getting user input location
                location = etLocation.getText().toString();
               // Destination=etDest.getText().toString();
                if (location != null && !location.equals("")) {
                    new GeocoderTask(getApplicationContext()).execute(location);
                }
            }
        };

        // Setting button click event listener for the find button
        btn_find.setOnClickListener(findClickListener);


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_login, menu);
        return true;
    }*/


    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String,Void,List<Address>> {
        private Context mainContxt;


        public GeocoderTask(Context con) {
            mainContxt = con;
        }

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(mainContxt);
            List<Address> addresses = null;

           /* try {
                geoResults = geocoder.getFromLocationName(locationName[0], 1);
                while (geoResults.size()==0) {
                    geoResults = geocoder.getFromLocationName(locationName[0], 1);
                }
                if (geoResults.size()>0) {
                    Address  addresses = geoResults.get(0);

                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }*/

            try {
                // Getting a maximum of 3 Address that match}es the input text

                    addresses = geocoder.getFromLocationName(locationName[0], 1);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }


        @Override
        protected void onPostExecute(List<Address> addresses) {
             float zoomlevel=15.0f;           /*if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }*/

            // Clears all the existing markers on the map

            // Adding Markers on Google Map for each matching address
            if (addresses != null) {
                int t = addresses.size();
                int i;
                for (i = 0; i < t; i++) {
                    Address address = addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());
                    CameraUpdate center=
                            CameraUpdateFactory.newLatLng(latLng);
                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(40);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomlevel));
                    markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(addressText);

                    googleMap.addMarker(markerOptions);


                    // Locate the first location
                    if (i == 0)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                }
            }
        }

    }

}