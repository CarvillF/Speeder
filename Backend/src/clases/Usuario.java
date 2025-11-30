package clases;

public class Usuario {
    // atributos
    private String nombre;
    private String correo;
    private String direccion;
    private String CI;

    // constructor
    public Usuario(String nombre, String correo, String CI) {
        this.nombre = nombre;
        this.correo = correo;
        this.CI = CI;
    }

    // setters
    public void setNombre(String new_nombre) {
        this.nombre = new_nombre;
    }

    public void setCorreo(String new_correo) {
        this.correo = new_correo;
    }

    public void setDireccion(String new_direccion) {
        this.direccion = new_direccion;
    }

    // getters
    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCI() {
        return CI;
    }
}
