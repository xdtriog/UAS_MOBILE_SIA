package com.example.siamobal;

import android.content.Intent;
import android.os.Bundle;
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

public class EditBarangActivity extends AppCompatActivity {
    private EditText editTextKdBarang, editTextNamaBarang, editTextHarga;
    private Switch switchStatus;
    private Button buttonSimpan;
    private String kdBarang; // Kode barang yang akan diedit
    private static final String URL_UPDATE_BARANG = "https://kevindinata.my.id/SIALAN/update_barang.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_barang);

        editTextKdBarang = findViewById(R.id.editTextKdBarang);
        editTextNamaBarang = findViewById(R.id.editTextNamaBarang);
        editTextHarga = findViewById(R.id.editTextHarga);
        switchStatus = findViewById(R.id.switchStatus);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        // Ambil data dari Intent
        Intent intent = getIntent();
        kdBarang = intent.getStringExtra("KD_BARANG");
        String nama = intent.getStringExtra("NAMA_BARANG");
        String harga = intent.getStringExtra("HARGA_BARANG");
        String status = intent.getStringExtra("STATUS"); // Ambil status (1 atau 0)

        // Set data ke EditText
        editTextKdBarang.setText(kdBarang);
        editTextKdBarang.setEnabled(false); // Membuat Kode Barang tidak dapat diubah
        editTextNamaBarang.setText(nama);

        // Menghapus simbol "Rp" dan hanya menampilkan angka
        if (harga != null) {
            harga = harga.replace("Rp. ", "").replace(".", "").trim(); // Menghapus "Rp" dan titik
        }
        editTextHarga.setText(harga); // Pastikan harga hanya angka

        // Set switch berdasarkan status
        switchStatus.setChecked("1".equals(status)); // Aktif jika status adalah "1"

        buttonSimpan.setOnClickListener(v -> updateBarang());
    }

    private void updateBarang() {
        String namaBarang = editTextNamaBarang.getText().toString().trim();
        String harga = editTextHarga.getText().toString().trim();
        int status = switchStatus.isChecked() ? 1 : 0; // 1 untuk Aktif, 0 untuk Non-Aktif

        // Validasi input
        if (namaBarang.isEmpty() || harga.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi harga hanya angka
        if (!harga.matches("\\d+")) {
            Toast.makeText(this, "Harga harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim data ke server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_BARANG,
                response -> {
                    Toast.makeText(EditBarangActivity.this, "Barang berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Mengatur hasil untuk kembali ke BarangActivity
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(EditBarangActivity.this, "Gagal memperbarui barang", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kd_barang", kdBarang); // Kode barang yang akan diperbarui
                params.put("nama_barang", namaBarang);
                params.put("harga_barang", harga);
                params.put("status", String.valueOf(status)); // Kirim status sebagai string
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}