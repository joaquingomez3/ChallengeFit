package com.example.challengefit.modelos;

public class Notificacion {
    private String titulo;
    private String cuerpo;
    private String tiempo;

    public Notificacion(String titulo, String cuerpo, String tiempo) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.tiempo = tiempo;
    }

    public String getTitulo() { return titulo; }
    public String getCuerpo() { return cuerpo; }
    public String getTiempo() { return tiempo; }
}
