package com.example.challengefit.ui.trainer;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.RutinaEjercicio;
import com.example.challengefit.request.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<RutinaEjercicio> exerciseList;
    private boolean esAlumno;
    private boolean canManage;

    public ExerciseAdapter(List<RutinaEjercicio> exerciseList, boolean esAlumno) {
        this.exerciseList = exerciseList;
        this.esAlumno = esAlumno;
        this.canManage = false;
    }

    public ExerciseAdapter(List<RutinaEjercicio> exerciseList, boolean esAlumno, boolean canManage) {
        this.exerciseList = exerciseList;
        this.esAlumno = esAlumno;
        this.canManage = canManage;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        RutinaEjercicio re = exerciseList.get(position);
        
        if (re.getEjercicio() != null) {
            holder.tvName.setText(re.getEjercicio().getNombre());
        }
        holder.tvSeries.setText(re.getSeries() + " Series x " + re.getRepeticiones() + " Reps");

        if (esAlumno) {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnComplete.setVisibility(View.VISIBLE);
            
            if (re.isCompletado()) {
                holder.btnComplete.setImageResource(android.R.drawable.checkbox_on_background);
                holder.btnComplete.setColorFilter(Color.parseColor("#13ec37"));
            } else {
                holder.btnComplete.setImageResource(android.R.drawable.checkbox_off_background);
                holder.btnComplete.setColorFilter(Color.GRAY);
            }

            holder.btnComplete.setOnClickListener(v -> {
                if (!re.isCompletado()) {
                    marcarComoCompletado(re, holder);
                }
            });
            holder.btnComplete.setEnabled(!re.isCompletado());
        } else {
            holder.btnComplete.setVisibility(View.GONE);
            if (canManage) {
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setOnClickListener(v -> eliminarEjercicio(re, position, holder));
                holder.btnEdit.setOnClickListener(v -> mostrarDialogoEditar(re, position, holder));
            } else {
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnEdit.setVisibility(View.GONE);
            }
        }
    }

    private void mostrarDialogoEditar(RutinaEjercicio re, int position, ExerciseViewHolder holder) {
        View view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.dialog_add_exercise_params, null);
        EditText etSeries = view.findViewById(R.id.etSeriesDialog);
        EditText etReps = view.findViewById(R.id.etRepsDialog);

        etSeries.setText(String.valueOf(re.getSeries()));
        etReps.setText(String.valueOf(re.getRepeticiones()));

        new AlertDialog.Builder(holder.itemView.getContext(), R.style.CustomAlertDialog)
                .setTitle("Editar Ejercicio")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    int series = Integer.parseInt(etSeries.getText().toString());
                    int reps = Integer.parseInt(etReps.getText().toString());
                    editarEjercicio(re, series, reps, position, holder);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void editarEjercicio(RutinaEjercicio re, int series, int reps, int position, ExerciseViewHolder holder) {
        String token = ApiClient.leerToken(holder.itemView.getContext());
        ApiClient.EditarEjercicioRequest request = new ApiClient.EditarEjercicioRequest(series, reps);
        
        ApiClient.getChallengeFitService().editarEjercicioEnRutina(token, re.getId(), request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    re.setSeries(series);
                    re.setRepeticiones(reps);
                    notifyItemChanged(position);
                    Toast.makeText(holder.itemView.getContext(), "Ejercicio actualizado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private void eliminarEjercicio(RutinaEjercicio re, int position, ExerciseViewHolder holder) {
        String token = ApiClient.leerToken(holder.itemView.getContext());
        ApiClient.getChallengeFitService().eliminarEjercicio(token, re.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    exerciseList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, exerciseList.size());
                    Toast.makeText(holder.itemView.getContext(), "Ejercicio eliminado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private void marcarComoCompletado(RutinaEjercicio re, ExerciseViewHolder holder) {
        String token = ApiClient.leerToken(holder.itemView.getContext());
        ApiClient.getChallengeFitService().completarEjercicio(token, re.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    re.setCompletado(true);
                    holder.btnComplete.setImageResource(android.R.drawable.checkbox_on_background);
                    holder.btnComplete.setColorFilter(Color.parseColor("#13ec37"));
                    holder.btnComplete.setEnabled(false);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList != null ? exerciseList.size() : 0;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSeries;
        ImageButton btnComplete, btnDelete, btnEdit;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExerciseName);
            tvSeries = itemView.findViewById(R.id.tvExerciseSeries);
            btnComplete = itemView.findViewById(R.id.btnCompleteExercise);
            btnDelete = itemView.findViewById(R.id.btnDeleteExercise);
            btnEdit = itemView.findViewById(R.id.btnEditExercise);
        }
    }
}
