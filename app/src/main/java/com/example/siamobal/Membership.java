package com.example.siamobal;

public class Membership {
    private String nama;
    private String harga;
    private String status;

    public Membership(String nama, String harga, String status) {
        this.nama = nama;
        this.harga = harga;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }

    public String getStatus() {
        return status;
    }
}