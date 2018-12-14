/*
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity_Dependants extends AppCompatActivity implements View.OnClickListener {

    EditText contibutor_national_id, first_name, last_name, dependant_dob, dependant_national_id;
    Spinner spinnerGender, dependant_relationship;
    Button next_layout, dependant_photo, son_daughter_photo, id_copy_photo, btn_prev, btn_submit;
    ImageView dependant_image, son_daughter_image, id_copy_image;
    LinearLayout son_daughter, wife_husband;
    private ViewFlipper flipper;
    Bitmap photo;
    String dependant_image_string = "";
    String birth_cert_string = "";

    String RegDate = "";
    String name = "";
    private SQLiteHandler db;

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    public static final String DATA_SAVED_BROADCAST = "dependants";
    private BroadcastReceiver broadcastReceiver;
    SQLiteDatabase sqLiteDatabase;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependants);
        setTitle("Dependant Details");

        // Intent intent = getIntent();
       */
/* Bundle extras = getIntent().getExtras();

        if(extras.containsKey("id")){
            String contributor_id = extras.getString("id");
            contibutor_national_id.setText(contributor_id);
            Toast.makeText(context, "id"+contributor_id, Toast.LENGTH_SHORT).show();


        }
*//*






        registerReceiver(new DependantNetworkChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("phone");

        // Toast.makeText(this, "phone" + name, Toast.LENGTH_SHORT).show();


        Date currentTime = Calendar.getInstance().getTime();
        RegDate = currentTime.toString();

        contibutor_national_id = (EditText) findViewById(R.id.contibutor_national_id);
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        dependant_dob = (EditText) findViewById(R.id.dependant_dob);
        dependant_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropReadyFromDate();
            }
        });
        dependant_national_id = (EditText) findViewById(R.id.dependant_national_id);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        dependant_relationship = (Spinner) findViewById(R.id.dependant_relationship);

        next_layout = (Button) findViewById(R.id.next_layout);
        dependant_photo = (Button) findViewById(R.id.dependant_photo);
        son_daughter_photo = (Button) findViewById(R.id.son_daughter_photo);
        //id_copy_photo = (Button) findViewById(R.id.id_copy_photo);
        btn_prev = (Button) findViewById(R.id.btn_prev);
        next_layout = (Button) findViewById(R.id.next_layout);
        next_layout.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        dependant_image = (ImageView) findViewById(R.id.dependant_image);
        son_daughter_image = (ImageView) findViewById(R.id.son_daughter_image);
        //id_copy_image = (ImageView) findViewById(R.id.id_copy_image);

        //son_daughter = (LinearLayout) findViewById(R.id.son_daughter);
        wife_husband = (LinearLayout) findViewById(R.id.wife_husband);

        //
        // son_daughter.setVisibility(View.GONE);
        wife_husband.setVisibility(View.GONE);


        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
                setTitle("Dependant Details");


            }
        });


        dependant_relationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Son")) {
                    // son_daughter.setVisibility(View.VISIBLE);
                    wife_husband.setVisibility(View.GONE);
                } else if (selectedItem.equals("Daughter")) {
                    //   son_daughter.setVisibility(View.VISIBLE);
                    wife_husband.setVisibility(View.GONE);
                } else if (selectedItem.equals("Wife")) {
                    wife_husband.setVisibility(View.VISIBLE);

                } else if (selectedItem.equals("Husband")) {
                    wife_husband.setVisibility(View.VISIBLE);

                } else {
                    // son_daughter.setVisibility(View.GONE);
                    wife_husband.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.next_layout:

                String contibutor_national_id_txt = contibutor_national_id.getText().toString();
                String first_name_txt = first_name.getText().toString();
                String last_name_txt = last_name.getText().toString();
                String dependant_dob_txt = dependant_dob.getText().toString();

                String dependant_relationship_txt = dependant_relationship.getSelectedItem().toString();

                if (contibutor_national_id_txt.isEmpty()) {
                    Toast.makeText(this, "Enter Contributor National ID", Toast.LENGTH_SHORT).show();
                } else if (first_name_txt.isEmpty()) {
                    Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
                } else if (last_name_txt.isEmpty()) {
                    Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (dependant_dob_txt.isEmpty()) {
                    Toast.makeText(this, "Enter Date of Birth", Toast.LENGTH_SHORT).show();
                } else if (spinnerGender.getSelectedItem().toString().trim().equalsIgnoreCase("--Select Gender--")) {
                    Toast.makeText(this, "Please Select Gender",
                            Toast.LENGTH_SHORT).show();
                } else if (dependant_relationship.getSelectedItem().toString().trim().equalsIgnoreCase("--Select Dependant--")) {
                    Toast.makeText(this, "Please Select Relation with dependant",
                            Toast.LENGTH_SHORT).show();
                } else {
                    flipper.showNext();
                    setTitle("Dependant Details");
                }
                break;


            case R.id.btn_submit:

                String dependant_national_id_txt = dependant_national_id.getText().toString();

                if (dependant_image.getDrawable() == null || son_daughter_image.getDrawable()==null) {
                    Toast.makeText(getApplicationContext(), "Ensure that all Photos are taken ",
                            Toast.LENGTH_SHORT).show();
                } */
/*else if (dependant_relationship.getSelectedItem().toString().trim().equalsIgnoreCase("Wife") ||
                        dependant_relationship.getSelectedItem().toString().trim().equalsIgnoreCase("Husband") && dependant_national_id_txt.isEmpty()) {
                    Toast.makeText(this, "Please Select Relation with dependant",
                            Toast.LENGTH_SHORT).show();
                }
*//*

              */
/*  else if (dependant_national_id_txt.isEmpty()) {
                    Toast.makeText(this, "Enter Dependant National ID Number", Toast.LENGTH_SHORT).show();
                }*//*

                else {
                    /submit logic/
                    saveDependantToServer();

                }
        }

    }

    public void onSelectImageClick(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

    public void onSelectBirthCert(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 1);

    }


    public void CropReadyFromDate() {
        final Calendar mcurrentDate = Calendar.getInstance();

        mcurrentDate.set(mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH),
                mcurrentDate.get(Calendar.HOUR_OF_DAY), mcurrentDate.get(Calendar.MINUTE), 0);


        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog mDatePicker = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                */
/*      Your code   to get date and time    *//*

                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd/MMM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                dependant_dob.setText(sdf.format(mcurrentDate.getTime()));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                ImageView member = (ImageView) findViewById(R.id.dependant_image);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                member.setImageBitmap(photo);
                dependant_image_string = getStringImage(photo);

            }
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                ImageView birth_cert = (ImageView) findViewById(R.id.son_daughter_image);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                birth_cert.setImageBitmap(photo);
                birth_cert_string = getStringImage(photo);

            }
        }

    }

    private void saveDependantToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Details...");
        progressDialog.show();

        final String contibutor_national_id_txt = contibutor_national_id.getText().toString().trim();
        final String first_name_txt = first_name.getText().toString();
        final String last_name_txt = last_name.getText().toString();
        final String dependant_dob_txt = dependant_dob.getText().toString();
        final String dependant_relationship_txt = dependant_relationship.getSelectedItem().toString();
        final String dependant_national_id_txt = dependant_national_id.getText().toString();
        final String spinnerGender_txt = spinnerGender.getSelectedItem().toString();

        final String dependant_image = dependant_image_string;
        final String birth_cert = birth_cert_string;
        //final String id_copy_image = id_copy_image_string;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DEPENDANTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                //saveNameToLocalStorage(name, phone, txtId, height, weight, pressure, pressure1, glucose,  venue, clientsmoking, fecilityref, screendate, gender, alcohol, medication, yob, remarks, clientknownhtn, diabeticknown, occupation, diabeteshistory, hyperhistory, loguser, county, village,outcome, project, NAME_SYNCED_WITH_SERVER);
                                Toast.makeText(Activity_Dependants.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                //db.deleteSreeningData();
                                AddDependant();
                            } else if (obj.getBoolean("error")) {
                                Toast.makeText(Activity_Dependants.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                Toast.makeText(Activity_Dependants.this, "content", Toast.LENGTH_LONG).show();

                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveDependantLocally(contibutor_national_id_txt, first_name_txt, last_name_txt, dependant_dob_txt,
                                        dependant_relationship_txt, spinnerGender_txt, dependant_image, birth_cert,
                                        dependant_national_id_txt, RegDate, name, NAME_NOT_SYNCED_WITH_SERVER);


                            }

                            //saveToFailoverServer();
                            // AddDependant();
                            //}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced

                        saveDependantLocally(contibutor_national_id_txt, first_name_txt, last_name_txt, dependant_dob_txt,
                                dependant_relationship_txt, spinnerGender_txt, dependant_image, birth_cert,
                                dependant_national_id_txt, RegDate, name, NAME_NOT_SYNCED_WITH_SERVER);
                        //saveToFailoverServer();
                        //AddDependant();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Config.FIRSTNAME, first_name_txt);
                params.put(Config.LASTNAME, last_name_txt);
                params.put(Config.RELATIONSHIP, dependant_relationship_txt);
                params.put(Config.MEMBERPHOTO, dependant_image);
                params.put(Config.IDENTIFICATION_DOCUMENT, birth_cert);
                params.put(Config.BIRTHDATE, dependant_dob_txt);
                params.put(Config.REGDATE, RegDate);
                params.put(Config.CHV_DETAIL, name);
                params.put(Config.CONTRIBUTOR_ID, contibutor_national_id_txt);
                params.put(Config.GENDER, spinnerGender_txt);
                params.put(Config.ID_NUMBER, dependant_national_id_txt);

                return params;
            }
        };

        amref.org.mjali.nhif_registration.services.VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void nextAc() {
        startActivity(new Intent(this, Activity_Home.class));
    }


    private void saveDependantLocally(String contributorId, String fName, String lName, String dob,
                                      String gender, String relationship, String passport,
                                      String document, String dependantId, String RegDate, String ChvDetail, int status) {

        db.dependantDetails(contributorId, fName, lName, dob, gender, relationship, passport, document, dependantId, RegDate, ChvDetail, status);
        Toast.makeText(Activity_Dependants.this, "Details Successfully Saved", Toast.LENGTH_LONG).show();
        //nextAc();
        // saveToFailoverServer();
        AddDependant();
    }

    public void AddDependant() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_Dependants.this);

        // Setting Dialog Title
        alertDialog.setTitle("Add Another Dependant");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to add another dependant?");

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {


                //nextAc();
                // Write your code here to invoke YES event
                // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Activity_Dependants.class));

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                nextAc();
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();


    }
}*/
