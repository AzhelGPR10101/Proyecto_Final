package Controladores;

import Conexion.Conexion;
import DAO.NotificacionDAO;
import Modelo.Notificacion;
import Modelo.Sesion;
import Vista.IA.AsistenteIA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ControladorNotificacion {

    private final NotificacionDAO notificacionDAO = new NotificacionDAO();

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
        String sql = "SELECT id_producto, nombre_producto, stock_actual, stock_minimo FROM producto "
                + "WHERE id_negocio = ? AND estado = 'activo' AND stock_actual <= stock_minimo";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String idProducto = rs.getString("id_producto");
                    String nombre = rs.getString("nombre_producto");
                    int actual = rs.getInt("stock_actual");
                    int minimo = rs.getInt("stock_minimo");
                    String tipo = "STOCK_BAJO_" + idProducto;
                    String hecho = "El producto '" + nombre + "' tiene stock " + actual
                            + " y su minimo configurado es " + minimo
                            + ". Recomienda brevemente al dueno reabastecer este producto.";
                    String respaldo = "⚠️ Stock bajo: " + nombre + " (quedan " + actual + ", minimo " + minimo + ").";
                    generarNotificacion(idUsuario, tipo, hecho, respaldo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void revisarPagaresPorVencer(String idNegocio, String idUsuario) {
        String sql = "SELECT pg.id_pagare, pg.saldo_pendiente, pg.fecha_vencimiento, "
                + "p.nombre_proveedor, p.apellido_proveedor "
                + "FROM pagare pg "
                + "JOIN compra c ON c.id_compra = pg.id_compra "
                + "JOIN proveedor p ON p.id_proveedor = c.id_proveedor "
                + "WHERE c.id_negocio = ? AND pg.estado = 'pendiente' "
                + "AND pg.fecha_vencimiento IS NOT NULL "
                + "AND pg.fecha_vencimiento <= CURRENT_DATE + INTERVAL '3 days'";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String idPagare = rs.getString("id_pagare");
                    String apellido = rs.getString("apellido_proveedor");
                    String proveedor = rs.getString("nombre_proveedor")
                            + (apellido == null || apellido.isEmpty() ? "" : " " + apellido);
                    double saldo = rs.getDouble("saldo_pendiente");
                    String fechaVence = rs.getDate("fecha_vencimiento").toString();
                    String tipo = "PAGARE_VENCE_" + idPagare;
                    String hecho = "Hay un pagare con el proveedor " + proveedor + " por $" + saldo
                            + " que vence el " + fechaVence + ".";
                    String respaldo = "💰 Pagare por vencer: " + proveedor + " - $" + String.format("%.2f", saldo)
                            + " (vence " + fechaVence + ").";
                    generarNotificacion(idUsuario, tipo, hecho, respaldo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void revisarFacturasSriPendientes(String idNegocio, String idUsuario) {
        String sql = "SELECT id_factura, num_factura, fecha, total FROM factura "
                + "WHERE id_negocio = ? AND estado_sri = 'Pendiente' "
                + "AND fecha <= CURRENT_DATE - INTERVAL '1 day'";
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String idFactura = rs.getString("id_factura");
                    String numFactura = rs.getString("num_factura");
                    double total = rs.getDouble("total");
                    String fecha = rs.getDate("fecha").toString();
                    String tipo = "FACTURA_SRI_" + idFactura;
                    String hecho = "La factura " + numFactura + " del " + fecha + " por $" + total
                            + " sigue con estado SRI pendiente.";
                    String respaldo = "🧾 Factura pendiente ante el SRI: " + numFactura + " ($" + String.format("%.2f", total) + ").";
                    generarNotificacion(idUsuario, tipo, hecho, respaldo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
