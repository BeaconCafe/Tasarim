package com.tasarim.tasarim;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Kampanyalar extends AppCompatActivity {

    TextView tv_girisSayisi;
    Button birinci,ikinci,ucuncu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampanyalar);

        setTitle("Kampanyalarım");

        Bundle extras = getIntent().getExtras();
        String girisSayisi = extras.getString("girisSayisi");


        tv_girisSayisi=(TextView)findViewById(R.id.girisSayisi);
        birinci=(Button)findViewById(R.id.birinci);
        ikinci=(Button)findViewById(R.id.ikinci);
        ucuncu=(Button)findViewById(R.id.ucuncu);

        birinci.setVisibility(View.INVISIBLE);
        ikinci.setVisibility(View.INVISIBLE);
        ucuncu.setVisibility(View.INVISIBLE);

        tv_girisSayisi.setText("Giriş sayınız: "+girisSayisi);

        if(Integer.parseInt(girisSayisi)>=20){
            birinci.setVisibility(View.VISIBLE);
            birinci.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   alertDialogOlustur("Axy7O23 kodunu kasaya iletiniz...");
                }
            });
        }

        if(Integer.parseInt(girisSayisi)>=30){
           ikinci.setVisibility(View.VISIBLE);
           ikinci.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   alertDialogOlustur("ok5w23 kodunu kasaya iletiniz...");
               }
           });
        }
        if(Integer.parseInt(girisSayisi)>=40){
            ucuncu.setVisibility(View.VISIBLE);
            alertDialogOlustur("ed5ser6 kodunu kasaya iletiniz...");
        }

    }

    public void alertDialogOlustur(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Kampanyalar.this);
        builder.setTitle("Beacon Cafe");
        builder.setMessage(message);
        builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Tamam butonuna basılınca yapılacaklar

            }
        });


        builder.show();
    }
}
