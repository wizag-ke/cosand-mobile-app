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

public class Activity_Indvidual_Client_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, phone;
    Button next;
    String individual_fname, individual_lname, individual_email, individual_phone, individual_id_no;
    JSONArray roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_client_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        individual_fname = sp.getString("individual_fname", null);
        individual_lname = sp.getString("individual_lname", null);
        individual_email = sp.getString("individual_email", null);
        individual_phone = sp.getString("individual_phone", null);
        individual_id_no = sp.getString("individual_id_no", null);

        try {
            roles = new JSONArray(sp.getString("user_type", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (roles.length() < 1) {
            Toast.makeText(this, "You got no roles", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean available = false;
        for(int i = 0; i<roles.length();i++){
            try {
                JSONObject role = roles.getJSONObject(i);
                if(role.getString("code").contains("XIND")){
                    available = true;
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (!available) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Create Individual Client account to continue");
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


        flipper = findViewById(R.id.flipper);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        fname.setText(individual_fname);
        lname.setText(individual_lname);
        email.setText(individual_email);
        phone.setText(individual_phone);
        id_no.setText(individual_id_no);

        next = findViewById(R.id.next);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
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
                            Toast.makeText(Activity_Indvidual_Client_Profile.this, "No roles defined", Toast.LENGTH_SHORT).show();


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
