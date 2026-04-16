package com.example.challengefit.modelos;

import java.util.List;

public class Rutina {
    private int id;
    private String nombre;
    private String nivel;
    private String descripcion;
    private int duracion;
    private Integer idEntrenador;
    private List<RutinaEjercicio> rutinaEjercicios;
    private int porcentaje; // AÑADIDO: Para mostrar el progreso en la lista del entrenador

    public Rutina() {}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public Integer getIdEntrenador() { return idEntrenador; }
    public void setIdEntrenador(Integer idEntrenador) { this.idEntrenador = idEntrenador; }
    public List<RutinaEjercicio> getRutinaEjercicios() { return rutinaEjercicios; }
    public void setRutinaEjercicios(List<RutinaEjercicio> rutinaEjercicios) { this.rutinaEjercicios = rutinaEjercicios; }
    public int getPorcentaje() { return porcentaje; }
    public void setPorcentaje(int porcentaje) { this.porcentaje = porcentaje; }
}
