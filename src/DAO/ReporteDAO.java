package DAO;

import Conexion.Conexion;
import Modelo.MovimientoFinanciero;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ReporteDAO {

    public List<MovimientoFinanciero> listarIngresos(String idNegocio, Date desde, Date hasta) {
        List<MovimientoFinanciero> lista = new ArrayList<>();
        String sql = "SELECT i.fecha, i.monto, f.id_factura, f.num_factura, "
                + "c.nombre_cliente, mp.nombre_metodo_pago, u.nombres, u.apellidos "
                + "FROM ingreso i "
                + "JOIN factura f ON f.id_factura = i.id_factura "
                + "JOIN cliente c ON c.id_cliente = f.id_cliente "
                + "JOIN metodo_pago mp ON mp.id_metodo_pago = f.id_metodo_pago "
                + "JOIN empleado e ON e.id_empleado = f.id_empleado "
                + "JOIN usuario u ON u.id_usuario = e.id_empleado "
                + "WHERE i.id_negocio = ? AND i.fecha BETWEEN ? AND ? "
                + "ORDER BY i.fecha DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setDate(2, desde);
            ps.setDate(3, hasta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String empleado = (rs.getString("nombres") + " " + rs.getString("apellidos")).trim();
                    lista.add(new MovimientoFinanciero(
                            MovimientoFinanciero.TIPO_INGRESO,
                            rs.getDate("fecha").toString(),
                            rs.getString("num_factura"),
                            rs.getString("nombre_cliente"),
                            empleado,
                            rs.getString("nombre_metodo_pago"),
                            rs.getDouble("monto"),
                            rs.getString("id_factura")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<MovimientoFinanciero> listarEgresos(String idNegocio, Date desde, Date hasta) {
        List<MovimientoFinanciero> lista = new ArrayList<>();
        String sql = "SELECT e.fecha, e.monto, e.concepto, mp.nombre_metodo_pago "
                + "FROM egreso e "
                + "LEFT JOIN pago_proveedor pp ON pp.id_pago = e.id_pago "
                + "LEFT JOIN metodo_pago mp ON mp.id_metodo_pago = pp.id_metodo_pago "
                + "WHERE e.id_negocio = ? AND e.fecha BETWEEN ? AND ? "
                + "ORDER BY e.fecha DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setDate(2, desde);
            ps.setDate(3, hasta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new MovimientoFinanciero(
                            MovimientoFinanciero.TIPO_EGRESO,
                            rs.getDate("fecha").toString(),
                            "-",
                            rs.getString("concepto"),
                            null,
                            rs.getString("nombre_metodo_pago"),
                            rs.getDouble("monto"),
                            null
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<String> listarClientesParaFiltro(String idNegocio) {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT DISTINCT nombre_cliente FROM cliente WHERE id_negocio = ? ORDER BY nombre_cliente";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nombres.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombres;
    }

    public List<String> listarEmpleadosParaFiltro(String idNegocio) {
        LinkedHashSet<String> nombres = new LinkedHashSet<>();
        String sql = "SELECT u.nombres, u.apellidos FROM empleado e "
                + "JOIN usuario u ON u.id_usuario = e.id_empleado "
                + "WHERE e.id_negocio = ? ORDER BY u.nombres";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nombres.add((rs.getString("nombres") + " " + rs.getString("apellidos")).trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(nombres);
    }
}