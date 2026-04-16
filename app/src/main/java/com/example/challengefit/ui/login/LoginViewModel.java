package com.example.challengefit.ui.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.challengefit.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensaje(){
        if(mMensaje == null){
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public LiveData<Boolean> getLoginExitoso() {
        return loginExitoso;
    }

    public void login(String email, String password){
        ApiClient.ChallengeFitService api = ApiClient.getChallengeFitService();
        Call<String> llamada = api.login(email, password);
        
        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String token = response.body();
                    ApiClient.guardarToken(getApplication(), token);

                    String claveSecreta = "EstaEsUnaClaveSuperSeguraDeMasDe32Caracteres123!"; // ejemplo, si esa fuera realmente la SecretKey

                    try {
                        TokenUtils.DatosToken datos = TokenUtils.validarYObtenerDatos(token, claveSecreta);

                        Log.d("mail","Mail: " + datos.mail);
                        Log.d("id","Id: " + datos.id);
                        Log.d("rol","Rol: " + datos.rol);
                        if (datos.rol.equals("Entrenador")) {
                            ApiClient.guardarRol(getApplication(), "Entrenador");
                        } else {
                            ApiClient.guardarRol(getApplication(), "Alumno");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    loginExitoso.postValue(true);
                } else {
                    if (response.code() == 400) {
                        mMensaje.postValue("Datos Incorrectos");
                    } else {
                        mMensaje.postValue("Error en el servidor: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mMensaje.postValue("Error de conexión");
            }
        });
    }
}
