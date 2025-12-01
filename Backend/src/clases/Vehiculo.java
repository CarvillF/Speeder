package clases;

public class Vehiculo {
    // atributos
    private int id_vehiculo;
    private String transportista_cedula;
    private String nombre_modelo;
    private String color;
    private String placa;

    // constructor
    public Vehiculo(int id_vehiculo, String transportista_cedula, String nombre_modelo, String color, String placa) {
        this.id_vehiculo = id_vehiculo;
        this.transportista_cedula = transportista_cedula;
        this.nombre_modelo = nombre_modelo;
        this.color = color;
        this.placa = placa;
    }

    // setters
    public void setIdVehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public void setTransportistaCedula(String transportista_cedula) {
        this.transportista_cedula = transportista_cedula;
    }

    public void setNombreModelo(String nombre_modelo) {
        this.nombre_modelo = nombre_modelo;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    // getters
    public int getIdVehiculo() {
        return id_vehiculo;
    }

    public String getTransportistaCedula() {
        return transportista_cedula;
    }

    public String getNombreModelo() {
        return nombre_modelo;
    }

    public String getColor() {
        return color;
    }

    public String getPlaca() {
        return placa;
    }
}
