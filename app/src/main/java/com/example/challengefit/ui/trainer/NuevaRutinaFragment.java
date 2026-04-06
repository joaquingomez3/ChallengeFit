package com.example.challengefit.ui.trainer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.challengefit.databinding.FragmentNuevaRutinaBinding;
import com.example.challengefit.modelos.Ejercicio;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.modelos.RutinaEjercicio;
import com.example.challengefit.request.ApiClient;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaRutinaFragment extends Fragment {

    private FragmentNuevaRutinaBinding binding;
    private SearchExerciseAdapter searchAdapter;
    private SelectedExercisesAdapter selectedAdapter;
    private List<RutinaEjercicio> selectedExercises = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNuevaRutinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedAdapter = new SelectedExercisesAdapter(selectedExercises);
        binding.rvSelectedExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSelectedExercises.setAdapter(selectedAdapter);

        searchAdapter = new SearchExerciseAdapter(new ArrayList<>(), ejercicio -> {
            RutinaEjercicio nuevo = new RutinaEjercicio();
            nuevo.setEjercicio(ejercicio);
            nuevo.setIdEjercicio(ejercicio.getId());
            nuevo.setSeries(3); // Valor por defecto
            nuevo.setRepeticiones(10); // Valor por defecto
            selectedExercises.add(nuevo);
            selectedAdapter.notifyDataSetChanged();
            
            binding.etSearchExercise.setText("");
            searchAdapter.setList(new ArrayList<>());
        });
        binding.rvSearchExerciseResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSearchExerciseResults.setAdapter(searchAdapter);

        binding.etSearchExercise.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) buscar(s.toString());
                else searchAdapter.setList(new ArrayList<>());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnGuardarRutina.setOnClickListener(v -> guardarRutina());
    }

    private void buscar(String texto) {
        String token = ApiClient.leerToken(requireContext());
        ApiClient.getChallengeFitService().buscarEjercicios(token, texto).enqueue(new Callback<List<Ejercicio>>() {
            @Override
            public void onResponse(Call<List<Ejercicio>> call, Response<List<Ejercicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchAdapter.setList(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Ejercicio>> call, Throwable t) {
                Log.e("NuevaRutina", "Error buscando: " + t.getMessage());
            }
        });
    }

    private void guardarRutina() {
        String nombre = binding.etRoutineName.getText().toString().trim();
        String desc = binding.etDescription.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa el nombre", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedExercises.isEmpty()) {
            Toast.makeText(getContext(), "Agrega al menos un ejercicio", Toast.LENGTH_SHORT).show();
            return;
        }

        // LIMPIEZA DE DATOS (Para evitar Error 500)
        List<RutinaEjercicio> listaLimpia = new ArrayList<>();
        for (RutinaEjercicio re : selectedExercises) {
            RutinaEjercicio item = new RutinaEjercicio();
            item.setIdEjercicio(re.getIdEjercicio());
            item.setSeries(re.getSeries());
            item.setRepeticiones(re.getRepeticiones());
            listaLimpia.add(item);
        }

        Rutina nueva = new Rutina();
        nueva.setNombre(nombre);
        nueva.setDescripcion(desc);
        nueva.setNivel("Intermedio");
        nueva.setDuracion(60);
        nueva.setRutinaEjercicios(listaLimpia);

        // LOG para depuración (Ver JSON en Logcat)
        Log.d("NuevaRutina", "Enviando JSON: " + new Gson().toJson(nueva));

        String token = ApiClient.leerToken(requireContext());
        ApiClient.getChallengeFitService().crearRutina(token, nueva).enqueue(new Callback<Rutina>() {
            @Override
            public void onResponse(Call<Rutina> call, Response<Rutina> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "¡Rutina creada!", Toast.LENGTH_LONG).show();
                    requireActivity().onBackPressed();
                } else {
                    Log.e("NuevaRutina", "Error 500/Fail: " + response.errorBody());
                    Toast.makeText(getContext(), "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Rutina> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
