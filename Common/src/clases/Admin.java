package clases;

public class Admin extends User {

    private String codigo_empleado;
    private boolean activo;
    private int id_direccion_principal;

    // constructor
    public Admin(String cedula, String nombre, String apellidos, String correo, String contrasena,
            String numero_telefono, int id_direccion_principal, String codigo_empleado, boolean activo) {
        super(cedula, nombre, apellidos, correo, numero_telefono, "", "Admin");
        this.setContrasena(contrasena);
        this.id_direccion_principal = id_direccion_principal;
        this.codigo_empleado = codigo_empleado;
        this.activo = activo;
    }

    // getters and setters
    public String getCodigoEmpleado() {
        return codigo_empleado;
    }

    public void setCodigoEmpleado(String codigo_empleado) {
        this.codigo_empleado = codigo_empleado;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIdDireccionPrincipal() {
        return id_direccion_principal;
    }

    public void setIdDireccionPrincipal(int id_direccion_principal) {
        this.id_direccion_principal = id_direccion_principal;
    }

    // funciones
    public void eliminarUsuario(int id_usuario) {

    }

    public void agregarUsuario() {

    }
}
