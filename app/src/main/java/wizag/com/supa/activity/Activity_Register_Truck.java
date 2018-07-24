package wizag.com.supa.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.models.Model_Tonnage;

public class Activity_Register_Truck extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageView truckImage, truck_edit_image;
    EditText truck_no;
    Spinner tonnage;
    SessionManager sessionManager;
    String token;
    String URL = "http://sduka.wizag.biz/api/tonnage";
    String truck = "http://sduka.wizag.biz/api/truck";
    ArrayList<String> CountryName;
    LinearLayout parent_layout;
    Button submit_truck;
    Bitmap myBitmap;
    Uri picUri;
    String encodedCameraImage, encodedGalleryImage;
    private int SELECT_FILE = 1;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private int REQUEST_CAMERA = 0;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    HashMap<String, String> tonnage_values;
    Bitmap photo;
    String id_tonnage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_truck);

        final Model_Tonnage modelTonnage = new Model_Tonnage();

        CountryName = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");
        truckImage = (ImageView) findViewById(R.id.truck_image);
        truck_edit_image = (ImageView) findViewById(R.id.truck_edit_image);
        truck_no = (EditText) findViewById(R.id.truck_no);
        tonnage = (Spinner) findViewById(R.id.tonnage);
        submit_truck = (Button) findViewById(R.id.submit_truck);
        tonnage_values = new HashMap<String, String>();
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);

        loadSpinnerData(URL);


        submit_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (truck_no.getText().toString().isEmpty()) {
                    truck_no.setError("Please enter plate Number");
                } else {
                    loadRequest();
                }

            }
        });

        tonnage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//               int tonnage_id = getTonnage_id(i);
                int id_txt = modelTonnage.getTonnage_id();
                Toast.makeText(getApplicationContext(), ""+id_txt, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void loadSpinnerData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.hide();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray materials = data.getJSONArray("tonnages");

                        if (materials != null) {
                            for (int i = 0; i < materials.length(); i++) {

                                JSONObject material_items = materials.getJSONObject(i);
                                Model_Tonnage model_tonnage = new Model_Tonnage();
                                model_tonnage.setDescription(material_items.getString("description"));
                                model_tonnage.setValue(material_items.getString("value"));
                                model_tonnage.setTonnage_id(material_items.getInt("id"));


                              /*  String tonnage_value = material_items.getString("value");
                                String tonnage_description = material_items.getString("description");
                                tonnage_values.put(tonnage_value, tonnage_description);
*/
                                // Toast.makeText(getApplicationContext(), ""+map_values, Toast.LENGTH_SHORT).show();

                                if (material_items != null) {
                                    if (CountryName.contains(materials.getJSONObject(i).getString("description"))) {

                                    } else {

                                        CountryName.add(materials.getJSONObject(i).getString("description"));

                                    }


                                }

                            }
                        }
                    }

                    tonnage.setAdapter(new ArrayAdapter<String>(Activity_Register_Truck.this, android.R.layout.simple_spinner_dropdown_item, CountryName));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();
                pDialog.hide();
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
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    private void loadRequest() {
        encodedCameraImage = encodeImage(photo);
        final String plate_no = truck_no.getText().toString();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Register_Truck.this);
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, truck,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");
//                            String success_message = data.getString("message");

                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), "New request created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Snackbar.make(parent_layout, "Request could not be placed", Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("plate_no", plate_no);
                params.put("avatar", encodedCameraImage);
                params.put("tonnage_id","1" );

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

    public void showMenu(View view) {

        PopupMenu popup = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.truck_menu);
        popup.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.existing:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                return true;
            case R.id.camera_photo:

                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }

                return true;
            default:
                return false;
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        truckImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        //encodedGalleryImage = encodeImage(bm);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                photo = null;
                if (data != null) {
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                truckImage.setImageBitmap(photo);
            } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                photo = (Bitmap) data.getExtras().get("data");
                truckImage.setImageBitmap(photo);

                encodedCameraImage = encodeImage(photo);


            }


        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}