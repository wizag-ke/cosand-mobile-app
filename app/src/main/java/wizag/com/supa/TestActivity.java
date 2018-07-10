package wizag.com.supa;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TestActivity extends AppCompatActivity {
    Spinner spinner, buy_spinner_size, buy_spinner_quality, buy_spinner_category;
    String URL = "http://sduka.wizag.biz/api/material";
    String POST_MATERIAL_URL = "http://sduka.wizag.biz/api/order-request";

    LinearLayout buy_layout;
    ArrayList<String> CountryName;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String token;
    Button proceed;
    GPSLocation gps;

    ArrayList<String> CategoryName;
    ArrayList<String> QualityName;
    ArrayList<String> SizeName;
    ArrayList<String> PriceName;


    HashMap<String, String> map_values;
    HashMap<String, String> quality_values;
    HashMap<String, String> size_values;
    HashMap<String, String> category_values;

    String id_material;
    String id_quality;
    String id_size;
    String id_category;

    EditText quantity;
    String location;

    ProgressDialog progressDialog;
    String quantity_txt;
    private static final String TAG = "MyFirebaseIdService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        EditText textView = (EditText) findViewById(R.id.txt);

        String token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(this, "data\n" + token, Toast.LENGTH_SHORT).show();
        textView.setText(token);

        Log.d("firebase_id", "Refreshed token: " + token);

    }

}

