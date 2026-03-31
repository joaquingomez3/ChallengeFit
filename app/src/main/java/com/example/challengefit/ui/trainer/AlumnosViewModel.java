package com.example.challengefit.ui.trainer;

import android.app.Application;
import android.util.Log;
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

public class AlumnosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Usuario>> mLista;

    public AlumnosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Usuario>> getLista(){
        if(mLista == null){
            mLista = new MutableLiveData<>();
        }
        return mLista;
    }

    public void cargarAlumnos(){
        String token = ApiClient.leerToken(getApplication());
        if(token != null){
            ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
            Call<List<Usuario>> llamada = api.obtenerAlumnosConProgreso(token);
            
            llamada.enqueue(new Callback<List<Usuario>>() {
                @Override
                public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        mLista.postValue(response.body());
                    } else {
                        Log.e("AlumnosVM", "Error en respuesta: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Usuario>> call, Throwable t) {
                    Log.e("AlumnosVM", "Fallo conexión: " + t.getMessage());
                }
            });
        }
    }
}
