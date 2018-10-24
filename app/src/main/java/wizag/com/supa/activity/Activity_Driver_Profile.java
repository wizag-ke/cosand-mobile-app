package wizag.com.supa.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import wizag.com.supa.R;

public class Activity_Driver_Profile extends AppCompatActivity implements View.OnClickListener {
    EditText phone, fname, lname, id_no, email;
    ViewFlipper flipper;
    EditText plate_no, logbook, tonnage, make, model, year;
    Button next, previous, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String driver_code = sp.getString("driver_code", null);
        String driver_fname = sp.getString("driver_fname", null);
        String driver_lname = sp.getString("driver_lname", null);
        String driver_email = sp.getString("driver_email", null);
        String driver_phone = sp.getString("driver_phone", null);
        String driver_id_no = sp.getString("driver_id_no", null);
        String driver_plate_no = sp.getString("driver_plate_no", null);
        String driver_description = sp.getString("driver_description", null);
        String driver_log_book = sp.getString("driver_logbook", null);
        String driver_make = sp.getString("driver_make", null);
        String driver_model = sp.getString("driver_model", null);
        String driver_year = sp.getString("driver_year", null);
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
}
