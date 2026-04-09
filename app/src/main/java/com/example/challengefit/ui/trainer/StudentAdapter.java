package com.example.challengefit.ui.trainer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
        
        holder.btnRutina.setOnClickListener(v -> listener.onAsignarRutina(student));
        holder.btnDesafio.setOnClickListener(v -> listener.onAsignarDesafio(student));
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        MaterialButton btnRutina, btnDesafio;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            btnRutina = itemView.findViewById(R.id.btnAsignarRutina);
            btnDesafio = itemView.findViewById(R.id.btnAsignarDesafio);
        }
    }
}
