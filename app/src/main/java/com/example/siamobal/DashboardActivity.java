package com.example.siamobal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Ambil nama pengguna dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "User");

        // Tampilkan nama pengguna di TextView
        TextView textViewGreeting = findViewById(R.id.textViewGreeting);
        textViewGreeting.setText("Halo, " + nama);


        // Menghubungkan tombol dengan fungsi
        Button btnPointOfSale = findViewById(R.id.btnPointOfSale);
        Button btnBeliMembership = findViewById(R.id.btnBeliMembership);
        Button btnCustomer = findViewById(R.id.btnCustomer);
        Button btnPiutang = findViewById(R.id.btnPiutang);
        Button btnJurnalUmum = findViewById(R.id.btnJurnalUmum);
        Button btnBukuBesar = findViewById(R.id.btnBukuBesar);
        Button btnKodeAkun = findViewById(R.id.btnKodeAkun);
        Button btnMetodePembayaran = findViewById(R.id.btnMetodePembayaran);
        Button btnKaryawan = findViewById(R.id.btnKaryawan);
        Button btnMembership = findViewById(R.id.btnMembership);
        Button btnBarang = findViewById(R.id.btnBarang);

        // Set OnClickListener untuk setiap tombol
//        btnPointOfSale.setOnClickListener(v -> {
//            // Pindah ke Activity Point Of Sale
//            startActivity(new Intent(DashboardActivity.this, PointOfSaleActivity.class));
//        });
//
//        btnBeliMembership.setOnClickListener(v -> {
//            // Pindah ke Activity Beli Membership
//            startActivity(new Intent(DashboardActivity.this, BeliMembershipActivity.class));
//        });
//
//        btnCustomer.setOnClickListener(v -> {
//            // Pindah ke Activity Customer
//            startActivity(new Intent(DashboardActivity.this, CustomerActivity.class));
//        });
//
//        btnPiutang.setOnClickListener(v -> {
//            // Pindah ke Activity Piutang
//            startActivity(new Intent(DashboardActivity.this, PiutangActivity.class));
//        });
//
//        btnJurnalUmum.setOnClickListener(v -> {
//            // Pindah ke Activity Jurnal Umum
//            startActivity(new Intent(DashboardActivity.this, JurnalUmumActivity.class));
//        });
//
//        btnBukuBesar.setOnClickListener(v -> {
//            // Pindah ke Activity Buku Besar
//            startActivity(new Intent(DashboardActivity.this, BukuBesarActivity.class));
//        });
//
//        btnKodeAkun.setOnClickListener(v -> {
//            // Pindah ke Activity Kode Akun
//            startActivity(new Intent(DashboardActivity.this, KodeAkunActivity.class));
//        });
//
//        btnMetodePembayaran.setOnClickListener(v -> {
//            // Pindah ke Activity Metode Pembayaran
//            startActivity(new Intent(DashboardActivity.this, MetodePembayaranActivity.class));
//        });
//
//        btnKaryawan.setOnClickListener(v -> {
//            // Pindah ke Activity Karyawan
//            startActivity(new Intent(DashboardActivity.this, KaryawanActivity.class));
//        });

        btnMembership.setOnClickListener(v -> {
            // Pindah ke Activity Membership
            startActivity(new Intent(DashboardActivity.this, MembershipActivity.class));
        });

//        btnBarang.setOnClickListener(v -> {
//            // Pindah ke Activity Barang
//            startActivity(new Intent(DashboardActivity.this, BarangActivity.class));
//        });
    }
}