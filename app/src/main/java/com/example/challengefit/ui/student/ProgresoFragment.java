package com.example.challengefit.ui.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.challengefit.databinding.FragmentProgresoBinding;
import com.example.challengefit.modelos.Usuario;
import java.util.ArrayList;

public class ProgresoFragment extends Fragment {

    private ProgresoViewModel mViewModel;
    private FragmentProgresoBinding binding;
    private TrainerSearchAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProgresoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProgresoViewModel.class);

        // Configurar RecyclerView
        binding.rvTrainerResults.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainerSearchAdapter(new ArrayList<>(), trainer -> {
            // Acción al tocar "Enviar Solicitud"
            mViewModel.enviarSolicitud(trainer.getId());
        });
        binding.rvTrainerResults.setAdapter(adapter);

        // Observar resultados del ViewModel
        mViewModel.getTrainers().observe(getViewLifecycleOwner(), trainers -> {
            if (trainers != null) {
                adapter.setTrainers(trainers);
            }
        });

        // Configurar Buscador en Tiempo Real
        binding.etSearchTrainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) { // Buscar a partir de 3 letras
                    mViewModel.buscarEntrenadores(s.toString());
                } else if (s.length() == 0) {
                    adapter.setTrainers(new ArrayList<>()); // Limpiar si borra todo
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
