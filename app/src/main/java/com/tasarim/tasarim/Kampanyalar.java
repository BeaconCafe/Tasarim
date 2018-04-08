package com.tasarim.tasarim;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Kampanyalar extends AppCompatActivity {


    ViewFlipper v_flipper;
    TextView tv_girisSayisi;
    Button birinci,ikinci,ucuncu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampanyalar);

        setTitle("Kampanyalarım");


        int images[] = {R.drawable.waffle1 , R.drawable.pasta1 , R.drawable.sufle1};

        v_flipper = findViewById(R.id.v_flipper);

        for(int image: images){
            flipperImages(image);
        }


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

    public void flipperImages(int image){

        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);

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
