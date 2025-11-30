package clases;

public class Vehiculo {
    // atributos
    private String matricula;
    private String modelo;
    private int anio;
    private double volumen;

    // constructor
    public Vehiculo(String matricula, String modelo, int anio, double volumen) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.anio = anio;
        this.volumen = volumen;
    }

    // setters
    public void setVolument(double new_volumen) {
        this.volumen = new_volumen;
    }

    // getters
    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public double getVolumen() {
        return volumen;
    }
}
