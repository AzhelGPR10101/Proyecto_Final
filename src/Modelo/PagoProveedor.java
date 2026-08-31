package Modelo;

import java.io.Serializable;

public class PagoProveedor implements Serializable {

    private String idPago;
    private String idPagare;
    private String metodoPago;
    private double monto;
    private String fechaPago;

    public PagoProveedor() {
    }

    public PagoProveedor(String idPago, String idPagare, String metodoPago, double monto, String fechaPago) {
        this.idPago = idPago;
        this.idPagare = idPagare;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }

    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public String getIdPagare() {
        return idPagare;
    }

    public void setIdPagare(String idPagare) {
        this.idPagare = idPagare;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
