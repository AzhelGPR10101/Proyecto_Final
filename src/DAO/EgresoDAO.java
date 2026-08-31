package DAO;

import Conexion.Conexion;
import Modelo.Egreso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EgresoDAO {

    public List<Egreso> listarPorNegocio(String idNegocio) {
        List<Egreso> lista = new ArrayList<>();
        // Se unen egreso -> pago_proveedor -> pagare -> compra -> proveedor para poder
        // mostrar el numero de factura del proveedor y el nombre del proveedor en
        // columnas separadas, en vez de tenerlo todo mezclado en "concepto".
        String sql = "SELECT e.id_egreso, e.fecha, e.monto, e.concepto, mp.nombre_metodo_pago, "
                + "c.num_factura_proveedor, "
                + "TRIM(COALESCE(prov.nombre_proveedor, '') || ' ' || COALESCE(prov.apellido_proveedor, '')) AS proveedor "
                + "FROM egreso e "
                + "LEFT JOIN pago_proveedor pp ON pp.id_pago = e.id_pago "
                + "LEFT JOIN metodo_pago mp ON mp.id_metodo_pago = pp.id_metodo_pago "
                + "LEFT JOIN pagare pg ON pg.id_pagare = pp.id_pagare "
                + "LEFT JOIN compra c ON c.id_compra = pg.id_compra "
                + "LEFT JOIN proveedor prov ON prov.id_proveedor = c.id_proveedor "
                + "WHERE e.id_negocio = ? "
                + "ORDER BY e.fecha DESC, e.id_egreso DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Egreso(
                            rs.getString("id_egreso"),
                            rs.getDate("fecha") == null ? "" : rs.getDate("fecha").toString(),
                            rs.getDouble("monto"),
                            rs.getString("concepto"),
                            rs.getString("nombre_metodo_pago"),
                            rs.getString("num_factura_proveedor"),
                            rs.getString("proveedor")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double totalPorNegocio(String idNegocio) {
        String sql = "SELECT COALESCE(SUM(monto),0) FROM egreso WHERE id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
