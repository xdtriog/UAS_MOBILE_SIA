package com.example.siamobal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditCustomerActivity extends AppCompatActivity {

    private EditText etNIKCustomer, etNamaCustomer, etEmail;
    private Button btnSimpan;
    private String nikCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        etNIKCustomer = findViewById(R.id.et_nik_customer);
        etNamaCustomer = findViewById(R.id.et_nama_customer);
        etEmail = findViewById(R.id.et_email);
        btnSimpan = findViewById(R.id.btn_simpan);

        // Ambil data dari Intent
        nikCustomer = getIntent().getStringExtra("NIK_CUTOMER");
        String namaCustomer = getIntent().getStringExtra("NAMA_CUSTOMER");
        String email = getIntent().getStringExtra("EMAIL");

        // Mengatur nilai ke EditText
        etNIKCustomer.setText(nikCustomer);
        etNIKCustomer.setEnabled(false);  // NIK tidak bisa diubah
        etNamaCustomer.setText(namaCustomer);
        etEmail.setText(email);

        btnSimpan.setOnClickListener(v -> updateCustomer());
    }

    private void updateCustomer() {
        String url = "https://kevindinata.my.id/SIALAN/update_customer.php"; // Ganti dengan URL yang sesuai

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            Toast.makeText(EditCustomerActivity.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Set hasil untuk kembali
                            finish(); // Kembali ke aktivitas sebelumnya
                        } else {
                            Toast.makeText(EditCustomerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EditCustomerActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(EditCustomerActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("NIK_CUTOMER", nikCustomer);
                params.put("NAMA_CUSTOMER", etNamaCustomer.getText().toString());
                params.put("EMAIL", etEmail.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}