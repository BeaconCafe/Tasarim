package com.tasarim.tasarim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KampanyaEkle extends AppCompatActivity implements View.OnClickListener{

    EditText icerik;
    Button ekle;
    Spinner spinner;
    TextView mevcutKampanya;
    private ArrayAdapter<String> dataAdapterForAylar;

    FirebaseDatabase db;

    String dbAy,dbIcerik,fAyIsmi,fIcerik;

    private String[]aylar={"Ocak","Şubat","Mart","Nisan","Mayıs","Haziran","Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampanya_ekle);

        this.setTitle("KAMPANYA EKLE");

        icerik=(EditText)findViewById(R.id.kampanyaIcerik);
        ekle=(Button)findViewById(R.id.ekle);
        spinner=(Spinner)findViewById(R.id.spinner);
        mevcutKampanya=(TextView)findViewById(R.id.mevcutKampanya);

        db=FirebaseDatabase.getInstance();

        mevcutKampanyaCek();
        setSpinner();
        ekle.setOnClickListener(this);

    }

    private void kampanyaGuncelle(final String dbAy, final String dbIcerik){


        if(dbIcerik.equals("")){
            Toast.makeText(KampanyaEkle.this, "Boş alan bırakmayınız.", Toast.LENGTH_SHORT).show();
        }
        else{
            final DatabaseReference dbRef=db.getReference("AylıkKampanya");
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dbRef.child("AyIsmi").setValue(dbAy);
                    dbRef.child("KampanyaIcerik").setValue(dbIcerik);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setSpinner(){
        dataAdapterForAylar = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aylar);
        dataAdapterForAylar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterForAylar);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dbAy=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(KampanyaEkle.this, "Boş alan bırakmayınız.", Toast.LENGTH_SHORT).show();
            }
        });
    }

  private void mevcutKampanyaCek(){
        DatabaseReference dbRef=db.getReference("AylıkKampanya");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fAyIsmi=  dataSnapshot.child("AyIsmi").getValue().toString();
                fIcerik=dataSnapshot.child("KampanyaIcerik").getValue().toString();
                mevcutKampanya.setText(fAyIsmi+": "+fIcerik);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        dbIcerik=icerik.getText().toString();
        kampanyaGuncelle(dbAy,dbIcerik);
    }
}
