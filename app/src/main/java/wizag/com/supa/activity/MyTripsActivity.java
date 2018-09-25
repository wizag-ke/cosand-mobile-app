package wizag.com.supa.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import wizag.com.supa.BoughtFragment;
import wizag.com.supa.R;
import wizag.com.supa.SoldFragment;
import wizag.com.supa.all_trips_fragment;

public class MyTripsActivity extends AppCompatActivity {
    private String tabTitles[] = new String[]{"All", "Bought", "Sold"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return (position == 0) ? "All Trips" : "Bought";
            return tabTitles[position];

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            //return
            if (position == 0) {
                return new all_trips_fragment();
            } else if (position == 1) {
                return new BoughtFragment();
            } else {
                return new SoldFragment();
            }

            //return (position == 0) ? new all_trips_fragment() : new BoughtFragment() : new SoldFragment();
        }
    }


}

