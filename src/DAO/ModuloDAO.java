package DAO;

import Conexion.Conexion;
import Modelo.Modulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuloDAO {

    public List<Modulo> obtenerModulos() {
        List<Modulo> lista = new ArrayList<>();
        String sql = "SELECT id_modulo, nombre_modulo, descripcion FROM modulo ORDER BY nombre_modulo";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Modulo(rs.getString("id_modulo"), rs.getString("nombre_modulo"), rs.getString("descripcion"), false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Modulo> obtenerModulosDeNegocio(String idNegocio) {
        List<Modulo> lista = new ArrayList<>();
        String sql = "SELECT m.id_modulo, m.nombre_modulo, m.descripcion, "
                + "COALESCE(nm.activo, FALSE) AS activo "
                + "FROM modulo m LEFT JOIN negocio_modulo nm ON m.id_modulo = nm.id_modulo AND nm.id_negocio = ? "
                + "ORDER BY m.nombre_modulo";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Modulo(rs.getString("id_modulo"), rs.getString("nombre_modulo"),
                            rs.getString("descripcion"), rs.getBoolean("activo")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<String> obtenerNombresModulosActivos(String idNegocio) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT m.nombre_modulo FROM negocio_modulo nm JOIN modulo m ON nm.id_modulo = m.id_modulo "
                + "WHERE nm.id_negocio = ? AND nm.activo = TRUE";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean activarModulo(String idNegocio, String idModulo) {
        return fijarEstado(idNegocio, idModulo, true);
    }

    public boolean desactivarModulo(String idNegocio, String idModulo) {
        return fijarEstado(idNegocio, idModulo, false);
    }

    private boolean fijarEstado(String idNegocio, String idModulo, boolean activo) {
        String sql = "INSERT INTO negocio_modulo (id_negocio, id_modulo, activo) VALUES (?,?,?) "
                + "ON CONFLICT (id_negocio, id_modulo) DO UPDATE SET activo = EXCLUDED.activo, fecha_activacion = CURRENT_DATE";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNegocio);
            ps.setString(2, idModulo);
            ps.setBoolean(3, activo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarModulo(Modulo modulo) {
        String sql = "UPDATE modulo SET nombre_modulo = ?, descripcion = ? WHERE id_modulo = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, modulo.getNombreModulo());
            ps.setString(2, modulo.getDescripcion());
            ps.setString(3, modulo.getIdModulo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String buscarIdPorNombre(String nombreModulo) {
        String sql = "SELECT id_modulo FROM modulo WHERE nombre_modulo = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreModulo);
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
