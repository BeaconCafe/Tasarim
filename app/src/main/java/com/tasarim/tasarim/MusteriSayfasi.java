package com.tasarim.tasarim;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.UUID;

public class MusteriSayfasi extends AppCompatActivity {

    TextView tv_hosgeldiniz, tv_girisSayisi;
    FirebaseDatabase db;
    String eposta;
    BeaconManager beaconManager;
    String gelenEposta;
    DatabaseReference dbRef;
    int db_girisSayisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteri_sayfasi);



        db=FirebaseDatabase.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();

        tv_hosgeldiniz=(TextView) findViewById(R.id.hosgeldiniz);
        tv_girisSayisi=(TextView)findViewById(R.id.girisSayisi);

        eposta= getIntent().getExtras().getString("email");

        isimGetir();

       beaconBagla();




    }


    public void isimGetir(){


        DatabaseReference dbIsimler=db.getReference("Müşteri");


        dbIsimler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot isimler:dataSnapshot.getChildren()){

                     gelenEposta=isimler.getValue(Musteri.class).getEposta();

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

    public void bildirimAt(String title, String message){

        Intent notifyIntent = new Intent(this, MusteriSayfasi.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,

                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)

                .setSmallIcon(android.R.drawable.ic_dialog_info)

                .setContentTitle(title)

                .setAutoCancel(true)

                .setContentIntent(pendingIntent)

                .setStyle(new Notification.BigTextStyle().bigText(message))

                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);

    }

    public void beaconBagla(){

        beaconManager=new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> list) {
                bildirimAt("hoşgeldiniz","merhaba");
                girisSayisiArttır(eposta);

            }

            @Override
            public void onExitedRegion(BeaconRegion region) {

                bildirimAt("yine bekleriz","güle güle");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion("monitored region", UUID.fromString("f3d1d52b-6eb0-fdaf-b51c-1ade24648c14"),1,9));
            }
        });
    }

    public void girisSayisiArttır(final String eposta){


        DatabaseReference dbIsimler=db.getReference("Müşteri");
        dbIsimler.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot kisiler:dataSnapshot.getChildren()){

                    gelenEposta=kisiler.getValue(Musteri.class).getEposta();

                    if(gelenEposta.equals(eposta)){

                        db_girisSayisi=kisiler.getValue(Musteri.class).getGirisSayisi();
                        db_girisSayisi++;
                        dbRef.child("Müşteri").child(kisiler.getKey().toString()).child("girisSayisi").setValue(db_girisSayisi);
                        tv_girisSayisi.setText("Giriş sayınız: "+kisiler.getValue(Musteri.class).getGirisSayisi());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        isimGetir();





    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }




}
