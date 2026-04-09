package com.example.challengefit.ui.trainer;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.challengefit.modelos.Desafio;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.modelos.Usuario;
import com.example.challengefit.request.ApiClient;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlumnosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Usuario>> mLista;
    private MutableLiveData<List<Rutina>> mRutinasDisponibles = new MutableLiveData<>();
    private MutableLiveData<List<Desafio>> mDesafiosDisponibles = new MutableLiveData<>();
    private MutableLiveData<String> mMensajeExito = new MutableLiveData<>();

    public AlumnosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Usuario>> getLista() {
        if (mLista == null) mLista = new MutableLiveData<>();
        return mLista;
    }

    public LiveData<List<Rutina>> getRutinasDisponibles() { return mRutinasDisponibles; }
    public LiveData<List<Desafio>> getDesafiosDisponibles() { return mDesafiosDisponibles; }
    public LiveData<String> getMensajeExito() { return mMensajeExito; }

    public void cargarAlumnos() {
        String token = ApiClient.leerToken(getApplication());
        if (token != null) {
            ApiClient.getChallengeFitService().obtenerAlumnosConProgreso(token).enqueue(new Callback<List<Usuario>>() {
                @Override
                public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                    if (response.isSuccessful()) mLista.postValue(response.body());
                }
                @Override
                public void onFailure(Call<List<Usuario>> call, Throwable t) {}
            });
        }
    }

    public void cargarRutinasYDesafios() {
        String token = ApiClient.leerToken(getApplication());
        if (token == null) return;

        ApiClient.getChallengeFitService().obtenerRutinas(token).enqueue(new Callback<List<Rutina>>() {
            @Override
            public void onResponse(Call<List<Rutina>> call, Response<List<Rutina>> response) {
                if (response.isSuccessful()) mRutinasDisponibles.postValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Rutina>> call, Throwable t) {}
        });

        ApiClient.getChallengeFitService().obtenerDesafios(token).enqueue(new Callback<List<Desafio>>() {
            @Override
            public void onResponse(Call<List<Desafio>> call, Response<List<Desafio>> response) {
                if (response.isSuccessful()) mDesafiosDisponibles.postValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Desafio>> call, Throwable t) {}
        });
    }

    public void asignarRutina(int idAlumno, int idRutina) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.AsignarRutinaRequest request = new ApiClient.AsignarRutinaRequest(idAlumno, idRutina);
        ApiClient.getChallengeFitService().asignarRutina(token, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) mMensajeExito.postValue("Rutina asignada correctamente");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    public void asignarDesafio(int idAlumno, int idDesafio) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.AsignarDesafioRequest request = new ApiClient.AsignarDesafioRequest(idAlumno, idDesafio);
        ApiClient.getChallengeFitService().asignarDesafio(token, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) mMensajeExito.postValue("Desafío asignado correctamente");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }
}
