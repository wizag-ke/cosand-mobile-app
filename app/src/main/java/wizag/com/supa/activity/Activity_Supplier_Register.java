package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.adapter.Supplier_Adapter;
import wizag.com.supa.models.Model_Supplier;


public class Activity_Supplier_Register extends AppCompatActivity {
    Spinner material_type, material_id, material_details, material_class,
            material_units;
    Button cancel, proceed;
    FloatingActionButton fab;
    SessionManager sessionManager;
    JSONArray materialTypes, materials, details_array, units_array, class_array;

    ArrayList<String> Type;
    ArrayList<String> Material;
    ArrayList<String> DetailsName;
    ArrayList<String> UnitsName;
    ArrayList<String> ClassName;
    List<Model_Supplier> list = new ArrayList<>();
    String supplier;
    int id_service;
    int id_class;
    int id_unit;
    int id_detail;
    int id_material;
    EditText unit_cost;
    Supplier_Adapter adapter;
    RecyclerView recyclerView;
    String type_name, details_name, material_name, units_name, class_name;
    Button submit;
    EditText kra_pin, name, location;
    String kra_pin_txt, location_txt, name_txt;
    String unit_cost_txt;
    String selected_material, selected_unit, selected_class, selected_detail;
    String supplier_Driver_url = "http://sduka.wizag.biz/api/v1/profiles/roles";
    JSONArray supplier_materials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_company_details);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Type = new ArrayList<>();
        Material = new ArrayList<>();
        DetailsName = new ArrayList<>();
        UnitsName = new ArrayList<>();
        ClassName = new ArrayList<>();

        submit = findViewById(R.id.submit);
        kra_pin = findViewById(R.id.kra_pin);
        location = findViewById(R.id.location);
        name = findViewById(R.id.name);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Supplier_Adapter(list, this);
        recyclerView.setAdapter(adapter);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaterialsDialog();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kra_pin_txt = kra_pin.getText().toString();
                location_txt = location.getText().toString();
                name_txt = name.getText().toString();

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    supplier_materials = jsonArray.put(list.get(i).getJSONObject());
                }

                registerSupplier();

            }
        });

//        showMaterialsDialog();


    }

    public void showMaterialsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_add_material, null);
        dialogBuilder.setView(dialogView);

        material_type = dialogView.findViewById(R.id.material_type);
        material_id = dialogView.findViewById(R.id.material_id);
        material_details = dialogView.findViewById(R.id.material_details);
        material_class = dialogView.findViewById(R.id.material_class);
        material_units = dialogView.findViewById(R.id.material_units);
        unit_cost = dialogView.findViewById(R.id.unit_cost);


        LoadMaterialTypeSpinner();
        /*get spinner type id*/
        material_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject dataClicked = materialTypes.getJSONObject(i);
                    id_service = dataClicked.getInt("id");


                    getMaterial();
//                    Material.clear();


                    Toast.makeText(Activity_Supplier_Register.this, id_service, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        material_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                try {
                    JSONObject dataClicked = materials.getJSONObject(position);
                    id_material = dataClicked.getInt("id");

//                    Model_Supplier itemSelected = (Model_Supplier) adapterView.getItemAtPosition(position);
//                    Toast.makeText(getApplicationContext(), id_material, Toast.LENGTH_SHORT).show();

                    selected_material = material_id.getSelectedItem().toString();

                    getMaterialDetails();
//                    DetailsName.clear();

                    getMaterialClasses();
//                    ClassName.clear();

                    getMaterialUnits();
//                    UnitsName.clear();


                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        material_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    JSONObject dataClicked = details_array.getJSONObject(i);
                    id_detail = dataClicked.getInt("id");

                    selected_detail = material_details.getSelectedItem().toString();


//                    getMaterialClasses();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        material_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    JSONObject dataClicked = class_array.getJSONObject(i);
                    id_class = dataClicked.getInt("id");

                    selected_class = material_class.getSelectedItem().toString();

//                    getMaterialUnits();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        material_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    JSONObject dataClicked = units_array.getJSONObject(i);
                    id_unit = dataClicked.getInt("id");
                    selected_unit = material_units.getSelectedItem().toString();


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        cancel = dialogView.findViewById(R.id.cancel);
        proceed = dialogView.findViewById(R.id.proceed);


        dialogBuilder.setTitle("Supplier Details");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                unit_cost_txt = unit_cost.getText().toString();


                list.add(new Model_Supplier(
                        id_material,
                        id_detail,
                        id_class,
                        id_unit,
                        selected_material,
                        selected_detail,
                        selected_class,
                        selected_unit,
                        unit_cost_txt));
                adapter.notifyDataSetChanged();


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

    private void LoadMaterialTypeSpinner() {
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

                            String type_id = materials_object.getString("id");
                            type_name = materials_object.getString("name");

                            if (materialTypes != null) {

                                if (Type.contains(type_name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Type.add(type_name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }

                        }


                    }
                    material_type.setAdapter(new ArrayAdapter<String>(Activity_Supplier_Register.this, android.R.layout.simple_spinner_dropdown_item, Type));


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


                        for (int p = 0; p < details_array.length(); p++) {
                            JSONObject materials_object = details_array.getJSONObject(p);
                            details_name = materials_object.getString("name");

                            if (details_array != null) {

                                if (DetailsName.contains(details_name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    DetailsName.add(details_name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    material_details.setAdapter(new ArrayAdapter<String>(Activity_Supplier_Register.this, android.R.layout.simple_spinner_dropdown_item, DetailsName));


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

                            material_name = materials_object.getString("name");


                            if (materials != null) {

                                if (Material.contains(material_name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Material.add(material_name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                          /*  Intent intent = new Intent();
                            intent.putExtra("type_id", type_id);
                            startActivity(intent);
                            finish();*/


                        }


                    }
                    material_id.setAdapter(new ArrayAdapter<String>(Activity_Supplier_Register.this, android.R.layout.simple_spinner_dropdown_item, Material));


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
                            units_name = materials_object.getString("name");

                            if (units_array != null) {

                                if (UnitsName.contains(units_name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    UnitsName.add(units_name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    material_units.setAdapter(new ArrayAdapter<String>(Activity_Supplier_Register.this, android.R.layout.simple_spinner_dropdown_item, UnitsName));


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
                            class_name = materials_object.getString("name");


                            if (class_array != null) {

                                if (ClassName.contains(class_name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    ClassName.add(class_name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    material_class.setAdapter(new ArrayAdapter<String>(Activity_Supplier_Register.this, android.R.layout.simple_spinner_dropdown_item, ClassName));


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

    private void registerSupplier() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Supplier_Register.this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //getText


        StringRequest stringRequest = new StringRequest(Request.Method.POST, supplier_Driver_url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String message = obj.getString("message");
                    String status = obj.getString("status");
//                            JSONObject data = new JSONObject("data");
                    if (status.equalsIgnoreCase("success")) {

                        /*message for verification*/


                        Toast.makeText(Activity_Supplier_Register.this, message, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                        finish();
                    } else if (status.equalsIgnoreCase("error")) {

                        Toast.makeText(Activity_Supplier_Register.this, message, Toast.LENGTH_LONG).show();


                    }

                    JSONArray jsonArray = obj.getJSONArray("data");
                    if (jsonArray != null) {
                        for (int k = 0; k < jsonArray.length(); k++) {
                            String data_message = jsonArray.getString(k);

                            if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(Activity_Supplier_Register.this, data_message, Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Supplier_Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Activity_Supplier_Register.this, "An error occurred" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


        ) {

            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name_txt);
//                params.put("kra_pin", kra_pin_txt);
                params.put("location", location_txt);
                params.put("materials", String.valueOf(supplier_materials));
                params.put("role_id", "XSUP");
//                params.put("licence_file", "adwerty");
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


        queue.add(stringRequest);

    }


}
