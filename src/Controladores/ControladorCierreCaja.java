
package Controladores;

import DAO.CierreCajaDAO;
import Modelo.CierreCaja;
import Modelo.MetodoPago;
import java.util.Map;

public class ControladorCierreCaja {

    private final CierreCajaDAO cierreCajaDAO = new CierreCajaDAO();

    public enum EstadoTurno {
        SIN_ABRIR,
        ABIERTO,
        YA_CERRADO
    }

    public EstadoTurno obtenerEstadoTurno() {
        CierreCaja cierre = cierreCajaDAO.obtenerCierreDeHoy();
        if (cierre == null) {
            return EstadoTurno.SIN_ABRIR;
        }
        return cierre.estaAbierta() ? EstadoTurno.ABIERTO : EstadoTurno.YA_CERRADO;
    }

    public CierreCaja obtenerTurnoDeHoy() {
        return cierreCajaDAO.obtenerCierreDeHoy();
    }

    public CierreCaja abrirTurno(double montoInicial, String notasApertura) {
        CierreCaja existente = cierreCajaDAO.obtenerCierreDeHoy();
        if (existente != null) {
            return null;
        }
        String idCierre = cierreCajaDAO.abrirTurno(montoInicial, notasApertura);
        if (idCierre == null) {
            return null;
        }
        return cierreCajaDAO.obtenerCierreDeHoy();
    }

    public CierreCaja calcularEsperado(CierreCaja turno) {
        Map<MetodoPago, Double> totales = cierreCajaDAO.calcularTotalesDeHoyPorMetodoPago();

        double efectivo = totales.getOrDefault(MetodoPago.EFECTIVO, 0.0);
        double tarjeta = totales.getOrDefault(MetodoPago.TARJETA, 0.0);
        double transferencia = totales.getOrDefault(MetodoPago.TRANSFERENCIA, 0.0);

        turno.setTotalEfectivo(efectivo);
        turno.setTotalTarjeta(tarjeta);
        turno.setTotalTransferencia(transferencia);
        turno.setMontoEsperado(turno.getMontoInicial() + efectivo);
        return turno;
    }

    public boolean cerrarTurno(CierreCaja turno, double montoContado) {
        double diferencia = montoContado - turno.getMontoEsperado();
        turno.setMontoReal(montoContado);
        turno.setDiferencia(diferencia);
        return cierreCajaDAO.cerrarTurno(turno);
    }
    public boolean reabrirCierre(String idCierre) {
        return cierreCajaDAO.reabrirCierre(idCierre);
    }

    public java.util.List<CierreCaja> listarHistorialDelNegocio() {
        String idNegocio = Modelo.Sesion.getIdNegocio();
        return cierreCajaDAO.listarHistorialNegocio(idNegocio);
    }
}
