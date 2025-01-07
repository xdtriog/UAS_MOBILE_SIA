package com.example.siamobal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KaryawanActivity extends AppCompatActivity {
    private static final int EDIT_KARYAWAN_REQUEST = 1; // Kode untuk permintaan edit karyawan
    private Button btnTambahKaryawan;
    private TableLayout tableKaryawan;
    private ArrayList<Karyawan> listKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan);

        btnTambahKaryawan = findViewById(R.id.btn_tambah_karyawan);
        tableKaryawan = findViewById(R.id.table_karyawan);
        listKaryawan = new ArrayList<>();

        btnTambahKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KaryawanActivity.this, TambahKaryawanActivity.class);
                startActivityForResult(intent, EDIT_KARYAWAN_REQUEST); // Menjalankan aktivitas dengan hasil
            }
        });

        getKaryawanData();
    }

    private void getKaryawanData() {
        String url = "https://kevindinata.my.id/SIALAN/get_karyawan.php"; // Ganti dengan URL PHP Anda

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            populateTable(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // Update bagian populateTable pada KaryawanActivity jika perlu
    private void populateTable(JSONArray jsonArray) throws JSONException {
        tableKaryawan.removeViews(1, tableKaryawan.getChildCount() - 1); // Hapus baris sebelumnya

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String nikKaryawan = jsonObject.getString("NIK_KARYAWAN");
            String namaKaryawan = jsonObject.getString("NAMA_KARYAWAN");
            String email = jsonObject.getString("EMAIL");
            String password = jsonObject.getString("PASSWORD"); // Ambil password, tetapi tidak ditampilkan

            Karyawan karyawan = new Karyawan(nikKaryawan, namaKaryawan, email, password); // Menyimpan password

            TableRow tableRow = new TableRow(this);
            TextView tvNIK = new TextView(this);
            TextView tvNama = new TextView(this);
            TextView tvEmail = new TextView(this);
            Button btnEdit = new Button(this);

            // Set values
            tvNIK.setText(karyawan.getNikKaryawan());
            tvNama.setText(karyawan.getNamaKaryawan());
            tvEmail.setText(karyawan.getEmail());
            btnEdit.setText("Edit");

            // Set layout params untuk TextView
            TableRow.LayoutParams paramsNIK = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            TableRow.LayoutParams paramsNama = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
            TableRow.LayoutParams paramsEmail = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
            TableRow.LayoutParams paramsButton = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            tvNIK.setLayoutParams(paramsNIK);
            tvNama.setLayoutParams(paramsNama);
            tvEmail.setLayoutParams(paramsEmail);
            btnEdit.setLayoutParams(paramsButton);

            tableRow.addView(tvNIK);
            tableRow.addView(tvNama);
            tableRow.addView(tvEmail);
            tableRow.addView(btnEdit);

            // Set listener untuk mengedit karyawan
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(KaryawanActivity.this, EditKaryawanActivity.class);
                    intent.putExtra("karyawan", karyawan); // Mengirim objek Karyawan
                    startActivityForResult(intent, EDIT_KARYAWAN_REQUEST);
                }
            });

            tableKaryawan.addView(tableRow);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_KARYAWAN_REQUEST && resultCode == RESULT_OK) {
            getKaryawanData(); // Reload data setelah edit
        }
    }
}