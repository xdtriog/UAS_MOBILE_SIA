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
    private static final int ADD_BARANG_REQUEST_CODE = 1; // Kode permintaan untuk tambah barang
    private static final int EDIT_BARANG_REQUEST_CODE = 2; // Kode permintaan untuk edit barang

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
            startActivityForResult(intent, ADD_BARANG_REQUEST_CODE); // Memulai aktivitas dengan request code
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
                            Button buttonEdit = new Button(BarangActivity.this); // Ubah menjadi Button

                            textViewNama.setText(namaBarang);
                            textViewHarga.setText("Rp. " + hargaBarang);
                            textViewStatus.setText(status.equals("1") ? "Aktif" : "Tidak Aktif");
                            buttonEdit.setText("Edit"); // Set teks tombol Edit

                            // Set click listener untuk tombol Edit
                            buttonEdit.setOnClickListener(v -> {
                                Intent intent = new Intent(BarangActivity.this, EditBarangActivity.class);
                                intent.putExtra("KD_BARANG", kdBarang);
                                intent.putExtra("NAMA_BARANG", namaBarang);
                                intent.putExtra("HARGA_BARANG", hargaBarang);
                                intent.putExtra("STATUS", status); // Kirim status langsung dari JSON (1 atau 0)
                                startActivityForResult(intent, EDIT_BARANG_REQUEST_CODE); // Kembali untuk memuat ulang
                            });

                            tableRow.addView(textViewNama);
                            tableRow.addView(textViewHarga);
                            tableRow.addView(textViewStatus);
                            tableRow.addView(buttonEdit); // Menambahkan tombol Edit ke tabel
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Jika kembali dari TambahBarangActivity atau EditBarangActivity, refresh data
            loadBarang();
        }
    }
}