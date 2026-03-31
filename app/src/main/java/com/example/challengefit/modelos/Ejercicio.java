package com.example.challengefit.modelos;

public class Ejercicio {
    private int id;
    private String nombre;
    private String grupoMuscular;
    public Ejercicio() {}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; }
}
