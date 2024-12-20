package com.example.siamobal;

public class Customer {
    private String namaCustomer;
    private String namaMembership;
    private String timestampHabis;

    public Customer(String namaCustomer, String namaMembership, String timestampHabis) {
        this.namaCustomer = namaCustomer;
        this.namaMembership = namaMembership;
        this.timestampHabis = timestampHabis;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public String getNamaMembership() {
        return namaMembership;
    }

    public String getTimestampHabis() {
        return timestampHabis;
    }
}