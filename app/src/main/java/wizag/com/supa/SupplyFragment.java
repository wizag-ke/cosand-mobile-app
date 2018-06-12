package wizag.com.supa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SupplyFragment extends Fragment {
    PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_supply,container,false);
            pieChart = (PieChart) v.findViewById(R.id.piechart);

            entries = new ArrayList<>();

            PieEntryLabels = new ArrayList<String>();

            AddValuesToPIEENTRY();

            AddValuesToPieEntryLabels();

            pieDataSet = new PieDataSet(entries, "");

            pieData = new PieData(PieEntryLabels, pieDataSet);

            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            pieChart.setData(pieData);

            pieChart.animateY(3000);
            return v;
        }

    public void AddValuesToPIEENTRY() {

        entries.add(new BarEntry(7f, 0));
        entries.add(new BarEntry(7f, 1));
        entries.add(new BarEntry(7f, 2));
        entries.add(new BarEntry(7f, 3));
        entries.add(new BarEntry(7f, 4));
        entries.add(new BarEntry(7f, 5));
        entries.add(new BarEntry(7f, 6));

    }

    public void AddValuesToPieEntryLabels() {

        PieEntryLabels.add("Transport");
        PieEntryLabels.add("White Sand");
        PieEntryLabels.add("Black Sand");
        PieEntryLabels.add("Stones");
        PieEntryLabels.add("Ballast");
        PieEntryLabels.add("Mchele");
        PieEntryLabels.add("Quarry Dust");

    }
    }