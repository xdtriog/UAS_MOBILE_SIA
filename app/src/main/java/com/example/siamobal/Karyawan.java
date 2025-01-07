package com.example.siamobal;

import java.io.Serializable;

public class Karyawan implements Serializable {
    private String nikKaryawan;
    private String namaKaryawan;
    private String email;
    private String password; // Menambahkan password

    public Karyawan(String nikKaryawan, String namaKaryawan, String email, String password) {
        this.nikKaryawan = nikKaryawan;
        this.namaKaryawan = namaKaryawan;
        this.email = email;
        this.password = password; // Menginisialisasi password
    }

    public String getNikKaryawan() {
        return nikKaryawan;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password; // Menambahkan getter untuk password
    }
}