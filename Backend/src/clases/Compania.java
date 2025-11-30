package clases;

public class Compania {
    private String RUC;
    private String empresario_cedula;
    private String nombre_compania;
    private String tipo_compania;
    private String descripcion;

    public Compania(String RUC, String empresario_cedula, String nombre_compania, String tipo_compania,
            String descripcion) {
        this.RUC = RUC;
        this.empresario_cedula = empresario_cedula;
        this.nombre_compania = nombre_compania;
        this.tipo_compania = tipo_compania;
        this.descripcion = descripcion;
    }

    // Getters and Setters
    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
    }

    public String getEmpresarioCedula() {
        return empresario_cedula;
    }

    public void setEmpresarioCedula(String empresario_cedula) {
        this.empresario_cedula = empresario_cedula;
    }

    public String getNombreCompania() {
        return nombre_compania;
    }

    public void setNombreCompania(String nombre_compania) {
        this.nombre_compania = nombre_compania;
    }

    public String getTipoCompania() {
        return tipo_compania;
    }

    public void setTipoCompania(String tipo_compania) {
        this.tipo_compania = tipo_compania;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
