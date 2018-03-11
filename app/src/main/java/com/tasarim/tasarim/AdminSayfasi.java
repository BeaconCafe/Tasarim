package com.tasarim.tasarim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.BatchUpdateException;

public class AdminSayfasi extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    Button btn_analiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sayfasi);

        mAuth=FirebaseAuth.getInstance();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        editor.putString("rol","admin");
        editor.commit();

        btn_analiz=(Button)findViewById(R.id.analizSayfasinaGit);


        btn_analiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(AdminSayfasi.this,AnalizSayfasi.class);
                startActivity(intent);
            }
        });

    }

    public void MenüyeSayfaEkle(View v){
        Intent ıntent=new Intent(this,AdminMenu.class);
        this.startActivity(ıntent);
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
