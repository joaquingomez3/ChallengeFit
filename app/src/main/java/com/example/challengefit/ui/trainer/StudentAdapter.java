package com.example.challengefit.ui.trainer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Usuario;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Usuario> studentList;

    public StudentAdapter(List<Usuario> studentList) {
        this.studentList = studentList;
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
        
        // Usamos el progreso real que viene de la API
        int progressValue = student.getProgreso();
        holder.pbProgress.setProgress(progressValue);
        holder.tvPercent.setText(progressValue + "%");
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPercent;
        ProgressBar pbProgress;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvPercent = itemView.findViewById(R.id.tvProgressPercent);
            pbProgress = itemView.findViewById(R.id.pbStudentProgress);
        }
    }
}
