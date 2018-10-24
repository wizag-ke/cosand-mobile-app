package wizag.com.supa.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import wizag.com.supa.R;

public class Activity_Corporate_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next;
    EditText company_name, kra_pin, certificate_no, company_mobile_no, location;
    Button previous, company_next;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_client_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String corporate_fname = sp.getString("corporate_fname", null);
        String corporate_lname = sp.getString("corporate_lname", null);
        String corporate_email = sp.getString("corporate_email", null);
        String corporate_phone = sp.getString("phone", null);
        String corporate_id_no = sp.getString("corporate_id_no", null);
        String company_name_txt = sp.getString("company_name", null);
        String company_kra = sp.getString("company_kra_pin", null);
        String company_cert = sp.getString("company_cert_no", null);
        String company_phone = sp.getString("company_phone", null);
        String company_location = sp.getString("company_location", null);


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
        company_name.setText(company_name_txt);
        kra_pin.setText(company_kra);
        certificate_no.setText(company_cert);
        company_mobile_no.setText(company_phone);
        location.setText(company_location);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.company_next:
                /*lie low for now*/

                break;
        }
    }
}
