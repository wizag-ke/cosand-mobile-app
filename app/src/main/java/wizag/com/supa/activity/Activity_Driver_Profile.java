package wizag.com.supa.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wizag.com.supa.R;

public class Activity_Driver_Profile extends AppCompatActivity implements View.OnClickListener {
    EditText phone, fname, lname, id_no, email;
    ViewFlipper flipper;
    EditText plate_no, logbook, tonnage, make, model, year;
    Button next, previous, update;

    String driver_fname, driver_lname, driver_email, driver_phone, driver_id_no, driver_plate_no, driver_description, driver_log_book, driver_make, driver_model, driver_year;
    JSONArray roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);

        driver_fname = sp.getString("driver_fname", null);
        driver_lname = sp.getString("driver_lname", null);
        driver_email = sp.getString("driver_email", null);
        driver_phone = sp.getString("driver_phone", null);
        driver_id_no = sp.getString("driver_id_no", null);
        driver_plate_no = sp.getString("driver_plate_no", null);
        driver_description = sp.getString("driver_description", null);
        driver_log_book = sp.getString("driver_logbook", null);
        driver_make = sp.getString("driver_make", null);
        driver_model = sp.getString("driver_model", null);
        driver_year = sp.getString("driver_year", null);

        try {
            roles = new JSONArray(sp.getString("driver_roles", null));
        } catch (JSONException e) {
            e.printStackTrace();
            roles = new JSONArray();
        }
        if (roles.length() < 1) {
            Toast.makeText(this, "You got no roles", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean available  = false;
        for(int i=0; i<roles.length();i++){

            try {
                JSONObject role = roles.getJSONObject(i);
                if(role.getString("code").equalsIgnoreCase("XDRI")){
                    available = true;
                    JSONObject truck = role.getJSONObject("details").getJSONObject("truck");
                    driver_plate_no = truck.getString("plate_no");
                     driver_log_book = truck.getString("log_book");
                     driver_make = truck.getString("make");
                     driver_model = truck.getString("model");
                     driver_year = truck.getString("year");

                    JSONObject tonnage = role.getJSONObject("details").getJSONObject("truck").getJSONObject("tonnage");
                    driver_description = tonnage.getString("description");



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!available) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Create Driver account to continue");
            builder1.setCancelable(true);

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





        /*initialize views*/
        phone = findViewById(R.id.phone);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        plate_no = findViewById(R.id.plate_no);
        logbook = findViewById(R.id.logbook);
        tonnage = findViewById(R.id.tonnage);
        model = findViewById(R.id.model);
        make = findViewById(R.id.make);
        year = findViewById(R.id.year);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        update = findViewById(R.id.update);
        flipper = findViewById(R.id.flipper);

        next.setOnClickListener(this);
        update.setOnClickListener(this);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

        phone.setText(driver_phone);
        fname.setText(driver_fname);
        lname.setText(driver_lname);
        id_no.setText(driver_id_no);
        email.setText(driver_email);

        plate_no.setText(driver_plate_no);
        logbook.setText(driver_log_book);
        tonnage.setText(driver_description);
        model.setText(driver_model);
        make.setText(driver_make);
        year.setText(driver_year);

        flipper = findViewById(R.id.flipper);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;


            case R.id.update:
//                edit profile

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.roles_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switch_role) {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an Account");

// add a list
            String[] codes = {"Corporate Client", "Truck Owner", "Individual Client", "Supplier"};
            builder.setItems(codes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            startActivity(new Intent(getApplicationContext(), Activity_Corporate_Profile.class));
                            break;

                        case 1:
                            startActivity(new Intent(getApplicationContext(), Activity_Truck_Owner_Profile.class));
                            break;

                        case 2:
                            startActivity(new Intent(getApplicationContext(), Activity_Indvidual_Client_Profile.class));
                            break;

                        case 3:
                            startActivity(new Intent(getApplicationContext(), Activity_Supplier_Profile.class));
                            break;

                        default:
                            Toast.makeText(Activity_Driver_Profile.this, "No roles defined", Toast.LENGTH_SHORT).show();


                    }

                }


            });

            // create and show the alert dialog
            AlertDialog dialog_opt = builder.create();
            dialog_opt.show();
        } else if (id == R.id.add_role) {
            /*open the registration dashboard*/
            startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));

        }


        return super.onOptionsItemSelected(item);
    }
}