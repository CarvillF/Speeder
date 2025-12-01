package clases;

import java.time.LocalDate;
import java.time.Duration;

enum Estado {
    // estados del paquete
    RECOGIDA("En recogida"),
    PROGRESO("En progreso"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    // atributo
    private String desc;

    // constructor
    private Estado(String desc) {
        this.desc = desc;
    }

    // getter
    public String getEstado() {
        return this.desc;
    }
}

public class Envio {
    // atributos
    private int id_envio;
    private String sucursal_RUC;
    private int sucursal_id_direccion;
    private int id_direccion_entrega;
    private String transportista_cedula;
    private int id_paquete;
    private LocalDate fecha_hora_inicio;
    private LocalDate fecha_hora_final;
    private double tarifa;
    private Estado estado;
    private Paquete paquete; // 1:1 relationship in DB

    // constructor
    public Envio(int id_envio, String sucursal_RUC, int sucursal_id_direccion, int id_direccion_entrega,
            String transportista_cedula, int id_paquete, LocalDate fecha_hora_inicio, LocalDate fecha_hora_final,
            double tarifa, Estado estado, Paquete paquete) {
        this.id_envio = id_envio;
        this.sucursal_RUC = sucursal_RUC;
        this.sucursal_id_direccion = sucursal_id_direccion;
        this.id_direccion_entrega = id_direccion_entrega;
        this.transportista_cedula = transportista_cedula;
        this.id_paquete = id_paquete;
        this.fecha_hora_inicio = fecha_hora_inicio;
        this.fecha_hora_final = fecha_hora_final;
        this.tarifa = tarifa;
        this.estado = estado;
        this.paquete = paquete;
    }

    // setters
    public void setIdEnvio(int id_envio) {
        this.id_envio = id_envio;
    }

    public void setSucursalRUC(String sucursal_RUC) {
        this.sucursal_RUC = sucursal_RUC;
    }

    public void setSucursalIdDireccion(int sucursal_id_direccion) {
        this.sucursal_id_direccion = sucursal_id_direccion;
    }

    public void setIdDireccionEntrega(int id_direccion_entrega) {
        this.id_direccion_entrega = id_direccion_entrega;
    }

    public void setTransportistaCedula(String transportista_cedula) {
        this.transportista_cedula = transportista_cedula;
    }

    public void setIdPaquete(int id_paquete) {
        this.id_paquete = id_paquete;
    }

    public void setFechaHoraInicio(LocalDate fecha_hora_inicio) {
        this.fecha_hora_inicio = fecha_hora_inicio;
    }

    public void setFechaHoraFinal(LocalDate fecha_hora_final) {
        this.fecha_hora_final = fecha_hora_final;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    // getters
    public int getIdEnvio() {
        return id_envio;
    }

    public String getSucursalRUC() {
        return sucursal_RUC;
    }

    public int getSucursalIdDireccion() {
        return sucursal_id_direccion;
    }

    public int getIdDireccionEntrega() {
        return id_direccion_entrega;
    }

    public String getTransportistaCedula() {
        return transportista_cedula;
    }

    public int getIdPaquete() {
        return id_paquete;
    }

    public LocalDate getFechaHoraInicio() {
        return fecha_hora_inicio;
    }

    public LocalDate getFechaHoraFinal() {
        return fecha_hora_final;
    }

    public double getTarifa() {
        return tarifa;
    }

    public Estado getEstado() {
        return estado;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    // funciones extra
    public Long getDuracionDias() {
        if (fecha_hora_inicio == null || fecha_hora_final == null)
            return 0L;
        Duration duracion = Duration.between(fecha_hora_inicio.atStartOfDay(), fecha_hora_final.atStartOfDay());
        return duracion.toDays();
    }
}
