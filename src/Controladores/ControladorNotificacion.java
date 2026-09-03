package Controladores;

import DAO.NotificacionDAO;
import Modelo.Notificacion;
import Modelo.Sesion;
import Vista.IA.AsistenteIA;
import java.util.List;

public class ControladorNotificacion {

    private final NotificacionDAO notificacionDAO = new NotificacionDAO();
    private final DAO.ProductoDAO productoDAO = new DAO.ProductoDAO();
    private final DAO.PagareDAO pagareDAO = new DAO.PagareDAO();
    private final DAO.FacturaDAO facturaDAO = new DAO.FacturaDAO();

    public void revisarYGenerarNotificaciones() {
        String idNegocio = Sesion.getIdNegocio();
        String idUsuario = Sesion.getIdUsuario();
        if (idNegocio == null || idUsuario == null) {
            return;
        }
        revisarStockBajo(idNegocio, idUsuario);
        revisarPagaresPorVencer(idNegocio, idUsuario);
    }

    private void revisarStockBajo(String idNegocio, String idUsuario) {
        for (Object[] fila : productoDAO.listarStockBajoParaNotificar(idNegocio)) {
            String idProducto = (String) fila[0];
            String nombre = (String) fila[1];
            int actual = (int) fila[2];
            int minimo = (int) fila[3];
            String tipo = "STOCK_BAJO_" + idProducto;
            String hecho = "El producto '" + nombre + "' tiene stock " + actual
                    + " y su minimo configurado es " + minimo
                    + ". Recomienda brevemente al dueno reabastecer este producto.";
            String respaldo = "⚠️ Stock bajo: " + nombre + " (quedan " + actual + ", minimo " + minimo + ").";
            generarNotificacion(idUsuario, tipo, hecho, respaldo);
        }
    }

    private void revisarPagaresPorVencer(String idNegocio, String idUsuario) {
        for (Object[] fila : pagareDAO.listarPorVencerParaNotificar(idNegocio)) {
            String idPagare = (String) fila[0];
            String proveedor = (String) fila[1];
            double saldo = (double) fila[2];
            String fechaVence = (String) fila[3];
            String tipo = "PAGARE_VENCE_" + idPagare;
            String hecho = "Hay un pagare con el proveedor " + proveedor + " por $" + saldo
                    + " que vence el " + fechaVence + ".";
            String respaldo = "💰 Pagare por vencer: " + proveedor + " - $" + String.format("%.2f", saldo)
                    + " (vence " + fechaVence + ").";
            generarNotificacion(idUsuario, tipo, hecho, respaldo);
        }
    }

    private void revisarFacturasSriPendientes(String idNegocio, String idUsuario) {
        for (Object[] fila : facturaDAO.listarSriPendientesParaNotificar(idNegocio)) {
            String idFactura = (String) fila[0];
            String numFactura = (String) fila[1];
            String fecha = (String) fila[2];
            double total = (double) fila[3];
            String tipo = "FACTURA_SRI_" + idFactura;
            String hecho = "La factura " + numFactura + " del " + fecha + " por $" + total
                    + " sigue con estado SRI pendiente.";
            String respaldo = "🧾 Factura pendiente ante el SRI: " + numFactura + " ($" + String.format("%.2f", total) + ").";
            generarNotificacion(idUsuario, tipo, hecho, respaldo);
        }
    }

    private void generarNotificacion(String idUsuario, String tipo, String hechoParaLaIA, String mensajeRespaldo) {
        if (notificacionDAO.existeNoLeidaTipo(idUsuario, tipo)) {
            return;
        }
        String mensajeFinal = mensajeRespaldo;
        try {
            String prompt = "Redacta en una sola linea, breve y clara (maximo 20 palabras), "
                    + "una notificacion para el dueno del negocio sobre este hecho: " + hechoParaLaIA
                    + " No agregues etiquetas [IR_A:...], ni saludos, solo la notificacion.";
            String respuestaIA = AsistenteIA.preguntar("", new java.util.ArrayList<>(), prompt);
            if (respuestaIA != null && !respuestaIA.isBlank()
                    && !respuestaIA.startsWith("Error") && !respuestaIA.startsWith("Falta configurar")) {
                mensajeFinal = respuestaIA.trim();
            }
        } catch (Exception e) {
        }
        Notificacion n = new Notificacion();
        n.setIdUsuario(idUsuario);
        n.setTipo(tipo);
        n.setMensaje(mensajeFinal);
        notificacionDAO.insertarSiNoExiste(n);
    }

    public List<Notificacion> listarNoLeidas(String idUsuario) {
        return notificacionDAO.listarPorUsuario(idUsuario, true);
    }

    public List<Notificacion> listarTodas(String idUsuario) {
        return notificacionDAO.listarPorUsuario(idUsuario, false);
    }

    public int contarNoLeidas(String idUsuario) {
        return notificacionDAO.contarNoLeidas(idUsuario);
    }

    public boolean marcarComoLeida(String idNotificacion) {
        return notificacionDAO.marcarComoLeida(idNotificacion);
    }

    public boolean marcarTodasComoLeidas(String idUsuario) {
        return notificacionDAO.marcarTodasComoLeidas(idUsuario);
    }

    public void notificarEvento(String idNegocio, String tipo, String hechoParaLaIA) {
        String idDueno = notificacionDAO.obtenerIdUsuarioDuenoPorNegocio(idNegocio);
        if (idDueno == null) {
            return;
        }
        generarNotificacion(idDueno, tipo, hechoParaLaIA, hechoParaLaIA);
    }
}
