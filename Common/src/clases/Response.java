package clases;

public class Response {

    private String status; // "SUCCESS", "ERROR"
    private String message; // Mensaje para la UI
    private Object data; // Datos (lista de envíos, usuario, etc.)

    public Response(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Response(String status, String message) {
        this(status, message, null);
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}