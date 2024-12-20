package com.example.siamobal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customerList;

    public CustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.namaCustomer.setText(customer.getNamaCustomer());
        holder.namaMembership.setText(customer.getNamaMembership());
        holder.timestampHabis.setText(customer.getTimestampHabis());

        holder.buttonEdit.setOnClickListener(v -> {
            // Logika untuk mengedit customer
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView namaCustomer, namaMembership, timestampHabis;
        Button buttonEdit;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            namaCustomer = itemView.findViewById(R.id.textViewNamaCustomer);
            namaMembership = itemView.findViewById(R.id.textViewNamaMembership);
            timestampHabis = itemView.findViewById(R.id.textViewTimestampHabis);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}