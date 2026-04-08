package com.example.challengefit.modelos;

public class Solicitud {
    private int id;
    private int idAlumno;
    private Usuario alumno;
    private int idEntrenador;
    private String estado; // "Pendiente", "Aceptada", "Rechazada"
    private String fechaCreacion;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdAlumno() { return idAlumno; }
    public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }
    public Usuario getAlumno() { return alumno; }
    public void setAlumno(Usuario alumno) { this.alumno = alumno; }
    public int getIdEntrenador() { return idEntrenador; }
    public void setIdEntrenador(int idEntrenador) { this.idEntrenador = idEntrenador; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
