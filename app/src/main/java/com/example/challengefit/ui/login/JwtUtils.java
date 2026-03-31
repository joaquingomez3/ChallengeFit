package com.example.challengefit.ui.login;


import android.util.Base64;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class JwtUtils {

    public static class UsuarioToken {
        public String mail;
        public String id;
        public String rol;
    }

    public static UsuarioToken obtenerDatosDesdeToken(String jwt) throws Exception {
        String[] partes = jwt.split("\\."); //Separa en fragmentos el token
        if (partes.length != 3) {
            throw new Exception("Token JWT inválido");
        }

// El payload es la segunda parte
        String payload = partes[1];

// Decodificación Base64 URL Safe
        byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
        String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

        JSONObject json = new JSONObject(decodedPayload);

        UsuarioToken usuario = new UsuarioToken();

// Email: según cómo .NET serialice el claim puede venir como unique_name o name
        usuario.mail = json.optString("unique_name", json.optString("name", ""));

// Id: en tu caso lo cargaste como "Id"
        usuario.id = json.optString("Id", "");

// Rol: normalmente role
        usuario.rol = json.optString("role", "");

        return usuario;
    }
}