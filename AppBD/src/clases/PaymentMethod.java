package clases;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PaymentMethod {

    private IntegerProperty idMetodoPago = new SimpleIntegerProperty();
    private StringProperty tipo = new SimpleStringProperty();
    private StringProperty datos = new SimpleStringProperty();
    private StringProperty createdAt = new SimpleStringProperty();

    public PaymentMethod() {
    }

    public int getIdMetodoPago() {
        return idMetodoPago.get();
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago.set(idMetodoPago);
    }

    public IntegerProperty idMetodoProperty() {
        return idMetodoPago;
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public String getDatos() {
        return datos.get();
    }

    public void setDatos(String datos) {
        this.datos.set(datos);
    }

    public StringProperty datosProperty() {
        return datos;
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }
}