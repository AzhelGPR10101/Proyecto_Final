package DAO;

import Conexion.Conexion;
import Modelo.Negocio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NegocioDAO {

    private String crearDireccion(Connection con, String callePrincipal, String calleSecundaria, String ciudad) throws SQLException {
        String sql = "INSERT INTO direccion (calle_principal, calle_secundaria, ciudad) VALUES (?,?,?) RETURNING id_direccion";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, callePrincipal);
            ps.setString(2, calleSecundaria);
            ps.setString(3, ciudad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    public boolean existeIdNegocio(String idNegocio) {
        if (idNegocio == null || idNegocio.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM negocio WHERE id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeRuc(String ruc) {
        String sql = "SELECT 1 FROM negocio WHERE ruc_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruc);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String registrar(Negocio negocio) {
        String sql = "INSERT INTO negocio (id_usuario, id_direccion, nombre_negocio, ruc_negocio, correo_contacto) VALUES (?,?,?,?,?) RETURNING id_negocio";
        try (Connection con = Conexion.getConnection()) {
            String idDireccion = crearDireccion(con, negocio.getCallePrincipal(), negocio.getCalleSecundaria(), negocio.getCiudad());
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, negocio.getIdUsuario());
                ps.setString(2, idDireccion);
                ps.setString(3, negocio.getNombreNegocio());
                ps.setString(4, negocio.getRucNegocio());
                ps.setString(5, negocio.getCorreoContacto());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Negocio buscarPorIdUsuario(String idUsuario) {
        String sql = "SELECT n.id_negocio, n.id_usuario, n.nombre_negocio, n.ruc_negocio, n.correo_contacto "
                + "FROM negocio n "
                + "WHERE n.id_usuario = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Negocio n = new Negocio();
                    n.setIdNegocio(rs.getString("id_negocio"));
                    n.setIdUsuario(rs.getString("id_usuario"));
                    n.setNombreNegocio(rs.getString("nombre_negocio"));
                    n.setRucNegocio(rs.getString("ruc_negocio"));
                    n.setCorreoContacto(rs.getString("correo_contacto"));
                    return n;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizar(Negocio negocio) {
        String sql = "UPDATE negocio SET nombre_negocio = ?, correo_contacto = ? WHERE id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, negocio.getNombreNegocio());
            ps.setString(2, negocio.getCorreoContacto());
            ps.setString(3, negocio.getIdNegocio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Negocio buscarPorIdNegocio(String idNegocio) {
        String sql = "SELECT n.id_negocio, n.id_usuario, n.nombre_negocio, n.ruc_negocio, n.correo_contacto "
                + "FROM negocio n "
                + "WHERE n.id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Negocio n = new Negocio();
                    n.setIdNegocio(rs.getString("id_negocio"));
                    n.setIdUsuario(rs.getString("id_usuario"));
                    n.setNombreNegocio(rs.getString("nombre_negocio"));
                    n.setRucNegocio(rs.getString("ruc_negocio"));
                    n.setCorreoContacto(rs.getString("correo_contacto"));
                    return n;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
