package com.example.challengefit.ui.trainer;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleAlumnoViewModel extends AndroidViewModel {
    private MutableLiveData<List<Rutina>> mRutinasActivas = new MutableLiveData<>();
    private MutableLiveData<List<Rutina>> mRutinasCompletadas = new MutableLiveData<>();

    public DetalleAlumnoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Rutina>> getRutinasActivas() { return mRutinasActivas; }
    public LiveData<List<Rutina>> getRutinasCompletadas() { return mRutinasCompletadas; }

    public void cargarRutinasPorEstado(int alumnoId, String estado) {
        String token = ApiClient.leerToken(getApplication());
        if (token == null) return;

        ApiClient.getChallengeFitService().obtenerRutinasDeAlumnoPorEstado(token, alumnoId, estado).enqueue(new Callback<List<Rutina>>() {
            @Override
            public void onResponse(Call<List<Rutina>> call, Response<List<Rutina>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("activas".equals(estado)) {
                        mRutinasActivas.postValue(response.body());
                    } else {
                        mRutinasCompletadas.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Rutina>> call, Throwable t) {}
        });
    }
}
