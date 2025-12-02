package clases;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vehicle {

    private final IntegerProperty idVehiculo = new SimpleIntegerProperty();
    private final StringProperty placa = new SimpleStringProperty();
    private final StringProperty nombreModelo = new SimpleStringProperty();
    private final StringProperty color = new SimpleStringProperty();
    private final StringProperty transportistaCedula = new SimpleStringProperty();

    public Vehicle() {
    }

    public Vehicle(int idVehiculo, String placa, String nombreModelo, String color) {
        this.idVehiculo.set(idVehiculo);
        this.placa.set(placa);
        this.nombreModelo.set(nombreModelo);
        this.color.set(color);
    }

    public int getIdVehiculo() {
        return idVehiculo.get();
    }

    public void setIdVehiculo(int id) {
        this.idVehiculo.set(id);
    }

    public IntegerProperty idVehiculoProperty() {
        return idVehiculo;
    }

    public String getPlaca() {
        return placa.get();
    }

    public void setPlaca(String placa) {
        this.placa.set(placa);
    }

    public StringProperty placaProperty() {
        return placa;
    }

    public String getNombreModelo() {
        return nombreModelo.get();
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo.set(nombreModelo);
    }

    public StringProperty nombreModeloProperty() {
        return nombreModelo;
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public StringProperty colorProperty() {
        return color;
    }

    public String getTransportistaCedula() {
        return transportistaCedula.get();
    }

    public void setTransportistaCedula(String transportistaCedula) {
        this.transportistaCedula.set(transportistaCedula);
    }

    public StringProperty transportistaCedulaProperty() {
        return transportistaCedula;
    }

    @Override
    public String toString() {
        return getIdVehiculo() + " - " + getPlaca() + " (" + getNombreModelo() + ")";
    }
}