package wizag.com.supa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

public class PaymentActivity extends AppCompatActivity {
    private String tabTitles[] = new String[] { "All", "Paid", "Received" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPaymentPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);
    }

    class MyPaymentPagerAdapter extends FragmentPagerAdapter {

        public MyPaymentPagerAdapter(FragmentManager fm) {
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
            if (position==0) {
                return new AllPaymentsFragment();
            }

            else if (position==1){
                return new PaidFragment();
            }

            else {
                return new ReceivedFragment();
            }

            //return (position == 0) ? new all_trips_fragment() : new BoughtFragment() : new SoldFragment();
        }
    }
}
