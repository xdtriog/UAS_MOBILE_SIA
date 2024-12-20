package com.example.siamobal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

public class MembershipActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MembershipAdapter adapter;
    private List<Membership> membershipList;
    private static final String URL_GET_MEMBERSHIPS = "https://kevindinata.my.id/SIALAN/get_memberships.php"; // Ganti dengan URL server Anda
    private static final int ADD_MEMBERSHIP_REQUEST_CODE = 1; // Kode permintaan untuk menambah membership

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        recyclerView = findViewById(R.id.recyclerViewMembership);
        Button btnTambahMembership = findViewById(R.id.btnTambahMembership);

        // Inisialisasi daftar membership
        membershipList = new ArrayList<>();

        // Setup RecyclerView
        adapter = new MembershipAdapter(membershipList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Ambil data membership dari server
        fetchMemberships();

        // Tambah Membership
        btnTambahMembership.setOnClickListener(v -> {
            // Pindah ke TambahMembershipActivity
            Intent intent = new Intent(MembershipActivity.this, TambahMembershipActivity.class);
            startActivityForResult(intent, ADD_MEMBERSHIP_REQUEST_CODE); // Menggunakan startActivityForResult
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MEMBERSHIP_REQUEST_CODE && resultCode == RESULT_OK) {
            // Jika kembali dari TambahMembershipActivity, refresh data
            fetchMemberships();
        }
    }

    private void fetchMemberships() {
        membershipList.clear(); // Kosongkan daftar sebelum mengisi ulang
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_GET_MEMBERSHIPS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String nama = response.getJSONObject(i).getString("nama");
                                String harga = response.getJSONObject(i).getString("harga");
                                String potongan = response.getJSONObject(i).getString("potongan");
                                int status = response.getJSONObject(i).getInt("status");

                                // Menentukan status
                                String statusText = (status == 1) ? "Aktif" : "Non-Aktif";

                                membershipList.add(new Membership(nama, "Rp. " + harga, statusText));
                            }
                            adapter.notifyDataSetChanged(); // Notifikasi adapter untuk memperbarui tampilan
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MembershipActivity.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MembershipActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}