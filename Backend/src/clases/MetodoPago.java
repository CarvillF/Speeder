package clases;

public class MetodoPago {
    private int id_metodo_pago;
    private String usuario_cedula;
    private String tipo;
    private String datos; // JSON string
    private boolean predeterminado;

    public MetodoPago(int id_metodo_pago, String usuario_cedula, String tipo, String datos, boolean predeterminado) {
        this.id_metodo_pago = id_metodo_pago;
        this.usuario_cedula = usuario_cedula;
        this.tipo = tipo;
        this.datos = datos;
        this.predeterminado = predeterminado;
    }

    // Getters and Setters
    public int getIdMetodoPago() {
        return id_metodo_pago;
    }

    public void setIdMetodoPago(int id_metodo_pago) {
        this.id_metodo_pago = id_metodo_pago;
    }

    public String getUsuarioCedula() {
        return usuario_cedula;
    }

    public void setUsuarioCedula(String usuario_cedula) {
        this.usuario_cedula = usuario_cedula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public boolean isPredeterminado() {
        return predeterminado;
    }

    public void setPredeterminado(boolean predeterminado) {
        this.predeterminado = predeterminado;
    }
}
