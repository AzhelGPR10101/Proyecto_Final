
package DAO;

import Conexion.Conexion;
import Modelo.CierreCaja;
import Modelo.MetodoPago;
import Modelo.Sesion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CierreCajaDAO {

    public CierreCaja obtenerCierreDeHoy() {
        String idEmpleado = Sesion.getIdUsuario();
        String sql = "SELECT id_cierre, id_empleado, fecha_inicio, fecha_fin, "
                + "monto_inicial, notas_apertura, "
                + "total_efectivo, total_tarjeta, total_transferencia, "
                + "monto_esperado, monto_real, diferencia "
                + "FROM cierre_caja "
                + "WHERE id_empleado = ? AND fecha_inicio::date = CURRENT_DATE "
                + "ORDER BY fecha_inicio DESC LIMIT 1";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String abrirTurno(double montoInicial, String notasApertura) {
        String idNegocio = Sesion.getIdNegocio();
        String idEmpleado = Sesion.getIdUsuario();
        String sql = "INSERT INTO cierre_caja (id_negocio, id_empleado, fecha_inicio, monto_inicial, notas_apertura) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?) RETURNING id_cierre";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setString(2, idEmpleado);
            ps.setDouble(3, montoInicial);
            ps.setString(4, notasApertura);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<MetodoPago, Double> calcularTotalesDeHoyPorMetodoPago() {
        String idEmpleado = Sesion.getIdUsuario();
        Map<MetodoPago, Double> totales = new HashMap<>();
        String sql = "SELECT mp.nombre_metodo_pago, SUM(f.total) AS total "
                + "FROM factura f "
                + "JOIN metodo_pago mp ON mp.id_metodo_pago = f.id_metodo_pago "
                + "WHERE f.id_empleado = ? AND f.fecha = CURRENT_DATE "
                + "GROUP BY mp.nombre_metodo_pago";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MetodoPago metodo = MetodoPago.desdeNombreEnBD(rs.getString("nombre_metodo_pago"));
                    if (metodo != null) {
                        totales.put(metodo, rs.getDouble("total"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totales;
    }

    public boolean cerrarTurno(CierreCaja cierre) {
        String sql = "UPDATE cierre_caja SET fecha_fin = CURRENT_TIMESTAMP, "
                + "total_efectivo = ?, total_tarjeta = ?, total_transferencia = ?, "
                + "monto_esperado = ?, monto_real = ?, diferencia = ? "
                + "WHERE id_cierre = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, cierre.getTotalEfectivo());
            ps.setDouble(2, cierre.getTotalTarjeta());
            ps.setDouble(3, cierre.getTotalTransferencia());
            ps.setDouble(4, cierre.getMontoEsperado());
            ps.setDouble(5, cierre.getMontoReal());
            ps.setDouble(6, cierre.getDiferencia());
            ps.setString(7, cierre.getIdCierre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<CierreCaja> listarHistorialNegocio(String idNegocio) {
        java.util.List<CierreCaja> lista = new java.util.ArrayList<>();
        if (idNegocio == null) {
            return lista;
        }
        String sql = "SELECT cc.id_cierre, cc.id_empleado, cc.fecha_inicio, cc.fecha_fin, "
                + "cc.monto_inicial, cc.notas_apertura, "
                + "cc.total_efectivo, cc.total_tarjeta, cc.total_transferencia, "
                + "cc.monto_esperado, cc.monto_real, cc.diferencia, "
                + "u.nombres, u.apellidos "
                + "FROM cierre_caja cc "
                + "JOIN usuario u ON u.id_usuario = cc.id_empleado "
                + "WHERE cc.id_negocio = ? "
                + "ORDER BY cc.fecha_inicio DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CierreCaja c = mapear(rs);
                    String nombres = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");
                    c.setNombreEmpleado(((nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "")).trim());
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    public boolean reabrirCierre(String idCierre) {
        String sql = "UPDATE cierre_caja SET fecha_fin = NULL WHERE id_cierre = ?";
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idCierre);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private CierreCaja mapear(ResultSet rs) throws SQLException {
        CierreCaja c = new CierreCaja();
        c.setIdCierre(rs.getString("id_cierre"));
        c.setIdEmpleado(rs.getString("id_empleado"));
        c.setFechaInicio(rs.getString("fecha_inicio"));
        c.setFechaFin(rs.getString("fecha_fin"));
        c.setMontoInicial(rs.getDouble("monto_inicial"));
        c.setNotasApertura(rs.getString("notas_apertura"));
        c.setTotalEfectivo(rs.getDouble("total_efectivo"));
        c.setTotalTarjeta(rs.getDouble("total_tarjeta"));
        c.setTotalTransferencia(rs.getDouble("total_transferencia"));
        c.setMontoEsperado(rs.getDouble("monto_esperado"));
        c.setMontoReal(rs.getDouble("monto_real"));
        c.setDiferencia(rs.getDouble("diferencia"));
        return c;
    }
}
