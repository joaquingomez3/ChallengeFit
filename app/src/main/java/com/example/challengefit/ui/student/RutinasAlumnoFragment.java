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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.challengefit.R;
import com.example.challengefit.ui.trainer.RoutineAdapter;
import java.util.ArrayList;

public class RutinasAlumnoFragment extends Fragment {

    private RutinasAlumnoViewModel mViewModel;
    private RecyclerView rvRutinas;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RoutineAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutinas_alumno, container, false);

        rvRutinas = root.findViewById(R.id.rvRutinasAlumno);
        rvRutinas.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = root.findViewById(R.id.swipeRutinasAlumno);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> mViewModel.cargarRutinas());
        }

        adapter = new RoutineAdapter(new ArrayList<>(), false, false);
        rvRutinas.setAdapter(adapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(RutinasAlumnoViewModel.class);

        mViewModel.getLista().observe(getViewLifecycleOwner(), rutinas -> {
            if (rutinas != null) {
                adapter.setRutinas(rutinas);
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            }
        });

        mViewModel.cargarRutinas();
    }
}
