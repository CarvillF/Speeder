package clases;

public class Usuario {
    // atributos
    private String cedula; // Renamed from CI
    private String nombre;
    private String apellidos; // New field
    private String correo;
    private String contrasena; // New field
    private String numero_telefono; // New field
    private int id_direccion_principal; // Changed from String direccion

    // constructor
    public Usuario(String cedula, String nombre, String apellidos, String correo, String contrasena,
            String numero_telefono, int id_direccion_principal) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.numero_telefono = numero_telefono;
        this.id_direccion_principal = id_direccion_principal;
    }

    // setters
    public void setCedula(String new_cedula) {
        this.cedula = new_cedula;
    }

    public void setNombre(String new_nombre) {
        this.nombre = new_nombre;
    }

    public void setApellidos(String new_apellidos) {
        this.apellidos = new_apellidos;
    }

    public void setCorreo(String new_correo) {
        this.correo = new_correo;
    }

    public void setContrasena(String new_contrasena) {
        this.contrasena = new_contrasena;
    }

    public void setNumeroTelefono(String new_numero_telefono) {
        this.numero_telefono = new_numero_telefono;
    }

    public void setIdDireccionPrincipal(int new_id_direccion) {
        this.id_direccion_principal = new_id_direccion;
    }

    // getters
    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNumeroTelefono() {
        return numero_telefono;
    }

    public int getIdDireccionPrincipal() {
        return id_direccion_principal;
    }
}
