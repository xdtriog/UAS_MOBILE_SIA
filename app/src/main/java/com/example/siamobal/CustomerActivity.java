package com.example.siamobal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;
    private Button buttonTambahCustomer;
    private static final String URL_GET_CUSTOMERS = "https://kevindinata.my.id/SIALAN/get_customers.php"; // Ganti dengan URL server Anda
    private static final int ADD_CUSTOMER_REQUEST_CODE = 1; // Kode permintaan untuk hasil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        recyclerView = findViewById(R.id.recyclerView);
        buttonTambahCustomer = findViewById(R.id.buttonTambahCustomer);
        customerList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(customerAdapter);

        // Logika untuk membuka TambahCustomerActivity
        buttonTambahCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerActivity.this, TambahCustomerActivity.class);
            startActivityForResult(intent, ADD_CUSTOMER_REQUEST_CODE); // Menggunakan startActivityForResult
        });

        fetchCustomers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CUSTOMER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Memuat ulang data setelah kembali dari TambahCustomerActivity
            fetchCustomers();
        }
    }

    private void fetchCustomers() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_GET_CUSTOMERS, null,
                response -> {
                    try {
                        customerList.clear(); // Kosongkan daftar sebelum menambahkan data baru
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject customerObject = response.getJSONObject(i);
                            String namaCustomer = customerObject.getString("NAMA_CUSTOMER");
                            String namaMembership = customerObject.getString("NAMA_MEMBERSHIP");
                            String timestampHabis = customerObject.getString("TIMESTAMP_HABIS");

                            customerList.add(new Customer(namaCustomer, namaMembership, timestampHabis));
                        }
                        customerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CustomerActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(CustomerActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}