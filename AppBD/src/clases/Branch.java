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

    public int getDireccionId() {
        return direccionId.get();
    }

    public void setDireccionId(int value) {
        direccionId.set(value);
    }

    public IntegerProperty direccionIdProperty() {
        return direccionId;
    }

    public String getCompaniaRuc() {
        return companiaRuc.get();
    }

    public void setCompaniaRuc(String value) {
        companiaRuc.set(value);
    }

    public StringProperty companiaRucProperty() {
        return companiaRuc;
    }

    public boolean isActiva() {
        return activa.get();
    }

    public void setActiva(boolean value) {
        activa.set(value);
    }

    public BooleanProperty activaProperty() {
        return activa;
    }
}