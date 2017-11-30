package com.meteorit.sioma;

/**
 * Created by Minami on 05/11/2017.
 */

public class Homemodel {

    int imgid;
    String judul;

    public Homemodel() {
    }

    public Homemodel(int imgid, String judul) {
        this.imgid = imgid;
        this.judul = judul;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
