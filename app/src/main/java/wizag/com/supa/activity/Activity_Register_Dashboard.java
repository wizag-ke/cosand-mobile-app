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
    CardView driver;
TextView txt_driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dashboard);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome_webfont.ttf");



        driver = findViewById(R.id.driver);
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Driver_Register.class));
            }
        });
    }
}
