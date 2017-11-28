package com.tasarim.tasarim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Giris extends AppCompatActivity {

    TextView kayitOl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        kayitOl=(TextView) findViewById(R.id.kayitOlSayfasi);

        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(Giris.this,KayitOl.class);
                startActivity(intent);
            }
        });
    }
}
