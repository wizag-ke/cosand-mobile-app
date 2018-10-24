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

public class Activity_Indvidual_Client_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email,phone;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_client_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String individual_fname = sp.getString("individual_fname", null);
        String individual_lname = sp.getString("individual_lname", null);
        String individual_email = sp.getString("individual_email", null);
        String individual_phone = sp.getString("phone", null);
        String individual_id_no = sp.getString("individual_id_no", null);


        flipper = findViewById(R.id.flipper);
        fname =findViewById(R.id.fname);
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
        switch (view.getId()){
            case R.id.next:
                flipper.showNext();
                break;

        }
    }
}
