package wizag.com.supa.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import wizag.com.supa.R;

public class Activity_track_Driver extends AppCompatActivity {
    TextView phone, driver_name;
    Button track;
    String driver_name_txt, driver_phone_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_driver);

        phone = findViewById(R.id.phone);
        track = findViewById(R.id.track);
        driver_name = findViewById(R.id.driver_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            driver_name_txt = extras.getString("driver_name");
            driver_phone_txt = extras.getString("driver_phone");
//            Toast.makeText(this, order_id, Toast.LENGTH_SHORT).show();

            driver_name.setText(driver_name_txt);
            phone.setText(driver_phone_txt);

        }

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*open track activity*/
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Driver_Movement.class));

            }
        });

    }


}
