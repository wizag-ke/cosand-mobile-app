package wizag.com.supa;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Quotation extends AppCompatActivity {
    Button submit;
    TextView unit_cost, distance,quantity_text,number_text;
    EditText quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        unit_cost=(TextView) findViewById(R.id.unit_cost);
        distance=(TextView) findViewById(R.id.distance);
        quantity_text=(TextView) findViewById(R.id.quantity_text);
        number_text=(TextView) findViewById(R.id.number_text);
        quantity_text.setVisibility(View.GONE);
        number_text.setVisibility(View.GONE);

        quantity=(EditText) findViewById(R.id.quantity);

        submit=(Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Activity_Quotation.this, "Data" +
                        "", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
