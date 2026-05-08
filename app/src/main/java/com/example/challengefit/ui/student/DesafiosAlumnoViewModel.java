package com.example.challengefit.ui.student;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.challengefit.modelos.Desafio;
import com.example.challengefit.modelos.DesafioUsuario;
import com.example.challengefit.request.ApiClient;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesafiosAlumnoViewModel extends AndroidViewModel {
    private MutableLiveData<List<Desafio>> mLista;
    private MutableLiveData<String> mMensaje;

    public DesafiosAlumnoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Desafio>> getLista(){
        if(mLista == null){
            mLista = new MutableLiveData<>();
        }
        return mLista;
    }

    public LiveData<String> getMensaje(){
        if(mMensaje == null){
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public void cargarDesafios(){
        String token = ApiClient.leerToken(getApplication());
        if(token != null){
            ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
            Call<List<DesafioUsuario>> llamada = api.obtenerMisDesafios(token);
            
            llamada.enqueue(new Callback<List<DesafioUsuario>>() {
                @Override
                public void onResponse(Call<List<DesafioUsuario>> call, Response<List<DesafioUsuario>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        List<Desafio> listaDesafios = new ArrayList<>();
                        for (DesafioUsuario du : response.body()) {
                            if (du.getDesafio() != null) {
                                Desafio d = du.getDesafio();
                                // IMPORTANTE: Usamos el ID de la inscripción (DesafioUsuario) 
                                // para que el endpoint de progreso funcione correctamente.
                                d.setId(du.getId()); 
                                d.setEstado(du.isCompletado() ? "Finalizado" : "Activo");
                                listaDesafios.add(d);
                            }
                        }
                        mLista.postValue(listaDesafios);
                    } else {
                        Log.e("DesafiosAlumnoVM", "Error en respuesta: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<DesafioUsuario>> call, Throwable t) {
                    Log.e("DesafiosAlumnoVM", "Fallo conexión: " + t.getMessage());
                }
            });
        }
    }

    public void completarDesafio(int idInscripcion) {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
            // Enviamos 100 como progreso para marcarlo como completado
            RequestBody bodyProgreso = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "100");
            Call<ResponseBody> llamada = api.completarDesafio(token, idInscripcion, bodyProgreso);
            
            llamada.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        mMensaje.postValue("¡Desafío completado!");
                        cargarDesafios(); // Recargamos para actualizar el estado visual
                    } else {
                        Log.e("DesafiosAlumnoVM", "Error al completar (ID " + idInscripcion + "): " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("DesafiosAlumnoVM", "Error de red: " + t.getMessage());
                }
            });
        }
    }
}
