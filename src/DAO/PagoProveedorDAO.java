package DAO;

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagoProveedorDAO {

    private final PagareDAO pagareDAO = new PagareDAO();

    private String obtenerOCrearMetodoPago(Connection con, String nombre) throws SQLException {
        String buscar = "SELECT id_metodo_pago FROM metodo_pago WHERE nombre_metodo_pago = ?";
        try (PreparedStatement ps = con.prepareStatement(buscar)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        String insertar = "INSERT INTO metodo_pago (nombre_metodo_pago) VALUES (?) RETURNING id_metodo_pago";
        try (PreparedStatement ps = con.prepareStatement(insertar)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    public boolean registrarPago(String idPagare, String metodoPagoNombre, double monto) {
        String sqlPago = "INSERT INTO pago_proveedor (id_pagare, id_metodo_pago, monto) VALUES (?,?,?) RETURNING id_pago";
        String sqlEgreso = "INSERT INTO egreso (id_negocio, id_pago, fecha, monto, concepto) "
                + "VALUES (?,?,CURRENT_DATE,?,?)";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);
            try {
                String[] contexto = pagareDAO.obtenerContextoParaPago(con, idPagare);
                if (contexto == null) {
                    con.rollback();
                    return false;
                }
                String idNegocio = contexto[0];
                String numFacturaProveedor = contexto[1];
                String nombreProveedor = contexto[2];
                double saldoPendiente = Double.parseDouble(contexto[3]);

                if (monto <= 0 || monto > saldoPendiente + 0.005) {
                    con.rollback();
                    return false;
                }

                String idMetodoPago = obtenerOCrearMetodoPago(con, metodoPagoNombre);
                if (idMetodoPago == null) {
                    con.rollback();
                    return false;
                }

                String idPago;
                try (PreparedStatement ps = con.prepareStatement(sqlPago)) {
                    ps.setString(1, idPagare);
                    ps.setString(2, idMetodoPago);
                    ps.setDouble(3, monto);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        idPago = rs.getString(1);
                    }
                }

                if (!pagareDAO.actualizarSaldo(con, idPagare, saldoPendiente - monto)) {
                    con.rollback();
                    return false;
                }

                String concepto = "Abono a proveedor " + nombreProveedor + " - Factura Prov. "
                        + numFacturaProveedor + " (" + metodoPagoNombre + ")";
                try (PreparedStatement ps = con.prepareStatement(sqlEgreso)) {
                    ps.setString(1, idNegocio);
                    ps.setString(2, idPago);
                    ps.setDouble(3, monto);
                    ps.setString(4, concepto);
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
}
