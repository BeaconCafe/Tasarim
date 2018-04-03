package com.tasarim.tasarim;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnalizYas extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout yenileme_nesnesi;

    ProgressBar pb;
    FirebaseDatabase db;
    DatabaseReference dbRef;

    BarChart chart;
    float barWidth;
    float barSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analiz_yas);

        setTitle("YAŞ ANALİZİ");
        yenileme_nesnesi = (SwipeRefreshLayout)findViewById(R.id.yenileme_nesnesi);
        yenileme_nesnesi.setOnRefreshListener(this);


        db=FirebaseDatabase.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();


        pb=(ProgressBar)findViewById(R.id.progress_bar);

        setupPieChart();

    }

    public void setupPieChart(){
        barWidth = 0.7f;
        barSpace = 0f;


        chart = (BarChart)findViewById(R.id.barChart);
        chart.setDescription(null);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(true);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        ArrayList<String> yaslar=new ArrayList<String>();

        yaslar.add("15-25");yaslar.add("26-40"); yaslar.add("41-55");yaslar.add("55+");

        final int[] INCOME_COLORS = {
                Color.rgb(111 ,128, 74),
                Color.rgb(238, 221, 130	),
                Color.rgb(106 ,90, 205),
                Color.rgb(152 ,251, 152),

        };

        final ArrayList barEntries=new ArrayList();

        final DatabaseReference dbAylar=db.getReference().child("Yaş");

        dbAylar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot key:dataSnapshot.getChildren()){

                    barEntries.add(new BarEntry(i,Float.parseFloat(key.getValue().toString())));

                    i++;
                }
                pb.setVisibility(View.GONE);

                BarDataSet set1;
                set1 = new BarDataSet(barEntries, "Yaş");
                set1.setColors(INCOME_COLORS);

                BarData data = new BarData(set1);
                data.setValueFormatter(new LargeValueFormatter());
                chart.setData(data);
                chart.getBarData().setBarWidth(barWidth);
                chart.getData().setHighlightEnabled(false);
                chart.invalidate();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(yaslar));

        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

    }

    @Override
    public void onRefresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);


        yenileme_nesnesi.setRefreshing(false);
    }
}
