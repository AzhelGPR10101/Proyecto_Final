
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
        cerrarTurnoVencidoSilenciosamente();
        CierreCaja cierre = cierreCajaDAO.obtenerCierreDeHoy();
        if (cierre == null) {
            return EstadoTurno.SIN_ABRIR;
        }
        return cierre.estaAbierta() ? EstadoTurno.ABIERTO : EstadoTurno.YA_CERRADO;
    }

    private CierreCaja cerrarTurnoVencidoSilenciosamente() {
        CierreCaja turnoVencido = cierreCajaDAO.obtenerCierreAbiertoAnteriorAHoy();
        if (turnoVencido == null) {
            return null;
        }
        try {
            String fechaInicio = turnoVencido.getFechaInicio();
            java.sql.Date fecha = java.sql.Date.valueOf(fechaInicio.substring(0, 10));

            Map<MetodoPago, Double> totales = cierreCajaDAO.calcularTotalesPorFecha(
                    turnoVencido.getIdEmpleado(), fecha);
            double efectivo = totales.getOrDefault(MetodoPago.EFECTIVO, 0.0);
            double tarjeta = totales.getOrDefault(MetodoPago.TARJETA, 0.0);
            double transferencia = totales.getOrDefault(MetodoPago.TRANSFERENCIA, 0.0);
            double montoEsperado = turnoVencido.getMontoInicial() + efectivo;

            turnoVencido.setTotalEfectivo(efectivo);
            turnoVencido.setTotalTarjeta(tarjeta);
            turnoVencido.setTotalTransferencia(transferencia);
            turnoVencido.setMontoEsperado(montoEsperado);
            turnoVencido.setMontoReal(montoEsperado);
            turnoVencido.setDiferencia(0);

            cierreCajaDAO.cerrarTurno(turnoVencido);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return turnoVencido;
    }

    public String cerrarAutomaticamenteTurnoVencidoSiExiste() {
        CierreCaja turnoVencido = cerrarTurnoVencidoSilenciosamente();
        if (turnoVencido == null) {
            return null;
        }
        String fechaBonita = turnoVencido.getFechaInicio();
        try {
            fechaBonita = java.time.LocalDate.parse(turnoVencido.getFechaInicio().substring(0, 10))
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
        }
        return String.format(
                "Se detecto que la caja del %s quedo abierta y no se cerro manualmente.%n"
                + "Se cerro automaticamente con un monto esperado de $%.2f (sin diferencia registrada).%n"
                + "Ya puedes abrir la caja de hoy con normalidad.",
                fechaBonita, turnoVencido.getMontoEsperado());
    }

    public boolean esRolCajero(String rol) {
        return rol != null
                && ("Cajero".equalsIgnoreCase(rol) || "Vendedor".equalsIgnoreCase(rol));
    }

    public boolean facturacionHabilitadaParaSesionActual() {
        String rol = Modelo.Sesion.getRolUsuario();
        if (!esRolCajero(rol)) {
            return true;
        }
        return obtenerEstadoTurno() == EstadoTurno.ABIERTO;
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

    public double calcularDiferencia(double montoContado, CierreCaja turno) {
        return montoContado - turno.getMontoEsperado();
    }

    public boolean cerrarTurno(CierreCaja turno, double montoContado) {
        double diferencia = calcularDiferencia(montoContado, turno);
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
