package wizag.com.supa.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import wizag.com.supa.R;

public class FontAwesome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_awesome);

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome_webfont.ttf");

        TextView fontAwesomeAndroidIcon = (TextView) findViewById(R.id.font_awesome_android_icon);
        TextView fontAwesomeAreaChartIcon = (TextView) findViewById(R.id.font_awesome_area_chart_icon);
        TextView fontAwesomeCubesIcon = (TextView) findViewById(R.id.font_awesome_cubes_icon);
        TextView fontAwesomeMobilePhoneIcon = (TextView) findViewById(R.id.font_awesome_mobile_phone_icon);

        fontAwesomeAndroidIcon.setTypeface(fontAwesomeFont);
        fontAwesomeAreaChartIcon.setTypeface(fontAwesomeFont);
        fontAwesomeCubesIcon.setTypeface(fontAwesomeFont);
        fontAwesomeMobilePhoneIcon.setTypeface(fontAwesomeFont);
    }
}