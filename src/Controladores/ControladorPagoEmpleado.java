package Controladores;

import DAO.EmpleadoDAO;
import DAO.PagoEmpleadoDAO;
import Modelo.Empleado;
import Modelo.PagoEmpleado;
import Modelo.Sesion;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorPagoEmpleado {

    private final PagoEmpleadoDAO pagoEmpleadoDAO = new PagoEmpleadoDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public boolean generarPago(java.awt.Component parent, String idEmpleado, String periodo,
            String bonificacionStr, String descuentoStr, String observaciones) {

        if (Validaciones.camposVacios(idEmpleado, periodo)) {
            JOptionPane.showMessageDialog(parent, "Completa empleado y periodo.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String bonifTexto = (bonificacionStr == null || bonificacionStr.trim().isEmpty())
                ? "0" : bonificacionStr.trim();
        String descTexto = (descuentoStr == null || descuentoStr.trim().isEmpty())
                ? "0" : descuentoStr.trim();

        if (!Validaciones.validarSueldo(bonifTexto)) {
            JOptionPane.showMessageDialog(parent, "La bonificacion ingresada no es valida.",
                    "Dato invalido", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Validaciones.validarSueldo(descTexto)) {
            JOptionPane.showMessageDialog(parent, "El descuento ingresado no es valido.",
                    "Dato invalido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        double bonificacion = Double.parseDouble(bonifTexto);
        double descuento = Double.parseDouble(descTexto);

        Empleado empleadoOficial = empleadoDAO.buscarPorId(idEmpleado);
        if (empleadoOficial == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo validar el sueldo oficial del empleado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        double sueldoOficial = empleadoOficial.getSueldo();

        double total = sueldoOficial + bonificacion - descuento;
        if (total <= 0) {
            JOptionPane.showMessageDialog(parent,
                    "El total a pagar debe ser mayor a 0 (revisa el descuento ingresado).",
                    "Total invalido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo determinar el negocio activo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        PagoEmpleado pago = new PagoEmpleado();
        pago.setIdEmpleado(idEmpleado);
        pago.setPeriodo(periodo.trim());
        pago.setMonto(total);
        pago.setObservaciones(observaciones == null ? "" : observaciones.trim());

        String nombreEmpleado = (empleadoOficial.getNombres() != null ? empleadoOficial.getNombres() : "")
                + " " + (empleadoOficial.getApellidos() != null ? empleadoOficial.getApellidos() : "");
        String conceptoEgreso = "Pago sueldo - " + nombreEmpleado.trim() + " - " + periodo.trim();

        String idPago = pagoEmpleadoDAO.registrarConEgreso(pago, idNegocio, conceptoEgreso);
        if (idPago == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo registrar el pago.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(parent,
                String.format("Pago registrado correctamente.%nSueldo base: $%.2f%nBonificaciones: $%.2f%nDescuentos: $%.2f%nTotal pagado: $%.2f",
                        sueldoOficial, bonificacion, descuento, total),
                "Pago registrado", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public List<PagoEmpleado> listarHistorial() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new java.util.ArrayList<>();
        }
        return pagoEmpleadoDAO.listarPorNegocio(idNegocio);
    }
}
