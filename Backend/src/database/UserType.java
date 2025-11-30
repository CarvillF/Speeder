package database;

/**
 * Enum que representa los diferentes tipos de usuarios del sistema Speeder.
 * Cada tipo de usuario tiene acceso a diferentes recursos de la base de datos.
 */
public enum UserType {
    /**
     * Emprendedor/Empresario: Usuario que gestiona compañías y sucursales.
     * Archivo de credenciales: emprendedor.properties
     */
    EMPRENDEDOR("emprendedor.properties"),

    /**
     * Transportista: Usuario que gestiona envíos y vehículos.
     * Archivo de credenciales: transportista.properties
     */
    TRANSPORTISTA("transportista.properties"),

    /**
     * Administrador: Usuario con acceso completo al sistema.
     * Archivo de credenciales: administrador.properties
     */
    ADMINISTRADOR("administrador.properties");

    private final String credentialsFile;

    UserType(String credentialsFile) {
        this.credentialsFile = credentialsFile;
    }

    /**
     * Obtiene el nombre del archivo de credenciales para este tipo de usuario.
     * 
     * @return Nombre del archivo de credenciales
     */
    public String getCredentialsFile() {
        return credentialsFile;
    }
}
