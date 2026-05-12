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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Desafio;
import java.util.ArrayList;

public class DesafiosEntrenadorFragment extends Fragment {

    private DesafiosEntrenadorViewModel mViewModel;
    private RecyclerView rvDesafios;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChallengeAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafios_entrenador, container, false);

        rvDesafios = root.findViewById(R.id.rvDesafiosEntrenador);
        rvDesafios.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = root.findViewById(R.id.swipeDesafiosEntrenador);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> mViewModel.cargarDesafios());
        }

        adapter = new ChallengeAdapter(new ArrayList<>());
        rvDesafios.setAdapter(adapter);

        View btnNuevoDesafio = root.findViewById(R.id.btnNuevoDesafio);
        if (btnNuevoDesafio != null) {
            btnNuevoDesafio.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_new_challenge));
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(DesafiosEntrenadorViewModel.class);

        mViewModel.getLista().observe(getViewLifecycleOwner(), desafios -> {
            if (desafios != null) {
                adapter.setDesafios(desafios);
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            }
        });

        mViewModel.cargarDesafios();
    }
}
