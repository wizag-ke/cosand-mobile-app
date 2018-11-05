package wizag.com.supa.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import wizag.com.supa.Adapter_Existing_Places;
import wizag.com.supa.Geofencing;
import wizag.com.supa.PlaceContract;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.adapter.Custom_List_Adapter;
import wizag.com.supa.models.Model_Existing_Places;

public class Activity_Search_Places extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener {

    // Constants
    public static final String TAG = Activity_Search_Places.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String SHARED_PREF_NAME = "location";
    private static final String SHARED_PREF_CORDINATES = "cordinates";
    // Member variables
    private Adapter_Existing_Places mAdapter;
    private RecyclerView mRecyclerView;
    private boolean mIsEnabled;
    private GoogleApiClient mClient;
    private Geofencing mGeofencing;

    String name, address;
    String POST_Location_URL = "http://sduka.wizag.biz/api/user-ddp";
    String get_Location_URL = "http://sduka.wizag.biz/api/user-ddp";
    String post_Location_calc = "http://sduka.wizag.biz/api/qc";
    String token;

    ArrayList<String> LocationName;
    HashMap<String, String> location_values;
    String id_location;

    String material;
    String quality;
    String size;

    SessionManager sessionManager;
    Spinner location_spinner;
    Button next_location;

    int distance;
    String unit_measure;
    String cost_of_delivery_per_unit;

    /*setting up listview*/

    private List<Model_Existing_Places> Location_List = new ArrayList<Model_Existing_Places>();
    private ListView listView;
    private Custom_List_Adapter adapter;
    String cordinates;
    String existing_name, existing_cordinates;
    AlertDialog.Builder dialogBuilder;
    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);
        dialogBuilder = new AlertDialog.Builder(this);

        // Set up the rlistview
        listView = (ListView) findViewById(R.id.list);

//get item from order-request
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            material = extras.getString("material_id");
            quality = extras.getString("quality_id");
            size = extras.getString("size_id");


        }
        loadProducts();


//loading spinner
        //loadProducts();

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");

        location_values = new HashMap<String, String>();
        LocationName = new ArrayList<>();

        adapter = new Custom_List_Adapter(this, Location_List);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

//dial
                Model_Existing_Places data = (Model_Existing_Places) adapterView.getItemAtPosition(position);
                existing_name = data.getName();
                existing_cordinates = data.getCordinates();


                dialogBuilder.setTitle("Confirm Action");
                dialogBuilder.setMessage("Would you like to add selected location?");
                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do something with edt.getText().toString();
                        postExistingLocationCalc();

                        postExistingLocation();

                        // Toast.makeText(Activity_Wallet.this, amount_txt, Toast.LENGTH_SHORT).show();

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();


            }
        });


        Switch onOffSwitch = (Switch) findViewById(R.id.enable_switch);
        mIsEnabled = getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.setting_enabled), false);
        onOffSwitch.setChecked(mIsEnabled);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putBoolean(getString(R.string.setting_enabled), isChecked);
                mIsEnabled = isChecked;
                editor.commit();
                if (isChecked) mGeofencing.registerAllGeofences();
                else mGeofencing.unRegisterAllGeofences();
            }

        });


        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();

        mGeofencing = new Geofencing(this, mClient);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    /***
     * Called when the Google API Client is successfully connected
     *
     * @param connectionHint Bundle of data provided to clients by Google Play services
     */
    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        refreshPlacesData();
        Log.i(TAG, "API Client Connection Successful!");
    }

    /***
     * Called when the Google API Client is suspended
     *
     * @param cause cause The reason for the disconnection. Defined by constants CAUSE_*.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "API Client Connection Suspended!");
    }

    /***
     * Called when the Google API Client failed to connect to Google Play Services
     *
     * @param result A ConnectionResult that can be used for resolving the error
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "API Client Connection Failed!");
    }

    public void refreshPlacesData() {
        Uri uri = PlaceContract.PlaceEntry.CONTENT_URI;
        Cursor data = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        if (data == null || data.getCount() == 0) return;
        List<String> guids = new ArrayList<String>();
        while (data.moveToNext()) {
            guids.add(data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ID)));
        }
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                guids.toArray(new String[guids.size()]));
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                // mAdapter.swapPlaces(places);
                mGeofencing.updateGeofencesList(places);
                if (mIsEnabled) mGeofencing.registerAllGeofences();
            }
        });
    }


    public void onAddPlaceButtonClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
            return;
        }
        try {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }


    /***
     * Called when the Place Picker Activity returns back with a selected place (or after canceling)
     *
     * @param requestCode The request code passed when calling startActivityForResult
     * @param resultCode  The result code specified by the second activityfrecyc
     * @param data        The Intent that carries the result data.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
             latitude = place.getLatLng().latitude;
             longitude = place.getLatLng().longitude;

//           name =   getAddress(this, latitude, longitude);
            name = place.getName().toString();
            address = String.valueOf(latitude) + "," + String.valueOf(longitude);

            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();

            SharedPreferences sp = getSharedPreferences(SHARED_PREF_CORDINATES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("latitude", String.valueOf(latitude));
            editor.putString("longitude", String.valueOf(longitude));
            editor.apply();


            //Toast.makeText(this, "Data"+name, Toast.LENGTH_SHORT).show();


            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }

            String placeID = place.getId();
            //Toast.makeText(this, "data"+placeID, Toast.LENGTH_LONG).show();

            // Insert a new place into DB
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, placeID);
            getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI, contentValues);

            //post location data
            postLocation();

            //calculate location
            postLocationCalc();


            // Get live data information
            refreshPlacesData();
        }
    }

    public static String getAddress(Context context, double LATITUDE, double LONGITUDE) {

//Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Initialize location permissions checkbox
        CheckBox locationPermissions = (CheckBox) findViewById(R.id.location_permission_checkbox);
        locationPermissions.setChecked(true);
        if (ActivityCompat.checkSelfPermission(Activity_Search_Places.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissions.setChecked(false);
        } else {
            locationPermissions.setChecked(true);
            locationPermissions.setEnabled(false);
        }

        // Initialize ringer permissions checkbox
        CheckBox ringerPermissions = (CheckBox) findViewById(R.id.ringer_permissions_checkbox);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Check if the API supports such permission change and check if permission is granted
        ringerPermissions.setChecked(true);
        if (android.os.Build.VERSION.SDK_INT >= 24 && !nm.isNotificationPolicyAccessGranted()) {
            ringerPermissions.setChecked(false);
        } else {
            ringerPermissions.setChecked(true);
            ringerPermissions.setEnabled(false);
        }
    }

    public void onRingerPermissionsClicked(View view) {
        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivity(intent);
    }

    public void onLocationPermissionClicked(View view) {
        ActivityCompat.requestPermissions(Activity_Search_Places.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    private void postLocation() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("address", address);
        editor.apply();


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Search_Places.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_Location_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), success_message, Toast.LENGTH_SHORT).show();
                            /*startActivity(new Intent(getApplicationContext(), Activity_Quotation
                                    .class));
*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Search_Places.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Location could not be added", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("cordinates", address);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void postExistingLocation() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("address", address);
        editor.apply();


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Search_Places.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_Location_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), success_message, Toast.LENGTH_SHORT).show();
                            /*startActivity(new Intent(getApplicationContext(), Activity_Quotation
                                    .class));
*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Search_Places.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Location could not be added", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", existing_name);
                params.put("cordinates", existing_cordinates);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void postLocationCalc() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Search_Places.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, post_Location_calc,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");

                            distance = data.getInt("distance");
                            unit_measure = data.getString("unit_measure");
                            cost_of_delivery_per_unit = data.getString("cost_of_delivery_per_unit");

                            // Toast.makeText(Activity_Search_Places.this, "Data\n" + unit_measure,+cost_of_delivery_per_unit, Toast.LENGTH_SHORT).show();


                            // String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Activity_Quotation.class);
                            intent.putExtra("location", "Nairobi");
                            intent.putExtra("distance", "10");
                            intent.putExtra("unit_measure", "Tonnage");
                            intent.putExtra("cost_of_delivery_per_unit", "200");
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Search_Places.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(getApplicationContext(), Activity_Quotation.class);
                startActivity(intent);

//                Toast.makeText(getApplicationContext(), "Location could not be added", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("material_id", "1");
                params.put("location", "Nairobi");
                params.put("quality_id", "1");
                params.put("size_id", "1");

                //params.put("code", "blst786");
                //  params.put("")
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void postExistingLocationCalc() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Search_Places.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, post_Location_calc,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");

                            distance = data.getInt("distance");
                            unit_measure = data.getString("unit_measure");
                            cost_of_delivery_per_unit = data.getString("cost_of_delivery_per_unit");

                            // Toast.makeText(Activity_Search_Places.this, "Data\n" + unit_measure,+cost_of_delivery_per_unit, Toast.LENGTH_SHORT).show();


                            // String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Activity_Quotation.class);
                            intent.putExtra("location", "Nairobi");
                            intent.putExtra("distance", "10");
                            intent.putExtra("unit_measure", "Tonnage");
                            intent.putExtra("cost_of_delivery_per_unit", "200");
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Search_Places.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(getApplicationContext(), Activity_Quotation.class);

                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Location could not be added", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("material_id", material);
                params.put("location", cordinates);
                params.put("quality_id", quality);
                params.put("size_id", size);

                //params.put("code", "blst786");
                //  params.put("")
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void loadProducts() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, get_Location_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.hide();
                            if (jsonObject != null) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONArray locations = data.getJSONArray("dropoffs");

                                if (locations != null) {
                                    //traversing through all the object
                                    for (int i = 0; i < locations.length(); i++) {

                                        //getting product object from json array
                                        JSONObject product = locations.getJSONObject(i);

                                        Model_Existing_Places model_existing_places = new Model_Existing_Places();
                                        model_existing_places.setName(product.getString("name"));
                                        model_existing_places.setCordinates(product.getString("cordinates"));
                                        model_existing_places.setId(product.getInt("id"));

                                        if (!Location_List.contains(model_existing_places)) {
                                            Location_List.add(model_existing_places);
                                        }


                                    }

                                }

                            }


                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }


        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).

                add(stringRequest);

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

}


