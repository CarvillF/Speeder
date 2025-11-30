package clases;

public class Paquete {
    // atributos
    private String descripcion;
    private float peso;
    private float[] dimensiones = new float[3]; // alto[0] - largo[1] - profundidad[2]
    private boolean fragilidad;
    private int riesgo_salud;
    private int inflamabilidad;
    private int reactividad;
    private String riesgo_especifico;

    // constructor
    public Paquete(String descripcion, float peso, float[] dimensiones) {
        this.descripcion = descripcion;
        this.peso = peso;
        this.dimensiones = dimensiones;
    }

    // setter
    public void setDescripcion(String new_descr) {
        this.descripcion = new_descr;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public void setDimensiones(float[] dimensiones) {
        this.dimensiones = dimensiones;
    }

    public void setFragilidad(boolean fragilidad) {
        this.fragilidad = fragilidad;
    }

    public void setRiesgo_salud(int riesgo_salud) {
        this.riesgo_salud = riesgo_salud;
    }

    public void setInflamabilidad(int inflamabilidad) {
        this.inflamabilidad = inflamabilidad;
    }

    public void setReactividad(int reactividad) {
        this.reactividad = reactividad;
    }

    public void setRiesgo_especifico(String riesgo_especifico) {
        this.riesgo_especifico = riesgo_especifico;
    }

    // getters
    public String getDescripcion() {
        return descripcion;
    }

    public float getPeso() {
        return peso;
    }

    // -----Dimensiones-----
    public float[] getDimensiones() {
        return dimensiones;
    }

    public float getAlto() {
        return dimensiones[0];
    }

    public float getAncho() {
        return dimensiones[1];
    }

    public float getProfundidad() {
        return dimensiones[2];
    }
    // ---------------------

    public boolean getFragilidad() {
        return fragilidad;
    }

    public int getRiesgo_salud() {
        return riesgo_salud;
    }

    public int getInflamabilidad() {
        return inflamabilidad;
    }

    public int getReactividad() {
        return reactividad;
    }

    public String getRiesgo_especifico() {
        return riesgo_especifico;
    }
}