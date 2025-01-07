package com.example.siamobal;

import android.os.Bundle;
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

public class TambahKaryawanActivity extends AppCompatActivity {
    private EditText editNik, editNama, editEmail, editPassword;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_karyawan);

        editNik = findViewById(R.id.edit_nik);
        editNama = findViewById(R.id.edit_nama);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahKaryawan();
            }
        });
    }

    private void tambahKaryawan() {
        String url = "https://kevindinata.my.id/SIALAN/add_karyawan.php"; // Pastikan URL ini benar

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TambahKaryawanActivity.this, "Karyawan ditambahkan!", Toast.LENGTH_LONG).show();

                        // Mengatur hasil intent
                        setResult(RESULT_OK);
                        finish(); // Kembali ke aktivitas sebelumnya
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TambahKaryawanActivity.this, "Gagal menambahkan karyawan: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NIK_KARYAWAN", editNik.getText().toString().trim());
                params.put("NAMA_KARYAWAN", editNama.getText().toString().trim());
                params.put("EMAIL", editEmail.getText().toString().trim());
                params.put("PASSWORD", editPassword.getText().toString().trim());
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}