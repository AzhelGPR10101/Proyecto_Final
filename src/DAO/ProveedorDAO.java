package DAO;

import Conexion.Conexion;
import Modelo.Proveedores;
import Modelo.Sesion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    private String obtenerOCrearDireccion(Connection con, String direccionTexto) throws SQLException {
        if (direccionTexto == null || direccionTexto.trim().isEmpty()) {
            return null;
        }
        String sql = "INSERT INTO direccion (calle_principal) VALUES (?) RETURNING id_direccion";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, direccionTexto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    public boolean existeRuc(String ruc) {
        String sql = "SELECT 1 FROM proveedor WHERE ruc = ?";
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

    public boolean guardar(Proveedores nuevoProveedor) {
        if (existeRuc(nuevoProveedor.getRuc())) {
            return false;
        }
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            System.out.println("No hay negocio activo en la sesion.");
            return false;
        }
        String sql = "INSERT INTO proveedor (id_negocio, id_direccion, ruc, nombre_proveedor, apellido_proveedor, correo, telefono) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = Conexion.getConnection()) {
            String idDireccion = obtenerOCrearDireccion(con, nuevoProveedor.getDireccion());
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, idNegocio);
                ps.setString(2, idDireccion);
                ps.setString(3, nuevoProveedor.getRuc());
                ps.setString(4, nuevoProveedor.getNombreEmpresa());
                ps.setString(5, nuevoProveedor.getNombreContacto());
                ps.setString(6, nuevoProveedor.getCorreo());
                ps.setString(7, nuevoProveedor.getTelefono());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Proveedores buscarPorRuc(String ruc) {
        String sql = "SELECT p.ruc, p.nombre_proveedor, p.apellido_proveedor, p.telefono, p.correo, d.calle_principal "
                + "FROM proveedor p LEFT JOIN direccion d ON p.id_direccion = d.id_direccion WHERE p.ruc = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruc);
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

        public boolean modificar(Proveedores proveedorModificado) {
        String sql = "UPDATE proveedor SET nombre_proveedor=?, apellido_proveedor=?, telefono=?, correo=? WHERE ruc=? AND id_negocio=?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, proveedorModificado.getNombreEmpresa());
            ps.setString(2, proveedorModificado.getNombreContacto());
            ps.setString(3, proveedorModificado.getTelefono());
            ps.setString(4, proveedorModificado.getCorreo());
            ps.setString(5, proveedorModificado.getRuc());
            ps.setString(6, proveedorModificado.getIdNegocio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Proveedores> listarTodos() {
        List<Proveedores> lista = new ArrayList<>();
        String idNegocio = Sesion.getIdNegocio();
        String sql = "SELECT p.ruc, p.nombre_proveedor, p.apellido_proveedor, p.telefono, p.correo, d.calle_principal "
                + "FROM proveedor p LEFT JOIN direccion d ON p.id_direccion = d.id_direccion "
                + (idNegocio != null ? "WHERE p.id_negocio = ?" : "");
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idNegocio != null) {
                ps.setString(1, idNegocio);
            }
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

           public boolean eliminar(String idNegocio, String ruc) {
        String sql = "DELETE FROM proveedor WHERE ruc = ? AND id_negocio = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruc);
            ps.setString(2, idNegocio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Proveedores mapear(ResultSet rs) throws SQLException {
        return new Proveedores(
                rs.getString("ruc"),
                rs.getString("nombre_proveedor"),
                rs.getString("apellido_proveedor"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("calle_principal")
        );
    }
    public String obtenerIdPorRuc(String ruc) {
    String sql = "SELECT id_proveedor FROM proveedor WHERE ruc = ?";
    try (Connection con = Conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, ruc);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getString(1) : null;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}
}
