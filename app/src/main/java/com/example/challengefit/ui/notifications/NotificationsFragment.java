package com.example.challengefit.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Notificacion;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        rvNotifications = root.findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        // Datos de prueba
        List<Notificacion> list = new ArrayList<>();
        list.add(new Notificacion("Nueva Rutina", "Tu entrenador ha asignado 'Piernas Pro'.", "Hace 5m"));
        list.add(new Notificacion("Desafío Completado", "¡Felicidades! Completaste el reto de 30 días.", "Hace 1h"));
        list.add(new Notificacion("Recordatorio", "No olvides registrar tu progreso de hoy.", "Hace 3h"));

        adapter = new NotificationAdapter(list);
        rvNotifications.setAdapter(adapter);

        return root;
    }
}
