package com.example.challengefit.ui.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenUtils {

    public static class DatosToken {
        public String mail;
        public String id;
        public String rol;
    }

    public static DatosToken validarYObtenerDatos(String token, String secretKey) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("ApiChallengeFit") // opcional pero recomendable
            .withAudience("ApiChallengeFitUsers") // opcional pero recomendable
            .build();

            DecodedJWT jwt = verifier.verify(token);

            DatosToken datos = new DatosToken();

            // Según .NET puede venir como "unique_name"
            datos.mail = jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name").asString();

            // En tu backend lo cargaste como "Id"
            datos.id = jwt.getClaim("Id").asString();

            // Role suele salir como "role"
            datos.rol = jwt.getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role").asString();

            return datos;

        } catch (JWTVerificationException e) {
            throw new Exception("Token inválido o firma incorrecta");
        }
    }
}
