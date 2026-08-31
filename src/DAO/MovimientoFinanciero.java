package Modelo;

import java.io.Serializable;

public class MovimientoFinanciero implements Serializable {

    public static final String TIPO_INGRESO = "Ingreso";
    public static final String TIPO_EGRESO = "Egreso";

    private String tipo;
    private String fecha;
    private String referencia;
    private String detalle;
    private String empleado;
    private String metodoPago;
    private double monto;
    private String idFactura;

    public MovimientoFinanciero(String tipo, String fecha, String referencia, String detalle,
            String empleado, String metodoPago, double monto, String idFactura) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.referencia = referencia;
        this.detalle = detalle;
        this.empleado = empleado;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.idFactura = idFactura;
    }

    public String getTipo() { return tipo; }
    public String getFecha() { return fecha; }
    public String getReferencia() { return referencia; }
    public String getDetalle() { return detalle; }
    public String getEmpleado() { return empleado == null ? "" : empleado; }
    public String getMetodoPago() { return metodoPago == null ? "-" : metodoPago; }
    public double getMonto() { return monto; }
    public String getIdFactura() { return idFactura; }
    public boolean esIngreso() { return TIPO_INGRESO.equals(tipo); }
}