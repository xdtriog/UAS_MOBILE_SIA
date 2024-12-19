package com.example.siamobal; // Ganti dengan nama paket Anda

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.ViewHolder> {
    private List<Membership> membershipList;

    public MembershipAdapter(List<Membership> membershipList) {
        this.membershipList = membershipList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_membership, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Membership membership = membershipList.get(position);
        holder.textViewNama.setText(membership.getNama());
        holder.textViewStatus.setText(membership.getStatus());

        holder.btnEdit.setOnClickListener(v -> {
            // Tambahkan logika untuk mengedit membership
        });
    }

    @Override
    public int getItemCount() {
        return membershipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNama, textViewStatus;
        Button btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNama = itemView.findViewById(R.id.textViewNama);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}