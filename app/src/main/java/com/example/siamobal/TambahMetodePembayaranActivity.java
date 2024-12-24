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

public class TambahMetodePembayaranActivity extends AppCompatActivity {
    private EditText editTextNamaMetode;
    private Button buttonSimpan;
    private static final String URL_ADD_METODE_PEMBAYARAN = "https://kevindinata.my.id/SIALAN/add_metode_pembayaran.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_metode_pembayaran);

        editTextNamaMetode = findViewById(R.id.editTextNamaMetode);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(v -> saveMetodePembayaran());
    }

    private void saveMetodePembayaran() {
        String namaMetode = editTextNamaMetode.getText().toString().trim();

        if (namaMetode.isEmpty()) {
            Toast.makeText(this, "Harap isi nama metode pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_METODE_PEMBAYARAN,
                response -> {
                    Toast.makeText(TambahMetodePembayaranActivity.this, response, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Mengatur hasil sebagai OK
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(TambahMetodePembayaranActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NAMA_MPPN", namaMetode);
                return params; // Mengirim NAMA_MPPN saja
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}