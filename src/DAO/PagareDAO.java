package DAO;

import Conexion.Conexion;
import Modelo.Pagare;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagareDAO {

    public List<Pagare> listarPendientesPorNegocio(String idNegocio) {
        List<Pagare> lista = new ArrayList<>();
        String sql = "SELECT pg.id_pagare, pg.monto_total, pg.saldo_pendiente, pg.fecha_emision, "
                + "pg.fecha_vencimiento, pg.estado, c.num_factura_proveedor, "
                + "p.nombre_proveedor, p.apellido_proveedor "
                + "FROM pagare pg "
                + "JOIN compra c ON c.id_compra = pg.id_compra "
                + "JOIN proveedor p ON p.id_proveedor = c.id_proveedor "
                + "WHERE c.id_negocio = ? AND pg.estado = 'pendiente' "
                + "ORDER BY pg.fecha_vencimiento ASC NULLS LAST";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String apellido = rs.getString("apellido_proveedor");
                    String nombreProv = rs.getString("nombre_proveedor") + (apellido == null || apellido.isEmpty() ? "" : " " + apellido);
                    lista.add(new Pagare(
                            rs.getString("id_pagare"),
                            rs.getString("num_factura_proveedor"),
                            nombreProv,
                            rs.getDouble("monto_total"),
                            rs.getDouble("saldo_pendiente"),
                            rs.getDate("fecha_emision") == null ? "" : rs.getDate("fecha_emision").toString(),
                            rs.getDate("fecha_vencimiento") == null ? "" : rs.getDate("fecha_vencimiento").toString(),
                            rs.getString("estado")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public String[] obtenerContextoParaPago(Connection con, String idPagare) throws SQLException {
        String sql = "SELECT c.id_negocio, c.num_factura_proveedor, p.nombre_proveedor, p.apellido_proveedor, "
                + "pg.saldo_pendiente "
                + "FROM pagare pg "
                + "JOIN compra c ON c.id_compra = pg.id_compra "
                + "JOIN proveedor p ON p.id_proveedor = c.id_proveedor "
                + "WHERE pg.id_pagare = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPagare);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String apellido = rs.getString("apellido_proveedor");
                String nombreProv = rs.getString("nombre_proveedor") + (apellido == null || apellido.isEmpty() ? "" : " " + apellido);
                return new String[]{
                    rs.getString("id_negocio"),
                    rs.getString("num_factura_proveedor"),
                    nombreProv,
                    String.valueOf(rs.getDouble("saldo_pendiente"))
                };
            }
        }
    }

    public boolean actualizarSaldo(Connection con, String idPagare, double nuevoSaldo) throws SQLException {
        String estado = nuevoSaldo <= 0.005 ? "pagado" : "pendiente";
        double saldoFinal = nuevoSaldo <= 0.005 ? 0.0 : nuevoSaldo;
        String sql = "UPDATE pagare SET saldo_pendiente = ?, estado = ? WHERE id_pagare = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, saldoFinal);
            ps.setString(2, estado);
            ps.setString(3, idPagare);
            return ps.executeUpdate() > 0;
        }
    }
}
