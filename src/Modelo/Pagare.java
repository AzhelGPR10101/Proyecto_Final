package Modelo;

import java.io.Serializable;

public class Pagare implements Serializable {

    private String idPagare;
    private String numFacturaProveedor;
    private String nombreProveedor;
    private double montoTotal;
    private double saldoPendiente;
    private String fechaEmision;
    private String fechaVencimiento;
    private String estado;

    public Pagare() {
    }

    public Pagare(String idPagare, String numFacturaProveedor, String nombreProveedor,
            double montoTotal, double saldoPendiente, String fechaEmision,
            String fechaVencimiento, String estado) {
        this.idPagare = idPagare;
        this.numFacturaProveedor = numFacturaProveedor;
        this.nombreProveedor = nombreProveedor;
        this.montoTotal = montoTotal;
        this.saldoPendiente = saldoPendiente;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
    }

    public String getIdPagare() {
        return idPagare;
    }

    public void setIdPagare(String idPagare) {
        this.idPagare = idPagare;
    }

    public String getNumFacturaProveedor() {
        return numFacturaProveedor;
    }

    public void setNumFacturaProveedor(String numFacturaProveedor) {
        this.numFacturaProveedor = numFacturaProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return numFacturaProveedor + " - " + nombreProveedor + " - Saldo: $" + String.format("%.2f", saldoPendiente);
    }
}
