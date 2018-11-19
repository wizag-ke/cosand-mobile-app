package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.iid.FirebaseInstanceId;
import com.koushikdutta.async.callback.ConnectCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizag.com.supa.GPSLocation;
import wizag.com.supa.Geofencing;
import wizag.com.supa.MySingleton;
import wizag.com.supa.PlaceContract;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.activity.Activity_Search_Places;
import wizag.com.supa.models.Model_Buy;
import wizag.com.supa.models.Model_Supplier;
import wizag.com.supa.models.Model_Truck_Owner;
import wizag.com.supa.utils.Constants;


public class Activity_Buy extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    Spinner spinner_service_id, material_item_id, material_detail_id, material_class_id, material_unit_id;
    EditText quantity_text;
    Button proceed_buy;
    AlertDialog alertDialog = null;
    String quantity;
    SessionManager sessionManager;
    JSONArray materialTypes, materials, details_array, class_array, units_array;
    ArrayList<String> Type;
    ArrayList<String> Material;
    ArrayList<String> DetailsName;
    ArrayList<String> ClassName;
    ArrayList<String> UnitsName;
    int id_service, id_material, id_detail, id_class, id_unit;
    private GoogleApiClient mClient;
    private Geofencing mGeofencing;
    public static final String TAG = Activity_Search_Places.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    String name, address;
    Double latitude, longitude;
    private boolean mIsEnabled;
    Button proceed_location, submit, previous;
    ViewFlipper flipper;
    EditText location_description;
    TextView location_name;
    List<Model_Buy> list = new ArrayList<>();
    JSONArray buy_materials;
    String OrderRequest = "http://sduka.wizag.biz/api/v1/orders/new";
    String PostToken = "http://sduka.wizag.biz/api/v1/profiles/token";
    String code;
    TextView service, material, detail, material_class, unit, quantity_confirm, location_confirm;
    String service_name, material_name, details_name, class_name, unit_name;
    String firebase_token;
    private static final String SHARED_PREF_NAME = "confirm_notification";
    LinearLayout buy_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        /*check network connectivity*/
        isNetworkConnectionAvailable();
        ChooseRole();

        firebase_token = FirebaseInstanceId.getInstance().getToken();
        postFirebaseToken();


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*get roles*/
        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String driver_code = sp.getString("user_type", null);

        try {
            JSONArray user_role = new JSONArray(driver_code);
            for (int m = 0; m < user_role.length(); m++) {

                JSONObject user_role_object = user_role.getJSONObject(m);
                code = user_role_object.getString("code");


                Toast.makeText(this, code, Toast.LENGTH_SHORT).show();


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (sp != null) {
            if (!code.contains("XIND") || code.contains("XCOR")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Create Corporate or Individual Client account to continue");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "Not now",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        }


        spinner_service_id = findViewById(R.id.spinner_service_id);
        material_item_id = findViewById(R.id.material_item_id);
        material_detail_id = findViewById(R.id.material_detail_id);
        material_class_id = findViewById(R.id.material_class_id);
        material_unit_id = findViewById(R.id.material_unit_id);

        location_description = findViewById(R.id.location_description);
        location_name = findViewById(R.id.location_name);

        flipper = findViewById(R.id.flipper);
        proceed_location = findViewById(R.id.proceed_location);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
        previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        proceed_location.setOnClickListener(this);

        Type = new ArrayList<>();
        Material = new ArrayList<>();
        DetailsName = new ArrayList<>();
        ClassName = new ArrayList<>();
        UnitsName = new ArrayList<>();

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


        getServiceType();

        quantity_text = findViewById(R.id.quantity_text);


        spinner_service_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                service_name = spinner_service_id.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), ""+value, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject dataClicked = materialTypes.getJSONObject(i);
                    id_service = dataClicked.getInt("id");

                    getMaterial();
                    Material.clear();

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        material_item_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                material_name = material_item_id.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), ""+value, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject dataClicked = materials.getJSONObject(i);
                    id_material = dataClicked.getInt("id");

                    getMaterialDetails();
                    DetailsName.clear();
                    getMaterialClasses();
                    ClassName.clear();
                    getMaterialUnits();
                    UnitsName.clear();


                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        material_detail_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                details_name = material_detail_id.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = details_array.getJSONObject(i);
                    id_detail = dataClicked.getInt("id");

//                    getMaterialClasses();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        material_class_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                class_name = material_class_id.getSelectedItem().toString();
                Log.d("empty_json", class_name);
                ClassName.clear();
                try {
                    JSONObject dataClicked = class_array.getJSONObject(i);
                    id_class = dataClicked.getInt("id");
                    Toast.makeText(getApplicationContext(), "" + id_class, Toast.LENGTH_SHORT).show();

//                    getMaterialUnits();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        material_unit_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit_name = material_unit_id.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = units_array.getJSONObject(i);
                    id_unit = dataClicked.getInt("id");


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void getServiceType() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/types", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        materialTypes = data.getJSONArray("materialTypes");

                        for (int p = 0; p < materialTypes.length(); p++) {
                            JSONObject materials_object = materialTypes.getJSONObject(p);


                            String name = materials_object.getString("name");

                            if (materialTypes != null) {

                                if (Type.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Type.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }

                        }


                    }
                    spinner_service_id.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, Type));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");

                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    /*Selecting Material type*/
    private void getMaterial() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/types/" + id_service + "/items", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        materials = data.getJSONArray("materials");

                        for (int n = 0; n < materials.length(); n++) {
                            JSONObject materials_object = materials.getJSONObject(n);

                            String material_id = materials_object.getString("id");
                            String name = materials_object.getString("name");


                            if (materials != null) {

                                if (Material.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Material.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }

                        }


                    }
                    material_item_id.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, Material));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");

                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    /*get material details*/
    private void getMaterialDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/" + id_material, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject details_obj = data.getJSONObject("materialItem");
                        details_array = details_obj.getJSONArray("materialDetails");


                        for (int s = 0; s < details_array.length(); s++) {
                            JSONObject materials_object = details_array.getJSONObject(s);
                            String detail_id = materials_object.getString("id");
                            String name = materials_object.getString("name");


                            if (details_array != null) {

                                if (DetailsName.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    DetailsName.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    material_detail_id.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, DetailsName));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");

                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    /*get material classes*/
    private void getMaterialClasses() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/" + id_material, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();

                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject classes_obj = data.getJSONObject("materialItem");
                        class_array = classes_obj.getJSONArray("materialClasses");


                        for (int m = 0; m < class_array.length(); m++) {
                            JSONObject materials_object = class_array.getJSONObject(m);
                            String classes_id = materials_object.getString("id");
                            String name = materials_object.getString("name");

//                            Toast.makeText(Activity_Buy.this, name, Toast.LENGTH_SHORT).show();


                            if (class_array != null) {

                                if (ClassName.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    ClassName.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }


                    material_class_id.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, ClassName));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");

                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    /*get material units*/
    private void getMaterialUnits() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/" + id_material, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject units_obj = data.getJSONObject("materialItem");
                        units_array = units_obj.getJSONArray("materialUnits");


                        for (int q = 0; q < units_array.length(); q++) {
                            JSONObject materials_object = units_array.getJSONObject(q);
                            String detail_id = materials_object.getString("id");
                            String name = materials_object.getString("name");

                            if (units_array != null) {

                                if (UnitsName.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    UnitsName.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    material_unit_id.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, UnitsName));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");

                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }
        };


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        refreshPlacesData();
        Log.i(TAG, "API Client Connection Successful!");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "API Client Connection Suspended!");
    }


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

            location_name.setText(name);
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


            refreshPlacesData();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.proceed_location:
                quantity = quantity_text.getText().toString();
                if (quantity.isEmpty()) {
                    Toast.makeText(this, "Enter Quantity to proceed", Toast.LENGTH_SHORT).show();
                } else {
                    flipper.showNext();
                }
                break;
            case R.id.submit:
                /*submit to db*/

                list.add(new Model_Buy(
                        id_service,
                        id_material,
                        id_unit,
                        id_class,
                        id_detail,
                        quantity));


                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    buy_materials = jsonArray.put(list.get(i).getJSONObject());
                }

//                confirmQuote();
                orderRequest();


        }
    }


    private void orderRequest() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Buy.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OrderRequest,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String message = jsonObject.getString("message");
                            JSONObject data = jsonObject.getJSONObject("data");
                            String order_id = data.getString("order_id");

                            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("order_id", order_id);
                            editor.apply();
                            Toast.makeText(Activity_Buy.this, order_id, Toast.LENGTH_SHORT).show();

//                            Toast.makeText(Activity_Buy.this, order_id, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Activity_Buy_Quotation.class);
                            intent.putExtra("order_id", order_id);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Activity_Buy.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("client_location", address);
                params.put("client_location_name", name);
                params.put("client_location_details", location_description.getText().toString());
                params.put("order_details", String.valueOf(buy_materials));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");

                String bearer = "Bearer " + accessToken;
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

    /*Service selection*/
    private void confirmQuote() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Buy.this);
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_confirm_order_registration, null);

        service = dialogView.findViewById(R.id.service);
        material = dialogView.findViewById(R.id.material);
        detail = dialogView.findViewById(R.id.detail);
        material_class = dialogView.findViewById(R.id.material_class);
        unit = dialogView.findViewById(R.id.unit);
        quantity_confirm = dialogView.findViewById(R.id.quantity);
        location_confirm = dialogView.findViewById(R.id.location);

        final Button cancel = dialogView.findViewById(R.id.cancel);
        final Button proceed = dialogView.findViewById(R.id.proceed);

//        set text

        service.setText(service_name);
        material.setText(material_name);
        detail.setText(details_name);
        material_class.setText(class_name);
        unit.setText(unit_name);

        quantity_confirm.setText(quantity);
        location_confirm.setText(name);


        builder.setView(dialogView);
        builder.setCancelable(false);

        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*call order request*/
                orderRequest();

            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    private void postFirebaseToken() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Buy.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PostToken,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.getMessage();
                Toast.makeText(Activity_Buy.this, "Error sending instance token", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", firebase_token);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String token = user.get("access_token");
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


    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void ChooseRole() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Buy.this);
        builder.setTitle("Choose Role to continue");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_select_buy_role, null);
        buy_role = dialogView.findViewById(R.id.buy_role);

        /*load roles from sharedprefs into dynamic textviews*/
        for (int i = 0; i < code.length(); i++) {
            TextView myText = new TextView(this);
            myText.setText(code);
            buy_role.addView(myText);
        }


        final Button cancel = dialogView.findViewById(R.id.cancel);
        final Button proceed = dialogView.findViewById(R.id.proceed);


        builder.setView(dialogView);
        builder.setCancelable(false);


        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*validate and start main activity*/


            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }
}

