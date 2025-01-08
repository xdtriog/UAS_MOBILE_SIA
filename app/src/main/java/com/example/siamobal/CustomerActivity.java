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

public class CustomerActivity extends AppCompatActivity {

    private Button btnAddCustomer;
    private TableLayout tableCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        btnAddCustomer = findViewById(R.id.btn_add_customer);
        tableCustomer = findViewById(R.id.table_customer);

        btnAddCustomer.setOnClickListener(v -> {
            // Navigasi ke TambahCustomerActivity
            Intent intent = new Intent(CustomerActivity.this, TambahCustomerActivity.class);
            startActivityForResult(intent, 1); // Menggunakan startActivityForResult
        });

        loadCustomers();
    }

    private void loadCustomers() {
        String url = "https://kevindinata.my.id/SIALAN/get_customers.php"; // Ganti dengan URL yang sesuai

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        populateTable(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CustomerActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(CustomerActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void populateTable(JSONArray jsonArray) throws JSONException {
        tableCustomer.removeViews(1, tableCustomer.getChildCount() - 1); // Clear existing rows

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TableRow tableRow = new TableRow(this);

            TextView tvNamaCustomer = new TextView(this);
            TextView tvMembership = new TextView(this);
            TextView tvTimestampHabis = new TextView(this);
            Button btnEdit = new Button(this);

            tvNamaCustomer.setText(jsonObject.getString("NAMA_CUSTOMER").trim());
            tvMembership.setText(jsonObject.getString("NAMA_MEMBERSHIP").trim());
            tvTimestampHabis.setText(jsonObject.getString("TIMESTAMP_HABIS").trim());
            btnEdit.setText("Edit");

            // Action for the Edit button
            btnEdit.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(CustomerActivity.this, EditCustomerActivity.class);
                    intent.putExtra("NIK_CUTOMER", jsonObject.getString("NIK_CUTOMER"));
                    intent.putExtra("NAMA_CUSTOMER", jsonObject.getString("NAMA_CUSTOMER"));
                    intent.putExtra("EMAIL", jsonObject.getString("EMAIL")); // Mengambil EMAIL dari JSON
                    startActivityForResult(intent, 1); // Menggunakan startActivityForResult
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CustomerActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });

            // Set layout parameters to match header widths
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            tvNamaCustomer.setLayoutParams(params);
            tvMembership.setLayoutParams(params);
            tvTimestampHabis.setLayoutParams(params);
            btnEdit.setLayoutParams(params);

            tableRow.addView(tvNamaCustomer);
            tableRow.addView(tvMembership);
            tableRow.addView(tvTimestampHabis);
            tableRow.addView(btnEdit);

            tableCustomer.addView(tableRow);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Pastikan ini sesuai dengan requestCode yang Anda tentukan
            if (resultCode == RESULT_OK) {
                loadCustomers(); // Muat ulang data
            }
        }
    }
}