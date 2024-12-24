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

public class MetodePembayaranActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button buttonTambahMetodePembayaran;
    private static final String URL_GET_DATA = "https://kevindinata.my.id/SIALAN/get_metode_pembayaran.php"; // Ganti dengan URL server Anda
    private static final int REQUEST_CODE_ADD = 1; // Kode permintaan untuk aktivitas tambah

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metode_pembayaran);

        tableLayout = findViewById(R.id.tableLayout);
        buttonTambahMetodePembayaran = findViewById(R.id.buttonTambahMetodePembayaran);

        // Memuat data metode pembayaran
        loadMetodePembayaran();

        // Listener untuk tombol tambah metode pembayaran
        buttonTambahMetodePembayaran.setOnClickListener(v -> {
            Intent intent = new Intent(MetodePembayaranActivity.this, TambahMetodePembayaranActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD); // Memulai aktivitas dengan kode permintaan
        });
    }

    private void loadMetodePembayaran() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DATA,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1); // Hapus semua baris kecuali header

                        // Proses data metode pembayaran
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject metodeObject = jsonArray.getJSONObject(i);
                            String idMpn = metodeObject.getString("ID_MMPN");
                            String namaMpn = metodeObject.getString("NAMA_MPPN");
                            String status = metodeObject.getString("STATUS");

                            // Tambahkan baris baru ke tabel
                            TableRow tableRow = new TableRow(MetodePembayaranActivity.this);
                            TextView textViewId = new TextView(MetodePembayaranActivity.this);
                            TextView textViewNama = new TextView(MetodePembayaranActivity.this);
                            TextView textViewStatus = new TextView(MetodePembayaranActivity.this);

                            textViewId.setText(idMpn);
                            textViewNama.setText(namaMpn);
                            textViewStatus.setText(status.equals("1") ? "Aktif" : "Tidak Aktif"); // Menampilkan status

                            tableRow.addView(textViewId);
                            tableRow.addView(textViewNama);
                            tableRow.addView(textViewStatus);
                            tableLayout.addView(tableRow);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MetodePembayaranActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MetodePembayaranActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            loadMetodePembayaran(); // Memuat ulang data setelah menambah metode pembayaran
        }
    }
}