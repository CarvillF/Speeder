package back.clases;

public class Cliente extends Usuario {
    //atributos
    private String dir_empresa;

    // constructor
    public Cliente(String nombre, String correo, String CI, String dir_empresa) {
        super(nombre, correo, CI);
        this.dir_empresa = dir_empresa;
    }

    //setter
    public void setDirEmpresa(String new_dir_empresa){
        this.dir_empresa = new_dir_empresa;
    }

    //getters
    public String getDirEmpresa(){
        return dir_empresa;
    }
}
