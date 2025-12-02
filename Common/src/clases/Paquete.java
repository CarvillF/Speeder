package clases;

public class Paquete {
    // atributos
    private int id_paquete;
    private String descripcion;
    private double peso;
    private String tipo;
    private double dimension_x;
    private double dimension_y;
    private double dimension_z;
    private String requisitos;

    // constructor
    public Paquete(int id_paquete, String descripcion, double peso, String tipo, double dimension_x, double dimension_y,
            double dimension_z, String requisitos) {
        this.id_paquete = id_paquete;
        this.descripcion = descripcion;
        this.peso = peso;
        this.tipo = tipo;
        this.dimension_x = dimension_x;
        this.dimension_y = dimension_y;
        this.dimension_z = dimension_z;
        this.requisitos = requisitos;
    }

    // setters
    public void setIdPaquete(int id_paquete) {
        this.id_paquete = id_paquete;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDimensionX(double dimension_x) {
        this.dimension_x = dimension_x;
    }

    public void setDimensionY(double dimension_y) {
        this.dimension_y = dimension_y;
    }

    public void setDimensionZ(double dimension_z) {
        this.dimension_z = dimension_z;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    // getters
    public int getIdPaquete() {
        return id_paquete;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public String getTipo() {
        return tipo;
    }

    public double getDimensionX() {
        return dimension_x;
    }

    public double getDimensionY() {
        return dimension_y;
    }

    public double getDimensionZ() {
        return dimension_z;
    }

    public String getRequisitos() {
        return requisitos;
    }
}
