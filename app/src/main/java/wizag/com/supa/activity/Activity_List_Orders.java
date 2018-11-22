package wizag.com.supa.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import wizag.com.supa.R;

public class Activity_List_Orders extends AppCompatActivity {
    AlertDialog alertDialog = null;
    EditText otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
    }

    public void Arrival(View view) {
        enterOTPDialog();
    }


    private void enterOTPDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_List_Orders.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_confirm_otp, null);
        otp = dialogView.findViewById(R.id.otp);


        final Button cancel = dialogView.findViewById(R.id.cancel);
        final Button proceed = dialogView.findViewById(R.id.confirm);


        builder.setView(dialogView);
        builder.setCancelable(false);


        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*validate otp then start questionaire activity*/
                if (!otp.getText().toString().isEmpty()) {

                    startActivity(new Intent(getApplicationContext(), Activity_Questionaire.class));
                } else {
                    Toast.makeText(Activity_List_Orders.this, "Enter OTP to continue", Toast.LENGTH_SHORT).show();
                }
//
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }
}
