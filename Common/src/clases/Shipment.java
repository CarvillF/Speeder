package clases;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Shipment {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty origen = new SimpleStringProperty();
    private final StringProperty destino = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final StringProperty tipoPaquete = new SimpleStringProperty();
    private final DoubleProperty peso = new SimpleDoubleProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final DoubleProperty tarifa = new SimpleDoubleProperty();

    public Shipment() {
    }

    public Shipment(int id,
                    String origen,
                    String destino,
                    String descripcion,
                    String tipoPaquete,
                    double peso,
                    String estado,
                    double tarifa) {
        this.id.set(id);
        this.origen.set(origen);
        this.destino.set(destino);
        this.descripcion.set(descripcion);
        this.tipoPaquete.set(tipoPaquete);
        this.peso.set(peso);
        this.estado.set(estado);
        this.tarifa.set(tarifa);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getOrigen() {
        return origen.get();
    }

    public void setOrigen(String origen) {
        this.origen.set(origen);
    }

    public StringProperty origenProperty() {
        return origen;
    }

    public String getDestino() {
        return destino.get();
    }

    public void setDestino(String destino) {
        this.destino.set(destino);
    }

    public StringProperty destinoProperty() {
        return destino;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public String getTipoPaquete() {
        return tipoPaquete.get();
    }

    public void setTipoPaquete(String tipoPaquete) {
        this.tipoPaquete.set(tipoPaquete);
    }

    public StringProperty tipoPaqueteProperty() {
        return tipoPaquete;
    }

    public double getPeso() {
        return peso.get();
    }

    public void setPeso(double peso) {
        this.peso.set(peso);
    }

    public DoubleProperty pesoProperty() {
        return peso;
    }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public double getTarifa() {
        return tarifa.get();
    }

    public void setTarifa(double tarifa) {
        this.tarifa.set(tarifa);
    }

    public DoubleProperty tarifaProperty() {
        return tarifa;
    }

    public double getPrecioEstimado() {
        return tarifa.get();
    }

    public void setPrecioEstimado(double precioEstimado) {
        this.tarifa.set(precioEstimado);
    }

    public DoubleProperty precioEstimadoProperty() {
        return tarifa;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + getId() +
                ", origen='" + getOrigen() + '\'' +
                ", destino='" + getDestino() + '\'' +
                ", descripcion='" + getDescripcion() + '\'' +
                ", tipoPaquete='" + getTipoPaquete() + '\'' +
                ", peso=" + getPeso() +
                ", estado='" + getEstado() + '\'' +
                ", tarifa=" + getTarifa() +
                '}';
    }
}