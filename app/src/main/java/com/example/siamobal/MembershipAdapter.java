package com.example.siamobal;

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
        holder.textViewHarga.setText(membership.getHarga());
        holder.textViewStatus.setText(membership.getStatus());

        // Set action button (Edit)
        holder.buttonEdit.setOnClickListener(v -> {
            // Logika untuk edit membership
        });
    }

    @Override
    public int getItemCount() {
        return membershipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNama, textViewHarga, textViewStatus;
        Button buttonEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNama = itemView.findViewById(R.id.textViewNama);
            textViewHarga = itemView.findViewById(R.id.textViewHarga);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}