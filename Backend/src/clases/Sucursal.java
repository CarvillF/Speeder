package clases;

public class Sucursal {
    private int id_direccion;
    private String compania_RUC;
    private boolean activa;

    public Sucursal(int id_direccion, String compania_RUC, boolean activa) {
        this.id_direccion = id_direccion;
        this.compania_RUC = compania_RUC;
        this.activa = activa;
    }

    // Getters and Setters
    public int getIdDireccion() {
        return id_direccion;
    }

    public void setIdDireccion(int id_direccion) {
        this.id_direccion = id_direccion;
    }

    public String getCompaniaRUC() {
        return compania_RUC;
    }

    public void setCompaniaRUC(String compania_RUC) {
        this.compania_RUC = compania_RUC;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
