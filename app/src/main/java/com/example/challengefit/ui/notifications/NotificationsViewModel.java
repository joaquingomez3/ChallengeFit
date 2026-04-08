package com.example.challengefit.ui.notifications;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.challengefit.modelos.Solicitud;
import com.example.challengefit.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Solicitud>> mSolicitudes = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Solicitud>> getSolicitudes() {
        return mSolicitudes;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void cargarSolicitudes() {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.getChallengeFitService().obtenerSolicitudesPendientes(token).enqueue(new Callback<List<Solicitud>>() {
                @Override
                public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                    if (response.isSuccessful()) {
                        mSolicitudes.postValue(response.body());
                    } else {
                        mError.postValue("Error al cargar solicitudes: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                    mError.postValue("Error de conexión");
                }
            });
        }
    }

    public void aceptarSolicitud(int id) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.getChallengeFitService().aceptarSolicitud(token, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    cargarSolicitudes(); // Recargar lista
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mError.postValue("Error al aceptar solicitud");
            }
        });
    }

    public void rechazarSolicitud(int id) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.getChallengeFitService().rechazarSolicitud(token, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    cargarSolicitudes(); // Recargar lista
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mError.postValue("Error al rechazar solicitud");
            }
        });
    }
}
