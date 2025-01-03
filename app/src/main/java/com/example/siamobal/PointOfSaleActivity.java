package com.example.siamobal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

public class PointOfSaleActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private EditText editTextTotal;
    private EditText editTextPotongan;
    private EditText editTextGrandTotal;
    private Button buttonSimpan;
    private Spinner spinnerCustomer;
    private static final String URL_GET_DATA = "https://kevindinata.my.id/SIALAN/get_barang_pos.php";
    private static final String URL_GET_CUSTOMERS = "https://kevindinata.my.id/SIALAN/get_customers_pos.php"; // URL untuk mendapatkan customer
    private static final String URL_GET_DISCOUNT = "https://kevindinata.my.id/SIALAN/get_discount_pos.php"; // URL untuk mendapatkan potongan
    private static final String URL_SAVE_RECEIPT = "https://kevindinata.my.id/SIALAN/save_receipt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_sale);

        tableLayout = findViewById(R.id.tableLayout);
        editTextTotal = findViewById(R.id.editTextTotal);
        editTextPotongan = findViewById(R.id.editTextPotongan);
        editTextGrandTotal = findViewById(R.id.editTextGrandTotal);
        buttonSimpan = findViewById(R.id.buttonSimpan);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);

        loadBarang();
        loadCustomers(); // Load customers when the activity starts

        buttonSimpan.setOnClickListener(v -> saveReceipt());
    }

    private void loadBarang() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DATA,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject barangObject = jsonArray.getJSONObject(i);
                            String kdBarang = barangObject.getString("KD_BARANG");
                            String namaBarang = barangObject.getString("NAMA_BARANG");
                            String hargaBarang = barangObject.getString("HARGA_BARANG");

                            TableRow tableRow = new TableRow(PointOfSaleActivity.this);
                            TextView textViewNama = new TextView(PointOfSaleActivity.this);
                            TextView textViewHarga = new TextView(PointOfSaleActivity.this);
                            TextView textViewJumlah = new TextView(PointOfSaleActivity.this);
                            Button buttonMinus = new Button(PointOfSaleActivity.this);
                            Button buttonPlus = new Button(PointOfSaleActivity.this);

                            int[] jumlahBeli = {0};

                            textViewNama.setText(namaBarang);
                            textViewHarga.setText("Rp. " + hargaBarang);
                            textViewJumlah.setText(String.valueOf(jumlahBeli[0]));
                            buttonMinus.setText("-");
                            buttonPlus.setText("+");

                            buttonMinus.setOnClickListener(v -> {
                                if (jumlahBeli[0] > 0) {
                                    jumlahBeli[0]--;
                                    textViewJumlah.setText(String.valueOf(jumlahBeli[0]));
                                    calculateTotal();
                                }
                            });

                            buttonPlus.setOnClickListener(v -> {
                                jumlahBeli[0]++;
                                textViewJumlah.setText(String.valueOf(jumlahBeli[0]));
                                calculateTotal();
                            });

                            tableRow.addView(textViewNama);
                            tableRow.addView(textViewHarga);
                            tableRow.addView(buttonMinus);
                            tableRow.addView(textViewJumlah);
                            tableRow.addView(buttonPlus);
                            tableLayout.addView(tableRow);
                            tableRow.setTag(kdBarang);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PointOfSaleActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PointOfSaleActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadCustomers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_CUSTOMERS,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        ArrayList<String> customerList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject customerObject = jsonArray.getJSONObject(i);
                            String nikCustomer = customerObject.getString("NIK_CUTOMER");
                            String namaCustomer = customerObject.getString("NAMA_CUSTOMER");
                            customerList.add(nikCustomer + " - " + namaCustomer);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, customerList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCustomer.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PointOfSaleActivity.this, "Gagal memuat customer", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PointOfSaleActivity.this, "Gagal memuat customer", Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void calculateTotal() {
        double total = 0;
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            TextView textViewHarga = (TextView) row.getChildAt(1);
            TextView textViewJumlah = (TextView) row.getChildAt(3);

            if (textViewHarga != null && textViewJumlah != null) {
                String hargaString = textViewHarga.getText().toString().replace("Rp. ", "").replace(".", "").trim();
                String jumlahString = textViewJumlah.getText().toString();

                if (!jumlahString.equals("0")) {
                    total += Double.parseDouble(hargaString) * Integer.parseInt(jumlahString);
                }
            }
        }
        editTextTotal.setText("Rp. " + String.format("%,d", (int) total));
        calculateDiscount(total); // Hitung potongan setelah total
    }

    private void calculateDiscount(double total) {
        // Ambil NIK_CUSTOMER yang dipilih dari spinner
        String selectedCustomer = spinnerCustomer.getSelectedItem().toString();
        String nikCustomer = selectedCustomer.split(" - ")[0]; // Ambil NIK_CUTOMER

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DISCOUNT,
                response -> {
                    try {
                        JSONObject discountObject = new JSONObject(response);
                        double potonganPersen = discountObject.getDouble("POTONGAN_"); // Ambil potongan persen
                        double potongan = total * (potonganPersen / 100);
                        double grandTotal = total - potongan;

                        editTextPotongan.setText("Rp. " + String.format("%,d", (int) potongan));
                        editTextGrandTotal.setText("Rp. " + String.format("%,d", (int) grandTotal));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PointOfSaleActivity.this, "Gagal menghitung potongan", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PointOfSaleActivity.this, "Gagal mendapatkan diskon", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NIK_CUTOMER", nikCustomer); // Kirim NIK_CUTOMER untuk mendapatkan potongan
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveReceipt() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String nikKaryawan = sharedPreferences.getString("nik_karyawan", "");
        // Ambil NIK_CUSTOMER yang dipilih dari spinner
        String selectedCustomer = spinnerCustomer.getSelectedItem().toString();
        String nikCustomer = selectedCustomer.split(" - ")[0];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_RECEIPT,
                response -> Toast.makeText(PointOfSaleActivity.this, response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(PointOfSaleActivity.this, "Gagal menyimpan nota", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NIK_KARYAWAN", nikKaryawan);
                params.put("NIK_CUSTOMER", nikCustomer);
                params.put("GRAND_TOTAL", String.valueOf(calculateFinalTotal()));

                // Kumpulan detail nota jual
                try {
                    JSONArray detailArray = new JSONArray();
                    for (int i = 1; i < tableLayout.getChildCount(); i++) {
                        TableRow row = (TableRow) tableLayout.getChildAt(i);
                        String kdBarang = (String) row.getTag();
                        String jumlahBeli = ((TextView) row.getChildAt(3)).getText().toString();

                        JSONObject detailObject = new JSONObject();
                        detailObject.put("KD_BARANG", kdBarang);
                        detailObject.put("JUMLAH", jumlahBeli);

                        detailArray.put(detailObject);
                    }
                    params.put("DETAIL_NOTA_JUAL", detailArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private int calculateFinalTotal() {
        String totalString = editTextGrandTotal.getText().toString().replace("Rp. ", "").replace(".", "").trim();
        return Integer.parseInt(totalString);
    }
}