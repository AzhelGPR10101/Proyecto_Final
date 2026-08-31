package DAO;

import Conexion.Conexion;
import Modelo.Permiso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermisoDAO {

    public String obtenerOCrearIdPermiso(Connection con, Permiso permiso) throws SQLException {
        String buscar = "SELECT id_permiso FROM permiso WHERE nombre_permiso = ?";
        try (PreparedStatement ps = con.prepareStatement(buscar)) {
            ps.setString(1, permiso.getNombrePermiso());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        String insertar = "INSERT INTO permiso (nombre_permiso) VALUES (?) RETURNING id_permiso";
        try (PreparedStatement ps = con.prepareStatement(insertar)) {
            ps.setString(1, permiso.getNombrePermiso());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    public boolean asignarPermiso(String idRol, String idPermiso) {
        String sql = "INSERT INTO rol_permiso (id_rol, id_permiso) VALUES (?,?) "
                + "ON CONFLICT (id_rol, id_permiso) DO NOTHING";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idRol);
            ps.setString(2, idPermiso);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean quitarPermiso(String idRol, String idPermiso) {
        String sql = "DELETE FROM rol_permiso WHERE id_rol = ? AND id_permiso = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idRol);
            ps.setString(2, idPermiso);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Permiso> listarPermisosDeRol(String idRol) {
        List<Permiso> lista = new ArrayList<>();
        String sql = "SELECT p.id_permiso, p.nombre_permiso FROM permiso p "
                + "JOIN rol_permiso rp ON rp.id_permiso = p.id_permiso "
                + "WHERE rp.id_rol = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Permiso permiso = new Permiso();
                    permiso.setIdPermiso(rs.getString("id_permiso"));
                    permiso.setNombrePermiso(rs.getString("nombre_permiso"));
                    lista.add(permiso);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean tienePermiso(String idRol, String nombrePermiso) {
        if (idRol == null) {
            return false;
        }
        String sql = "SELECT 1 FROM rol_permiso rp "
                + "JOIN permiso p ON p.id_permiso = rp.id_permiso "
                + "WHERE rp.id_rol = ? AND p.nombre_permiso = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idRol);
            ps.setString(2, nombrePermiso);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
