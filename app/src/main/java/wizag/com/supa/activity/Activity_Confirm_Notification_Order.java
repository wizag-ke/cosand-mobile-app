package wizag.com.supa.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import wizag.com.supa.R;
import wizag.com.supa.helper.MyApplication;

public class Activity_Confirm_Notification_Order extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Intent intent = getIntent();
        String id = intent.getStringExtra("order_id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
