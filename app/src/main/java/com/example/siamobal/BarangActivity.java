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

public class BarangActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button buttonTambahBarang;
    private static final String URL_GET_DATA = "https://kevindinata.my.id/SIALAN/get_barang.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);

        tableLayout = findViewById(R.id.tableLayout);
        buttonTambahBarang = findViewById(R.id.buttonTambahBarang);

        // Memuat data barang
        loadBarang();

        // Listener untuk tombol tambah barang
        buttonTambahBarang.setOnClickListener(v -> {
            Intent intent = new Intent(BarangActivity.this, TambahBarangActivity.class);
            startActivity(intent); // Memulai aktivitas TambahBarangActivity
        });
    }

    private void loadBarang() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DATA,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1); // Hapus semua baris kecuali header

                        // Proses data barang
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject barangObject = jsonArray.getJSONObject(i);
                            String kdBarang = barangObject.getString("KD_BARANG");
                            String namaBarang = barangObject.getString("NAMA_BARANG");
                            String hargaBarang = barangObject.getString("HARGA_BARANG");
                            String status = barangObject.getString("STATUS");

                            // Tambahkan baris baru ke tabel
                            TableRow tableRow = new TableRow(BarangActivity.this);
                            TextView textViewNama = new TextView(BarangActivity.this);
                            TextView textViewHarga = new TextView(BarangActivity.this);
                            TextView textViewStatus = new TextView(BarangActivity.this);
                            TextView textViewAction = new TextView(BarangActivity.this);

                            textViewNama.setText(namaBarang);
                            textViewHarga.setText("Rp. " + hargaBarang);
                            textViewStatus.setText(status.equals("1") ? "Aktif" : "Tidak Aktif");
                            textViewAction.setText("Edit"); // Placeholder untuk aksi edit

                            tableRow.addView(textViewNama);
                            tableRow.addView(textViewHarga);
                            tableRow.addView(textViewStatus);
                            tableRow.addView(textViewAction);
                            tableLayout.addView(tableRow);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(BarangActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(BarangActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}