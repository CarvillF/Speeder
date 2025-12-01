package clases;

import java.util.ArrayList;

public class Transportistas extends Usuario {
    // atributos
    private float monedero;
    private ArrayList<Vehiculo> vehiculos;

    public Transportistas(String nombre, String correo, String CI, float monedero, ArrayList<Vehiculo> vehiculos) {
        super(nombre, correo, CI);
        this.monedero = monedero;
        this.vehiculos = vehiculos;
    }

    // setters
    public void setMonedero(float new_monedero) {
        this.monedero = new_monedero;
    }

    // getters
    public float getMonedero() {
        return monedero;
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
