package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

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
import wizag.com.supa.adapter.Adapter_Transaction;
import wizag.com.supa.adapter.Adapter_Truck_Owner;
import wizag.com.supa.adapter.Adapter_Trucks;
import wizag.com.supa.models.Model_Transaction;
import wizag.com.supa.models.Model_Truck_Owner;
import wizag.com.supa.models.Model_Trucks;
import wizag.com.supa.models.Trucks;

public class Activity_Truck_Owner_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next;
    EditText sacco_name, sacco_number, kra_pin;
    Button previous, previous_list, next_trucks;
    SessionManager session;
    ListView listView;
    RecyclerView recyclerView;
    List<Trucks> trucksList;
    Adapter_Trucks adapter_trucks;
    JSONArray roles;
    String truck_kra_pin, truck_sacco, truck_sacco_member;
    String plate_no, axle_count, make, model, tonnage;
    Adapter_Trucks adapter;
    List<Model_Trucks> trucks_add_list = new ArrayList<>();
    Button previous_trucks;
    String LoadTrucks = "http://sduka.wizag.biz/api/v1/profiles";
    JSONObject details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner_user_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Trucks((ArrayList<Model_Trucks>) trucks_add_list, this);
        recyclerView.setAdapter(adapter);

        previous_trucks = findViewById(R.id.previous_trucks);
        previous_trucks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String truck_owner_fname = sp.getString("truck_owner_fname", null);
        String driver_code = sp.getString("user_type", null);
        String truck_owner_lname = sp.getString("truck_owner_lname", null);
        String truck_owner_email = sp.getString("truck_owner_email", null);
        String truck_owner_phone = sp.getString("truck_owner_phone", null);
        String truck_owner_id_no = sp.getString("truck_owner_id_no", null);


        try {
            roles = new JSONArray(sp.getString("user_type", null));
        } catch (JSONException e) {
            e.printStackTrace();
            roles = new JSONArray();
        }
        if (roles.length() < 1) {
            Toast.makeText(this, "You got no roles", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean available = false;
        for (int i = 0; i < roles.length(); i++) {

            try {
                JSONObject role = roles.getJSONObject(i);


                if (role.getString("code").contains("XTON")) {
                    available = true;
//                    JSONObject truck = role.getJSONObject("details").getJSONObject("company");
                    details = role.getJSONObject("details");
                    JSONObject company = details.getJSONObject("company");

                    truck_sacco = company.getString("sacco");
                    truck_sacco_member = company.getString("sacco_member");
                    truck_kra_pin = company.getString("kra_pin");


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!available) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Create Truck Owner account to continue");
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


        flipper = findViewById(R.id.flipper);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        mobile_no = findViewById(R.id.mobile_no);
        sacco_name = findViewById(R.id.sacco_name);
        sacco_number = findViewById(R.id.sacco_number);
        kra_pin = findViewById(R.id.kra_pin);

        next = findViewById(R.id.next);
//        previous_list = findViewById(R.id.previous_list);
        previous = findViewById(R.id.previous);
        next_trucks = findViewById(R.id.next_trucks);
        next.setOnClickListener(this);
        next_trucks.setOnClickListener(this);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });


        fname.setText(truck_owner_fname);
        lname.setText(truck_owner_lname);
        email.setText(truck_owner_email);
        mobile_no.setText(truck_owner_phone);
        id_no.setText(truck_owner_id_no);
        sacco_number.setText(truck_sacco_member);
        sacco_name.setText(truck_sacco);
        kra_pin.setText(truck_kra_pin);

        loadTrucks();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.next_trucks:
                flipper.showNext();
                break;
        }
    }

    private void loadTrucks() {

        try {

            JSONArray trucks = details.getJSONArray("trucks");
            for (int k = 0; k < trucks.length(); k++) {
                JSONObject trucks_object = trucks.getJSONObject(k);


                model = trucks_object.getString("model");
                make = trucks_object.getString("make");
                plate_no = trucks_object.getString("plate_no");
                axle_count = trucks_object.getString("axle_count");

                JSONObject tonnage_obj = trucks_object.getJSONObject("tonnage");
                tonnage = tonnage_obj.getString("description");

                Model_Trucks trucks_model = new Model_Trucks();
                trucks_model.setModel(model);
                trucks_model.setMake(make);
                trucks_model.setAxle_count(axle_count);
                trucks_model.setPlate_no(plate_no);
                trucks_model.setTonnage_id(tonnage);

                if (trucks_add_list.contains(plate_no)) {

                } else {
                    trucks_add_list.add(trucks_model);
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();


    }

}


