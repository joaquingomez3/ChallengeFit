package com.example.challengefit.modelos;

public class Desafio {
    private int id;
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private int puntos;
    private Integer idEntrenador;
    private String estado; // Campo para mostrar "Activo" o "Finalizado"

    public Desafio() {}

    public Desafio(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    public Integer getIdEntrenador() { return idEntrenador; }
    public void setIdEntrenador(Integer idEntrenador) { this.idEntrenador = idEntrenador; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
