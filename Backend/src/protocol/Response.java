package protocol;

/**
 * Clase que representa una respuesta del servidor al cliente.
 * Estructura estándar para comunicación JSON.
 */
public class Response {
    /**
     * Estado de la respuesta: SUCCESS, ERROR, etc.
     */
    private String status;

    /**
     * Mensaje descriptivo de la respuesta
     */
    private String message;

    /**
     * Datos adicionales de la respuesta (opcional)
     */
    private Object data;

    /**
     * Constructor por defecto (necesario para deserialización con Gson)
     */
    public Response() {
    }

    /**
     * Constructor con status y mensaje
     * 
     * @param status  Estado de la respuesta
     * @param message Mensaje descriptivo
     */
    public Response(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    /**
     * Constructor completo con datos adicionales
     * 
     * @param status  Estado de la respuesta
     * @param message Mensaje descriptivo
     * @param data    Datos adicionales
     */
    public Response(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters y Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{status='" + status + "', message='" + message + "', data=" + data + "}";
    }
}
