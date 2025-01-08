package com.example.siamobal;

// Model class for Customer
public class Customers {
    private String nikCustomer;
    private String namaCustomer;
    private String email;

    public Customers(String nikCustomer, String namaCustomer, String email) {
        this.nikCustomer = nikCustomer;
        this.namaCustomer = namaCustomer;
        this.email = email;
    }

    public String getNikCustomer() {
        return nikCustomer;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public String getEmail() {
        return email;
    }
}