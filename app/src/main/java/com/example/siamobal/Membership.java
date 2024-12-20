package com.example.siamobal;

public class Membership {
    private String id; // ID membership
    private String nama;
    private String harga;
    private String status;
    private String potongan;

    // Konstruktor untuk digunakan di EditMembershipActivity
    public Membership(String id, String nama, String harga, String status, String potongan) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.status = status;
        this.potongan = potongan;
    }

    public String getId() {
        return id;
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

    public String getPotongan() {
        return potongan;
    }
}