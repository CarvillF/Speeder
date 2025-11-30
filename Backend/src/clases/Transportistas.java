package clases;

import java.util.ArrayList;

public class Transportistas extends Usuario {
    // atributos
    private float comision;
    private ArrayList<Vehiculo> vehiculos;

    public Transportistas(String nombre, String correo, String CI, float comision, ArrayList<Vehiculo> vehiculos) {
        super(nombre, correo, CI);
        this.comision = comision;
        this.vehiculos = vehiculos;
    }

    // setters
    public void setComision(float new_comision) {
        this.comision = new_comision;
    }

    // getters
    public float getComision() {
        return comision;
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
