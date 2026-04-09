package com.example.challengefit.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.challengefit.modelos.Desafio;
import com.example.challengefit.modelos.DesafioResponse;
import com.example.challengefit.modelos.Ejercicio;
import com.example.challengefit.modelos.Objetivo;
import com.example.challengefit.modelos.Rutina;
import com.example.challengefit.modelos.Solicitud;
import com.example.challengefit.modelos.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
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
import retrofit2.http.PUT;
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

        // REGISTRO
        @GET("api/Usuario/objetivos")
        Call<List<Objetivo>> obtenerObjetivos();

        @GET("api/Usuario/especialidades")
        Call<List<Usuario.Especialidad>> obtenerEspecialidades();

        @POST("api/Usuario/crear")
        Call<ResponseBody> crearUsuario(@Body RegistroRequest request);

        // RUTINAS
        @GET("api/Rutina")
        Call<List<Rutina>> obtenerRutinas(@Header("Authorization") String token);

        @GET("api/Rutina/{id}")
        Call<Rutina> obtenerRutinaPorId(@Header("Authorization") String token, @Path("id") int id);

        @GET("api/Rutina/buscar-ejercicios")
        Call<List<Ejercicio>> buscarEjercicios(@Header("Authorization") String token, @Query("nombre") String nombre);

        @POST("api/Rutina")
        Call<Rutina> crearRutina(@Header("Authorization") String token, @Body Rutina rutina);

        @POST("api/Rutina/asignar")
        Call<ResponseBody> asignarRutina(@Header("Authorization") String token, @Body AsignarRutinaRequest request);

        // ALUMNOS CON PROGRESO
        @GET("api/Usuario/alumnos/progreso")
        Call<List<Usuario>> obtenerAlumnosConProgreso(@Header("Authorization") String token);

        // DESAFIOS
        @GET("api/Desafio")
        Call<List<Desafio>> obtenerDesafios(@Header("Authorization") String token);

        @GET("api/Desafio/mis-desafios")
        Call<List<Desafio>> obtenerMisDesafios(@Header("Authorization") String token);

        @POST("api/Desafio")
        Call<DesafioResponse> crearDesafio(@Header("Authorization") String token, @Body Desafio desafio);

        @POST("api/Desafio/asignar")
        Call<ResponseBody> asignarDesafio(@Header("Authorization") String token, @Body AsignarDesafioRequest request);

        // SOLICITUDES
        @GET("api/Solicitud/buscar-entrenadores")
        Call<List<Usuario>> buscarEntrenadores(@Header("Authorization") String token, @Query("nombre") String nombre);

        @POST("api/Solicitud")
        Call<String> enviarSolicitud(@Header("Authorization") String token, @Body SolicitudRequest solicitud);

        @GET("api/Solicitud/pendientes")
        Call<List<Solicitud>> obtenerSolicitudesPendientes(@Header("Authorization") String token);

        @PUT("api/Solicitud/{id}/aceptar")
        Call<String> aceptarSolicitud(@Header("Authorization") String token, @Path("id") int id);

        @PUT("api/Solicitud/{id}/rechazar")
        Call<String> rechazarSolicitud(@Header("Authorization") String token, @Path("id") int id);
    }

    public static class SolicitudRequest {
        public int idEntrenador;
        public SolicitudRequest(int idEntrenador) { this.idEntrenador = idEntrenador; }
    }

    public static class RegistroRequest {
        public String nombre;
        public String email;
        public String clave;
        public String rol;
        public String objetivo;
        public List<Integer> especialidadIds;
        public List<Integer> objetivoIds;
    }

    public static class AsignarRutinaRequest {
        public int idAlumno;
        public int idRutina;
        public AsignarRutinaRequest(int idAlumno, int idRutina) {
            this.idAlumno = idAlumno;
            this.idRutina = idRutina;
        }
    }

    public static class AsignarDesafioRequest {
        public int idAlumno;
        public int idDesafio;
        public AsignarDesafioRequest(int idAlumno, int idDesafio) {
            this.idAlumno = idAlumno;
            this.idDesafio = idDesafio;
        }
    }
}
