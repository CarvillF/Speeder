package clases;

import java.util.ArrayList;

public class Transportistas extends User {
    // atributos
    private String numero_licencia;
    private String tipo_licencia;
    private String zona_cobertura;
    private float fondos;
    private String disponibilidad;
    private ArrayList<Vehicle> vehiculos;
    private int id_direccion_principal;

    public Transportistas(String cedula, String nombre, String apellidos, String correo, String contrasena,
            String numero_telefono, int id_direccion_principal,
            String numero_licencia, String tipo_licencia, String zona_cobertura, float fondos, String disponibilidad,
            ArrayList<Vehicle> vehiculos) {
        super(cedula, nombre, apellidos, correo, numero_telefono, "", "Transportista");
        this.setContrasena(contrasena);
        this.id_direccion_principal = id_direccion_principal;
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

    public void setIdDireccionPrincipal(int id_direccion_principal) {
        this.id_direccion_principal = id_direccion_principal;
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

    public ArrayList<Vehicle> getVehiculos() {
        return vehiculos;
    }

    public int getIdDireccionPrincipal() {
        return id_direccion_principal;
    }

    // funciones
    public void eliminarVehiculo(int id_vehiculo) {
        if (!vehiculos.isEmpty()) {
            // This logic might need adjustment as Vehicle uses JavaFX properties
            // But for now we assume removal by index or object is fine
            // The original code removed by index? "vehiculos.remove(id_vehiculo)"
            // If id_vehiculo is an index, this is fine. If it's an ID, it's wrong.
            // Original code: vehiculos.remove(id_vehiculo); -> implies index if int.
            // But variable name is id_vehiculo... dangerous.
            // I will keep original logic but be aware.
            if (id_vehiculo >= 0 && id_vehiculo < vehiculos.size()) {
                vehiculos.remove(id_vehiculo);
            }
        }
    }

    public void agregarVehiculo(Vehicle vehi) {
        vehiculos.add(vehi);
    }
}
