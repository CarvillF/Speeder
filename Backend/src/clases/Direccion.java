package clases;

import java.math.BigDecimal;

public class Direccion {
    private int id_direccion;
    private String nombre_ciudad;
    private BigDecimal coordenada_x;
    private BigDecimal coordenada_y;
    private String calle_principal;
    private String calle_secundaria;
    private String numero_edificacion;
    private String detalle;

    public Direccion(int id_direccion, String nombre_ciudad, BigDecimal coordenada_x, BigDecimal coordenada_y,
            String calle_principal, String calle_secundaria, String numero_edificacion, String detalle) {
        this.id_direccion = id_direccion;
        this.nombre_ciudad = nombre_ciudad;
        this.coordenada_x = coordenada_x;
        this.coordenada_y = coordenada_y;
        this.calle_principal = calle_principal;
        this.calle_secundaria = calle_secundaria;
        this.numero_edificacion = numero_edificacion;
        this.detalle = detalle;
    }

    // Getters and Setters
    public int getIdDireccion() {
        return id_direccion;
    }

    public void setIdDireccion(int id_direccion) {
        this.id_direccion = id_direccion;
    }

    public String getNombreCiudad() {
        return nombre_ciudad;
    }

    public void setNombreCiudad(String nombre_ciudad) {
        this.nombre_ciudad = nombre_ciudad;
    }

    public BigDecimal getCoordenadaX() {
        return coordenada_x;
    }

    public void setCoordenadaX(BigDecimal coordenada_x) {
        this.coordenada_x = coordenada_x;
    }

    public BigDecimal getCoordenadaY() {
        return coordenada_y;
    }

    public void setCoordenadaY(BigDecimal coordenada_y) {
        this.coordenada_y = coordenada_y;
    }

    public String getCallePrincipal() {
        return calle_principal;
    }

    public void setCallePrincipal(String calle_principal) {
        this.calle_principal = calle_principal;
    }

    public String getCalleSecundaria() {
        return calle_secundaria;
    }

    public void setCalleSecundaria(String calle_secundaria) {
        this.calle_secundaria = calle_secundaria;
    }

    public String getNumeroEdificacion() {
        return numero_edificacion;
    }

    public void setNumeroEdificacion(String numero_edificacion) {
        this.numero_edificacion = numero_edificacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
