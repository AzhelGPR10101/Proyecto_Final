package Controladores;

import DAO.EgresoDAO;
import DAO.PagareDAO;
import DAO.PagoProveedorDAO;
import Modelo.Egreso;
import Modelo.Pagare;
import Modelo.Sesion;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorEgreso {

    private final PagareDAO pagareDAO = new PagareDAO();
    private final PagoProveedorDAO pagoProveedorDAO = new PagoProveedorDAO();
    private final EgresoDAO egresoDAO = new EgresoDAO();

    public List<Pagare> listarPagaresPendientes() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return pagareDAO.listarPendientesPorNegocio(idNegocio);
    }

    public boolean registrarPago(java.awt.Component parent, Pagare pagare, String metodoPago, double monto) {
        if (pagare == null) {
            JOptionPane.showMessageDialog(parent, "Debe seleccionar un pagaré.");
            return false;
        }
        if (monto <= 0) {
            JOptionPane.showMessageDialog(parent, "El monto a pagar debe ser mayor a cero.");
            return false;
        }
        if (monto > pagare.getSaldoPendiente() + 0.005) {
            JOptionPane.showMessageDialog(parent, "El monto ingresado supera el saldo pendiente ($"
                    + String.format("%.2f", pagare.getSaldoPendiente()) + ").");
            return false;
        }

        boolean exito = pagoProveedorDAO.registrarPago(pagare.getIdPagare(), metodoPago, monto);
        if (exito) {
            JOptionPane.showMessageDialog(parent, "Pago registrado y egreso generado exitosamente!");
        } else {
            JOptionPane.showMessageDialog(parent, "Error al registrar el pago. Intenta de nuevo.");
        }
        return exito;
    }

    public List<Egreso> listarEgresos() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return egresoDAO.listarPorNegocio(idNegocio);
    }

    public List<Egreso> filtrarEgresos(String texto) {
        List<Egreso> todos = listarEgresos();
        String textoLower = texto == null ? "" : texto.trim().toLowerCase();
        if (textoLower.isEmpty()) {
            return todos;
        }
        List<Egreso> filtrados = new ArrayList<>();
        for (Egreso e : todos) {
            boolean coincide = (e.getConcepto() != null && e.getConcepto().toLowerCase().contains(textoLower))
                    || (e.getProveedor() != null && e.getProveedor().toLowerCase().contains(textoLower))
                    || (e.getNumFacturaProveedor() != null && e.getNumFacturaProveedor().toLowerCase().contains(textoLower));
            if (coincide) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }

    public double totalEgresos() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return 0.0;
        }
        return egresoDAO.totalPorNegocio(idNegocio);
    }
}
