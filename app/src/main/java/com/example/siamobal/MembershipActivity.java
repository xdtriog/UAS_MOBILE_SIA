package com.example.siamobal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private TableLayout tableMembership; // Ganti RecyclerView dengan TableLayout
    private List<Membership> membershipList;
    private static final String URL_GET_MEMBERSHIPS = "https://kevindinata.my.id/SIALAN/get_memberships.php"; // Ganti dengan URL server Anda
    private static final int ADD_MEMBERSHIP_REQUEST_CODE = 1; // Kode permintaan untuk menambah membership

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        tableMembership = findViewById(R.id.tableMembership);
        Button btnTambahMembership = findViewById(R.id.btnTambahMembership);

        // Inisialisasi daftar membership
        membershipList = new ArrayList<>();

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
                            // Log respons JSON
                            Log.d("MembershipActivity", "Response: " + response.toString());
                            tableMembership.removeAllViews(); // Clear existing rows

                            // Buat header table
                            TableRow headerRow = new TableRow(MembershipActivity.this);
                            String[] header = {"Nama Membership", "Harga", "Potongan (%)", "Status", "Action"};
                            for (String title : header) {
                                TextView textView = new TextView(MembershipActivity.this);
                                textView.setText(title);
                                textView.setTextSize(16);
                                textView.setPadding(8, 8, 8, 8);
                                headerRow.addView(textView);
                            }
                            tableMembership.addView(headerRow); // Tambahkan header ke tabel

                            for (int i = 0; i < response.length(); i++) {
                                // Ambil data sesuai dengan kunci yang ada
                                String id = response.getJSONObject(i).getString("id"); // Ambil ID
                                String nama = response.getJSONObject(i).getString("nama");
                                String harga = response.getJSONObject(i).getString("harga");
                                String potongan = response.getJSONObject(i).getString("potongan"); // Ambil potongan
                                String status = response.getJSONObject(i).getString("status");

                                // Menentukan status
                                String statusText = status.equals("1") ? "Aktif" : "Tidak Aktif";

                                // Tambahkan ke daftar membership
                                membershipList.add(new Membership(id, nama, "Rp. " + harga, statusText, potongan));
                                addButtonRow(nama, "Rp. " + harga, potongan, statusText, id); // Menambahkan baris data ke tabel
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MembershipActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void addButtonRow(String nama, String harga, String potongan, String status, String id) {
        TableRow row = new TableRow(this);

        TextView tvNama = new TextView(this);
        TextView tvHarga = new TextView(this);
        TextView tvPotongan = new TextView(this); // TextView untuk Potongan
        TextView tvStatus = new TextView(this);
        Button btnEdit = new Button(this);

        tvNama.setText(nama);
        tvHarga.setText(harga);
        tvPotongan.setText(potongan + "%"); // Tambahkan format persentase
        tvStatus.setText(status);
        btnEdit.setText("Edit");

        // Set action button (Edit)
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(MembershipActivity.this, EditMembershipActivity.class);
            intent.putExtra("MEMBERSHIP_ID", id); // Kirim ID ke EditMembershipActivity
            intent.putExtra("NAMA", nama);
            intent.putExtra("HARGA", harga);
            intent.putExtra("POTONGAN", potongan);
            intent.putExtra("STATUS", status);// Kirim potongan ke EditMembershipActivity
            startActivityForResult(intent, ADD_MEMBERSHIP_REQUEST_CODE); // Kembali untuk memuat ulang
        });

        // Set layout parameters untuk membatasi lebar kolom
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tvNama.setLayoutParams(params);
        tvHarga.setLayoutParams(params);
        tvPotongan.setLayoutParams(params);
        tvStatus.setLayoutParams(params);
        btnEdit.setLayoutParams(params);

        row.addView(tvNama);
        row.addView(tvHarga);
        row.addView(tvPotongan); // Menambahkan kolom potongan ke baris
        row.addView(tvStatus);
        row.addView(btnEdit);

        tableMembership.addView(row); // Tambahkan baris ke tabel
    }
}