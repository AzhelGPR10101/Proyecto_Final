package DAO;

import Conexion.Conexion;
import Modelo.PagoEmpleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagoEmpleadoDAO {

    public String registrarConEgreso(PagoEmpleado pago, String idNegocio, String conceptoEgreso) {
        String sqlPago = "INSERT INTO pago_empleado (id_empleado, periodo, monto, observaciones) "
                + "VALUES (?, ?, ?, ?) RETURNING id_pago";
        String sqlEgreso = "INSERT INTO egreso (id_negocio, fecha, monto, concepto) "
                + "VALUES (?, CURRENT_DATE, ?, ?)";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                String idPago;
                try (PreparedStatement ps = con.prepareStatement(sqlPago)) {
                    ps.setString(1, pago.getIdEmpleado());
                    ps.setString(2, pago.getPeriodo());
                    ps.setDouble(3, pago.getMonto());
                    ps.setString(4, pago.getObservaciones());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return null;
                        }
                        idPago = rs.getString(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlEgreso)) {
                    ps.setString(1, idNegocio);
                    ps.setDouble(2, pago.getMonto());
                    ps.setString(3, conceptoEgreso);
                    ps.executeUpdate();
                }

                con.commit();
                return idPago;
            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
                return null;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PagoEmpleado> listarPorNegocio(String idNegocio) {
        List<PagoEmpleado> lista = new ArrayList<>();
        String sql = "SELECT p.id_pago, p.id_empleado, p.fecha_pago, p.periodo, p.monto, p.observaciones, "
                + "u.nombres, u.apellidos "
                + "FROM pago_empleado p "
                + "JOIN empleado e ON e.id_empleado = p.id_empleado "
                + "JOIN usuario u ON u.id_usuario = e.id_empleado "
                + "WHERE e.id_negocio = ? "
                + "ORDER BY p.fecha_pago DESC";
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

    private PagoEmpleado mapear(ResultSet rs) throws SQLException {
        PagoEmpleado p = new PagoEmpleado();
        p.setIdPago(rs.getString("id_pago"));
        p.setIdEmpleado(rs.getString("id_empleado"));
        p.setNombreEmpleado(rs.getString("nombres") + " " + rs.getString("apellidos"));
        java.sql.Date fecha = rs.getDate("fecha_pago");
        p.setFechaPago(fecha == null ? "" : fecha.toString());
        p.setPeriodo(rs.getString("periodo"));
        p.setMonto(rs.getDouble("monto"));
        p.setObservaciones(rs.getString("observaciones"));
        return p;
    }
}
