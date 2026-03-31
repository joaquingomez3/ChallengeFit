package com.example.challengefit.ui.student;

import android.app.Application;
import android.util.Log;
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

public class RutinasAlumnoViewModel extends AndroidViewModel {
    private MutableLiveData<List<Rutina>> mLista;

    public RutinasAlumnoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Rutina>> getLista(){
        if(mLista == null){
            mLista = new MutableLiveData<>();
        }
        return mLista;
    }

    public void cargarRutinas(){
        String token = ApiClient.leerToken(getApplication());
        if(token != null){
            ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
            // Reutilizamos el endpoint que ya configuramos
            Call<List<Rutina>> llamada = api.obtenerRutinas(token);
            
            llamada.enqueue(new Callback<List<Rutina>>() {
                @Override
                public void onResponse(Call<List<Rutina>> call, Response<List<Rutina>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        mLista.postValue(response.body());
                    } else {
                        Log.e("RutinasAlumnoVM", "Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Rutina>> call, Throwable t) {
                    Log.e("RutinasAlumnoVM", "Fallo: " + t.getMessage());
                }
            });
        }
    }
}
