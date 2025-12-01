package clases;

import java.util.ArrayList;

public class Transportistas extends Usuario {
    // atributos
    private String numero_licencia;
    private String tipo_licencia;
    private String zona_cobertura;
    private float fondos;
    private String disponibilidad;
    private ArrayList<Vehiculo> vehiculos;

    public Transportistas(String cedula, String nombre, String apellidos, String correo, String contrasena,
            String numero_telefono, int id_direccion_principal,
            String numero_licencia, String tipo_licencia, String zona_cobertura, float fondos, String disponibilidad,
            ArrayList<Vehiculo> vehiculos) {
        super(cedula, nombre, apellidos, correo, contrasena, numero_telefono, id_direccion_principal);
        this.numero_licencia = numero_licencia;
        this.tipo_licencia = tipo_licencia;
        this.zona_cobertura = zona_cobertura;
        this.fondos = fondos;
        this.disponibilidad = disponibilidad;
        this.vehiculos = vehiculos;
    }

    // setters
    public void setFondos(float new_fondos) {
        this.fondos = new_fondos;
    }

    public void setNumeroLicencia(String numero_licencia) {
        this.numero_licencia = numero_licencia;
    }

    public void setTipoLicencia(String tipo_licencia) {
        this.tipo_licencia = tipo_licencia;
    }

    public void setZonaCobertura(String zona_cobertura) {
        this.zona_cobertura = zona_cobertura;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    // getters
    public float getFondos() {
        return fondos;
    }

    public String getNumeroLicencia() {
        return numero_licencia;
    }

    public String getTipoLicencia() {
        return tipo_licencia;
    }

    public String getZonaCobertura() {
        return zona_cobertura;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    // funciones
    public void eliminarVehiculo(int id_vehiculo) {
        if (!vehiculos.isEmpty()) {
            vehiculos.remove(id_vehiculo);
        }
    }

    public void agregarVehiculo(Vehiculo vehi) {
        vehiculos.add(vehi);
    }
}
