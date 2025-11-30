package protocol;

/**
 * Clase que representa una petición del cliente al servidor.
 * Estructura estándar para comunicación JSON.
 */
public class Request {
    /**
     * Acción a realizar: LOGIN, CREATE_ORDER, UPDATE_STATUS, etc.
     */
    private String action;

    /**
     * Datos variables específicos de la acción (puede ser un Map, Object, etc.)
     */
    private Object payload;

    /**
     * Constructor por defecto (necesario para deserialización con Gson)
     */
    public Request() {
    }

    /**
     * Constructor con parámetros
     * 
     * @param action  Acción a realizar
     * @param payload Datos de la petición
     */
    public Request(String action, Object payload) {
        this.action = action;
        this.payload = payload;
    }

    // Getters y Setters

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Request{action='" + action + "', payload=" + payload + "}";
    }
}
