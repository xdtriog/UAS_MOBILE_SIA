package com.example.siamobal; // Ganti dengan nama paket Anda

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
            // Tambahkan logika untuk menambah membership baru
        });
    }

    private void fetchMemberships() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_GET_MEMBERSHIPS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String nama = response.getJSONObject(i).getString("nama");
                                String harga = response.getJSONObject(i).getString("harga");
                                String potongan = response.getJSONObject(i).getString("potongan");
                                String status = response.getJSONObject(i).getString("status");

                                membershipList.add(new Membership(nama, status + " - " + potongan + " (" + harga + ")"));
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