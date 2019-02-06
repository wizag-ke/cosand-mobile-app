package wizag.com.supa.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;

import static wizag.com.supa.activity.Activity_Driver_Register.encodeTobase64;

public class Activity_Register extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ProgressDialog progressDialog;
    EditText edit_firstname, edit_lastname, edit_id, edit_phone, email_address, create_password, confirm_password;
    private static final String SHARED_PREF_NAME = "profile";
    Button button_register, upload_image, upload_image_back;
    String RegisterUrl = "http://sduka.dnsalias.com/api/v1/auth/signup";
    private int SELECT_FILE = 1;
    private int SELECT_FILE_BACK = 2;
    private int REQUEST_CAMERA = 0;
    private int REQUEST_CAMERA_BACK = 3;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_CAMERA_PERMISSION_CODE_BACK = 200;

    LinearLayout image_layout, image_layout_back;
    String photo_path = "";
    TextView text_dummy_hint_first_name, text_dummy_hint_last_name,
            text_dummy_hint_id_passport_number, text_dummy_hint_phone_number,
            text_dummy_hint_email_address, text_dummy_hint_create_password,
            text_dummy_hint_confirm_password;
    ImageView id_image, id_image_back;
    Bitmap photo, photo_back;
    String front_id, back_id;

    String f_name, l_name, edit_id_txt, edit_phone_txt,
            email_address_txt, create_password_txt, confirm_password_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        image_layout = findViewById(R.id.image_layout);
        image_layout_back = findViewById(R.id.image_layout_back);
        id_image = findViewById(R.id.id_image);
        id_image_back = findViewById(R.id.id_image_back);
        edit_firstname = findViewById(R.id.edit_firstname);
        edit_lastname = findViewById(R.id.edit_lastname);
        edit_id = findViewById(R.id.edit_id);
        edit_phone = findViewById(R.id.edit_phone);
        email_address = findViewById(R.id.email_address);
        create_password = findViewById(R.id.create_password);
        confirm_password = findViewById(R.id.confirm_password);
        button_register = findViewById(R.id.button_register);
        upload_image = findViewById(R.id.upload_image);
        upload_image_back = findViewById(R.id.upload_image_back);
        text_dummy_hint_first_name = findViewById(R.id.text_dummy_hint_first_name);
        text_dummy_hint_last_name = findViewById(R.id.text_dummy_hint_last_name);
        text_dummy_hint_id_passport_number = findViewById(R.id.text_dummy_hint_id_passport_number);
        text_dummy_hint_phone_number = findViewById(R.id.text_dummy_hint_phone_number);
        text_dummy_hint_email_address = findViewById(R.id.text_dummy_hint_email_address);
        //handling editexts hints on focus changed
        // first name
        edit_firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            text_dummy_hint_first_name.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (edit_firstname.getText().length() > 0)
                        text_dummy_hint_first_name.setVisibility(View.VISIBLE);
                    else
                        text_dummy_hint_first_name.setVisibility(View.INVISIBLE);
                }
            }
        });

        edit_lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            text_dummy_hint_last_name.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (edit_lastname.getText().length() > 0)
                        text_dummy_hint_last_name.setVisibility(View.VISIBLE);
                    else
                        text_dummy_hint_last_name.setVisibility(View.INVISIBLE);
                }
            }
        });

        edit_id.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Show white background behind floating label
                        text_dummy_hint_id_passport_number.setVisibility(View.VISIBLE);
                    }
                }, 100);
            } else {
                // Required to show/hide white background behind floating label during focus change
                if (edit_id.getText().length() > 0)
                    text_dummy_hint_id_passport_number.setVisibility(View.VISIBLE);
                else
                    text_dummy_hint_id_passport_number.setVisibility(View.INVISIBLE);
            }
        });
        edit_phone.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Show white background behind floating label
                        text_dummy_hint_phone_number.setVisibility(View.VISIBLE);
                    }
                }, 100);
            } else {
                // Required to show/hide white background behind floating label during focus change
                if (edit_phone.getText().length() > 0)
                    text_dummy_hint_phone_number.setVisibility(View.VISIBLE);
                else
                    text_dummy_hint_phone_number.setVisibility(View.INVISIBLE);
            }
        });
        email_address.setOnFocusChangeListener((v, hasFocus) -> {

            if (hasFocus) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Show white background behind floating label
                        text_dummy_hint_email_address.setVisibility(View.VISIBLE);
                    }
                }, 100);
            } else {
                // Required to show/hide white background behind floating label during focus change
                if (email_address.getText().length() > 0)
                    text_dummy_hint_email_address.setVisibility(View.VISIBLE);
                else
                    text_dummy_hint_email_address.setVisibility(View.INVISIBLE);
            }
        });


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*store reg details on shared prefs*/
                SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                f_name = edit_firstname.getText().toString();
                l_name = edit_lastname.getText().toString();
                edit_id_txt = edit_id.getText().toString();
                edit_phone_txt = edit_phone.getText().toString();
                email_address_txt = email_address.getText().toString();
                create_password_txt = create_password.getText().toString();
                confirm_password_txt = confirm_password.getText().toString();

                editor.putString("reg_fname", f_name);
                editor.putString("reg_lname", l_name);
                editor.putString("reg_id", edit_id_txt);
                editor.putString("reg_phone", edit_phone_txt);
                editor.putString("reg_email", email_address_txt);
                editor.putString("role_id", "XIND");
                editor.apply();


                if (TextUtils.isEmpty(f_name)) {
                    edit_firstname.setError("Please enter first name");
                    edit_firstname.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(l_name)) {
                    edit_lastname.setError("Please enter last name");
                    edit_lastname.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(edit_id_txt)) {
                    edit_id.setError("Please enter ID or Passport number");
                    edit_id.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(edit_phone_txt) || !edit_phone_txt.startsWith("07") || edit_phone_txt.length() < 10) {
                    edit_phone.setError("Please enter a valid Phone number");
                    edit_phone.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address_txt).matches()) {
                    email_address.setError("Enter a valid email");
                    email_address.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(create_password_txt)) {
                    create_password.setError("Enter a password");
                    create_password.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(confirm_password_txt)) {
                    confirm_password.setError("Confirm password");
                    confirm_password.requestFocus();
                    return;
                } else if (!create_password_txt.equals(confirm_password_txt)) {
                    confirm_password.setError("Both passwords should match");
                    confirm_password.requestFocus();
                    return;
                }
                /*else if(id_image.getDrawable() == null){
                    Toast.makeText(Activity_Register.this, "", Toast.LENGTH_SHORT).show();
                }*/

                else if (!isNetworkConnected()) {

                    Toast.makeText(Activity_Register.this, "Ensure you have internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    registerUser();
                    Toast.makeText(Activity_Register.this, front_id + "\n", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Activity_Register.this, back_id + "\n", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private void registerUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //getText

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    //converting response to json object

                    JSONObject obj = new JSONObject(response);

                    String message = obj.getString("message");
                    String status = obj.getString("status");
//                            JSONObject data = new JSONObject("data");
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(Activity_Register.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Activity_Login.class));
                        finish();
                    } else if (status.equalsIgnoreCase("error")) {

                        Toast.makeText(Activity_Register.this, message, Toast.LENGTH_SHORT).show();
                    }
                    JSONArray jsonArray = obj.getJSONArray("data");
                    for (int k = 0; k < jsonArray.length(); k++) {
                        String data_message = jsonArray.getString(k);

                        if (status.equalsIgnoreCase("fail")) {
                            Toast.makeText(Activity_Register.this, data_message, Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.getMessage();
                        Toast.makeText(Activity_Register.this, "An error occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        ) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email",email_address_txt);
                params.put("id_no", edit_id_txt);
                params.put("password", create_password_txt);
                params.put("password_confirmation", confirm_password_txt);
                params.put("fname", f_name);
                params.put("lname", l_name);
                params.put("phone", edit_phone_txt);
                params.put("role_id", "XIND");
                params.put("id_file_front", front_id);
                params.put("id_file_back", back_id);
                params.put("client_type", "mobile");

//                params.put("role_id", "2");
                return params;
            }


        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    public void showOptions(View view) {


        PopupMenu popup = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.id_menu);
        popup.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.existing:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                return true;
            case R.id.camera_photo:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }

                return true;

            case R.id.existing_back:
                Intent back_intent = new Intent();
                back_intent.setType("image/*");
                back_intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(back_intent, "Select File"), SELECT_FILE_BACK);

                return true;
            case R.id.camera_photo_back:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE_BACK);
                } else {
                    Intent back_cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(back_cameraIntent, REQUEST_CAMERA_BACK);
                }

                return true;

            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.id_image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            image_layout.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(photo);
            front_id = encodeTobase64(photo);

        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            image_layout.setVisibility(View.VISIBLE);
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            front_id = encodeTobase64(photo);


        }


        if (requestCode == SELECT_FILE_BACK && resultCode == Activity.RESULT_OK) {

            photo_back = null;
            if (data != null) {
                try {

                    photo_back = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            image_layout_back.setVisibility(View.VISIBLE);
            id_image_back.setImageBitmap(photo_back);
            back_id = encodeTobase64(photo_back);

        } else if (requestCode == REQUEST_CAMERA_BACK && resultCode == Activity.RESULT_OK) {
            image_layout_back.setVisibility(View.VISIBLE);
            photo_back = (Bitmap) data.getExtras().get("data");
            id_image_back.setImageBitmap(photo_back);
            back_id = encodeTobase64(photo_back);
        }


    }


    public void showOptionsBack(View view) {

        PopupMenu popup = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.id_menu_back);
        popup.show();
    }


}
