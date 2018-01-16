package com.tasarim.tasarim;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Giris extends AppCompatActivity {

    TextView kayitOl;
    Button giris;
    EditText kullaniciAdi,sifre;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        this.setTitle("Giriş Yap");
        kayitOl=(TextView) findViewById(R.id.kayitOlSayfasi);
        giris=(Button)findViewById(R.id.giris);
        kullaniciAdi=(EditText)findViewById(R.id.kullaniciAdi);
        sifre=(EditText)findViewById(R.id.sifre);

        mAuth=FirebaseAuth.getInstance();

        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(Giris.this,KayitOl.class);
                startActivity(intent);
            }
        });

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(kullaniciAdi.getText().toString())||TextUtils.isEmpty(sifre.getText().toString()))
                {
                    Toast.makeText(Giris.this, "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    login(kullaniciAdi.getText().toString(),sifre.getText().toString());
                }
            }
        });

    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Giris.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Giris.this,MusteriSayfasi.class);
                    intent.putExtra("email",kullaniciAdi.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Giris.this, "Giriş Başarısız", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
