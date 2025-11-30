package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cargador de variables de entorno desde archivo .env
 * Permite cargar configuraciones sin dependencias externas.
 */
public class EnvLoader {

    private static final Map<String, String> envVars = new HashMap<>();
    private static boolean loaded = false;

    /**
     * Carga las variables del archivo .env en memoria.
     * Si el archivo no existe, solo se usarán las variables de entorno del sistema.
     */
    public static void load() {
        if (loaded)
            return;

        Path envPath = Paths.get(".env");
        if (Files.exists(envPath)) {
            try {
                List<String> lines = Files.readAllLines(envPath);
                for (String line : lines) {
                    String trimmedLine = line.trim();
                    // Ignorar comentarios y líneas vacías
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                        continue;
                    }

                    // Parsear KEY=VALUE
                    int equalsIndex = trimmedLine.indexOf('=');
                    if (equalsIndex > 0) {
                        String key = trimmedLine.substring(0, equalsIndex).trim();
                        String value = trimmedLine.substring(equalsIndex + 1).trim();
                        envVars.put(key, value);
                    }
                }
                System.out.println("Loaded .env file successfully");
            } catch (IOException e) {
                System.err.println("Warning: Could not read .env file: " + e.getMessage());
            }
        } else {
            System.out.println("No .env file found, relying on system environment variables");
        }

        loaded = true;
    }

    /**
     * Obtiene el valor de una variable de configuración.
     * Prioridad:
     * 1. Variable de entorno del sistema (System.getenv)
     * 2. Archivo .env
     * 
     * @param key Nombre de la variable
     * @return Valor de la variable o null si no existe
     */
    public static String get(String key) {
        if (!loaded)
            load();

        // Prioridad a variables del sistema (útil para producción/docker)
        String systemValue = System.getenv(key);
        if (systemValue != null) {
            return systemValue;
        }

        return envVars.get(key);
    }
}
