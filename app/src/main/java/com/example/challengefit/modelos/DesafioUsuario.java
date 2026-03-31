package com.example.challengefit.modelos;

public class DesafioUsuario {
    private int id;
    private int idUsuario;
    private int idDesafio;
    private Desafio desafio;
    private int progreso;
    private boolean completado;
    private String fechaAsignado;
    public DesafioUsuario() {}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdDesafio() { return idDesafio; }
    public void setIdDesafio(int idDesafio) { this.idDesafio = idDesafio; }
    public Desafio getDesafio() { return desafio; }
    public void setDesafio(Desafio desafio) { this.desafio = desafio; }
    public int getProgreso() { return progreso; }
    public void setProgreso(int progreso) { this.progreso = progreso; }
    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
    public String getFechaAsignado() { return fechaAsignado; }
    public void setFechaAsignado(String fechaAsignado) { this.fechaAsignado = fechaAsignado; }
}
