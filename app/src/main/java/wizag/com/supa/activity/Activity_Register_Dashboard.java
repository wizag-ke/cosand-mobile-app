package wizag.com.supa.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import wizag.com.supa.R;

public class Activity_Register_Dashboard extends AppCompatActivity {
    CardView driver,truck_owner,individual_client,corporate_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dashboard);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome_webfont.ttf");



        driver = findViewById(R.id.driver);
        corporate_client = findViewById(R.id.corporate_client);
        individual_client = findViewById(R.id.individual_client);
        truck_owner = findViewById(R.id.truck_owner);
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Driver_Register.class));
            }
        });

       /* truck_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Truck_Owner.class));
            }
        });*/

        individual_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Individual_Client.class));
            }
        });


        corporate_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Corporate_Client.class));
            }
        });
    }
}
