package DAO;

import Conexion.Conexion;
import Modelo.CategoriaProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaProductoDAO {

    public boolean existeNombre(String idNegocio, String nombreCategoria) {
        String sql = "SELECT 1 FROM categoria_producto WHERE id_negocio = ? AND nombre_categoria = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setString(2, nombreCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String registrar(CategoriaProducto categoria) {
        String sql = "INSERT INTO categoria_producto (id_negocio, nombre_categoria) VALUES (?,?) RETURNING id_categoria";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria.getIdNegocio());
            ps.setString(2, categoria.getNombreCategoria());
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

    public List<CategoriaProducto> listarPorNegocio(String idNegocio) {
        List<CategoriaProducto> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, id_negocio, nombre_categoria FROM categoria_producto WHERE id_negocio = ? ORDER BY nombre_categoria";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CategoriaProducto c = new CategoriaProducto();
                    c.setIdCategoria(rs.getString("id_categoria"));
                    c.setIdNegocio(rs.getString("id_negocio"));
                    c.setNombreCategoria(rs.getString("nombre_categoria"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public String obtenerIdPorNombre(String idNegocio, String nombreCategoria) {
        String sql = "SELECT id_categoria FROM categoria_producto WHERE id_negocio = ? AND nombre_categoria = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setString(2, nombreCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean eliminar(String idCategoria) {
        String sql = "DELETE FROM categoria_producto WHERE id_categoria = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(CategoriaProducto categoria) {
        String sql = "UPDATE categoria_producto SET nombre_categoria = ? WHERE id_categoria = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombreCategoria());
            ps.setString(2, categoria.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

  public boolean estaEnUso(String idCategoria) {
    String sql = "SELECT 1 FROM producto WHERE id_categoria = ? AND estado = 'activo' LIMIT 1";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
}
