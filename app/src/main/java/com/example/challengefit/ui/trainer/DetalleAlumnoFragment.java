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
import com.example.challengefit.databinding.FragmentDetalleAlumnoBinding;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class DetalleAlumnoFragment extends Fragment {

    private FragmentDetalleAlumnoBinding binding;
    private DetalleAlumnoViewModel mViewModel;
    private RoutineAdapter adapter;
    private int alumnoId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleAlumnoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleAlumnoViewModel.class);

        if (getArguments() != null) {
            alumnoId = getArguments().getInt("alumnoId");
            binding.tvNombreAlumnoDetalle.setText(getArguments().getString("alumnoNombre", "Detalle del Alumno"));
        }

        binding.rvRutinasDetalleAlumno.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutineAdapter(new ArrayList<>(), true);
        binding.rvRutinasDetalleAlumno.setAdapter(adapter);

        binding.btnBackDetalleAlumno.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.tabLayoutAlumno.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cargarDatosPorTab(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        mViewModel.getRutinasActivas().observe(getViewLifecycleOwner(), rutinas -> {
            if (binding.tabLayoutAlumno.getSelectedTabPosition() == 0) {
                adapter.setRutinas(rutinas);
                binding.tvEmptyRutinas.setVisibility(rutinas.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        mViewModel.getRutinasCompletadas().observe(getViewLifecycleOwner(), rutinas -> {
            if (binding.tabLayoutAlumno.getSelectedTabPosition() == 1) {
                adapter.setRutinas(rutinas);
                binding.tvEmptyRutinas.setVisibility(rutinas.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        // Carga inicial
        cargarDatosPorTab(0);
    }

    private void cargarDatosPorTab(int position) {
        String estado = (position == 0) ? "activas" : "completadas";
        mViewModel.cargarRutinasPorEstado(alumnoId, estado);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
