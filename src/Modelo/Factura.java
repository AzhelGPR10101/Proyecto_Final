package Modelo;

import java.io.Serializable;
import java.util.List;

public class Factura implements Serializable {

    private String idFactura;
    private String numFactura;
    private String fecha;
    private Cliente cliente;
    private String metodoPago;
    private List<DetalleFactura> detalles;
    private double subtotal;
    private double valorIva;
    private double descuento;
    private double total;
    private String estadoSri;

    private String idNegocio;
    private String idEmpleado;

    public String getIdNegocio() {
        return idNegocio;
    }

    public void setIdNegocio(String idNegocio) {
        this.idNegocio = idNegocio;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Factura() {
    }

    public Factura(String idFactura, String numFactura, String fecha, Cliente cliente,
            String metodoPago, List<DetalleFactura> detalles, double subtotal,
            double valorIva, double descuento, double total, String estadoSri) {
        this.idFactura = idFactura;
        this.numFactura = numFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
        this.detalles = detalles;
        this.subtotal = subtotal;
        this.valorIva = valorIva;
        this.descuento = descuento;
        this.total = total;
        this.estadoSri = estadoSri;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
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

    public String getEstadoSri() {
        return estadoSri;
    }

    public void setEstadoSri(String estadoSri) {
        this.estadoSri = estadoSri;
    }

    @Override
    public String toString() {
        return numFactura + " - " + (cliente != null ? (cliente.getNombre() + " " + cliente.getApellido()) : "") + " - $" + total;
    }
    private String nombreEmpleado;

public String getNombreEmpleado() {
    return nombreEmpleado;
}

public void setNombreEmpleado(String nombreEmpleado) {
    this.nombreEmpleado = nombreEmpleado;
}
}
