package com.example.challengefit.ui.trainer;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.RutinaEjercicio;
import java.util.List;

public class SelectedExercisesAdapter extends RecyclerView.Adapter<SelectedExercisesAdapter.ViewHolder> {

    private List<RutinaEjercicio> list;

    public SelectedExercisesAdapter(List<RutinaEjercicio> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_exercise_input, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RutinaEjercicio item = list.get(position);
        holder.tvName.setText(item.getEjercicio().getNombre());

        // Remover de la lista
        holder.btnRemove.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });

        // Listeners para guardar los valores mientras el usuario escribe
        holder.etSeries.setText(item.getSeries() > 0 ? String.valueOf(item.getSeries()) : "");
        holder.etSeries.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(String s) {
                if (!s.isEmpty()) item.setSeries(Integer.parseInt(s));
            }
        });

        holder.etReps.setText(item.getRepeticiones() > 0 ? String.valueOf(item.getRepeticiones()) : "");
        holder.etReps.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(String s) {
                if (!s.isEmpty()) item.setRepeticiones(Integer.parseInt(s));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        EditText etSeries, etReps, etTime;
        ImageView btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExName);
            etSeries = itemView.findViewById(R.id.etExSeries);
            etReps = itemView.findViewById(R.id.etExReps);
            etTime = itemView.findViewById(R.id.etExTime);
            btnRemove = itemView.findViewById(R.id.btnRemoveEx);
        }
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextChanged(s.toString());
        }
        @Override
        public void afterTextChanged(Editable s) {}
        public abstract void onTextChanged(String s);
    }
}
