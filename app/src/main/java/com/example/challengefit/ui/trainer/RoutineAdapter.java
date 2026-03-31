package com.example.challengefit.ui.trainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Rutina;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private List<Rutina> routineList;

    public RoutineAdapter(List<Rutina> routineList) {
        this.routineList = routineList;
    }

    public void setRutinas(List<Rutina> rutinas) {
        this.routineList = rutinas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        Rutina rutina = routineList.get(position);
        holder.tvRoutineName.setText(rutina.getNombre());
        
        int cantEjercicios = (rutina.getRutinaEjercicios() != null) ? rutina.getRutinaEjercicios().size() : 0;
        holder.tvExerciseCount.setText(cantEjercicios + " ejercicios");

        // Acción de clic para navegar al detalle
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("rutinaId", rutina.getId());
            Navigation.findNavController(v).navigate(R.id.navigation_detalle_rutina, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return routineList != null ? routineList.size() : 0;
    }

    public static class RoutineViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoutineName, tvExerciseCount;

        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoutineName = itemView.findViewById(R.id.text_routine_name);
            tvExerciseCount = itemView.findViewById(R.id.text_exercise_count);
        }
    }
}
