package com.example.challengefit.ui.student;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.challengefit.modelos.Usuario;
import com.example.challengefit.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgresoViewModel extends AndroidViewModel {
    private MutableLiveData<List<Usuario>> mTrainers = new MutableLiveData<>();

    public ProgresoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Usuario>> getTrainers() {
        return mTrainers;
    }

    public void buscarEntrenadores(String nombre) {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.getChallengeFitService().buscarEntrenadores(token, nombre).enqueue(new Callback<List<Usuario>>() {
                @Override
                public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                    if (response.isSuccessful()) {
                        mTrainers.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Usuario>> call, Throwable t) {
                    Log.e("ProgresoVM", "Error búsqueda: " + t.getMessage());
                }
            });
        }
    }

    public void enviarSolicitud(int idEntrenador) {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.getChallengeFitService().enviarSolicitud(token, new ApiClient.SolicitudRequest(idEntrenador))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication(), "Solicitud enviada con éxito", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("ProgresoVM", "Error solicitud: " + t.getMessage());
                    }
                });
        }
    }
}
