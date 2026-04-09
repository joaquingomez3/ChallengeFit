package com.example.challengefit.ui.trainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Desafio;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.modelos.Usuario;
import java.util.ArrayList;
import java.util.List;

public class AlumnosFragment extends Fragment {

    private AlumnosViewModel mViewModel;
    private RecyclerView rvAlumnos;
    private StudentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alumnos, container, false);

        rvAlumnos = root.findViewById(R.id.rvAlumnos);
        rvAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));

        // CORRECCIÓN: Inicializamos con la lista y el listener de acciones
        adapter = new StudentAdapter(new ArrayList<>(), new StudentAdapter.OnStudentActionListener() {
            @Override
            public void onAsignarRutina(Usuario student) {
                mostrarSelectorRutinas(student);
            }

            @Override
            public void onAsignarDesafio(Usuario student) {
                mostrarSelectorDesafios(student);
            }
        });
        rvAlumnos.setAdapter(adapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mViewModel = new ViewModelProvider(this).get(AlumnosViewModel.class);

        mViewModel.getLista().observe(getViewLifecycleOwner(), alumnos -> {
            if (alumnos != null) {
                adapter.setAlumnos(alumnos);
            }
        });

        // Observar mensajes de éxito en asignación
        mViewModel.getMensajeExito().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        mViewModel.cargarAlumnos();
        mViewModel.cargarRutinasYDesafios(); // Precargamos las opciones para los diálogos
    }

    private void mostrarSelectorRutinas(Usuario student) {
        List<Rutina> rutinas = mViewModel.getRutinasDisponibles().getValue();
        if (rutinas == null || rutinas.isEmpty()) {
            Toast.makeText(getContext(), "No tienes rutinas creadas", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] nombres = new String[rutinas.size()];
        for (int i = 0; i < rutinas.size(); i++) nombres[i] = rutinas.get(i).getNombre();

        new AlertDialog.Builder(requireContext())
                .setTitle("Asignar Rutina a " + student.getNombre())
                .setItems(nombres, (dialog, which) -> {
                    int idRutina = rutinas.get(which).getId();
                    mViewModel.asignarRutina(student.getId(), idRutina);
                })
                .show();
    }

    private void mostrarSelectorDesafios(Usuario student) {
        List<Desafio> desafios = mViewModel.getDesafiosDisponibles().getValue();
        if (desafios == null || desafios.isEmpty()) {
            Toast.makeText(getContext(), "No tienes desafíos creados", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] nombres = new String[desafios.size()];
        for (int i = 0; i < desafios.size(); i++) nombres[i] = desafios.get(i).getTitulo();

        new AlertDialog.Builder(requireContext())
                .setTitle("Asignar Desafío a " + student.getNombre())
                .setItems(nombres, (dialog, which) -> {
                    int idDesafio = desafios.get(which).getId();
                    mViewModel.asignarDesafio(student.getId(), idDesafio);
                })
                .show();
    }
}
