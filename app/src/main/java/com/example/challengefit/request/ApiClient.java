package com.example.challengefit.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.challengefit.modelos.Desafio;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.modelos.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {

    public static final String BASE_URL = "http://10.0.2.2:5289/";

    public static ChallengeFitService getChallengeFitService(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ChallengeFitService.class);
    }

    public static void guardarToken(Context context, String token){
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", "Bearer " + token);
        editor.apply();
    }

    public static String leerToken (Context context){
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }

    public static void guardarRol(Context context, String rol){
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("rol", rol);
        editor.apply();
    }

    public static String leerRol(Context context){
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("rol", null);
    }

    public static void cerrarSesion(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public interface ChallengeFitService {
        @FormUrlEncoded
        @POST("api/Usuario/login")
        Call<String> login (@Field("mail") String email, @Field("clave") String password);

        // RUTINAS
        @GET("api/Rutina")
        Call<List<Rutina>> obtenerRutinas(@Header("Authorization") String token);

        @GET("api/Rutina/{id}")
        Call<Rutina> obtenerRutinaPorId(@Header("Authorization") String token, @Path("id") int id);

        // ALUMNOS CON PROGRESO
        @GET("api/Usuario/alumnos/progreso")
        Call<List<Usuario>> obtenerAlumnosConProgreso(@Header("Authorization") String token);

        // DESAFIOS ENTRENADOR
        @GET("api/Desafio")
        Call<List<Desafio>> obtenerDesafios(@Header("Authorization") String token);

        // DESAFIOS ALUMNO
        @GET("api/Desafio/mis-desafios")
        Call<List<Desafio>> obtenerMisDesafios(@Header("Authorization") String token);

        // SOLICITUDES (BUSCAR ENTRENADORES)
        @GET("api/Solicitud/buscar-entrenadores")
        Call<List<Usuario>> buscarEntrenadores(@Header("Authorization") String token, @Query("nombre") String nombre);

        @POST("api/Solicitud")
        Call<String> enviarSolicitud(@Header("Authorization") String token, @Body SolicitudRequest solicitud);
    }

    // Clase auxiliar para el envío de solicitud
    public static class SolicitudRequest {
        public int idEntrenador;
        public SolicitudRequest(int idEntrenador) { this.idEntrenador = idEntrenador; }
    }
}
