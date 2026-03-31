package com.example.challengefit.ui.student;

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
import com.example.challengefit.ui.trainer.ChallengeAdapter;
import java.util.ArrayList;

public class DesafiosAlumnoFragment extends Fragment {

    private DesafiosAlumnoViewModel mViewModel;
    private RecyclerView rvDesafios;
    private ChallengeAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafios_alumno, container, false);

        rvDesafios = root.findViewById(R.id.rvDesafiosAlumno);
        rvDesafios.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChallengeAdapter(new ArrayList<>());
        rvDesafios.setAdapter(adapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mViewModel = new ViewModelProvider(this).get(DesafiosAlumnoViewModel.class);

        mViewModel.getLista().observe(getViewLifecycleOwner(), desafios -> {
            if (desafios != null) {
                adapter.setDesafios(desafios);
            }
        });

        mViewModel.cargarDesafios();
    }
}
