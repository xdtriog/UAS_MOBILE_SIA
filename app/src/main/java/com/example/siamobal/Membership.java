package com.example.siamobal; // Ganti dengan nama paket Anda

public class Membership {
    private String nama;
    private String status;

    public Membership(String nama, String status) {
        this.nama = nama;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public String getStatus() {
        return status;
    }
}