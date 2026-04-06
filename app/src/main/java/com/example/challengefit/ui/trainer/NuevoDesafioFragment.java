package com.example.challengefit.ui.trainer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Desafio;
import com.example.challengefit.modelos.DesafioResponse;
import com.example.challengefit.request.ApiClient;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoDesafioFragment extends Fragment {

    private EditText etNombre, etDescripcion, etPuntos;
    private TextView tvFecha;
    private String fechaSeleccionada = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nuevo_desafio, container, false);

        etNombre = root.findViewById(R.id.etNombreDesafio);
        etDescripcion = root.findViewById(R.id.etDescripcionDesafio);
        etPuntos = root.findViewById(R.id.etPuntosDesafio);
        tvFecha = root.findViewById(R.id.tvFechaLimite);

        root.findViewById(R.id.btnBackNuevoDesafio).setOnClickListener(v -> 
            Navigation.findNavController(v).popBackStack());

        root.findViewById(R.id.llFechaLimite).setOnClickListener(v -> mostrarDatePicker());

        root.findViewById(R.id.btnPublicarDesafio).setOnClickListener(v -> publicarDesafio());

        return root;
    }

    private void mostrarDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Formato ISO estricto: YYYY-MM-DD (con ceros a la izquierda)
                    fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    tvFecha.setText(fechaSeleccionada);
                    tvFecha.setTextColor(getResources().getColor(R.color.white));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void publicarDesafio() {
        String nombre = etNombre.getText().toString().trim();
        String desc = etDescripcion.getText().toString().trim();
        String puntosStr = etPuntos.getText().toString().trim();

        if (nombre.isEmpty() || puntosStr.isEmpty() || fechaSeleccionada.isEmpty()) {
            Toast.makeText(getContext(), "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Desafio nuevo = new Desafio();
        nuevo.setTitulo(nombre);
        nuevo.setDescripcion(desc);
        nuevo.setPuntos(Integer.parseInt(puntosStr));
        nuevo.setFechaFin(fechaSeleccionada);
        
        // Fecha inicio hoy en formato ISO
        Calendar hoy = Calendar.getInstance();
        String fechaInicio = String.format(Locale.getDefault(), "%04d-%02d-%02d", 
                hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH) + 1, hoy.get(Calendar.DAY_OF_MONTH));
        nuevo.setFechaInicio(fechaInicio);

        String token = ApiClient.leerToken(requireContext());
        
        // IMPORTANTE: Cambiamos a DesafioResponse según tu API
        ApiClient.getChallengeFitService().crearDesafio(token, nuevo).enqueue(new Callback<DesafioResponse>() {
            @Override
            public void onResponse(Call<DesafioResponse> call, Response<DesafioResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    Navigation.findNavController(requireView()).popBackStack();
                } else {
                    Log.e("NuevoDesafio", "Error 400/Fail: " + response.code());
                    Toast.makeText(getContext(), "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DesafioResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
