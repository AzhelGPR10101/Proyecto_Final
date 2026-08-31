package DAO;

import Conexion.Conexion;
import Modelo.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolDAO {

    public String obtenerOCrearIdRol(Connection con, Rol rol) throws SQLException {
        String buscar = "SELECT id_rol FROM rol WHERE id_negocio = ? AND nombre_rol = ?";
        try (PreparedStatement ps = con.prepareStatement(buscar)) {
            ps.setString(1, rol.getIdNegocio());
            ps.setString(2, rol.getNombreRol());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        String insertar = "INSERT INTO rol (id_negocio, nombre_rol) VALUES (?,?) RETURNING id_rol";
        try (PreparedStatement ps = con.prepareStatement(insertar)) {
            ps.setString(1, rol.getIdNegocio());
            ps.setString(2, rol.getNombreRol());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    public String buscarNombrePorId(String idRol) {
        String sql = "SELECT nombre_rol FROM rol WHERE id_rol = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
