package com.example.siamobal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeliMembershipActivity extends AppCompatActivity {
    private Spinner spinnerNikCustomer, spinnerIdMembership;
    private Button buttonBeliSimpan;
    private ArrayList<String> nikCustomerList = new ArrayList<>();
    private ArrayList<String> idMembershipList = new ArrayList<>();
    private ArrayList<String> namaMembershipList = new ArrayList<>();
    private static final String URL_GET_DATA = "https://kevindinata.my.id/SIALAN/get_alldata_beli_membership.php"; // Ganti dengan URL server Anda
    private static final String URL_ADD_TRANSACTION = "https://kevindinata.my.id/SIALAN/beli_membership.php"; // Ganti dengan URL server Anda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_membership);

        // Inisialisasi UI
        spinnerNikCustomer = findViewById(R.id.spinnerNikCustomer);
        spinnerIdMembership = findViewById(R.id.spinnerIdMembership);
        buttonBeliSimpan = findViewById(R.id.buttonBeliSimpan);

        // Memuat data customer dan membership
        loadCustomersAndMemberships();

        // Listener untuk spinner NIK Customer
        spinnerNikCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Tidak perlu melakukan apa-apa saat memilih customer
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Listener untuk spinner ID Membership
        spinnerIdMembership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Tidak perlu melakukan apa-apa saat memilih membership
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Listener untuk tombol simpan
        buttonBeliSimpan.setOnClickListener(v -> saveTransaction());
    }

    private void loadCustomersAndMemberships() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DATA,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray customersArray = jsonResponse.getJSONArray("customers");
                        JSONArray membershipsArray = jsonResponse.getJSONArray("memberships");

                        // Proses data customer
                        for (int i = 0; i < customersArray.length(); i++) {
                            JSONObject customerObject = customersArray.getJSONObject(i);
                            String nik = customerObject.getString("NIK_CUTOMER");
                            String nama = customerObject.getString("NAMA_CUSTOMER");
                            nikCustomerList.add(nik + " - " + nama); // Menampilkan NIK dan Nama
                        }

                        // Proses data membership
                        for (int i = 0; i < membershipsArray.length(); i++) {
                            JSONObject membershipObject = membershipsArray.getJSONObject(i);
                            String id = membershipObject.getString("ID_MASTER_MEMBERSHIP");
                            String nama = membershipObject.getString("NAMA_MEMBERSHIP");
                            idMembershipList.add(id);
                            namaMembershipList.add(nama);
                        }

                        // Set adapter untuk spinner customer
                        ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nikCustomerList);
                        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerNikCustomer.setAdapter(customerAdapter);

                        // Set adapter untuk spinner membership
                        ArrayAdapter<String> membershipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaMembershipList);
                        membershipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerIdMembership.setAdapter(membershipAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(BeliMembershipActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(BeliMembershipActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveTransaction() {
        String selectedNik = nikCustomerList.get(spinnerNikCustomer.getSelectedItemPosition()).split(" - ")[0]; // Ambil NIK
        String selectedId = idMembershipList.get(spinnerIdMembership.getSelectedItemPosition()); // Ambil ID Membership

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_TRANSACTION,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if (status.equals("success")) {
                            String idJurnalUmum = jsonResponse.getString("id_jurnal_umum");
                            Toast.makeText(BeliMembershipActivity.this, "Transaksi berhasil disimpan. ID Jurnal: " + idJurnalUmum, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BeliMembershipActivity.this, "Gagal menyimpan transaksi: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(BeliMembershipActivity.this, "Gagal memuat respons", Toast.LENGTH_SHORT).show();
                    }
                    finish(); // Kembali ke activity sebelumnya
                },
                error -> Toast.makeText(BeliMembershipActivity.this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_MASTER_MEMBERSHIP", selectedId);
                params.put("NIK_CUTOMER", selectedNik);
                return params; // Hanya mengirim NIK dan ID Membership
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}