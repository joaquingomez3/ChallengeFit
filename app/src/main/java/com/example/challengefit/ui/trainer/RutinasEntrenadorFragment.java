package com.example.challengefit.ui.trainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import java.util.ArrayList;

public class RutinasEntrenadorFragment extends Fragment {

    private RutinasEntrenadorViewModel mViewModel;
    private RecyclerView rvRutinas;
    private RoutineAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutinas_entrenador, container, false);

        rvRutinas = root.findViewById(R.id.rvRutinasEntrenador);
        rvRutinas.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new RoutineAdapter(new ArrayList<>());
        rvRutinas.setAdapter(adapter);

        // CONFIGURACIÓN DEL BOTÓN NUEVA RUTINA
        View btnNuevaRutina = root.findViewById(R.id.btnNuevaRutina);
        if (btnNuevaRutina != null) {
            btnNuevaRutina.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_new_routine));
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mViewModel = new ViewModelProvider(this).get(RutinasEntrenadorViewModel.class);

        mViewModel.getLista().observe(getViewLifecycleOwner(), rutinas -> {
            if (rutinas != null) {
                adapter.setRutinas(rutinas);
            }
        });

        mViewModel.cargarRutinas();
    }
}
