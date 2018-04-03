package com.tasarim.tasarim;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;



public class MusteriSayfasi extends AppCompatActivity {

    TextView tv_hosgeldiniz, tv_girisSayisi,tv,tv_ikramSayisi;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    String eposta;
    BeaconManager beaconManager;
    String gelenEposta;
    DatabaseReference dbRef;
    int db_girisSayisi,db_ikramSayisi;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArcProgress arcProgress;
    int progressStatus=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteri_sayfasi);

        getSupportActionBar().setElevation(0);

        setTitle("BEACON CAFE");

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        editor.putString("rol","müşteri");
        editor.commit();

        db=FirebaseDatabase.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        arcProgress=(ArcProgress)findViewById(R.id.pb);
        tv_hosgeldiniz=(TextView) findViewById(R.id.hosgeldiniz);
        tv_girisSayisi=(TextView)findViewById(R.id.girisSayisi);
        tv=(TextView)findViewById(R.id.tv);
        tv_ikramSayisi=(TextView)findViewById(R.id.ikramSayisi);

        eposta=preferences.getString("eposta","");

        isimGetir();
        beaconBagla();




    }

    public void MenüGöster(View v){
        Intent ıntent=new Intent(this,ImagesActivity.class);
        this.startActivity(ıntent);
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

                       db_girisSayisi=isimler.getValue(Musteri.class).getGirisSayisi();
                       tv_girisSayisi.setText(Integer.toString(db_girisSayisi));

                       db_ikramSayisi=isimler.getValue(Musteri.class).getIkramSayisi();
                       tv_ikramSayisi.setText(Integer.toString(db_ikramSayisi));



                       arcProgress.setProgress(db_girisSayisi*10);
                       tv.setText((db_girisSayisi-(db_ikramSayisi*10))+"/10");


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
                bildirimAt("Hoşgeldiniz","merhaba");
                girisSayisiArttır(eposta);
                ayVerisiArttir();
                cinsiyetVerisiArttir(eposta);
                yasArttir(eposta);
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

                        db_girisSayisi=kisiler.getValue(Musteri.class).getGirisSayisi(); progressStatus=db_girisSayisi;

                        db_girisSayisi++;
                        dbRef.child("Müşteri").child(kisiler.getKey().toString()).child("girisSayisi").setValue(db_girisSayisi);
                        tv_girisSayisi.setText(Integer.toString(kisiler.getValue(Musteri.class).getGirisSayisi()));


                        if(db_girisSayisi%10==0){
                            db_ikramSayisi=kisiler.getValue(Musteri.class).getIkramSayisi();
                            db_ikramSayisi++;
                            dbRef.child("Müşteri").child(kisiler.getKey().toString()).child("ikramSayisi").setValue(db_ikramSayisi);
                            tv_ikramSayisi.setText(Integer.toString(kisiler.getValue(Musteri.class).getIkramSayisi()));

                        }
                        arcProgress.setProgress(db_girisSayisi*10);
                        tv.setText((db_girisSayisi-(db_ikramSayisi*10))+"/10");



                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    int sayi=-1;

    public void ayVerisiArttir(){

        Calendar takvim = Calendar.getInstance(TimeZone.getDefault());
        final int ay = takvim.get(Calendar.MONTH);

        DatabaseReference dbAylar=db.getReference("Aylar");

        dbAylar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(ay==0){
                    sayi=  Integer.parseInt(dataSnapshot.child("a_Ocak").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("a_Ocak").setValue(sayi);

                }
                if(ay==1){
                    sayi=  Integer.parseInt(dataSnapshot.child("b_Şubat").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("b_Şubat").setValue(sayi);

                }if(ay==2){
                    sayi=  Integer.parseInt(dataSnapshot.child("c_Mart").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("c_Mart").setValue(sayi);

                }if(ay==3){
                    sayi=  Integer.parseInt(dataSnapshot.child("d_Nisan").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("d_Nisan").setValue(sayi);

                }if(ay==4){
                    sayi=  Integer.parseInt(dataSnapshot.child("e_Mayıs").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("e_Mayıs").setValue(sayi);

                }if(ay==5){
                    sayi=  Integer.parseInt(dataSnapshot.child("f_Haziran").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("f_Haziran").setValue(sayi);

                }if(ay==6){
                    sayi=  Integer.parseInt(dataSnapshot.child("g_Temmuz").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("g_Temmuz").setValue(sayi);

                }if(ay==7){
                    sayi=  Integer.parseInt(dataSnapshot.child("h_Ağustos").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("h_Ağustos").setValue(sayi);

                }if(ay==8){
                    sayi=  Integer.parseInt(dataSnapshot.child("i_Eylül").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("i_Eylül").setValue(sayi);

                }if(ay==9){
                    sayi=  Integer.parseInt(dataSnapshot.child("j_Ekim").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("j_Ekim").setValue(sayi);

                }if(ay==10){
                    sayi=  Integer.parseInt(dataSnapshot.child("k_Kasım").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("k_Kasım").setValue(sayi);

                }
                if(ay==11){
                    sayi=  Integer.parseInt(dataSnapshot.child("z_Aralık").getValue().toString());  sayi++;
                    dbRef.child("Aylar").child("z_Aralık").setValue(sayi);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    int kadinSayisi,erkekSayisi;

    public void cinsiyetVerisiArttir(final String eposta){

        final DatabaseReference dbCinsiyet=db.getReference("Cinsiyet");
        DatabaseReference dbMusteriler=db.getReference("Müşteri");
        dbMusteriler.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot musteriler:dataSnapshot.getChildren()){
                    gelenEposta=musteriler.getValue(Musteri.class).getEposta();
                    if(gelenEposta.equals(eposta)){

                        String kisininCinsiyeti=musteriler.getValue(Musteri.class).getCinsiyet().toString();
                        if(kisininCinsiyeti.equals("Kadın")){
                            dbCinsiyet.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    kadinSayisi=Integer.parseInt(dataSnapshot.child("Kadın").getValue().toString());
                                    kadinSayisi++;
                                    dbCinsiyet.child("Kadın").setValue(kadinSayisi);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            dbCinsiyet.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    erkekSayisi=Integer.parseInt(dataSnapshot.child("Erkek").getValue().toString());
                                    erkekSayisi++;
                                    dbCinsiyet.child("Erkek").setValue(erkekSayisi);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    int birinciGrup,ikinciGrup,ucuncuGrup,dorduncuGrup;
    public void yasArttir(final String eposta){
        final DatabaseReference dbYas=db.getReference("Yaş");
        DatabaseReference dbMusteriler=db.getReference("Müşteri");
        dbMusteriler.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot musteriler:dataSnapshot.getChildren()){
                    gelenEposta=musteriler.getValue(Musteri.class).getEposta();
                    if(gelenEposta.equals(eposta)){
                        int kisininYasi=musteriler.getValue(Musteri.class).getYas();
                        if(kisininYasi>=15&&kisininYasi<=25){
                            dbYas.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    birinciGrup=Integer.parseInt(dataSnapshot.child("15-25").getValue().toString());
                                    birinciGrup++;
                                    dbYas.child("15-25").setValue(birinciGrup);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                        else if(kisininYasi>=26&& kisininYasi<=40){
                            dbYas.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ikinciGrup=Integer.parseInt(dataSnapshot.child("26-40").getValue().toString());
                                    ikinciGrup++;
                                    dbYas.child("26-40").setValue(ikinciGrup);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else if(kisininYasi>=41&& kisininYasi<=55){
                            dbYas.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ucuncuGrup=Integer.parseInt(dataSnapshot.child("41-55").getValue().toString());
                                    ucuncuGrup++;
                                    dbYas.child("41-55").setValue( ucuncuGrup);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        else if(kisininYasi>=56){
                            dbYas.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dorduncuGrup=Integer.parseInt(dataSnapshot.child("56+").getValue().toString());
                                    dorduncuGrup++;
                                    dbYas.child("56+").setValue(dorduncuGrup);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_exit:
               editor.putBoolean("login",false);
                editor.commit();
                mAuth.signOut();
                Intent i=new Intent(getApplicationContext(),Giris.class);
                startActivity(i);
                finish();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
