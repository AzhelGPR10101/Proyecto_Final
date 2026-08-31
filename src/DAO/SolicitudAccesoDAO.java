package DAO;

import Conexion.Conexion;
import Modelo.SolicitudAcceso;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SolicitudAccesoDAO {

    public String crear(SolicitudAcceso solicitud) {
        String sql = "INSERT INTO solicitud_acceso (id_usuario, id_negocio, id_rol, estado) "
                + "VALUES (?,?,?,'pendiente') RETURNING id_solicitud";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, solicitud.getIdUsuario());
            ps.setString(2, solicitud.getIdNegocio());
            ps.setString(3, solicitud.getIdRol());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SolicitudAcceso> listarPendientesPorNegocio(String idNegocio) {
        List<SolicitudAcceso> lista = new ArrayList<>();
        String sql = "SELECT s.id_solicitud, s.id_usuario, s.id_negocio, s.id_rol, r.nombre_rol, "
                + "s.fecha_solicitud, s.estado, u.cedula, u.nombres, u.apellidos "
                + "FROM solicitud_acceso s "
                + "JOIN usuario u ON u.id_usuario = s.id_usuario "
                + "JOIN rol r ON r.id_rol = s.id_rol "
                + "WHERE s.id_negocio = ? AND s.estado = 'pendiente' "
                + "ORDER BY s.fecha_solicitud";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean tieneSolicitudPendiente(String idUsuario) {
        String sql = "SELECT 1 FROM solicitud_acceso WHERE id_usuario = ? AND estado = 'pendiente'";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean aprobar(String idSolicitud, double salario) {
        String sqlSolicitud = "SELECT id_usuario, id_negocio, id_rol FROM solicitud_acceso "
                + "WHERE id_solicitud = ? AND estado = 'pendiente'";
        String sqlEmpleado = "INSERT INTO empleado (id_empleado, id_negocio, id_rol, salario, fecha_ingreso) "
                + "VALUES (?,?,?,?,?)";
        String sqlActualizar = "UPDATE solicitud_acceso SET estado = 'aprobada' WHERE id_solicitud = ?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                String idUsuario, idNegocio, idRol;
                try (PreparedStatement ps = con.prepareStatement(sqlSolicitud)) {
                    ps.setString(1, idSolicitud);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        idUsuario = rs.getString(1);
                        idNegocio = rs.getString(2);
                        idRol = rs.getString(3);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlEmpleado)) {
                    ps.setString(1, idUsuario);
                    ps.setString(2, idNegocio);
                    ps.setString(3, idRol);
                    ps.setDouble(4, salario);
                    ps.setDate(5, Date.valueOf(java.time.LocalDate.now()));
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(sqlActualizar)) {
                    ps.setString(1, idSolicitud);
                    ps.executeUpdate();
                }

                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SolicitudAcceso mapear(ResultSet rs) throws SQLException {
        SolicitudAcceso s = new SolicitudAcceso();
        s.setIdSolicitud(rs.getString("id_solicitud"));
        s.setIdUsuario(rs.getString("id_usuario"));
        s.setIdNegocio(rs.getString("id_negocio"));
        s.setIdRol(rs.getString("id_rol"));
        s.setNombreRol(rs.getString("nombre_rol"));
        java.sql.Date fecha = rs.getDate("fecha_solicitud");
        s.setFechaSolicitud(fecha == null ? "" : fecha.toString());
        s.setEstado(rs.getString("estado"));
        s.setCedula(rs.getString("cedula"));
        s.setNombres(rs.getString("nombres"));
        s.setApellidos(rs.getString("apellidos"));
        return s;
    }
}
