package com.example.challengefit.modelos;

public class UsuarioRutina {
    private int id;
    private int idUsuario;
    private int idRutina;
    private Rutina rutina;
    private String fechaAsignacion;
    private String fechaFinalizacion;
    private boolean completado;
    public UsuarioRutina() {}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdRutina() { return idRutina; }
    public void setIdRutina(int idRutina) { this.idRutina = idRutina; }
    public Rutina getRutina() { return rutina; }
    public void setRutina(Rutina rutina) { this.rutina = rutina; }
    public String getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(String fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
    public String getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(String fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }
    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}
