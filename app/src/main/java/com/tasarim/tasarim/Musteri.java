package com.tasarim.tasarim;

/**
 * Created by Kübra Özbaykus on 16.01.2018.
 */

public class Musteri {
    private String eposta, ad, soyad;
    private int girisSayisi;

    public Musteri(){

    }


    public Musteri(String eposta, String ad, String soyad, int girisSayisi){

        this.eposta=eposta;
        this.ad=ad;
        this.soyad=soyad;
        this.girisSayisi=girisSayisi;


    }



    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public int getGirisSayisi() {
        return girisSayisi;
    }

    public void setGirisSayisi(int girisSayisi) {
        this.girisSayisi = girisSayisi;
    }
}
