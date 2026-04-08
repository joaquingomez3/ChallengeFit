package com.example.challengefit.ui.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.challengefit.databinding.FragmentProgresoBinding;
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

        binding.rvTrainerResults.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainerSearchAdapter(new ArrayList<>(), trainer -> {
            mViewModel.enviarSolicitud(trainer.getId());
        });
        binding.rvTrainerResults.setAdapter(adapter);

        // OBSERVAR MENSAJES DE ESTADO (Para el Toast)
        mViewModel.getStatusMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getTrainers().observe(getViewLifecycleOwner(), trainers -> {
            if (trainers != null) {
                adapter.setTrainers(trainers);
            }
        });

        binding.etSearchTrainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    mViewModel.buscarEntrenadores(s.toString());
                } else if (s.length() == 0) {
                    adapter.setTrainers(new ArrayList<>());
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
