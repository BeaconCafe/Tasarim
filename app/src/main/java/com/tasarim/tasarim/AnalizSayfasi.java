package com.tasarim.tasarim;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

    PieChart chart;

    String aylar[]={"Aralık","Ağustos","Ekim","Eylül","Haziran",
            "Kasım","Mart","Mayıs","Nisan","Ocak","Temmuz","Şubat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analiz_sayfasi);


        db=FirebaseDatabase.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();

        chart=(PieChart)findViewById(R.id.pieChart);
        pb=(ProgressBar)findViewById(R.id.progress_bar);


        setupPieChart();

    }

    public void setupPieChart(){

        final ArrayList<PieEntry> pieEntries=new ArrayList<>();

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


        chart.setDrawHoleEnabled(false);
        chart.setNoDataText("");

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);


        final DatabaseReference dbAylar=db.getReference().child("Aylar");

        dbAylar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot key:dataSnapshot.getChildren()){
                    pieEntries.add(new PieEntry(Float.parseFloat(key.getValue().toString()),aylar[i]));
                    i++;
                }
                pb.setVisibility(View.GONE);
                chart.notifyDataSetChanged();
                PieDataSet dataSet=new PieDataSet(pieEntries,"");

                PieData data=new PieData(dataSet);
                dataSet.setSliceSpace(4);
                dataSet.setColors(INCOME_COLORS);
                dataSet.setValueTextSize(10f);
                chart.setData(data);
                chart.invalidate();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
