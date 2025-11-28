package clases;

public class Request {

    private String action;   // Ej: "LOGIN", "REGISTER_DRIVER", etc.
    private Object payload;  // Lo que quieras enviar (Map, User, id, etc.)

    public Request(String action, Object payload) {
        this.action = action;
        this.payload = payload;
    }

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
}