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
    private int progreso; // Campo anterior, se mantiene por compatibilidad si es necesario
    private int porcentajeGeneral; // Nuevo campo de la API
    private List<RutinaProgreso> rutinas; // Nueva estructura de rutinas para progreso
    private List<Especialidad> especialidades;

    public Usuario() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public int getPorcentajeGeneral() { return porcentajeGeneral; }
    public void setPorcentajeGeneral(int porcentajeGeneral) { this.porcentajeGeneral = porcentajeGeneral; }
    public List<RutinaProgreso> getRutinas() { return rutinas; }
    public void setRutinas(List<RutinaProgreso> rutinas) { this.rutinas = rutinas; }
    public List<Especialidad> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<Especialidad> especialidades) { this.especialidades = especialidades; }

    public static class Especialidad {
        private int id;
        private String nombre;
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class RutinaProgreso {
        private int id;
        private int idRutina;
        private String nombreRutina;
        private boolean completado;
        private int porcentaje;
        private int ejerciciosCompletados;
        private int totalEjercicios;

        public String getNombreRutina() { return nombreRutina; }
        public int getPorcentaje() { return porcentaje; }
        public int getEjerciciosCompletados() { return ejerciciosCompletados; }
        public int getTotalEjercicios() { return totalEjercicios; }
    }
}
