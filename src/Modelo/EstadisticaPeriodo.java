package Modelo;

import java.io.Serializable;

public class EstadisticaPeriodo implements Serializable {

    private String periodo;
    private double ganancias;
    private double gastos;
    private int productosVendidos;

    public EstadisticaPeriodo() {
    }

    public EstadisticaPeriodo(String periodo, double ganancias, double gastos, int productosVendidos) {
        this.periodo = periodo;
        this.ganancias = ganancias;
        this.gastos = gastos;
        this.productosVendidos = productosVendidos;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public double getBalance() {
        return ganancias - gastos;
    }
}
