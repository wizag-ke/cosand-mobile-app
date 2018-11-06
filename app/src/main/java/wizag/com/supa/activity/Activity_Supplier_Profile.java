package wizag.com.supa.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wizag.com.supa.R;
import wizag.com.supa.adapter.Adapter_Supplier_Items;
import wizag.com.supa.adapter.Adapter_Supplier_Items;
import wizag.com.supa.models.Model_Supplier_Profile;
import wizag.com.supa.models.Model_Trucks;

public class Activity_Supplier_Profile extends AppCompatActivity implements View.OnClickListener {
    JSONArray roles;
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next, next_supplier_company, previous, previous_materials;
    JSONObject details;
    EditText location, kra_pin, name;
    RecyclerView recyclerView;
    String name_txt, kra_pin_txt, location_txt;
    Adapter_Supplier_Items adapter;
    List<Model_Supplier_Profile> materials_list = new ArrayList<>();
    JSONObject role;
    String item, detail, class_txt, unit_txt, unit_price;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_profile);

        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String supplier_fname = sp.getString("supplier_fname", null);
        String driver_code = sp.getString("user_type", null);
        String supplier_lname = sp.getString("supplier_lname", null);
        String supplier_email = sp.getString("supplier_email", null);
        String supplier_phone = sp.getString("supplier_phone", null);
        String supplier_id_no = sp.getString("supplier_id_no", null);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Supplier_Items((ArrayList<Model_Supplier_Profile>) materials_list, this);
        recyclerView.setAdapter(adapter);


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
                role = roles.getJSONObject(i);

                if (role.getString("code").contains("XSUP")) {
                    available = true;
//                    JSONObject truck = role.getJSONObject("details").getJSONObject("company");details = role.getJSONObject("details");
                    JSONObject company = role.getJSONObject("details").getJSONObject("company");
                    name_txt = company.getString("name");
                    location_txt = company.getString("location");
                    kra_pin_txt = company.getString("kra_pin");
                    details = role.getJSONObject("details");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!available) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Create Supplier account to continue");
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
        location = findViewById(R.id.location);
        name = findViewById(R.id.name);
        kra_pin = findViewById(R.id.kra_pin);

        fname.setText(supplier_fname);
        lname.setText(supplier_lname);
        id_no.setText(supplier_id_no);
        email.setText(supplier_email);
        mobile_no.setText(supplier_phone);

        name.setText(name_txt);
        location.setText(location_txt);
        kra_pin.setText(kra_pin_txt);


        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        next_supplier_company = findViewById(R.id.next_supplier_company);
        next_supplier_company.setOnClickListener(this);
        previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        previous_materials = findViewById(R.id.previous_materials);
        previous_materials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        loadSupplierMaterials();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.next_supplier_company:
                flipper.showNext();
                break;
        }

    }


    private void loadSupplierMaterials() {

        try {


            JSONArray trucks = details.getJSONArray("materials");
            for (int k = 0; k < trucks.length(); k++) {
                JSONObject trucks_object = trucks.getJSONObject(k);


                item = trucks_object.getString("item");
                detail = trucks_object.getString("detail");
                class_txt = trucks_object.getString("class");
                unit_txt = trucks_object.getString("unit");
                unit_price = trucks_object.getString("unit_price");


                Model_Supplier_Profile supplier_model = new Model_Supplier_Profile();
                supplier_model.setMaterial_name(item);
                supplier_model.setDetails_name(detail);
                supplier_model.setClass_name(class_txt);
                supplier_model.setUnits_name(unit_txt);
                supplier_model.setCost(unit_price);

                if (materials_list.contains(detail)) {

                } else {
                    materials_list.add(supplier_model);
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();


    }

}
