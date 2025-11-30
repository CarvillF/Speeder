package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuración de base de datos que soporta múltiples perfiles de usuario.
 * Los archivos de credenciales se encuentran en DbServer/credentials/
 */
public class DatabaseConfig {

    private static final Properties defaultProperties = new Properties();
    private static final Map<UserType, Properties> userPropertiesCache = new HashMap<>();

    // Ruta relativa desde Backend/src hasta DbServer/credentials
    private static final String CREDENTIALS_BASE_PATH = "../../DbServer/credentials/";

    static {
        // Intentar cargar el archivo config.properties por defecto (backward
        // compatibility)
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                defaultProperties.load(input);
                System.out.println("Loaded default config.properties successfully");
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load default config.properties - " + e.getMessage());
        }
    }

    /**
     * Obtiene las propiedades para un tipo de usuario específico.
     * Primero busca en el cache, si no existe lo carga del archivo de credenciales.
     * 
     * @param userType Tipo de usuario
     * @return Properties con las credenciales del usuario
     */
    private static Properties getPropertiesForUser(UserType userType) {
        // Si ya está en cache, retornar
        if (userPropertiesCache.containsKey(userType)) {
            return userPropertiesCache.get(userType);
        }

        // Si no está en cache, cargar del archivo
        Properties props = new Properties();
        String credentialsFile = userType.getCredentialsFile();

        try {
            // Obtener la ruta absoluta del archivo de credenciales
            Path currentPath = Paths.get("").toAbsolutePath();
            Path credentialsPath = currentPath.resolve(CREDENTIALS_BASE_PATH + credentialsFile);

            System.out.println("Loading credentials from: " + credentialsPath.toAbsolutePath());

            try (FileInputStream input = new FileInputStream(credentialsPath.toFile())) {
                props.load(input);
                userPropertiesCache.put(userType, props);
                System.out.println("Successfully loaded credentials for " + userType);
            }
        } catch (IOException e) {
            System.err.println("Error loading credentials for " + userType + ": " + e.getMessage());
            System.err.println("Falling back to default properties if available");
            return defaultProperties; // Fallback to default
        }

        return props;
    }

    /**
     * Obtiene la URL de conexión para un tipo de usuario específico.
     * 
     * @param userType Tipo de usuario
     * @return URL de conexión JDBC
     */
    public static String getDbUrl(UserType userType) {
        return getPropertiesForUser(userType).getProperty("db.url");
    }

    /**
     * Obtiene el nombre de usuario para un tipo de usuario específico.
     * 
     * @param userType Tipo de usuario
     * @return Nombre de usuario de la base de datos
     */
    public static String getDbUsername(UserType userType) {
        return getPropertiesForUser(userType).getProperty("db.username");
    }

    /**
     * Obtiene la contraseña para un tipo de usuario específico.
     * 
     * @param userType Tipo de usuario
     * @return Contraseña de la base de datos
     */
    public static String getDbPassword(UserType userType) {
        return getPropertiesForUser(userType).getProperty("db.password");
    }

    // Métodos mantenidos para backward compatibility con código existente
    /**
     * @deprecated Usar getDbUrl(UserType) en su lugar
     */
    @Deprecated
    public static String getDbUrl() {
        return defaultProperties.getProperty("db.url");
    }

    /**
     * @deprecated Usar getDbUsername(UserType) en su lugar
     */
    @Deprecated
    public static String getDbUsername() {
        return defaultProperties.getProperty("db.username");
    }

    /**
     * @deprecated Usar getDbPassword(UserType) en su lugar
     */
    @Deprecated
    public static String getDbPassword() {
        return defaultProperties.getProperty("db.password");
    }
}