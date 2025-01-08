package com.example.siamobal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CicilActivity extends AppCompatActivity {

    private EditText etIdPiutangCustomer, etNomorNota, etNamaCustomer, etJumlahPiutang, etJumlahCicil, etSisaPiutang;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicil);

        etIdPiutangCustomer = findViewById(R.id.etIdPiutangCustomer);
        etNomorNota = findViewById(R.id.etNomorNota);
        etNamaCustomer = findViewById(R.id.etNamaCustomer);
        etJumlahPiutang = findViewById(R.id.etJumlahPiutang);
        etJumlahCicil = findViewById(R.id.etJumlahCicil);
        etSisaPiutang = findViewById(R.id.etSisaPiutang);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Ambil data dari intent
        String idPiutangCustomer = getIntent().getStringExtra("ID_PIUTANG_CUSTOMER");
        String nomorNota = getIntent().getStringExtra("NOMOR_NOTA");
        String namaCustomer = getIntent().getStringExtra("NAMA_CUSTOMER");
        double jumlahPiutang = getIntent().getDoubleExtra("JUMLAH_PIUTANG", 0.0);

        // Format jumlah piutang dan set data ke EditText
        etIdPiutangCustomer.setText(idPiutangCustomer);
        etNomorNota.setText(nomorNota);
        etNamaCustomer.setText(namaCustomer);
        etJumlahPiutang.setText(formatCurrency(jumlahPiutang)); // Format ke "Rp. X.XXX"
        etSisaPiutang.setText(formatCurrency(jumlahPiutang)); // Set juga sisa piutang

        // Tambahkan TextWatcher untuk menghitung sisa piutang
        etJumlahCicil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateRemaining();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSimpan.setOnClickListener(v -> {
            String jumlahCicil = etJumlahCicil.getText().toString().replace("Rp. ", "").replace(".", "").trim();
            String jumlahPiutangStr = etJumlahPiutang.getText().toString().replace("Rp. ", "").replace(".", "").trim();

            if (!jumlahCicil.isEmpty() && !jumlahPiutangStr.isEmpty()) {
                double jumlahCicilValue = Double.parseDouble(jumlahCicil);
                double jumlahPiutangValue = Double.parseDouble(jumlahPiutangStr);

                // Cek jika jumlah cicil lebih besar dari jumlah piutang
                if (jumlahCicilValue > jumlahPiutangValue) {
                    Toast.makeText(CicilActivity.this, "Jumlah Cicil tidak boleh lebih besar dari Jumlah Piutang", Toast.LENGTH_SHORT).show();
                } else {
                    simpanCicilan(idPiutangCustomer, jumlahCicil);
                }
            } else {
                Toast.makeText(CicilActivity.this, "Jumlah Cicil tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateRemaining() {
        String jumlahPiutangStr = etJumlahPiutang.getText().toString().replace("Rp. ", "").replace(".", "").trim();
        String jumlahCicilStr = etJumlahCicil.getText().toString().trim();

        double jumlahPiutang = Double.parseDouble(jumlahPiutangStr.isEmpty() ? "0" : jumlahPiutangStr);
        double jumlahCicil = Double.parseDouble(jumlahCicilStr.isEmpty() ? "0" : jumlahCicilStr);

        double sisaPiutang = jumlahPiutang - jumlahCicil;
        etSisaPiutang.setText(formatCurrency(sisaPiutang)); // Format sisa piutang
    }

    private String formatCurrency(double amount) {
        return String.format("Rp. %,d", Math.round(amount)); // Format ke "Rp. X.XXX"
    }

    private void simpanCicilan(String idPiutangCustomer, String jumlahCicil) {
        String url = "https://kevindinata.my.id/SIALAN/simpan_cicilan.php"; // Ganti dengan URL PHP Anda

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CicilActivity.this, "Cicilan berhasil disimpan", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK); // Set hasil kembali ke PiutangActivity
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CicilActivity.this, "Gagal menyimpan cicilan", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_PIUTANG_CUSTOMER", idPiutangCustomer);
                params.put("JUMLAH_BAYAR", jumlahCicil);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}