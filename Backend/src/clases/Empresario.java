package clases;

public class Empresario extends Usuario {
    // atributos
    private String cargo_empresa;
    private String correo_empresarial;

    // constructor
    public Empresario(String cedula, String nombre, String apellidos, String correo, String contrasena,
            String numero_telefono, int id_direccion_principal, String cargo_empresa, String correo_empresarial) {
        super(cedula, nombre, apellidos, correo, contrasena, numero_telefono, id_direccion_principal);
        this.cargo_empresa = cargo_empresa;
        this.correo_empresarial = correo_empresarial;
    }

    // setters
    public void setCargoEmpresa(String cargo_empresa) {
        this.cargo_empresa = cargo_empresa;
    }

    public void setCorreoEmpresarial(String correo_empresarial) {
        this.correo_empresarial = correo_empresarial;
    }

    // getters
    public String getCargoEmpresa() {
        return cargo_empresa;
    }

    public String getCorreoEmpresarial() {
        return correo_empresarial;
    }
}
