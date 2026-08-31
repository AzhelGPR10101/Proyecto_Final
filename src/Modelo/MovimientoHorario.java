package Modelo;

import java.io.Serializable;

public class MovimientoHorario implements Serializable {

    private int hora;
    private double ganancias;
    private double gastos;
    private int productosVendidos;

    public MovimientoHorario() {
    }

    public MovimientoHorario(int hora, double ganancias, double gastos, int productosVendidos) {
        this.hora = hora;
        this.ganancias = ganancias;
        this.gastos = gastos;
        this.productosVendidos = productosVendidos;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
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
