package com.provider.admin.cpepsi_provider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.LocationUtil.GpsTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Get_locattion_activity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleApiClient.ConnectionCallbacks {
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient, mGoogleApiClient2;
    LocationRequest mLocationRequest;
    LatLng p1 = null;
    private GoogleMap mMap;
    EditText get_loc_map;
    public Button give_loc_back;
    GpsTracker gpsTracker;
    String address1 = "";
    double longitude;
    String Lat_Long_User;
    Geocoder geocoder;
    int for_sub_id;
    Marker current_marker;
    String loction_str;
    Address address;
    double latitude;
    List<Address> event_address;
 //   String locality = "Indore", place = "Krishnodaya Nagar";

    String state_of_provider, place_of_provider;
    //    private static final String GOOGLE_API_CLIENT_ID = "330185352783-5qm1sk3uk1pc9ibct4dqmvghdvu8sf7n.apps.googleusercontent.com ";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int PERMISSION_REQUEST_CODE = 100;
    GeoDataClient geoDataClient;
    int selected_ser_name_from;
    Intent to_add_event;
    private List<Address> addresses_major;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_locattion_activity);
//        mGoogleApiClient = new GoogleApiClient.Builder(Get_locattion_activity.this)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
//                .build();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        checkthepermisions();
        try {
            for_sub_id = getIntent().getIntExtra("sub_ser_ty2", 10);
            selected_ser_name_from = getIntent().getIntExtra("ser_ty2", 4);
        } catch (NullPointerException e) {

        }
        gpsTracker = new GpsTracker(this);
        geocoder = new Geocoder(this);
        mapFragment.getMapAsync(this);
        get_loc_map = (EditText) findViewById(R.id.get_loc_map);
        give_loc_back = (Button) findViewById(R.id.give_loc_back);


        give_loc_back.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (!get_loc_map.getText().toString().isEmpty()) {
                                                     if (lets_get_current_loc()) {
                                                         loction_str = get_loc_map.getText().toString();
                                                         to_add_event = new Intent(Get_locattion_activity.this, Service_provider_reg1.class);
//                                                         to_add_event.putExtra("my_loc", loction_str);
                                                         to_add_event.putExtra("my_loc", loction_str);
                                                         to_add_event.putExtra("city_of_pro", state_of_provider);
                                                         Log.e("state is" , ""+state_of_provider);
                                                         to_add_event.putExtra("place_of_pro", place_of_provider);
                                                         Log.e("state is" , ""+place_of_provider);
                                                         to_add_event.putExtra("selected_ser_name_back", selected_ser_name_from);
                                                         to_add_event.putExtra("ser_id_back", for_sub_id);

                                                         startActivity(to_add_event);
                                                         finish();

                                                     } else {
                                                         Toast.makeText(gpsTracker, "Please choose a valid place again", Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             }
                                         }
        );
    }

    private void checkthepermisions() {

        if (ActivityCompat.checkSelfPermission(Get_locattion_activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(Get_locattion_activity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            lets_get_current_loc();
            buildGoogleApiClient();
            //    mMap.setMyLocationEnabled(true);
        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
        mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.PLACE_DETECTION_API).build();
        mGoogleApiClient2.connect();

    }

    private Boolean lets_get_current_loc() throws SecurityException {
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            //////Cobvert Lat long to address
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            if (latitude == 0.0 && longitude == 0.0) {

                Toast.makeText(Get_locattion_activity.this, "GPS is not able to find your location please enter manually", Toast.LENGTH_SHORT).show();
                return false;
//                Toast.makeText(LandDetailActivity.this, addresslatitude + "\n" + addresslongitude, Toast.LENGTH_SHORT).show();
            } else {

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5\
//                    state_of_provider = address.getAdminArea().toString();
//                    place_of_provider = address.getSubLocality().toString();
//                    to_add_event.putExtra("city_of_pro",state_of_provider);
//                    to_add_event.putExtra("place_of_pro",place_of_provider);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
//                    String address = "";
                    if (addresses.get(0).getMaxAddressLineIndex() == 1 || addresses.get(0).getMaxAddressLineIndex() == 0) {
                        address1 = address1 + " " + addresses.get(0).getAddressLine(0);
                        return true;
                    } else {
                        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                            address1 = address1 + " " + addresses.get(0).getAddressLine(i); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            return true;
                        }
                    }

                }
            }
        } else {

            gpsTracker.showSettingsAlert();
            return false;
        }
        return false;
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
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                       @Override
                                       public void onMapClick(LatLng latLng) {

                                           if (current_marker.getPosition() != null) {
                                               current_marker.remove();
                                               MarkerOptions markerOptions = new MarkerOptions();
                                               markerOptions.position(latLng);
                                               markerOptions.title("Not  the Position");
                                               markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                               current_marker = mMap.addMarker(markerOptions);
                                               mMap.animateCamera(CameraUpdateFactory.zoomTo(40));
                                               geocoder = new Geocoder(Get_locattion_activity.this);
                                               try {
                                                   addresses_major = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
                                               } catch (IOException e) {
                                                   e.printStackTrace();
                                               }

                                               try {
                                                   address = addresses_major.get(0);
                                                   state_of_provider = address.getLocality().toString();
                                                   place_of_provider = address.getSubLocality().toString();
                                                   Log.e("location getLocality", "" + address.getLocality().toString());
                                                   Log.e("location getSubLocality", "" + address.getSubLocality().toString());
                                                   Log.e("location getAdminArea", "" + address.getAdminArea().toString());
                                                   Toast.makeText(Get_locattion_activity.this, "Address is" + address.getAddressLine(0), Toast.LENGTH_SHORT).show();
                                               } catch (Exception e) {
                                                   Toast.makeText(Get_locattion_activity.this, "Please choose right address", Toast.LENGTH_SHORT).show();
                                               }


                                               loction_str = String.valueOf(address.getAddressLine(0));
                                               Lat_Long_User = String.valueOf(latLng);
                                               get_loc_map.setText(loction_str);

//        final PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
//                .getCurrentPlace(mGoogleApiClient2, null);
//        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
//            @Override
//            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                    Log.i("LOG marker", String.format("Place '%s' with " +
//                                    "likelihood: %g",
//                            placeLikelihood.getPlace().getName(),
//                            placeLikelihood.getLikelihood()));
//                    Toast.makeText(Get_locattion_activity.this, ""+String.format("Place '%s' with " + "likelihood: %g", placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood()), Toast.LENGTH_SHORT).show();
//                }
//                likelyPlaces.release();
//            }
//        });
//                                               current_marker.remove();
//                                               current_marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Selected location"));
//                                               List<Address> addresses = null;
//                                               try {
//
//                                                   geocoder = new Geocoder(Get_locattion_activity.this);
//                                                   addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
//
//                                                   address = addresses.get(0);
//                                                   try {
//
//                                                   state_of_provider = address.getLocality().toString();
//                                                   place_of_provider = address.getSubLocality().toString();
//                                                   Log.e("getFeatureName:",""+ address.getFeatureName().toString() );
//                                                   Log.e("getAdminArea :",""+ address.getAdminArea());
//                                                   Log.e("getLocality :",""+ address.getLocality() );
//                                                   Log.e("getPremises :",""+ address.getPremises() );
//                                                   Log.e("getplace_of_provider :",""+ place_of_provider );
//                                                   Log.e("getSubLocality :",""+ address.getSubLocality() );
//                                                   Log.e("getThoroughfare :",""+ address.getThoroughfare());
//                                                   }catch (Exception e)
//                                                   {
//
//                                                   }
//                                                   loction_str = address.getAddressLine(0);
//                                                   Lat_Long_User = String.valueOf(latLng);
//                                                   get_loc_map.setText(loction_str);
////                      if (loction_str != null) {
////                          StringBuilder sb = new StringBuilder();
////                          for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//////                              sb.append(address.getAddressLine(i) + "\n");
////                              get_loc_map.setText(""+ sb.append(address.getAddressLine(i) + "\n"));
////                          }
////                      }
//                                               } catch (Exception e) {
//                                                   Toast.makeText(Get_locattion_activity.this, "Please choose a place not road", Toast.LENGTH_SHORT).show();
//                                                   e.printStackTrace();
//                                               }
//                  loction_str = current_marker.

                                           }
                                       }
                                   }
        );

        if (address1 != null) {
            LatLng current = new LatLng(latitude, longitude);
            current_marker = mMap.addMarker(new MarkerOptions().position(current).title("Your Current location"));
            get_loc_map.setText(address1);
            final CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(40)
                    .bearing(90)
                    .tilt(30)
                    .build();
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(40));
            mMap.addMarker(new MarkerOptions().position(new LatLng(current.latitude, current.longitude)).title("Your location is here"));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(40));
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                                        @Override
                                                        public boolean onMyLocationButtonClick() {
//                                                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(40));
                                                            return true;
                                                        }
                                                    }
            );
            Log.e("xyz is", "" + address1.toString());
            Lat_Long_User = current.toString();

//            mMap.addMarker(new MarkerOptions().position(current).title("my location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        } else {
//            LatLng sydney2 = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

       /* if (!locality.isEmpty() && !place.isEmpty()) {
            LatLng provider_location = put_location_to_the_marker();
            mMap.addMarker(new MarkerOptions().position(provider_location).title("Provider Location"));
        }*/
    }

    private LatLng put_location_to_the_marker() throws SecurityException {
//
//        final PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
//                .getCurrentPlace(mGoogleApiClient, null);
//        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
//            @Override
//            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                    Log.i("LOG marker", String.format("Place '%s' with " +
//                                    "likelihood: %g",
//                            placeLikelihood.getPlace().getName(),
//                            placeLikelihood.getLikelihood()));
//                }
//                likelyPlaces.release();
//            }
//        });
        geocoder = new Geocoder(this);
        try {
       //     event_address = geocoder.getFromLocationName(place, 5);
            if (event_address.isEmpty()) {
                return null;
            } else {
                Address location = event_address.get(0);
                state_of_provider = location.getLocality().toString();
                place_of_provider = location.getSubLocality().toString();
                Log.e("location getLocality", "" + location.getLocality().toString());
                Log.e("location getSubLocality", "" + location.getSubLocality().toString());
                Log.e("location getAdminArea", "" + location.getAdminArea().toString());
                location.getLatitude();
                location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("LOg at services", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                    }
                });
            }catch (Exception e){
                Toast.makeText(Get_locattion_activity.this, "ffgggghgh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
