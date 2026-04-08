package com.example.challengefit.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.challengefit.R;
import com.example.challengefit.databinding.FragmentRegistroBinding;
import com.example.challengefit.modelos.Objetivo;
import com.example.challengefit.modelos.Usuario;
import com.example.challengefit.request.ApiClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private List<Objetivo> listaObjetivos = new ArrayList<>();
    private List<Usuario.Especialidad> listaEspecialidades = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBackRegistro.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.tvLoginLink.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        cargarDatosSelectores();

        binding.rgRol.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbAlumno) {
                binding.rbAlumno.setBackgroundResource(R.drawable.bg_tag_primary);
                binding.rbAlumno.setTextColor(getResources().getColor(R.color.accent_green));
                binding.rbEntrenador.setBackground(null);
                binding.rbEntrenador.setTextColor(getResources().getColor(R.color.text_hint));
                binding.llCamposAlumno.setVisibility(View.VISIBLE);
                binding.llCamposEntrenador.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbEntrenador) {
                binding.rbEntrenador.setBackgroundResource(R.drawable.bg_tag_primary);
                binding.rbEntrenador.setTextColor(getResources().getColor(R.color.accent_green));
                binding.rbAlumno.setBackground(null);
                binding.rbAlumno.setTextColor(getResources().getColor(R.color.text_hint));
                binding.llCamposEntrenador.setVisibility(View.VISIBLE);
                binding.llCamposAlumno.setVisibility(View.GONE);
            }
        });

        binding.btnCrearCuenta.setOnClickListener(v -> registrarUsuario());
    }

    private void cargarDatosSelectores() {
        ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();

        api.obtenerObjetivos().enqueue(new Callback<List<Objetivo>>() {
            @Override
            public void onResponse(Call<List<Objetivo>> call, Response<List<Objetivo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaObjetivos = response.body();
                    List<String> nombres = new ArrayList<>();
                    for (Objetivo o : listaObjetivos) nombres.add(o.getNombre());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, nombres);
                    binding.actvObjetivo.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Objetivo>> call, Throwable t) {
                Log.e("Registro", "Error cargando objetivos: " + t.getMessage());
            }
        });

        api.obtenerEspecialidades().enqueue(new Callback<List<Usuario.Especialidad>>() {
            @Override
            public void onResponse(Call<List<Usuario.Especialidad>> call, Response<List<Usuario.Especialidad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaEspecialidades = response.body();
                    List<String> nombres = new ArrayList<>();
                    for (Usuario.Especialidad e : listaEspecialidades) nombres.add(e.getNombre());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, nombres);
                    binding.actvEspecialidad.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Usuario.Especialidad>> call, Throwable t) {
                Log.e("Registro", "Error cargando especialidades: " + t.getMessage());
            }
        });
    }

    private void registrarUsuario() {
        String nombre = binding.etNombreRegistro.getText().toString().trim();
        String email = binding.etEmailRegistro.getText().toString().trim();
        String clave = binding.etPassRegistro.getText().toString().trim();
        String confirmClave = binding.etConfirmPassRegistro.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || clave.isEmpty()) {
            Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!clave.equals(confirmClave)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.RegistroRequest request = new ApiClient.RegistroRequest();
        request.nombre = nombre;
        request.email = email;
        request.clave = clave;

        if (binding.rbAlumno.isChecked()) {
            request.rol = "Alumno";
            String objSeleccionado = binding.actvObjetivo.getText().toString();
            request.objetivo = objSeleccionado;
            for (Objetivo o : listaObjetivos) {
                if (o.getNombre().equals(objSeleccionado)) {
                    request.objetivoIds = Collections.singletonList(o.getId());
                    break;
                }
            }
        } else {
            request.rol = "Entrenador";
            String espSeleccionada = binding.actvEspecialidad.getText().toString();
            for (Usuario.Especialidad e : listaEspecialidades) {
                if (e.getNombre().equals(espSeleccionada)) {
                    request.especialidadIds = Collections.singletonList(e.getId());
                    break;
                }
            }
        }

        ApiClient.getChallengeFitService().crearUsuario(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "¡Cuenta creada! Inicia sesión para continuar.", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(requireView()).popBackStack();
                } else {
                    Log.e("Registro", "Error al crear: " + response.code());
                    Toast.makeText(getContext(), "El correo ya está registrado o datos inválidos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red o datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
