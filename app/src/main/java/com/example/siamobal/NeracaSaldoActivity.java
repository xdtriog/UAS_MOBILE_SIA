package com.example.siamobal;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class NeracaSaldoActivity extends AppCompatActivity {

    private Spinner spinnerBulanTahun;
    private Button btnFilter;
    private TableLayout tableBukuBesar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neraca_saldo);

        spinnerBulanTahun = findViewById(R.id.spinnerBulanTahun);
        btnFilter = findViewById(R.id.btnFilter);
        tableBukuBesar = findViewById(R.id.tableBukuBesar);

        // Setup spinner adapter
        setupSpinner();

        btnFilter.setOnClickListener(v -> {
            String selectedDate = spinnerBulanTahun.getSelectedItem().toString();
            getData(selectedDate);
        });
    }

    private void setupSpinner() {
        ArrayList<String> monthYearList = new ArrayList<>();
        monthYearList.add("01 - 2025");
        monthYearList.add("02 - 2025");
        // Tambahkan lebih banyak bulan/ tahun sesuai kebutuhan

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthYearList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBulanTahun.setAdapter(adapter);
    }

    private void getData(String selectedDate) {
        String url = "https://kevindinata.my.id/SIALAN/get_neraca_saldo.php?tanggal=" + selectedDate; // Gantilah dengan URL yang sesuai

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
                Toast.makeText(NeracaSaldoActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void populateTable(JSONArray jsonArray) throws JSONException {
        tableBukuBesar.removeViews(1, tableBukuBesar.getChildCount() - 1); // Clear existing rows except header

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int kodeAkun = jsonObject.getInt("KODE_AKUN");
            String keterangan = jsonObject.getString("NAMA_AKUN");
            long debitValue = jsonObject.getLong("DEBIT"); // Menerima nilai debit dari API
            long kreditValue = jsonObject.getLong("KREDIT"); // Menerima nilai kredit dari API

            TableRow tableRow = new TableRow(this);
            TextView tvKodeAkun = new TextView(this);
            TextView tvKeterangan = new TextView(this);
            TextView tvDebit = new TextView(this);
            TextView tvKredit = new TextView(this);

            tvKodeAkun.setText(String.valueOf(kodeAkun));
            tvKeterangan.setText(keterangan);
            tvDebit.setText(formatCurrency(debitValue)); // Format currency
            tvKredit.setText(formatCurrency(kreditValue)); // Format currency

            tableRow.addView(tvKodeAkun);
            tableRow.addView(tvKeterangan);
            tableRow.addView(tvDebit);
            tableRow.addView(tvKredit);

            tableBukuBesar.addView(tableRow);
        }
    }

    private String formatCurrency(long amount) {
        return String.format("Rp. %,d", amount); // Format ke "Rp. X.XXX"
    }
}