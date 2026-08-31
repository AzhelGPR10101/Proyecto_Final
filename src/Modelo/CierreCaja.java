package Modelo;

import java.io.Serializable;

public class CierreCaja implements Serializable {

    private String idCierre;
    private String idEmpleado;
    private String nombreEmpleado;
    private String fechaInicio;
    private String fechaFin;
    private double montoInicial;
    private String notasApertura;
    private double totalEfectivo;
    private double totalTarjeta;
    private double totalTransferencia;
    private double montoEsperado;
    private double montoReal;
    private double diferencia;

    public CierreCaja() {
    }

    public String getIdCierre() {
        return idCierre;
    }

    public void setIdCierre(String idCierre) {
        this.idCierre = idCierre;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public String getNotasApertura() {
        return notasApertura;
    }

    public void setNotasApertura(String notasApertura) {
        this.notasApertura = notasApertura;
    }

    public double getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(double totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public double getTotalTarjeta() {
        return totalTarjeta;
    }

    public void setTotalTarjeta(double totalTarjeta) {
        this.totalTarjeta = totalTarjeta;
    }

    public double getTotalTransferencia() {
        return totalTransferencia;
    }

    public void setTotalTransferencia(double totalTransferencia) {
        this.totalTransferencia = totalTransferencia;
    }

    public double getMontoEsperado() {
        return montoEsperado;
    }

    public void setMontoEsperado(double montoEsperado) {
        this.montoEsperado = montoEsperado;
    }

    public double getMontoReal() {
        return montoReal;
    }

    public void setMontoReal(double montoReal) {
        this.montoReal = montoReal;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public boolean estaAbierta() {
        return fechaInicio != null && fechaFin == null;
    }

    public EstadoCaja getEstado() {
        return estaAbierta() ? EstadoCaja.ABIERTA : EstadoCaja.CERRADA;
    }

    @Override
    public String toString() {
        return (idCierre != null ? idCierre : "") + " - " + fechaInicio + " - Esperado: $" + montoEsperado;
    }
}
