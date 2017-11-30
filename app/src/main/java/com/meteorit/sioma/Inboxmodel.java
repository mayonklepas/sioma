package com.meteorit.sioma;

/**
 * Created by Minami on 13/11/2017.
 */

public class Inboxmodel {
    String id,tanggal,pesan;

    public Inboxmodel(String id, String tanggal, String pesan) {
        this.id = id;
        this.tanggal = tanggal;
        this.pesan = pesan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
