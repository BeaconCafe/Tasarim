package com.tasarim.tasarim;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MusteriSayfasi extends AppCompatActivity {

    TextView tv_hosgeldiniz, tv_girisSayisi;
    FirebaseDatabase db;
    String eposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteri_sayfasi);

        db=FirebaseDatabase.getInstance();

        tv_hosgeldiniz=(TextView) findViewById(R.id.hosgeldiniz);
        tv_girisSayisi=(TextView)findViewById(R.id.girisSayisi);

        eposta= getIntent().getExtras().getString("email");
        isimGetir();


    }
    public void isimGetir(){

        DatabaseReference dbIsimler=db.getReference("Müşteri");
        dbIsimler.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot isimler:dataSnapshot.getChildren()){

                    String gelenEposta=isimler.getValue(Musteri.class).getEposta();

                   if(gelenEposta.equals(eposta)){
                       tv_hosgeldiniz.setText("Hoşgeldiniz, " +isimler.getValue(Musteri.class).getAd()+" "+isimler.getValue(Musteri.class).getSoyad());
                       tv_girisSayisi.setText("Giriş sayınız: "+isimler.getValue(Musteri.class).getGirisSayisi());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
