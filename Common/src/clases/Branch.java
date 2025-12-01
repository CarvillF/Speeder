package clases;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Branch {

    private final IntegerProperty direccionId = new SimpleIntegerProperty();
    private final StringProperty companiaRuc = new SimpleStringProperty();
    private final BooleanProperty activa = new SimpleBooleanProperty();

    private final StringProperty ciudad = new SimpleStringProperty();
    private final StringProperty callePrincipal = new SimpleStringProperty();
    private final StringProperty calleSecundaria = new SimpleStringProperty();
    private final StringProperty numeroEdificacion = new SimpleStringProperty();
    private final StringProperty detalleDireccion = new SimpleStringProperty();

    public int getDireccionId() {
        return direccionId.get();
    }

    public void setDireccionId(int id) {
        this.direccionId.set(id);
    }

    public IntegerProperty direccionIdProperty() {
        return direccionId;
    }

    public String getCompaniaRuc() {
        return companiaRuc.get();
    }

    public void setCompaniaRuc(String ruc) {
        this.companiaRuc.set(ruc);
    }

    public StringProperty companiaRucProperty() {
        return companiaRuc;
    }

    public boolean isActiva() {
        return activa.get();
    }

    public void setActiva(boolean activa) {
        this.activa.set(activa);
    }

    public BooleanProperty activaProperty() {
        return activa;
    }

    public String getCiudad() {
        return ciudad.get();
    }

    public void setCiudad(String ciudad) {
        this.ciudad.set(ciudad);
    }

    public StringProperty ciudadProperty() {
        return ciudad;
    }

    public String getCallePrincipal() {
        return callePrincipal.get();
    }

    public void setCallePrincipal(String callePrincipal) {
        this.callePrincipal.set(callePrincipal);
    }

    public StringProperty callePrincipalProperty() {
        return callePrincipal;
    }

    public String getCalleSecundaria() {
        return calleSecundaria.get();
    }

    public void setCalleSecundaria(String calleSecundaria) {
        this.calleSecundaria.set(calleSecundaria);
    }

    public StringProperty calleSecundariaProperty() {
        return calleSecundaria;
    }

    public String getNumeroEdificacion() {
        return numeroEdificacion.get();
    }

    public void setNumeroEdificacion(String numeroEdificacion) {
        this.numeroEdificacion.set(numeroEdificacion);
    }

    public StringProperty numeroEdificacionProperty() {
        return numeroEdificacion;
    }

    public String getDetalleDireccion() {
        return detalleDireccion.get();
    }

    public void setDetalleDireccion(String detalleDireccion) {
        this.detalleDireccion.set(detalleDireccion);
    }

    public StringProperty detalleDireccionProperty() {
        return detalleDireccion;
    }
}