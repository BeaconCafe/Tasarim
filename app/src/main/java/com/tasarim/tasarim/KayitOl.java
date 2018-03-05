package com.tasarim.tasarim;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class KayitOl extends AppCompatActivity {

    EditText edt_mail, edt_sifre,edt_ad,edt_soyad,edt_dogumTarihi;
    RadioGroup radioGroup;
    RadioButton radioKadin,radioErkek;
    ImageButton dateTime;
    Button btn_kayitOl, btn_giris;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context context=this;
    String secilenCinsiyet;

    int yas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        getWindow().setBackgroundDrawableResource(R.drawable.kayitsayfasi) ;
        this.setTitle("Kayıt Ol");

        edt_mail=(EditText)findViewById(R.id.email);
        edt_sifre=(EditText)findViewById(R.id.sifre);
        edt_ad=(EditText)findViewById(R.id.isim);
        edt_soyad=(EditText)findViewById(R.id.soyisim);
        edt_dogumTarihi=(EditText)findViewById(R.id.dogumTarihi);

        dateTime=(ImageButton)findViewById(R.id.datetime);

        btn_kayitOl=(Button)findViewById(R.id.kayitOl);
        btn_giris=(Button)findViewById(R.id.girisSayfasi);


        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioKadin=(RadioButton)findViewById(R.id.radio_kadin);
        radioErkek=(RadioButton)findViewById(R.id.radio_erkek);

        mAuth=FirebaseAuth.getInstance();

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();


        btn_kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edt_mail.getText().toString())||TextUtils.isEmpty(edt_sifre.getText().toString()))
                {
                    Toast.makeText(KayitOl.this, "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    create(edt_mail.getText().toString(),edt_sifre.getText().toString());


                }


            }

        });

        btn_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(KayitOl.this,Giris.class);
                startActivity(intent);
            }
        });

        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,android.R.style.Theme_DeviceDefault_Light_Dialog,new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                month += 1;

                                edt_dogumTarihi.setText(dayOfMonth + "/" + month + "/" + year);

                                yas= 2018-year;
                            }
                        }, yil, ay, gun);



                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });

    }




    public void klavyekapat(View v) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(edt_mail.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(edt_ad.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(edt_soyad.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(edt_sifre.getWindowToken(), 0);
    }

    public void create(String email, String sifre){

        mAuth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(edt_sifre.getText().toString().length()<6){
                    Toast.makeText(KayitOl.this, "Şifre en az 6 karakterden oluşmalıdır.", Toast.LENGTH_SHORT).show();
                }
                if(task.isSuccessful()){
                    Toast.makeText(KayitOl.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();

                    FirebaseUser currentMusteri=FirebaseAuth.getInstance().getCurrentUser();
                    String id=currentMusteri.getUid();


                    int secilenRadio=radioGroup.getCheckedRadioButtonId();
                    switch (secilenRadio){
                        case R.id.radio_kadin:
                            secilenCinsiyet=radioKadin.getText().toString();
                            break;
                        case R.id.radio_erkek:
                            secilenCinsiyet=radioErkek.getText().toString();
                            break;
                            default:
                                break;
                    }



                    musteriEkle(id,edt_mail.getText().toString(), edt_ad.getText().toString(),edt_soyad.getText().toString(),0,0,secilenCinsiyet,yas);
                    editor.putBoolean("login",true);
                    editor.commit();
                }
                else
                {
                    Toast.makeText(KayitOl.this, "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void musteriEkle(String id,String eposta,String isim,String soyisim,int girisSayisi,int ikramSayisi,String cinsiyet,int yas) {
        Musteri musteri = new Musteri();
        musteri.setAd(isim);
        musteri.setSoyad(soyisim);
        musteri.setEposta(eposta);
        musteri.setGirisSayisi(girisSayisi);
        musteri.setAdmin("0");
        musteri.setIkramSayisi(ikramSayisi);
        musteri.setCinsiyet(cinsiyet);
        musteri.setYas(yas);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Müşteri");
       // String uid=dbRef.push().getKey();
        dbRef.child(id).setValue(musteri);
    }


}
