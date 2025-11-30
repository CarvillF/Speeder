package

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//obtiene los datos de config.properties
public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                System.exit(1);
            }

            // Load the properties file
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // obtiene url
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    // ontiene usuario
    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    // obtiene contraseña
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }
}