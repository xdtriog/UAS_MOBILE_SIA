package com.example.siamobal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditMembershipActivity extends AppCompatActivity {
    private EditText editTextIdMembership, editTextNamaMembership, editTextHarga, editTextPotongan;
    private Switch switchStatus;
    private Button buttonSimpan;
    private String membershipId; // ID membership yang akan diedit
    private static final String URL_UPDATE_MEMBERSHIP = "https://kevindinata.my.id/SIALAN/update_membership.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_membership);

        editTextIdMembership = findViewById(R.id.editTextIdMembership);
        editTextNamaMembership = findViewById(R.id.editTextNamaMembership);
        editTextHarga = findViewById(R.id.editTextHarga);
        editTextPotongan = findViewById(R.id.editTextPotongan);
        switchStatus = findViewById(R.id.switchStatus);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        // Ambil data dari Intent
        Intent intent = getIntent();
        membershipId = intent.getStringExtra("MEMBERSHIP_ID");
        String nama = intent.getStringExtra("NAMA");
        String harga = intent.getStringExtra("HARGA");
        String potongan = intent.getStringExtra("POTONGAN");
        int status = intent.getIntExtra("STATUS", 1); // Default aktif (1)

        // Set data ke EditText
        editTextIdMembership.setText(membershipId);
        editTextIdMembership.setEnabled(false); // Membuat ID Membership tidak dapat diubah
        editTextNamaMembership.setText(nama);

        // Menghapus simbol "Rp" dan hanya menampilkan angka
        if (harga != null) {
            harga = harga.replace("Rp. ", "").replace(".", "").trim(); // Menghapus "Rp" dan titik
        }
        editTextHarga.setText(harga); // Pastikan harga hanya angka
        editTextPotongan.setText(potongan);
        switchStatus.setChecked(status == 1); // Aktif jika status 1

        buttonSimpan.setOnClickListener(v -> updateMembership());
    }

    private void updateMembership() {
        String namaMembership = editTextNamaMembership.getText().toString().trim();
        String harga = editTextHarga.getText().toString().trim();
        String potongan = editTextPotongan.getText().toString().trim();
        int status = switchStatus.isChecked() ? 1 : 2; // 1 untuk Aktif, 2 untuk Non-Aktif

        // Validasi input
        if (namaMembership.isEmpty() || harga.isEmpty() || potongan.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi harga hanya angka
        if (!harga.matches("\\d+")) {
            Toast.makeText(this, "Harga harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log untuk memeriksa nilai yang akan dikirim
        Log.d("EditMembershipActivity", "ID: " + membershipId);
        Log.d("EditMembershipActivity", "Nama: " + namaMembership);
        Log.d("EditMembershipActivity", "Harga: " + harga);
        Log.d("EditMembershipActivity", "Potongan: " + potongan);
        Log.d("EditMembershipActivity", "Status: " + status);

        // Kirim data ke server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_MEMBERSHIP,
                response -> {
                    Toast.makeText(EditMembershipActivity.this, "Membership berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Mengatur hasil untuk kembali ke MembershipActivity
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(EditMembershipActivity.this, "Gagal memperbarui membership", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_membership", membershipId); // ID membership yang akan diperbarui
                params.put("nama_membership", namaMembership);
                params.put("harga_membership", harga);
                params.put("potongan", potongan);
                params.put("status", String.valueOf(status)); // Kirim status sebagai string
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}