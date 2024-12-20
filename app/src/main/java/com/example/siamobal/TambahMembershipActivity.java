package com.example.siamobal;

import android.content.SharedPreferences;
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

public class TambahMembershipActivity extends AppCompatActivity {
    private EditText editTextNamaMembership, editTextHarga, editTextPotongan;
    private Button buttonSimpan;
    private static final String URL_ADD_MEMBERSHIP = "https://kevindinata.my.id/SIALAN/add_membership.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_membership);

        editTextNamaMembership = findViewById(R.id.editTextNamaMembership);
        editTextHarga = findViewById(R.id.editTextHarga);
        editTextPotongan = findViewById(R.id.editTextPotongan);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(v -> tambahMembership());
    }

    private void tambahMembership() {
        String namaMembership = editTextNamaMembership.getText().toString().trim();
        String harga = editTextHarga.getText().toString().trim();
        String potongan = editTextPotongan.getText().toString().trim();

        if (namaMembership.isEmpty() || harga.isEmpty() || potongan.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil NIK_KARYAWAN dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String nikKaryawan = sharedPreferences.getString("nik_karyawan", "NIK");

        // Kirim data ke server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_MEMBERSHIP,
                response -> {
                    Toast.makeText(TambahMembershipActivity.this, "Membership berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Mengatur hasil untuk kembali ke MembershipActivity
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(TambahMembershipActivity.this, "Gagal menambahkan membership", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_membership", namaMembership);
                params.put("harga_membership", harga);
                params.put("potongan", potongan);
                params.put("nik_karyawan", nikKaryawan); // Kirim NIK_KARYAWAN
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}