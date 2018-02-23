package com.tasarim.tasarim;

/**
 * Created by Kübra Özbaykus on 16.01.2018.
 */

public class Musteri {
    private String eposta, ad, soyad,admin;
    private int girisSayisi,ikramSayisi;

    public Musteri(){

    }


    public Musteri(String eposta, String ad, String soyad,String admin, int girisSayisi,int ikramSayisi){

        this.eposta=eposta;
        this.ad=ad;
        this.soyad=soyad;
        this.girisSayisi=girisSayisi;
        this.admin="1";
        this.ikramSayisi=ikramSayisi;

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

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getIkramSayisi() {
        return ikramSayisi;
    }

    public void setIkramSayisi(int ikramSayisi) {
        this.ikramSayisi = ikramSayisi;
    }
}
