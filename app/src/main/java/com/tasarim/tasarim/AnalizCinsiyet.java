package com.tasarim.tasarim;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class AnalizCinsiyet extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout yenileme_nesnesi;

    ProgressBar pb;
    FirebaseDatabase db;
    DatabaseReference dbRef;

    PieChart chart;

    String cinsiyet[]={"Erkek","Kadın"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analiz_cinsiyet);


        yenileme_nesnesi = (SwipeRefreshLayout)findViewById(R.id.yenileme_nesnesi); // nesnemizi tanıttık
        yenileme_nesnesi.setOnRefreshListener(this); // nesnenin bu Class içerinde çalışağını belirttik
        // uygulama başlar başlamaz aktif oldu bu şekilde




        db=FirebaseDatabase.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();

        chart=(PieChart)findViewById(R.id.pieChart);
        pb=(ProgressBar)findViewById(R.id.progress_bar);


        setupPieChart();

    }


    @Override
    public void onRefresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);

        Toast.makeText(AnalizCinsiyet.this, "Yenileme başarılı", Toast.LENGTH_LONG).show();
        yenileme_nesnesi.setRefreshing(false); /* nesnenin yenileme özelliği kapatıldı
         aksi halde sürekli çalışır bu kısmı işleminiz yapılsada yapılmasada kullanın çünkü işlem başarısız olsada
         hata mesajı verirsiniz ama işlem yapılana kadar olan kısımda bu kodu kullanmayın sonrası için kullanın */
    }


    public void setupPieChart(){

        final ArrayList<PieEntry> pieEntries=new ArrayList<>();

        final int[] INCOME_COLORS = {
                Color.rgb(119, 136, 153),
                Color.rgb(185 ,211, 238),

        };


        chart.setDrawHoleEnabled(false);
        chart.setNoDataText("");

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);


        final DatabaseReference dbAylar=db.getReference().child("Cinsiyet");

        dbAylar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot key:dataSnapshot.getChildren()){
                    pieEntries.add(new PieEntry(Float.parseFloat(key.getValue().toString()),cinsiyet[i]));
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
