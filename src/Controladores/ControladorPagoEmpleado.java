package Controladores;

import DAO.PagoEmpleadoDAO;
import Modelo.PagoEmpleado;
import Modelo.Sesion;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorPagoEmpleado {

    private final PagoEmpleadoDAO pagoEmpleadoDAO = new PagoEmpleadoDAO();

    public boolean generarPago(java.awt.Component parent, String idEmpleado, String periodo,
            String montoStr, String observaciones) {
        if (Validaciones.camposVacios(idEmpleado, periodo, montoStr)) {
            JOptionPane.showMessageDialog(parent, "Completa empleado, periodo y monto.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        double monto;
        try {
            monto = Double.parseDouble(montoStr.trim());
            if (monto <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Ingresa un monto valido, mayor a 0.",
                    "Dato invalido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        PagoEmpleado pago = new PagoEmpleado();
        pago.setIdEmpleado(idEmpleado);
        pago.setPeriodo(periodo.trim());
        pago.setMonto(monto);
        pago.setObservaciones(observaciones == null ? "" : observaciones.trim());

        String idPago = pagoEmpleadoDAO.registrar(pago);
        if (idPago == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo registrar el pago.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(parent,
                String.format("Pago de $%.2f registrado correctamente.", monto),
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
