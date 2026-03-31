package com.example.challengefit.ui.trainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import java.util.ArrayList;

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

        // Inicializamos el adaptador con una lista vacía
        adapter = new StudentAdapter(new ArrayList<>());
        rvAlumnos.setAdapter(adapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mViewModel = new ViewModelProvider(this).get(AlumnosViewModel.class);

        // Observamos los cambios en la lista de alumnos
        mViewModel.getLista().observe(getViewLifecycleOwner(), alumnos -> {
            if (alumnos != null) {
                adapter.setAlumnos(alumnos);
            }
        });

        // Pedimos al ViewModel que cargue los datos reales desde la API
        mViewModel.cargarAlumnos();
    }
}
