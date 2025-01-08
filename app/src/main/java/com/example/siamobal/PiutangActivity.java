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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PiutangActivity extends AppCompatActivity {
    private TableLayout tablePiutang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piutang);

        tablePiutang = findViewById(R.id.table_piutang);
        getPiutangData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getPiutangData(); // Memanggil kembali untuk refresh data
        }
    }

    private void getPiutangData() {
        String url = "https://kevindinata.my.id/SIALAN/get_piutang.php"; // Ganti dengan URL PHP Anda

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
                Toast.makeText(PiutangActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void populateTable(JSONArray jsonArray) throws JSONException {
        tablePiutang.removeAllViews(); // Clear existing rows

        // Add the header row again to ensure it's visible
        TableRow headerRow = new TableRow(this);
        TextView tvNomorNotaHeader = new TextView(this);
        TextView tvNamaCustomerHeader = new TextView(this);
        TextView tvJumlahPiutangHeader = new TextView(this);
        TextView tvActionHeader = new TextView(this);

        // Set header values
        tvNomorNotaHeader.setText("Nomor Nota");
        tvNamaCustomerHeader.setText("Nama Customer");
        tvJumlahPiutangHeader.setText("Jumlah Piutang");
        tvActionHeader.setText("Action");

        // Set header styling
        tvNomorNotaHeader.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tvNamaCustomerHeader.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tvJumlahPiutangHeader.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tvActionHeader.setGravity(View.TEXT_ALIGNMENT_CENTER);

        tvNomorNotaHeader.setMaxWidth(120); // Set maxWidth secara konsisten
        tvNamaCustomerHeader.setMaxWidth(150);
        tvJumlahPiutangHeader.setMaxWidth(120);
        tvActionHeader.setMaxWidth(100);

        // Add header TextViews to the header row
        headerRow.addView(tvNomorNotaHeader);
        headerRow.addView(tvNamaCustomerHeader);
        headerRow.addView(tvJumlahPiutangHeader);
        headerRow.addView(tvActionHeader);

        // Add header row to the table
        tablePiutang.addView(headerRow);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String idPiutangCustomer = jsonObject.getString("ID_PIUTANG_CUSTOMER");
            String nomorNota = jsonObject.getString("NOMOR_NOTA");
            String namaCustomer = jsonObject.getString("NAMA_CUSTOMER");
            double jumlahPiutang = jsonObject.getDouble("JUMLAH_PIUTANG");

            TableRow tableRow = new TableRow(this);
            TextView tvNomorNota = new TextView(this);
            TextView tvNamaCustomer = new TextView(this);
            TextView tvJumlahPiutang = new TextView(this);
            Button btnCicil = new Button(this);

            // Format jumlah piutang tanpa desimal
            String formattedJumlahPiutang = String.format("Rp. %,.0f", jumlahPiutang);

            // Set values
            tvNomorNota.setText(nomorNota);
            tvNamaCustomer.setText(namaCustomer);
            tvJumlahPiutang.setText(formattedJumlahPiutang); // Gunakan format yang benar
            btnCicil.setText("Cicil");

            // Set layout parameters dengan max width
            tvNomorNota.setMaxWidth(120);
            tvNamaCustomer.setMaxWidth(150);
            tvJumlahPiutang.setMaxWidth(120);

            tableRow.addView(tvNomorNota);
            tableRow.addView(tvNamaCustomer);
            tableRow.addView(tvJumlahPiutang);
            tableRow.addView(btnCicil);

            // Set button click listener
            btnCicil.setOnClickListener(v -> {
                Intent intent = new Intent(PiutangActivity.this, CicilActivity.class);
                intent.putExtra("ID_PIUTANG_CUSTOMER", idPiutangCustomer);
                intent.putExtra("NOMOR_NOTA", nomorNota);
                intent.putExtra("NAMA_CUSTOMER", namaCustomer);
                intent.putExtra("JUMLAH_PIUTANG", jumlahPiutang);
                startActivityForResult(intent, 1);
            });

            // Disable button if JUMLAH_PIUTANG == 0
            if (jumlahPiutang <= 0) {
                btnCicil.setEnabled(false);
                btnCicil.setText("LUNAS");
            }

            tablePiutang.addView(tableRow);
        }
    }
}