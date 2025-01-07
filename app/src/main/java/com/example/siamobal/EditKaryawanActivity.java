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

public class EditKaryawanActivity extends AppCompatActivity {
    private EditText editNik, editNama, editEmail, editPassword;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_karyawan);

        editNik = findViewById(R.id.edit_nik);
        editNama = findViewById(R.id.edit_nama);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnSimpan = findViewById(R.id.btn_simpan);

        // Ambil objek Karyawan dari intent
        Karyawan karyawan = (Karyawan) getIntent().getSerializableExtra("karyawan");

        if (karyawan != null) {
            editNik.setText(karyawan.getNikKaryawan());
            editNama.setText(karyawan.getNamaKaryawan());
            editEmail.setText(karyawan.getEmail());
            editPassword.setText(karyawan.getPassword()); // Tampilkan password dalam field
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKaryawan(karyawan.getNikKaryawan());
            }
        });
    }

    private void updateKaryawan(String nikKaryawan) {
        String url = "https://kevindinata.my.id/SIALAN/update_karyawan.php"; // Ganti dengan URL PHP Anda

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditKaryawanActivity.this, "Karyawan diperbarui!", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK); // Mengatur hasil sebagai OK saat selesai
                        finish(); // Kembali ke aktivitas sebelumnya
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditKaryawanActivity.this, "Gagal memperbarui karyawan: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NIK_KARYAWAN", nikKaryawan);
                params.put("NAMA_KARYAWAN", editNama.getText().toString().trim());
                params.put("EMAIL", editEmail.getText().toString().trim());
                params.put("PASSWORD", editPassword.getText().toString().trim());
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}