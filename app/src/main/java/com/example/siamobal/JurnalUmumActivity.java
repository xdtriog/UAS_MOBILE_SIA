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
import com.example.siamobal.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class JurnalUmumActivity extends AppCompatActivity {
    private Spinner spinnerBulanTahun;
    private Button btnFilter;
    private TableLayout tableJurnal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurnal_umum);

        spinnerBulanTahun = findViewById(R.id.spinner_bulan_tahun);
        btnFilter = findViewById(R.id.btn_filter);
        tableJurnal = findViewById(R.id.table_jurnal);

        // Populate Spinner with Month-Year Options
        ArrayList<String> monthYearList = new ArrayList<>();
        monthYearList.add("01 - 2025");
        monthYearList.add("02 - 2025");
        // Add more options as needed

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthYearList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBulanTahun.setAdapter(adapter);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = spinnerBulanTahun.getSelectedItem().toString();
                getJurnalData(selected);
            }
        });
    }

    private void getJurnalData(String filter) {
        String url = "https://kevindinata.my.id/SIALAN/get_jurnal.php?filter=" + filter;

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

    private void populateTable(JSONArray jsonArray) throws JSONException {
        tableJurnal.removeViews(1, tableJurnal.getChildCount() - 1); // Remove previous rows

        String currentRef = "";
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TableRow tableRow = new TableRow(this);

            TextView tvTanggal = new TextView(this);
            TextView tvKeterangan = new TextView(this);
            TextView tvRef = new TextView(this);
            TextView tvDebit = new TextView(this);
            TextView tvKredit = new TextView(this);

            // Set the values with trim() to remove leading/trailing spaces
            tvTanggal.setText(jsonObject.getString("TANGGAL").trim());
            tvKeterangan.setText(jsonObject.getString("KETERANGAN").trim());
            tvRef.setText(jsonObject.getString("REF").trim());

            // Periksa nilai DEBIT dan KREDIT
            String debitValue = jsonObject.getString("DEBIT").trim();
            String debit2Value = jsonObject.optString("DEBIT2", "0").trim();
            String kreditValue = jsonObject.getString("KREDIT").trim();
            String kredit2Value = jsonObject.optString("KREDIT2", "0").trim();

            // Format nilai tanpa label "(DEBIT)" atau "(KREDIT)"
            tvDebit.setText(debitValue.equals("0") ? debit2Value : debitValue);
            tvKredit.setText(kreditValue.equals("0") ? kredit2Value : kreditValue);

            // Set layout parameters to allow wrapping
            tableRow.addView(tvTanggal, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tvKeterangan, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            tableRow.addView(tvRef, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tvDebit, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tvKredit, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            // Hanya menampilkan REF sekali
            if (!currentRef.equals(tvRef.getText().toString())) {
                currentRef = tvRef.getText().toString();
                tableJurnal.addView(tableRow);
            } else {
                // Menggunakan kolom yang sama dengan REF yang kosong
                tvRef.setText("");
                tableJurnal.addView(tableRow);
            }
        }
    }
}