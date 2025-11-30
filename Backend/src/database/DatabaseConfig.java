package database;

/**
 * Configuración de base de datos segura usando variables de entorno.
 * Utiliza EnvLoader para leer del archivo .env o variables del sistema.
 */
public class DatabaseConfig {

    static {
        // Cargar variables de entorno al iniciar la clase
        EnvLoader.load();
    }

    /**
     * Obtiene la URL de conexión.
     * Actualmente es compartida para todos los usuarios, pero acepta el tipo por si se requiere diferenciación futura.
     */
    public static String getDbUrl(UserType userType) {
        String url = EnvLoader.get("SPEEDER_DB_URL");
        if (url == null) {
            throw new RuntimeException("CRITICAL: SPEEDER_DB_URL not found in environment or .env file");
        }
        return url;
    }

    /**
     * Obtiene el nombre de usuario para un tipo de usuario específico.
     * Busca la variable: SPEEDER_[TIPO]_USER
     */
    public static String getDbUsername(UserType userType) {
        String key = "SPEEDER_" + userType.name() + "_USER";
        String value = EnvLoader.get(key);
        if (value == null) {
            throw new RuntimeException("CRITICAL: " + key + " not found in environment or .env file");
        }
        return value;
    }

    /**
     * Obtiene la contraseña para un tipo de usuario específico.
     * Busca la variable: SPEEDER_[TIPO]_PASS
     */
    public static String getDbPassword(UserType userType) {
        String key = "SPEEDER_" + userType.name() + "_PASS";
        String value = EnvLoader.get(key);
        if (value == null) {
            throw new RuntimeException("CRITICAL: " + key + " not found in environment or .env file");
        }
        return value;
    }
    
    // Métodos legacy eliminados para forzar el uso de la nueva configuración segura
}