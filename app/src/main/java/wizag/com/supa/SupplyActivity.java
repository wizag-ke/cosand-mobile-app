package wizag.com.supa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SupplyActivity extends AppCompatActivity {

    PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);

        pieChart = (PieChart) findViewById(R.id.piechart);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();

        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);

        pieChart.animateY(3000);

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
