package Modelo;

import java.sql.Date;
import java.sql.Time;

public class MovimientoInventario {

    private String tipo;
    private String nombreProducto;
    private int cantidad;
    private Date fecha;
    private Time hora;

    public MovimientoInventario(String tipo, String nombreProducto, int cantidad, Date fecha, Time hora) {
        this.tipo = tipo;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public Time getHora() {
        return hora;
    }
}
