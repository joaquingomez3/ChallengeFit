package com.example.challengefit.ui.trainer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challengefit.R;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleRutinaFragment extends Fragment {

    private TextView tvNombre, tvNivel, tvDuracion, tvDescripcion;
    private RecyclerView rvEjercicios;
    private ExerciseAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detalle_rutina, container, false);

        tvNombre = root.findViewById(R.id.tvDetalleNombre);
        tvNivel = root.findViewById(R.id.tvDetalleNivel);
        tvDuracion = root.findViewById(R.id.tvDetalleDuracion);
        tvDescripcion = root.findViewById(R.id.tvDetalleDescripcion);
        rvEjercicios = root.findViewById(R.id.rvEjerciciosRutina);
        rvEjercicios.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView btnBack = root.findViewById(R.id.btnBackDetalle);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            int id = getArguments().getInt("rutinaId");
            cargarDatosRutina(id);
        }
    }

    private void cargarDatosRutina(int id) {
        String token = ApiClient.leerToken(requireContext());
        ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
        
        Log.d("DetalleRutina", "Buscando rutina ID: " + id);

        // Usamos obtenerRutinas() porque sabemos que este endpoint funciona y trae los ejercicios
        api.obtenerRutinas(token).enqueue(new Callback<List<Rutina>>() {
            @Override
            public void onResponse(Call<List<Rutina>> call, Response<List<Rutina>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Rutina> lista = response.body();
                    Rutina encontrada = null;
                    
                    // Filtramos la rutina por ID manualmente
                    for (Rutina r : lista) {
                        if (r.getId() == id) {
                            encontrada = r;
                            break;
                        }
                    }

                    if (encontrada != null) {
                        actualizarUI(encontrada);
                    } else {
                        Log.e("DetalleRutina", "No se encontró la rutina con ID: " + id);
                    }
                } else {
                    Log.e("DetalleRutina", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Rutina>> call, Throwable t) {
                Log.e("DetalleRutina", "Fallo en la llamada: " + t.getMessage());
            }
        });
    }

    private void actualizarUI(Rutina r) {
        tvNombre.setText(r.getNombre());
        tvNivel.setText(r.getNivel());
        tvDuracion.setText(r.getDuracion() + " min");
        tvDescripcion.setText(r.getDescripcion());
        
        if (r.getRutinaEjercicios() != null && !r.getRutinaEjercicios().isEmpty()) {
            Log.d("DetalleRutina", "Ejercicios cargados: " + r.getRutinaEjercicios().size());
            adapter = new ExerciseAdapter(r.getRutinaEjercicios());
            rvEjercicios.setAdapter(adapter);
        } else {
            Log.e("DetalleRutina", "La rutina no tiene ejercicios asignados.");
        }
    }
}
