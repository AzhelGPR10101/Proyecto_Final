package Modelo;

import java.io.Serializable;
import java.util.List;

public class Compra implements Serializable {

    private String idCompra;
    private String numFacturaProveedor;
    private String fecha;
    private Proveedores proveedor;
    private List<DetalleCompra> detalles;
    private double subtotal;
    private double valorIva;
    private double descuento;
    private double total;
    private String formaPago;
    private String estadoPagare;

    public Compra() {
    }

    public Compra(String idCompra, String numFacturaProveedor, String fecha, Proveedores proveedor,
            List<DetalleCompra> detalles, double subtotal, double valorIva, double descuento,
            double total, String formaPago, String estadoPagare) {
        this.idCompra = idCompra;
        this.numFacturaProveedor = numFacturaProveedor;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.detalles = detalles;
        this.subtotal = subtotal;
        this.valorIva = valorIva;
        this.descuento = descuento;
        this.total = total;
        this.formaPago = formaPago;
        this.estadoPagare = estadoPagare;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getNumFacturaProveedor() {
        return numFacturaProveedor;
    }

    public void setNumFacturaProveedor(String numFacturaProveedor) {
        this.numFacturaProveedor = numFacturaProveedor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getValorIva() {
        return valorIva;
    }

    public void setValorIva(double valorIva) {
        this.valorIva = valorIva;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getEstadoPagare() {
        return estadoPagare;
    }

    public void setEstadoPagare(String estadoPagare) {
        this.estadoPagare = estadoPagare;
    }

    @Override
    public String toString() {
        return numFacturaProveedor + " - " + (proveedor != null ? proveedor.getNombreEmpresa() : "") + " - $" + total;
    }
}
