package com.example.siamobal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KodeAkunActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button buttonTambahKodeAkun;
    private static final String URL_GET_DATA = "https://kevindinata.my.id/SIALAN/get_kode_akun.php"; // Ganti dengan URL server Anda
    private static final int REQUEST_CODE_ADD = 1; // Kode permintaan untuk aktivitas tambah

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode_akun);

        tableLayout = findViewById(R.id.tableLayout);
        buttonTambahKodeAkun = findViewById(R.id.buttonTambahKodeAkun);

        // Memuat data kode akun
        loadKodeAkun();

        // Listener untuk tombol tambah kode akun
        buttonTambahKodeAkun.setOnClickListener(v -> {
            Intent intent = new Intent(KodeAkunActivity.this, TambahKodeAkunActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD); // Memulai aktivitas dengan kode permintaan
        });
    }

    private void loadKodeAkun() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DATA,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1); // Hapus semua baris kecuali header

                        // Proses data kode akun
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject akunObject = jsonArray.getJSONObject(i);
                            String kodeAkun = akunObject.getString("KODE_AKUN");
                            String namaAkun = akunObject.getString("NAMA_AKUN");

                            // Tambahkan baris baru ke tabel
                            TableRow tableRow = new TableRow(KodeAkunActivity.this);
                            TextView textViewKode = new TextView(KodeAkunActivity.this);
                            TextView textViewNama = new TextView(KodeAkunActivity.this);

                            textViewKode.setText(kodeAkun);
                            textViewNama.setText(namaAkun);

                            tableRow.addView(textViewKode);
                            tableRow.addView(textViewNama);
                            tableLayout.addView(tableRow);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(KodeAkunActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(KodeAkunActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            loadKodeAkun(); // Memuat ulang data setelah menambah kode akun
        }
    }
}