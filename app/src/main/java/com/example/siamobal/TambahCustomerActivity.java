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

public class TambahCustomerActivity extends AppCompatActivity {
    private EditText editTextNikCustomer, editTextNamaCustomer, editTextEmail;
    private Button buttonTambahSimpan;
    private static final String URL_ADD_CUSTOMER = "https://kevindinata.my.id/SIALAN/add_customer.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_customer);

        editTextNikCustomer = findViewById(R.id.editTextNikCustomer);
        editTextNamaCustomer = findViewById(R.id.editTextNamaCustomer);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonTambahSimpan = findViewById(R.id.buttonTambahSimpan);

        buttonTambahSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil data dari EditText
                String nik = editTextNikCustomer.getText().toString().trim();
                String nama = editTextNamaCustomer.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                if (nik.isEmpty() || nama.isEmpty() || email.isEmpty()) {
                    Toast.makeText(TambahCustomerActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    // Mengirim data ke server
                    addCustomerToServer(nik, nama, email);
                }
            }
        });
    }

    private void addCustomerToServer(String nik, String nama, String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_CUSTOMER,
                response -> {
                    Toast.makeText(TambahCustomerActivity.this, response, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Mengembalikan hasil OK
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(TambahCustomerActivity.this, "Gagal menambahkan customer", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nik_customer", nik);
                params.put("nama_customer", nama);
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}