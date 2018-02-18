package com.tasarim.tasarim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KayitOl extends AppCompatActivity {

    EditText edt_mail, edt_sifre,edt_ad,edt_soyad;
    Button btn_kayitOl, btn_giris;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        this.setTitle("Kayıt Ol");
        edt_mail=(EditText)findViewById(R.id.email);
        edt_sifre=(EditText)findViewById(R.id.sifre);
        edt_ad=(EditText)findViewById(R.id.isim);
        edt_soyad=(EditText)findViewById(R.id.soyisim);

        btn_kayitOl=(Button)findViewById(R.id.kayitOl);
        btn_giris=(Button)findViewById(R.id.girisSayfasi);

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
    }

    public void showSoftKeyboard(View view) {
        if (edt_ad.requestFocus()| edt_soyad.requestFocus()| edt_sifre.requestFocus()|edt_mail.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.showSoftInput(edt_ad, InputMethodManager.SHOW_IMPLICIT);
            imm.showSoftInput(edt_soyad,InputMethodManager.SHOW_IMPLICIT);
            imm.showSoftInput(edt_mail,InputMethodManager.SHOW_IMPLICIT);
            imm.showSoftInput(edt_sifre,InputMethodManager.SHOW_IMPLICIT);
            //  imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
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
                    musteriEkle(edt_mail.getText().toString(), edt_ad.getText().toString(),edt_soyad.getText().toString(),0);
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

    public void musteriEkle(String eposta,String isim,String soyisim,int girisSayisi) {
        Musteri musteri = new Musteri();
        musteri.setAd(isim);
        musteri.setSoyad(soyisim);
        musteri.setEposta(eposta);
        musteri.setGirisSayisi(girisSayisi);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Müşteri");
        String uid=dbRef.push().getKey();
        dbRef.child(uid).setValue(musteri);
    }


}
