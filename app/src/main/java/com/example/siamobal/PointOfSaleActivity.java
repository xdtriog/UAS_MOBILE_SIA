package com.example.siamobal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private EditText editTextDP; // Field DP
    private Button buttonSimpan;
    private Spinner spinnerCustomer;
    private Spinner spinnerMetodePembayaran; // Dropdown metode pembayaran
    private static final String URL_GET_DATA = "https://kevindinata.my.id/SIALAN/get_barang_pos.php";
    private static final String URL_GET_CUSTOMERS = "https://kevindinata.my.id/SIALAN/get_customers_pos.php"; // URL untuk pelanggan
    private static final String URL_GET_DISCOUNT = "https://kevindinata.my.id/SIALAN/get_discount_pos.php";
    private static final String URL_GET_PAYMENT_METHODS = "https://kevindinata.my.id/SIALAN/get_metode_pembayaran_pos.php"; // URL untuk metode pembayaran
    private static final String URL_SAVE_RECEIPT = "https://kevindinata.my.id/SIALAN/save_receipt.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_sale);

        tableLayout = findViewById(R.id.tableLayout);
        editTextTotal = findViewById(R.id.editTextTotal);
        editTextPotongan = findViewById(R.id.editTextPotongan);
        editTextGrandTotal = findViewById(R.id.editTextGrandTotal);
        editTextDP = findViewById(R.id.editTextDP);
        buttonSimpan = findViewById(R.id.buttonSimpan);
        spinnerCustomer = findViewById(R.id.spinnerCustomer);
        spinnerMetodePembayaran = findViewById(R.id.spinnerMetodePembayaran); // Inisialisasi spinner metode pembayaran

        loadBarang();
        loadCustomers();
        loadPaymentMethods(); // Load metode pembayaran saat activity dimulai

        spinnerMetodePembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMethod = (String) spinnerMetodePembayaran.getSelectedItem();
                if (selectedMethod != null) {
                    if (selectedMethod.startsWith("Hutang")) {
                        editTextDP.setEnabled(true);
                        editTextDP.setFocusable(true);
                        editTextDP.setFocusableInTouchMode(true); // Tambahkan ini untuk memungkinkan editing
                        editTextDP.requestFocus(); // Set fokus pada field DP
                    } else {
                        editTextDP.setEnabled(false);
                        editTextDP.setFocusable(false);
                        editTextDP.setFocusableInTouchMode(false); // Kendalikan editable mode
                        editTextDP.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle case when nothing is selected if necessary
            }
        });

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

    private void loadPaymentMethods() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PAYMENT_METHODS,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        ArrayList<String> paymentMethodList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject paymentMethodObject = jsonArray.getJSONObject(i);
                            String idMmpn = paymentMethodObject.getString("ID_MMPN");
                            String namaMppn = paymentMethodObject.getString("NAMA_MPPN");
                            paymentMethodList.add(namaMppn + " - " + idMmpn);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, paymentMethodList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMetodePembayaran.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PointOfSaleActivity.this, "Gagal memuat metode pembayaran", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(PointOfSaleActivity.this, "Gagal memuat metode pembayaran", Toast.LENGTH_SHORT).show());

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
        calculateDiscount(total);
    }

    private void calculateDiscount(double total) {
        String selectedCustomer = spinnerCustomer.getSelectedItem().toString();
        String nikCustomer = selectedCustomer.split(" - ")[0];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DISCOUNT,
                response -> {
                    try {
                        JSONObject discountObject = new JSONObject(response);
                        double potonganPersen = discountObject.getDouble("POTONGAN_");
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
                params.put("NIK_CUTOMER", nikCustomer);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveReceipt() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String nikKaryawan = sharedPreferences.getString("nik_karyawan", "");

        // Ambil customer yang dipilih
        String selectedCustomer = spinnerCustomer.getSelectedItem().toString();
        String nikCustomer = selectedCustomer.split(" - ")[0];

        // Ambil metode pembayaran yang terpilih
        String selectedPaymentMethod = spinnerMetodePembayaran.getSelectedItem().toString();
        String idMmpn = selectedPaymentMethod.split(" - ")[1]; // Ambil ID_MMPN

        String dp = editTextDP.getText().toString().replace("Rp. ", "").replace(".", "").trim(); // Ambil DP dari EditText

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_RECEIPT,
                response -> Toast.makeText(PointOfSaleActivity.this, response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(PointOfSaleActivity.this, "Gagal menyimpan nota", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NIK_KARYAWAN", nikKaryawan);
                params.put("NIK_CUSTOMER", nikCustomer);
                params.put("ID_MMPN", idMmpn); // Send ID_MMPN for the payment method
                params.put("GRAND_TOTAL", String.valueOf(calculateFinalTotal()));
                params.put("DP", dp); // Tambahkan DP ke dalam parameters

                try {
                    JSONArray detailArray = new JSONArray();
                    for (int i = 1; i < tableLayout.getChildCount(); i++) {
                        TableRow row = (TableRow) tableLayout.getChildAt(i);
                        String kdBarang = (String) row.getTag();
                        String jumlahBeli = ((TextView) row.getChildAt(3)).getText().toString();

                        if (!jumlahBeli.equals("0")) { // Hanya ambil barang dengan jumlah lebih dari 0
                            JSONObject detailObject = new JSONObject();
                            detailObject.put("KD_BARANG", kdBarang);
                            detailObject.put("JUMLAH", jumlahBeli);
                            detailArray.put(detailObject);
                        }
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