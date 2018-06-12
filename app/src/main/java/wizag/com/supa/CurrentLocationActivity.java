package wizag.com.supa;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class CurrentLocationActivity extends AppCompatActivity implements OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener, OnMapReadyCallback {
    private TextView locationText, activityText, geofenceText;
    private LocationGooglePlayServicesProvider provider;
    private GoogleMap mMap;

    private static final int LOCATION_PERMISSION_ID = 1001;
    TextView grey_background, getting_location;
    CircularProgressView progressView;
    SupportMapFragment mapFragment;
    Button add_this_location;

   TextView latitude_text, longitude_text;
   double lat, lng;
   double map_lat, map_lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        latitude_text = findViewById(R.id.your_latitude);
        longitude_text = findViewById(R.id.your_longitude);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mapFragment.getMapAsync(new OnMapReadyCallback() {

        /**    @Override
            public void onMapReady(GoogleMap googleMap) {

                // if (latitude_text!=null){
                if (!(TextUtils.isEmpty(latitude_text.getText()))&&!(latitude_text.getText().toString().trim().length()==0)){
                    map_lat = Double.valueOf(latitude_text.getText().toString());
                    map_lon = Double.valueOf(longitude_text.getText().toString());

                    // Add a marker in Sydney, Australia, and move the camera.
                    // LatLng sydney = new LatLng(-34, 151);
                    // LatLng my_loc = new LatLng(-1.263093,36.804523);

                    LatLng my_loc = new LatLng(map_lat,map_lon);
                    mMap.addMarker(new MarkerOptions().position(my_loc).title(locationText.getText().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(32.0f));
                }
            }
        }); */

       /** mapFragment.getMapAsync(new OnMapReadyCallback() {

                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {

                                       // if (latitude_text!=null){
                                        if (!(TextUtils.isEmpty(latitude_text.getText()))&&!(latitude_text.getText().toString().trim().length()==0)){
                                            map_lat = Double.valueOf(latitude_text.getText().toString());
                                            map_lon = Double.valueOf(longitude_text.getText().toString());
                                        }
                                    }
                                }); */

        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        grey_background = findViewById(R.id.grey_background);
        getting_location = findViewById(R.id.getting_location);

        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();

        grey_background.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                getting_location.setText("Please Wait...");
            }
        }, 500);

        // bind textviews
        locationText = (TextView) findViewById(R.id.location_text);
        activityText = (TextView) findViewById(R.id.activity_text);
        geofenceText = (TextView) findViewById(R.id.geofence_text);

        // Keep the screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Location permission not granted
        if (ContextCompat.checkSelfPermission(CurrentLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CurrentLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();
        showLast();

        add_this_location = findViewById(R.id.add_this_location);
        add_this_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                SaveLocationFragment saveLocationFragment = new SaveLocationFragment();
                saveLocationFragment.show(fm, "");
            }
        });

    }

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);

        // Create some geofences
        GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(500).build();
        smartLocation.geofencing().add(mestalla).start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }
    }

    private void showLast() {
        Location lastLocation = SmartLocation.with(this).location().getLastLocation();
        if (lastLocation != null) {
            locationText.setText(
                    String.format("[From Cache] Latitude %.6f, Longitude %.6f",
                            lastLocation.getLatitude(),
                            lastLocation.getLongitude())
            );
        }

        DetectedActivity detectedActivity = SmartLocation.with(this).activity().getLastActivity();
        if (detectedActivity != null) {
            activityText.setText(
                    String.format("[From Cache] Activity %s with %d%% confidence",
                            getNameFromType(detectedActivity),
                            detectedActivity.getConfidence())
            );


        }

        grey_background.setVisibility(View.GONE);
        progressView.stopAnimation();
        progressView.setVisibility(View.GONE);
        getting_location.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void stopLocation() {
        SmartLocation.with(this).location().stop();
        locationText.setText("Location stopped!");

        SmartLocation.with(this).activity().stop();
        activityText.setText("Activity Recognition stopped!");

        SmartLocation.with(this).geofencing().stop();
        geofenceText.setText("Geofencing stopped!");
    }

    private void showLocation(final Location location) {
        if (location != null) {
            final String text = String.format(" %.6f",
                    location.getLatitude());
            latitude_text.setText(text);

            final String text_longitude = String.format(" %.6f",
                    location.getLongitude());
            longitude_text.setText(text_longitude);

            // We are going to get the address for the current position
            SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder(text);
                        builder.append("\n\n[Location: ] ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        //locationText.setText(builder.toString());
                       // locationText.setText("Location: "+addressElements.toString());
                        locationText.setText("Location: "+addressElements);
                       // Toast.makeText(CurrentLocationActivity.this, " "+addressElements, Toast.LENGTH_LONG).show();


                    }



                }
            });
        } else {
            locationText.setText("No location Found");
        }
    }

    private void showActivity(DetectedActivity detectedActivity) {
        if (detectedActivity != null) {
            activityText.setText(
                    String.format("Activity %s with %d%% confidence",
                            getNameFromType(detectedActivity),
                            detectedActivity.getConfidence())
            );
        } else {
            activityText.setText("Null activity");
        }
    }

    private void showGeofence(Geofence geofence, int transitionType) {
        if (geofence != null) {
            geofenceText.setText("Transition " + getTransitionNameFromType(transitionType) + " for Geofence with id = " + geofence.getRequestId());
        } else {
            geofenceText.setText("Null geofence");
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {
        showActivity(detectedActivity);
    }

    @Override
    public void onGeofenceTransition(TransitionGeofence geofence) {
        showGeofence(geofence.getGeofenceModel().toGeofence(), geofence.getTransitionType());
    }

    private String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }

    private String getTransitionNameFromType(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "exit";
            default:
                return "dwell";
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //if (latitude_text!=null){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


        if (!(TextUtils.isEmpty(latitude_text.getText()))&&!(latitude_text.getText().toString().trim().length()==0)){
           map_lat = Double.valueOf(latitude_text.getText().toString());
           map_lon = Double.valueOf(longitude_text.getText().toString());

            // Add a marker in Sydney, Australia, and move the camera.
            // LatLng sydney = new LatLng(-34, 151);
            //LatLng my_loc = new LatLng(-1.263093,36.804523);

            LatLng my_loc = new LatLng(map_lat,map_lon);
            Marker marker = mMap.addMarker(new MarkerOptions().position(my_loc).title(locationText.getText().toString()));
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(26.0f));

    }

            }

        }, 10000);

    /**   @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        // LatLng sydney = new LatLng(-34, 151);
       // LatLng my_loc = new LatLng(-1.263093,36.804523);
        LatLng my_loc = new LatLng(map_lat,map_lon);
        mMap.addMarker(new MarkerOptions().position(my_loc).title(locationText.getText().toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(32.0f));

    } */


}
}