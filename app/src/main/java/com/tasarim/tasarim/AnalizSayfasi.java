package com.tasarim.tasarim;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnalizSayfasi extends AppCompatActivity {

    ProgressBar pb;
    FirebaseDatabase db;
    DatabaseReference dbRef;

    BarChart chart;

    ArrayList<String> aylar=new ArrayList<String>();

//    String aylar[]={"Aralık","Ağustos","Ekim","Eylül","Haziran",
//            "Kasım","Mart","Mayıs","Nisan","Ocak","Temmuz","Şubat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analiz_sayfasi);


        db=FirebaseDatabase.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();

        chart=(BarChart)findViewById(R.id.barChart);
        pb=(ProgressBar)findViewById(R.id.progress_bar);


        aylar.add("Ocak");aylar.add("Şubat"); aylar.add("Mart");aylar.add("Nisan");aylar.add("Mayıs");
        aylar.add("Haziran");aylar.add("Temmuz");aylar.add("Ağustos");aylar.add("Eylül");aylar.add("Ekim");
        aylar.add("Kasım");aylar.add("Aralık");



        setupPieChart();

    }

    public void setupPieChart(){

        final ArrayList<BarEntry> barEntries=new ArrayList<>();

        final int[] INCOME_COLORS = {
                Color.rgb(119, 136, 153),
                Color.rgb(185 ,211, 238),
                Color.rgb(74 ,128, 77),
                Color.rgb(111 ,128, 74),
                Color.rgb(139 ,101, 139),
                Color.rgb(160 ,82 ,45),
                Color.rgb(139 ,137, 137),
                Color.rgb(238, 221, 130	),
                Color.rgb(0 ,255 ,127),
                Color.rgb(106 ,90, 205),
                Color.rgb(106, 150, 31),
                Color.rgb(152 ,251, 152),
        };



        chart.setNoDataText("");

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);


        final DatabaseReference dbAylar=db.getReference().child("Aylar");

        dbAylar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot key:dataSnapshot.getChildren()){
                    if(!key.getValue().toString().equals("0")){
                        barEntries.add(new BarEntry(Float.parseFloat(key.getValue().toString()),i));
                    }

                    i++;
                }
                pb.setVisibility(View.GONE);

                chart.notifyDataSetChanged();
                BarDataSet dataSet=new BarDataSet(barEntries,"Aylar");

                chart.setDescription("");
                BarData data=new BarData(aylar,dataSet);
                dataSet.setColors(INCOME_COLORS);
                chart.setData(data);
                chart.invalidate();
                dataSet.setValueTextSize(5);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);




    }
}
