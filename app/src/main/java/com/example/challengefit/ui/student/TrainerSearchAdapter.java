package com.example.challengefit.ui.student;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Usuario;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.List;

public class TrainerSearchAdapter extends RecyclerView.Adapter<TrainerSearchAdapter.TrainerViewHolder> {

    private List<Usuario> trainers;
    private OnTrainerClickListener listener;

    public interface OnTrainerClickListener {
        void onSendRequest(Usuario trainer);
    }

    public TrainerSearchAdapter(List<Usuario> trainers, OnTrainerClickListener listener) {
        this.trainers = trainers;
        this.listener = listener;
    }

    public void setTrainers(List<Usuario> trainers) {
        this.trainers = trainers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainer_search, parent, false);
        return new TrainerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerViewHolder holder, int position) {
        Usuario trainer = trainers.get(position);
        holder.tvName.setText(trainer.getNombre());
        
        // Limpiamos los chips anteriores para evitar duplicados al reciclar vistas
        holder.cgSpecialties.removeAllViews();
        
        if (trainer.getEspecialidades() != null) {
            for (Usuario.Especialidad esp : trainer.getEspecialidades()) {
                Chip chip = new Chip(holder.itemView.getContext());
                chip.setText(esp.getNombre());
                chip.setChipBackgroundColorResource(R.color.card_dark);
                chip.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.accent_green));
                chip.setChipStrokeColorResource(R.color.accent_green);
                chip.setChipStrokeWidth(1f);
                chip.setTextSize(10);
                holder.cgSpecialties.addView(chip);
            }
        }

        // RESETEAR EL BOTÓN (por si se recicla la vista)
        holder.btnSend.setText("Enviar Solicitud");
        holder.btnSend.setEnabled(true);
        holder.btnSend.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.accent_green));

        holder.btnSend.setOnClickListener(v -> {
            // CAMBIO VISUAL INMEDIATO
            holder.btnSend.setText("PENDIENTE");
            holder.btnSend.setEnabled(false);
            holder.btnSend.setBackgroundColor(Color.parseColor("#333333")); // Gris oscuro
            
            listener.onSendRequest(trainer);
        });
    }

    @Override
    public int getItemCount() {
        return trainers != null ? trainers.size() : 0;
    }

    public static class TrainerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ChipGroup cgSpecialties;
        Button btnSend;

        public TrainerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTrainerName);
            cgSpecialties = itemView.findViewById(R.id.cgSpecialties);
            btnSend = itemView.findViewById(R.id.btnSendRequest);
        }
    }
}
