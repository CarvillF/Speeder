package back.clases;

import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;

enum Estado {
    //estados del paquete
    RECOGIDA("En recogida"),
    PROGRESO("En progreso"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    //atributo
    private String desc;

    //constructor
    private Estado(String desc){
        this.desc = desc;
    }

    //getter
    public String getEstado(){
        return this.desc;
    }
}

public class Envio {
    //atributos
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String dir_origen;
    private String dir_destino;
    private String ciudad_origen;
    private String ciudad_destino;
    private Estado estado;
    private ArrayList<Paquete> paquetes;

    //constructor
    private Envio(LocalDate fecha_inicio, LocalDate fecha_fin, String dir_origen, 
                String dir_destino, String ciudad_origen, String ciudad_destino, 
                Estado estado, ArrayList<Paquete> paquetes){
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.dir_origen = dir_origen;
        this.dir_destino = dir_destino;
        this.ciudad_origen = ciudad_origen;
        this.ciudad_destino = ciudad_destino;
        this.estado = estado;
        this.paquetes = paquetes;
    }

    //setters
    public void setFechaInicio(LocalDate new_fecha_inicio) {
        this.fecha_inicio = new_fecha_inicio;
    }
    public void setFechaFin(LocalDate new_fecha_fin) {
        this.fecha_fin = new_fecha_fin;
    }

    public void setDirOrigen(String new_dir_origen) {
        this.dir_origen = new_dir_origen;
    }
    public void setDirDestino(String new_dir_destino) {
        this.dir_destino = new_dir_destino;
    }
    
    public void setCiudadOrigen(String new_ciudad_origen) {
        this.ciudad_origen = new_ciudad_origen;
    }
    public void setCiudadDestino(String new_ciudad_destino) {
        this.ciudad_destino = new_ciudad_destino;
    }

    public void setEstado(Estado new_estado) {
        this.estado = new_estado;
    }

    //getters
    public LocalDate getFechaInicio() {
        return fecha_inicio;
    }
    public LocalDate getFechaFin() {
        return fecha_fin;
    }
    
    public String getDirOrigen() {
        return dir_origen;
    }
    public String getDirDestino() {
        return dir_destino;
    }

    public String getCiudadOrigen() {
        return ciudad_origen;
    }
    public String getCiudadDestino() {
        return ciudad_destino;
    }

    public Estado getEstado() {
        return estado;
    }

    public ArrayList<Paquete> getPaquetes() {
        return paquetes;
    }

    //funciones extra
    public Long getDuracionDias(){
        Duration duracion = Duration.between(fecha_inicio, fecha_fin);
        long dias = duracion.toDays();
        return dias;
    }

    public long[] getDuracionDiasHoras(int inicio, int fin){
        Duration duracion = Duration.between(fecha_inicio.atTime(inicio, 0), fecha_fin.atTime(fin, 0));
        long dias = duracion.toDays();
        long horas = duracion.minusDays(dias).toHours();
        
        long[] dur = new long[2];
        dur[0] = dias;
        dur[1] = horas;

        return dur;
    }

    public void eliminarVehiculo(int id_paquete){
        if (!paquetes.isEmpty()){
            paquetes.remove(id_paquete);
        }
    }

    public void agregarVehiculo(Paquete pack){
        paquetes.add(pack);
    }
}
