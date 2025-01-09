package com.example.siamobal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class TambahBarangActivity extends AppCompatActivity {
    private EditText editTextNamaBarang, editTextHargaBarang;
    private Button buttonSimpan;
    private static final String URL_ADD_BARANG = "https://kevindinata.my.id/SIALAN/add_barang.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        editTextNamaBarang = findViewById(R.id.editTextNamaBarang);
        editTextHargaBarang = findViewById(R.id.editTextHargaBarang);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(v -> saveBarang());
    }

    private void saveBarang() {
        String namaBarang = editTextNamaBarang.getText().toString().trim();
        String hargaBarang = editTextHargaBarang.getText().toString().trim();

        if (namaBarang.isEmpty() || hargaBarang.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_BARANG,
                response -> {
                    Toast.makeText(TambahBarangActivity.this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Mengatur hasil sebagai OK
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(TambahBarangActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NAMA_BARANG", namaBarang);
                params.put("HARGA_BARANG", hargaBarang);
                return params; // Mengirim NAMA_BARANG dan HARGA_BARANG
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}