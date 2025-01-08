package com.example.siamobal;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class BukuBesarActivity extends AppCompatActivity {

    private Spinner spinnerBulanTahun, spinnerKodeAkun;
    private Button btnFilter;
    private TableLayout tableBukuBesar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku_besar);

        spinnerBulanTahun = findViewById(R.id.spinner_bulan_tahun);
        spinnerKodeAkun = findViewById(R.id.spinner_kode_akun);
        btnFilter = findViewById(R.id.btn_filter);
        tableBukuBesar = findViewById(R.id.table_buku_besar);

        // Populate Spinner with Month-Year Options
        ArrayList<String> monthYearList = new ArrayList<>();
        monthYearList.add("01 - 2025");
        monthYearList.add("02 - 2025");
        // Add more options as needed

        ArrayAdapter<String> adapterMonthYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthYearList);
        adapterMonthYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBulanTahun.setAdapter(adapterMonthYear);

        // Load Kode Akun Options from Server
        loadKodeAkun();

        btnFilter.setOnClickListener(v -> {
            String selectedDate = spinnerBulanTahun.getSelectedItem().toString();
            String selectedKodeAkun = spinnerKodeAkun.getSelectedItem().toString().split(" - ")[0]; // Ambil kode akun
            getBukuBesarData(selectedDate, selectedKodeAkun);
        });
    }

    private void loadKodeAkun() {
        String url = "https://kevindinata.my.id/SIALAN/get_kode_akun.php";  // Ganti dengan URL yang sesuai

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        ArrayList<String> akunList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String kodeAkun = jsonObject.getString("KODE_AKUN");
                            String namaAkun = jsonObject.getString("NAMA_AKUN");
                            akunList.add(kodeAkun + " - " + namaAkun);
                        }
                        ArrayAdapter<String> adapterAkun = new ArrayAdapter<>(BukuBesarActivity.this,
                                android.R.layout.simple_spinner_item, akunList);
                        adapterAkun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerKodeAkun.setAdapter(adapterAkun);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getBukuBesarData(String filter, String kodeAkun) {
        String url = "https://kevindinata.my.id/SIALAN/get_buku_besar.php?filter=" + filter + "&kode_akun=" + kodeAkun;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        populateTable(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void populateTable(JSONArray jsonArray) throws JSONException {
        tableBukuBesar.removeViews(1, tableBukuBesar.getChildCount() - 1); // Remove previous rows

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TableRow tableRow = new TableRow(this);

            TextView tvTanggal = new TextView(this);
            TextView tvKeterangan = new TextView(this);
            TextView tvRef = new TextView(this);
            TextView tvDebit = new TextView(this);
            TextView tvKredit = new TextView(this);

            tvTanggal.setText(jsonObject.getString("TANGGAL").trim());
            tvKeterangan.setText(jsonObject.getString("KETERANGAN").trim());
            tvRef.setText(jsonObject.getString("REF").trim());

            String debitValue = jsonObject.getString("DEBIT").trim();
            String debit2Value = jsonObject.optString("DEBIT2", "0").trim();
            String kreditValue = jsonObject.getString("KREDIT").trim();
            String kredit2Value = jsonObject.optString("KREDIT2", "0").trim();

            tvDebit.setText(debitValue.equals("0") ? debit2Value : debitValue);
            tvKredit.setText(kreditValue.equals("0") ? kredit2Value : kreditValue);

            tableRow.addView(tvTanggal, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tvKeterangan, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            tableRow.addView(tvRef, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tvDebit, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tvKredit, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            tableBukuBesar.addView(tableRow);
        }
    }
}