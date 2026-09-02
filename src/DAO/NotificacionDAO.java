package DAO;

import Conexion.Conexion;
import Modelo.Notificacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    public boolean existeNoLeidaTipo(String idUsuario, String tipo) {
        String sql = "SELECT 1 FROM notificacion WHERE id_usuario = ? AND tipo = ? AND leido = false LIMIT 1";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            ps.setString(2, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final int INTENTOS_MAXIMOS_ID_DUPLICADO = 3;

    private static boolean esClaveDuplicada(SQLException e) {
        return "23505".equals(e.getSQLState());
    }

    public boolean insertar(Notificacion n) {
        String sql = "INSERT INTO notificacion (id_usuario, tipo, mensaje) VALUES (?, ?, ?)";
        for (int intento = 1; intento <= INTENTOS_MAXIMOS_ID_DUPLICADO; intento++) {
            try (Connection con = Conexion.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, n.getIdUsuario());
                ps.setString(2, n.getTipo());
                ps.setString(3, n.getMensaje());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                if (esClaveDuplicada(e) && intento < INTENTOS_MAXIMOS_ID_DUPLICADO) {
                    continue;
                }
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public List<Notificacion> listarPorUsuario(String idUsuario, boolean soloNoLeidas) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT id_notificacion, id_usuario, tipo, mensaje, fecha_generacion, leido "
                + "FROM notificacion WHERE id_usuario = ? "
                + (soloNoLeidas ? "AND leido = false " : "")
                + "ORDER BY fecha_generacion DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp fecha = rs.getTimestamp("fecha_generacion");
                    lista.add(new Notificacion(
                            rs.getString("id_notificacion"),
                            rs.getString("id_usuario"),
                            rs.getString("tipo"),
                            rs.getString("mensaje"),
                            fecha == null ? "" : fecha.toString(),
                            rs.getBoolean("leido")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int contarNoLeidas(String idUsuario) {
        String sql = "SELECT COUNT(*) FROM notificacion WHERE id_usuario = ? AND leido = false";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean marcarComoLeida(String idNotificacion) {
        String sql = "UPDATE notificacion SET leido = true WHERE id_notificacion = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNotificacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean marcarTodasComoLeidas(String idUsuario) {
        String sql = "UPDATE notificacion SET leido = true WHERE id_usuario = ? AND leido = false";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final Object LOCK_INSERCION = new Object();

    public boolean insertarSiNoExiste(Notificacion n) {
        synchronized (LOCK_INSERCION) {
            String sql = "INSERT INTO notificacion (id_usuario, tipo, mensaje) "
                    + "SELECT ?, ?, ? "
                    + "WHERE NOT EXISTS ("
                    + "  SELECT 1 FROM notificacion "
                    + "  WHERE id_usuario = ? AND tipo = ? AND leido = false"
                    + ")";
            for (int intento = 1; intento <= INTENTOS_MAXIMOS_ID_DUPLICADO; intento++) {
                try (Connection con = Conexion.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, n.getIdUsuario());
                    ps.setString(2, n.getTipo());
                    ps.setString(3, n.getMensaje());
                    ps.setString(4, n.getIdUsuario());
                    ps.setString(5, n.getTipo());
                    return ps.executeUpdate() > 0;
                } catch (SQLException e) {
                    if (esClaveDuplicada(e) && intento < INTENTOS_MAXIMOS_ID_DUPLICADO) {
                        continue;
                    }
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
    }
    public String obtenerIdUsuarioDuenoPorNegocio(String idNegocio) {
    String sql = "SELECT id_usuario FROM negocio WHERE id_negocio = ?";
    try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, idNegocio);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("id_usuario");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}