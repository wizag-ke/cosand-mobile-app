package wizag.com.supa;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BuyActivity extends AppCompatActivity implements OnDateSetListener {
    Button make_order;
    Spinner buy_spinner, buy_spinner_quality, buy_spinner_quantity;
    LinearLayout linear_day;

    /**
     * GeoDataClient wraps our service connection to Google Play services and provides access
     * to the Google Places API for Android.
     */
    protected GeoDataClient mGeoDataClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private TextView mPlaceDetailsText;

    private TextView mPlaceDetailsAttribution;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    public static final String DATEPICKER_TAG = "datepicker";
    TextView exact_day, exact_month, year1;

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

        // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.getGeoDataClient(this, null);
        setContentView(R.layout.buy);

        buy_spinner = findViewById(R.id.buy_spinner);
        buy_spinner_quality = findViewById(R.id.buy_spinner_quality);
        buy_spinner_quantity = findViewById(R.id.buy_spinner_quantity);

        exact_day = findViewById(R.id.buy_day);
        exact_month = findViewById(R.id.buy_month);

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());


        linear_day = findViewById(R.id.linear_day);
        linear_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                // datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

        }

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteView.setAdapter(mAdapter);


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
        buy_spinner.setAdapter(spinnerArrayAdapter);

        buy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        buy_spinner_quantity.setAdapter(quantityArrayAdapter);

        buy_spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        buy_spinner_quality.setAdapter(qualityArrayAdapter);

        buy_spinner_quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        make_order = findViewById(R.id.proceed_buy);
        make_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent make_order = new Intent(BuyActivity.this, ConfirmActivity.class);
                startActivity(make_order);
            }
        });


    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String the_month;
        exact_day.setText(String.valueOf(day)+" /");
        //exact_month.setText(String.valueOf(month));
       // year1.setText(String.valueOf(year));

        switch (month){
            case 0: exact_month.setText("January"); break;
            case 1: exact_month.setText("February"); break;
            case 2: exact_month.setText("March"); break;
            case 3: exact_month.setText("April"); break;
            case 4: exact_month.setText("May"); break;
            case 5: exact_month.setText("June"); break;
            case 6: exact_month.setText("July"); break;
            case 7: exact_month.setText("August"); break;
            case 8: exact_month.setText("September"); break;
            case 9: exact_month.setText("October"); break;
            case 10: exact_month.setText("November"); break;
            case 11: exact_month.setText("December"); break;
        }

        /**  if (month == 1) {
         exact_month.setText("January");
         } */


    }

    private boolean isVibrate() {
        //return ((CheckBox) findViewById(R.id.checkBoxVibrate)).isChecked();
        return true;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
                    final AutocompletePrediction item = mAdapter.getItem(position);
                    final String placeId = item.getPlaceId();
                    final CharSequence primaryText = item.getPrimaryText(null);

                    // Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
                    Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
                    placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

                    Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                            Toast.LENGTH_SHORT).show();
                    //Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
                }
            };

    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                // Format details of the place for display and show it in a TextView.
                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));

                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPlaceDetailsAttribution.setVisibility(View.GONE);
                } else {
                    mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                    mPlaceDetailsAttribution.setText(
                            Html.fromHtml(thirdPartyAttribution.toString()));
                }

                // Log.i(TAG, "Place details received: " + place.getName());

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                //  Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        // Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
        //    websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }





}
