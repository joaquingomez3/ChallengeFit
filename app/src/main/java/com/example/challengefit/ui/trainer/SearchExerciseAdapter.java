package com.example.challengefit.ui.trainer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.modelos.Ejercicio;
import java.util.List;

public class SearchExerciseAdapter extends RecyclerView.Adapter<SearchExerciseAdapter.ViewHolder> {

    private List<Ejercicio> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Ejercicio ejercicio);
    }

    public SearchExerciseAdapter(List<Ejercicio> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<Ejercicio> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ejercicio ej = list.get(position);
        holder.text.setText(ej.getNombre() + " (" + ej.getGrupoMuscular() + ")");
        holder.text.setTextColor(android.graphics.Color.WHITE);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(ej));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
        }
    }
}
