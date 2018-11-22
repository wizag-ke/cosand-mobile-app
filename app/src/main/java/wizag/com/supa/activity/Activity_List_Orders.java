package wizag.com.supa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import wizag.com.supa.R;
import wizag.com.supa.adapter.Adapter_View_orders;
import wizag.com.supa.models.Model_Orders;

public class Activity_List_Orders extends AppCompatActivity {
    AlertDialog alertDialog = null;
    EditText otp;
    RecyclerView recyclerView;
    Adapter_View_orders adapter;
    List<Model_Orders> materials_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_View_orders(materials_list, this);
        recyclerView.setAdapter(adapter);

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
