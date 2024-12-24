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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahKodeAkunActivity extends AppCompatActivity {
    private EditText editTextKodeAkun, editTextNamaAkun;
    private Button buttonSimpan;
    private static final String URL_ADD_KODE_AKUN = "https://kevindinata.my.id/SIALAN/add_kode_akun.php"; // Ganti dengan URL server Anda
    private static final String URL_CHECK_KODE_AKUN = "https://kevindinata.my.id/SIALAN/check_kode_akun.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kode_akun);

        editTextKodeAkun = findViewById(R.id.editTextKodeAkun);
        editTextNamaAkun = findViewById(R.id.editTextNamaAkun);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(v -> checkAndSaveKodeAkun());
    }

    private void checkAndSaveKodeAkun() {
        String kodeAkun = editTextKodeAkun.getText().toString().trim();
        String namaAkun = editTextNamaAkun.getText().toString().trim();

        if (kodeAkun.isEmpty() || namaAkun.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek apakah kode akun sudah ada
        StringRequest checkRequest = new StringRequest(Request.Method.POST, URL_CHECK_KODE_AKUN,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean exists = jsonResponse.getBoolean("exists");

                        if (exists) {
                            Toast.makeText(TambahKodeAkunActivity.this, "Kode Akun sudah ada!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Jika tidak ada, simpan kode akun
                            saveKodeAkun(kodeAkun, namaAkun);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TambahKodeAkunActivity.this, "Gagal memeriksa kode akun", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(TambahKodeAkunActivity.this, "Gagal memeriksa kode akun", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("KODE_AKUN", kodeAkun);
                return params; // Mengirim KODE_AKUN untuk pemeriksaan
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(checkRequest);
    }

    private void saveKodeAkun(String kodeAkun, String namaAkun) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_KODE_AKUN,
                response -> {
                    Toast.makeText(TambahKodeAkunActivity.this, response, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Mengatur hasil sebagai OK
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(TambahKodeAkunActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("KODE_AKUN", kodeAkun);
                params.put("NAMA_AKUN", namaAkun);
                return params; // Mengirim KODE_AKUN dan NAMA_AKUN
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}