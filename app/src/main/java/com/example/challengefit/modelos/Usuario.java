package com.example.challengefit.modelos;

import java.util.List;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String claveHash;
    private String rol;
    private String objetivo;
    private Integer entrenadorId;
    private int progreso;
    private List<Rutina> rutinas;
    private List<Especialidad> especialidades; // Nuevo campo

    public Usuario() {}

    // Getters y Setters existentes
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Especialidad> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<Especialidad> especialidades) { this.especialidades = especialidades; }

    // Clase interna para Especialidad
    public static class Especialidad {
        private int id;
        private String nombre;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
    
    // Mantener los otros getters si los necesitas...
    public int getProgreso() { return progreso; }
    public void setProgreso(int progreso) { this.progreso = progreso; }
}
