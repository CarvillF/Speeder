package clases;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PaymentMethod {

    private final IntegerProperty idMetodoPago = new SimpleIntegerProperty();
    private final StringProperty tipo = new SimpleStringProperty();
    private final StringProperty datos = new SimpleStringProperty();
    private final BooleanProperty predeterminado = new SimpleBooleanProperty();
    private final StringProperty createdAt = new SimpleStringProperty();

    public PaymentMethod() {
    }

    public int getIdMetodoPago() {
        return idMetodoPago.get();
    }

    public void setIdMetodoPago(int id) {
        this.idMetodoPago.set(id);
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

    public boolean isPredeterminado() {
        return predeterminado.get();
    }

    public void setPredeterminado(boolean pred) {
        this.predeterminado.set(pred);
    }

    public BooleanProperty predeterminadoProperty() {
        return predeterminado;
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