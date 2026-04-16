package com.example.challengefit.ui.trainer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challengefit.R;
import com.example.challengefit.modelos.Ejercicio;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.modelos.RutinaEjercicio;
import com.example.challengefit.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleRutinaFragment extends Fragment {

    private TextView tvNombre, tvNivel, tvDuracion, tvDescripcion;
    private RecyclerView rvEjercicios;
    private ExerciseAdapter adapter;
    private ImageView btnAddExercise;
    private int rutinaId;
    private boolean esSeguimiento = false;
    private List<RutinaEjercicio> listaActual = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detalle_rutina, container, false);

        tvNombre = root.findViewById(R.id.tvDetalleNombre);
        tvNivel = root.findViewById(R.id.tvDetalleNivel);
        tvDuracion = root.findViewById(R.id.tvDetalleDuracion);
        tvDescripcion = root.findViewById(R.id.tvDetalleDescripcion);
        rvEjercicios = root.findViewById(R.id.rvEjerciciosRutina);
        btnAddExercise = root.findViewById(R.id.btnAddExercise);
        
        rvEjercicios.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView btnBack = root.findViewById(R.id.btnBackDetalle);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        }

        btnAddExercise.setOnClickListener(v -> mostrarDialogoBusqueda());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            rutinaId = getArguments().getInt("rutinaId");
            esSeguimiento = getArguments().getBoolean("esSeguimiento", false);
            
            if (!esSeguimiento) {
                btnAddExercise.setVisibility(View.VISIBLE);
            }
            
            cargarDatosRutina(rutinaId);
        }
    }

    private void cargarDatosRutina(int id) {
        String token = ApiClient.leerToken(requireContext());
        ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
        
        api.obtenerRutinas(token).enqueue(new Callback<List<Rutina>>() {
            @Override
            public void onResponse(Call<List<Rutina>> call, Response<List<Rutina>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Rutina encontrada = null;
                    for (Rutina r : response.body()) {
                        if (r.getId() == id) {
                            encontrada = r;
                            break;
                        }
                    }

                    if (encontrada != null) {
                        if (!esSeguimiento && encontrada.getRutinaEjercicios() != null) {
                            for (RutinaEjercicio re : encontrada.getRutinaEjercicios()) {
                                re.setCompletado(false);
                            }
                        }
                        
                        listaActual.clear();
                        if (encontrada.getRutinaEjercicios() != null) {
                            listaActual.addAll(encontrada.getRutinaEjercicios());
                        }
                        actualizarUI(encontrada);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Rutina>> call, Throwable t) {}
        });
    }

    private void actualizarUI(Rutina r) {
        tvNombre.setText(r.getNombre());
        tvNivel.setText(r.getNivel());
        tvDuracion.setText(r.getDuracion() + " min");
        tvDescripcion.setText(r.getDescripcion());
        
        if (adapter == null) {
            adapter = new ExerciseAdapter(listaActual, false, !esSeguimiento);
            rvEjercicios.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void mostrarDialogoBusqueda() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_search_exercise, null);
        EditText etSearch = view.findViewById(R.id.etSearchExerciseDialog);
        RecyclerView rvResults = view.findViewById(R.id.rvSearchExerciseResultsDialog);
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));

        AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog)
                .setTitle("Agregar Ejercicio")
                .setView(view)
                .setNegativeButton("Cerrar", null)
                .create();

        SearchExerciseAdapter searchAdapter = new SearchExerciseAdapter(new ArrayList<>(), ejercicio -> {
            dialog.dismiss();
            mostrarDialogoParametros(ejercicio);
        });
        rvResults.setAdapter(searchAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) buscarEjercicios(s.toString(), searchAdapter);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        dialog.show();
    }

    private void buscarEjercicios(String texto, SearchExerciseAdapter searchAdapter) {
        String token = ApiClient.leerToken(requireContext());
        ApiClient.getChallengeFitService().buscarEjercicios(token, texto).enqueue(new Callback<List<Ejercicio>>() {
            @Override
            public void onResponse(Call<List<Ejercicio>> call, Response<List<Ejercicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchAdapter.setList(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Ejercicio>> call, Throwable t) {}
        });
    }

    private void mostrarDialogoParametros(Ejercicio ejercicio) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_exercise_params, null);
        EditText etSeries = view.findViewById(R.id.etSeriesDialog);
        EditText etReps = view.findViewById(R.id.etRepsDialog);

        new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog)
                .setTitle("Configurar " + ejercicio.getNombre())
                .setView(view)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    int series = Integer.parseInt(etSeries.getText().toString());
                    int reps = Integer.parseInt(etReps.getText().toString());
                    agregarEjercicio(ejercicio, series, reps);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void agregarEjercicio(Ejercicio ej, int series, int reps) {
        String token = ApiClient.leerToken(requireContext());
        ApiClient.AgregarEjercicioRequest request = new ApiClient.AgregarEjercicioRequest(ej.getId(), series, reps);

        ApiClient.getChallengeFitService().agregarEjercicioARutina(token, rutinaId, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ejercicio agregado", Toast.LENGTH_SHORT).show();
                    cargarDatosRutina(rutinaId); // Recargamos para obtener el ID de la RutinaEjercicio
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }
}
