package wizag.com.supa;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import wizag.com.supa.ModelResults.MaterialResults;
import wizag.com.supa.TopModels.TopMaterials;

public class SellActivity extends AppCompatActivity {
    TextView time, date;
    Spinner spinner_sell_material, spinner_sell_quantity, spinner_sell_quality;
    EditText linear_supplier;
    //MaterialService materialService;

    String[] material = new String[]{
            "White Sand", "Black Sand", "Rock Sand", "Transport",
            "Ballast", "Mchele", "Quarry Dust", "Machine Cut Stones"};

    String[] quality = new String[]{
            "1", "2", "3"};

    String[] quantity = new String[]{
            "11", "14", "16", "18"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);

        spinner_sell_material = findViewById(R.id.spinner_sell_material);
        spinner_sell_quantity = findViewById(R.id.spinner_sell_quantity);
        spinner_sell_quality = findViewById(R.id.spinner_sell_quality);
        linear_supplier = findViewById(R.id.linear_supplier);
        linear_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_supplier.setText(" ");
            }
        });

        final List<String> materialList = new ArrayList<>(Arrays.asList(material));
        final List<String> qualityList = new ArrayList<>(Arrays.asList(quality));
        final List<String> quantityList = new ArrayList<>(Arrays.asList(quantity));

        // Initializing Material ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, materialList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_sell_material.setAdapter(spinnerArrayAdapter);

        spinner_sell_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text

                    /** Toast.makeText
                     (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                     .show(); */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Initializing Quantity ArrayAdapter
        final ArrayAdapter<String> quantityArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, quantityList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        quantityArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_sell_quantity.setAdapter(quantityArrayAdapter);

        spinner_sell_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text

                    /** Toast.makeText
                     (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                     .show(); */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Initializing Quality ArrayAdapter
        final ArrayAdapter<String> qualityArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, qualityList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        qualityArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_sell_quality.setAdapter(qualityArrayAdapter);

        spinner_sell_quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text

                    /** Toast.makeText
                     (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                     .show(); */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     }






























    /**    spinner_sell_material = findViewById(R.id.spinner_sell_material);
        materialService = SupaDukaAPI.getClient().create(MaterialService.class);
        getMaterial();

            spinner_sell_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = parent.getItemAtPosition(position).toString();
//                requestDetailDosen(selectedName);
                Toast.makeText(SellActivity.this, " " + selectedName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    interface MaterialService {
        @GET("api/material/")
        Call<TopMaterials> getTopRatedImages();

    }

    private void getMaterial(){

        materialService.getTopRatedImages().enqueue(new Callback<TopMaterials>() {
            @Override
            public void onResponse(Call<TopMaterials> call, Response<TopMaterials> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(SellActivity.this, "onSuccess",Toast.LENGTH_LONG).show();

                    List<MaterialResults> semuadosenItems = response.body().getListPermissionsM();
                    List<String> listSpinner = new ArrayList<String>();

                    for (int i = 0; i < semuadosenItems.size(); i++){
                        listSpinner.add(semuadosenItems.get(i).getCompany());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SellActivity.this,
                            android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_sell_material.setAdapter(adapter);
                }

                else {
                    Toast.makeText(SellActivity.this, "onFailedGetResponse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TopMaterials> call, Throwable t) {
                Toast.makeText(SellActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        }); */
    }



