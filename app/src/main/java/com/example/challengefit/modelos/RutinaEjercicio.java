package com.example.challengefit.modelos;

public class RutinaEjercicio {
    private int id;
    private int idRutina;
    private int idEjercicio;
    private Ejercicio ejercicio;
    private int series;
    private int repeticiones;
    private boolean completado;
    public RutinaEjercicio() {}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdRutina() { return idRutina; }
    public void setIdRutina(int idRutina) { this.idRutina = idRutina; }
    public int getIdEjercicio() { return idEjercicio; }
    public void setIdEjercicio(int idEjercicio) { this.idEjercicio = idEjercicio; }
    public Ejercicio getEjercicio() { return ejercicio; }
    public void setEjercicio(Ejercicio ejercicio) { this.ejercicio = ejercicio; }
    public int getSeries() { return series; }
    public void setSeries(int series) { this.series = series; }
    public int getRepeticiones() { return repeticiones; }
    public void setRepeticiones(int repeticiones) { this.repeticiones = repeticiones; }
    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}
