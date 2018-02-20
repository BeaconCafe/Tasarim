package com.tasarim.tasarim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminSayfasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sayfasi);
    }

    public void MenüyeSayfaEkle(View v){
        Intent ıntent=new Intent(this,AdminMenu.class);
        this.startActivity(ıntent);
    }

}
