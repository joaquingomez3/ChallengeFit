package com.example.challengefit.ui.trainer;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Usuario;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Usuario> studentList;
    private OnStudentActionListener listener;

    public interface OnStudentActionListener {
        void onAsignarRutina(Usuario student);
        void onAsignarDesafio(Usuario student);
    }

    public StudentAdapter(List<Usuario> studentList, OnStudentActionListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    public void setAlumnos(List<Usuario> alumnos) {
        this.studentList = alumnos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Usuario student = studentList.get(position);
        
        holder.tvName.setText(student.getNombre());
        holder.tvObjective.setText("Objetivo: " + (student.getObjetivo() != null ? student.getObjetivo() : "Sin definir"));
        
        // Calcular estadísticas de ejercicios
        int completados = 0;
        int totales = 0;
        if (student.getRutinas() != null && !student.getRutinas().isEmpty()) {
            for (Usuario.RutinaProgreso rp : student.getRutinas()) {
                completados += rp.getEjerciciosCompletados();
                totales += rp.getTotalEjercicios();
            }
            holder.tvStats.setText(completados + " de " + totales + " ejercicios completados");
        } else {
            holder.tvStats.setText("Sin rutinas asignadas");
        }

        // PROGRESO CON ANIMACIÓN
        int progress = student.getPorcentajeGeneral();
        
        // CORRECCIÓN: Si totales es 0 y porcentajeGeneral es 100 (posible error API), mostramos 0
        if (totales == 0) progress = 0;
        
        animateProgressBar(holder.pbProgress, progress);
        
        // COLOR DINÁMICO DEL TEXTO
        holder.tvPercent.setText(progress + "%");
        if (progress >= 70) {
            holder.tvPercent.setTextColor(Color.parseColor("#4CAF50")); // Verde
        } else if (progress >= 30) {
            holder.tvPercent.setTextColor(Color.parseColor("#FF9800")); // Naranja
        } else {
            holder.tvPercent.setTextColor(Color.parseColor("#F44336")); // Rojo
        }

        // NAVEGACIÓN AL DETALLE
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("alumnoId", student.getId());
            bundle.putString("alumnoNombre", student.getNombre());
            Navigation.findNavController(v).navigate(R.id.navigation_detalle_alumno, bundle);
        });

        holder.btnRutina.setOnClickListener(v -> listener.onAsignarRutina(student));
        holder.btnDesafio.setOnClickListener(v -> listener.onAsignarDesafio(student));
    }

    private void animateProgressBar(ProgressBar progressBar, int to) {
        // Aseguramos que empiece desde 0 para cada bind
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, to);
        animation.setDuration(800);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvObjective, tvStats, tvPercent;
        ProgressBar pbProgress;
        MaterialButton btnRutina, btnDesafio;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvObjective = itemView.findViewById(R.id.tvStudentObjective);
            tvStats = itemView.findViewById(R.id.tvExerciseStats);
            tvPercent = itemView.findViewById(R.id.tvProgressPercent);
            pbProgress = itemView.findViewById(R.id.pbStudentProgress);
            btnRutina = itemView.findViewById(R.id.btnAsignarRutina);
            btnDesafio = itemView.findViewById(R.id.btnAsignarDesafio);
        }
    }
}
