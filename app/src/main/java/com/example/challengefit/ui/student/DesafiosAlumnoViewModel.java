package com.example.challengefit.ui.student;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.challengefit.modelos.Desafio;
import com.example.challengefit.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesafiosAlumnoViewModel extends AndroidViewModel {
    private MutableLiveData<List<Desafio>> mLista;

    public DesafiosAlumnoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Desafio>> getLista(){
        if(mLista == null){
            mLista = new MutableLiveData<>();
        }
        return mLista;
    }

    public void cargarDesafios(){
        String token = ApiClient.leerToken(getApplication());
        if(token != null){
            ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
            Call<List<Desafio>> llamada = api.obtenerMisDesafios(token);
            
            llamada.enqueue(new Callback<List<Desafio>>() {
                @Override
                public void onResponse(Call<List<Desafio>> call, Response<List<Desafio>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        mLista.postValue(response.body());
                    } else {
                        Log.e("DesafiosAlumnoVM", "Error en respuesta: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Desafio>> call, Throwable t) {
                    Log.e("DesafiosAlumnoVM", "Fallo conexión: " + t.getMessage());
                }
            });
        }
    }
}
