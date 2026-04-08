package com.example.challengefit.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Notificacion;
import com.example.challengefit.modelos.Solicitud;
import com.example.challengefit.request.ApiClient;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView rvNotifications;
    private NotificationsViewModel mViewModel;
    private RequestsAdapter requestsAdapter;
    private NotificationAdapter generalAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        rvNotifications = root.findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        String rol = ApiClient.leerRol(requireContext());
        Log.d("NotificationsFragment", "Rol detectado: " + rol);

        // USAMOS equalsIgnoreCase PARA EVITAR ERRORES DE MAYÚSCULAS/MINÚSCULAS
        if ("ENTRENADOR".equalsIgnoreCase(rol) || "2".equals(rol)) {
            configurarVistaEntrenador();
        } else {
            configurarVistaAlumno();
        }
    }

    private void configurarVistaEntrenador() {
        requestsAdapter = new RequestsAdapter(new ArrayList<>(), new RequestsAdapter.OnRequestActionListener() {
            @Override
            public void onAccept(Solicitud solicitud) {
                mViewModel.aceptarSolicitud(solicitud.getId());
                Toast.makeText(getContext(), "Aceptando solicitud...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReject(Solicitud solicitud) {
                mViewModel.rechazarSolicitud(solicitud.getId());
                Toast.makeText(getContext(), "Rechazando solicitud...", Toast.LENGTH_SHORT).show();
            }
        });
        rvNotifications.setAdapter(requestsAdapter);

        mViewModel.getSolicitudes().observe(getViewLifecycleOwner(), solicitudes -> {
            if (solicitudes != null) {
                Log.d("NotificationsFragment", "Solicitudes recibidas: " + solicitudes.size());
                requestsAdapter.setRequests(solicitudes);
                actualizarEmptyState(solicitudes.isEmpty());
            }
        });

        mViewModel.cargarSolicitudes();
    }

    private void configurarVistaAlumno() {
        // Notificaciones de prueba para el alumno
        List<Notificacion> list = new ArrayList<>();
        list.add(new Notificacion("Nueva Rutina", "Tu entrenador ha asignado 'Piernas Pro'.", "Hace 5m"));
        list.add(new Notificacion("Desafío Completado", "¡Felicidades! Completaste el reto de 30 días.", "Hace 1h"));
        
        generalAdapter = new NotificationAdapter(list);
        rvNotifications.setAdapter(generalAdapter);
        actualizarEmptyState(list.isEmpty());
    }

    private void actualizarEmptyState(boolean isEmpty) {
        View root = getView();
        if (root != null) {
            View emptyText = root.findViewById(R.id.tvEmptyNotif);
            if (emptyText != null) {
                emptyText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            }
        }
    }
}
