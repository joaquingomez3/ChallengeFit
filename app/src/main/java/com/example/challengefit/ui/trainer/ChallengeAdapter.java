package com.example.challengefit.ui.trainer;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Desafio;
import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private List<Desafio> challengeList;

    public ChallengeAdapter(List<Desafio> challengeList) {
        this.challengeList = challengeList;
    }

    public void setDesafios(List<Desafio> desafios) {
        this.challengeList = desafios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_challenge, parent, false);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Desafio desafio = challengeList.get(position);
        holder.tvTitle.setText(desafio.getTitulo());
        holder.tvDescription.setText(desafio.getDescripcion());
        
        // Manejo del estado (Activo/Finalizado)
        String estado = desafio.getEstado();
        if (estado == null) estado = "Activo"; // Por defecto si viene nulo
        
        holder.tvStatus.setText(estado);
        if (estado.equalsIgnoreCase("Finalizado")) {
            holder.tvStatus.setTextColor(Color.parseColor("#B3B3B3")); // Gris
            holder.tvStatus.setBackgroundResource(R.drawable.rounded_input_background);
            holder.tvStatus.getBackground().setTint(Color.parseColor("#333333"));
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#81F047")); // Verde neón
            holder.tvStatus.setBackgroundResource(R.drawable.rounded_input_background);
            holder.tvStatus.getBackground().setTint(Color.parseColor("#1A2A3A"));
        }
    }

    @Override
    public int getItemCount() {
        return challengeList != null ? challengeList.size() : 0;
    }

    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvStatus;

        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text_challenge_title);
            tvDescription = itemView.findViewById(R.id.text_challenge_description);
            tvStatus = itemView.findViewById(R.id.text_status);
        }
    }
}
