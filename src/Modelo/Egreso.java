package Modelo;

import java.io.Serializable;

public class Egreso implements Serializable {

    private String idEgreso;
    private String fecha;
    private double monto;
    private String concepto;
    private String metodoPago;
    private String numFacturaProveedor;
    private String proveedor;

    public Egreso() {
    }

    public Egreso(String idEgreso, String fecha, double monto, String concepto, String metodoPago) {
        this.idEgreso = idEgreso;
        this.fecha = fecha;
        this.monto = monto;
        this.concepto = concepto;
        this.metodoPago = metodoPago;
    }

    public Egreso(String idEgreso, String fecha, double monto, String concepto, String metodoPago,
            String numFacturaProveedor, String proveedor) {
        this.idEgreso = idEgreso;
        this.fecha = fecha;
        this.monto = monto;
        this.concepto = concepto;
        this.metodoPago = metodoPago;
        this.numFacturaProveedor = numFacturaProveedor;
        this.proveedor = proveedor;
    }

    public String getNumFacturaProveedor() {
        return numFacturaProveedor == null || numFacturaProveedor.isEmpty() ? "-" : numFacturaProveedor;
    }

    public void setNumFacturaProveedor(String numFacturaProveedor) {
        this.numFacturaProveedor = numFacturaProveedor;
    }

    public String getProveedor() {
        return proveedor == null || proveedor.isEmpty() ? "-" : proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getIdEgreso() {
        return idEgreso;
    }

    public void setIdEgreso(String idEgreso) {
        this.idEgreso = idEgreso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getMetodoPago() {
        return metodoPago == null ? "-" : metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
