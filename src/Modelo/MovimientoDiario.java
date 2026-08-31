package Modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class MovimientoDiario implements Serializable {

    private LocalDate fecha;
    private double ganancias;
    private double gastos;
    private int productosVendidos;

    public MovimientoDiario() {
    }

    public MovimientoDiario(LocalDate fecha, double ganancias, double gastos, int productosVendidos) {
        this.fecha = fecha;
        this.ganancias = ganancias;
        this.gastos = gastos;
        this.productosVendidos = productosVendidos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getGanancias() {
        return ganancias;
    }

    public void setGanancias(double ganancias) {
        this.ganancias = ganancias;
    }

    public double getGastos() {
        return gastos;
    }

    public void setGastos(double gastos) {
        this.gastos = gastos;
    }

    public int getProductosVendidos() {
        return productosVendidos;
    }

    public void setProductosVendidos(int productosVendidos) {
        this.productosVendidos = productosVendidos;
    }
}
