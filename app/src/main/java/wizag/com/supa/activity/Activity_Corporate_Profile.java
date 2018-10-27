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

public class Activity_Corporate_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next;
    EditText company_name, kra_pin, certificate_no, company_mobile_no, location, co_email;
    Button previous, company_next;
    JSONArray roles;
    String corporate_fname;
    String corporate_lname;
    String corporate_email;
    String corporate_phone;
    String corporate_id_no;
    String company_name_txt;
    String company_kra;
    String company_cert;
    String company_cert_file;
    String company_location;
    String company_email;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_client_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);

        corporate_fname = sp.getString("corporate_fname", null);
        corporate_lname = sp.getString("corporate_lname", null);
        corporate_email = sp.getString("corporate_email", null);
        corporate_phone = sp.getString("corporate_phone", null);
        corporate_id_no = sp.getString("corporate_id_no", null);;

        try {
            roles = new JSONArray(sp.getString("roles", null));
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
                if (role.getString("code").equalsIgnoreCase("XCOR")) {
                    available = true;

                    JSONObject company = role.getJSONObject("details").getJSONObject("company");
                    company_name_txt = company.getString("company");
                    company_kra = company.getString("kra_pin");
                    company_cert = company.getString("certificate_number");
                    company_cert_file = company.getString("certificate_file");
                    company_location = company.getString("location");
                    company_email = company.getString("email");
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!available) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Create Corporate Client account to continue");
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





        /*on changing role, eg to driver, open driver profile activity then the results will be populated */

        flipper = findViewById(R.id.flipper);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        fname = findViewById(R.id.fname);
        mobile_no = findViewById(R.id.mobile_no);
        company_name = findViewById(R.id.company_name);
        kra_pin = findViewById(R.id.kra_pin);
        certificate_no = findViewById(R.id.certificate_no);
        company_mobile_no = findViewById(R.id.company_mobile_no);
        location = findViewById(R.id.location);
        co_email = findViewById(R.id.co_email);


        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        previous = findViewById(R.id.previous);
        company_next = findViewById(R.id.company_next);
        company_next.setOnClickListener(this);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

        fname.setText(corporate_fname);
        lname.setText(corporate_lname);
        email.setText(corporate_email);
        mobile_no.setText(corporate_phone);
        id_no.setText(corporate_id_no);

        co_email.setText(company_email);
        company_name.setText(company_name_txt);
        kra_pin.setText(company_kra);
        certificate_no.setText(company_cert);
        location.setText(company_location);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.company_next:
                flipper.showNext();
                break;
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
            /*display a dialog with all roles to choose from*/
            startActivity(new Intent(getApplicationContext(), Activity_Indvidual_Client_Profile.class));


        } else if (id == R.id.add_role) {
            /*open the registration dashboard*/
            startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));

        }


        return super.onOptionsItemSelected(item);
    }
}
