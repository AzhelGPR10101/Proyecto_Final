package DAO;

import Conexion.Conexion;
import Modelo.Nota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotaDAO {

    public String registrar(Nota nota) {
        String sql = "INSERT INTO nota (id_usuario, titulo, cuerpo) VALUES (?,?,?) RETURNING id_nota";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nota.getIdUsuario());
            ps.setString(2, nota.getTitulo());
            ps.setString(3, nota.getCuerpo());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Nota> listarPorUsuario(String idUsuario) {
        List<Nota> lista = new ArrayList<>();
        String sql = "SELECT id_nota, id_usuario, titulo, cuerpo, fecha_creacion, fecha_modificacion "
                + "FROM nota WHERE id_usuario = ? ORDER BY fecha_modificacion DESC";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
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

    public boolean actualizar(Nota nota) {
        String sql = "UPDATE nota SET titulo = ?, cuerpo = ?, fecha_modificacion = CURRENT_TIMESTAMP WHERE id_nota = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nota.getTitulo());
            ps.setString(2, nota.getCuerpo());
            ps.setString(3, nota.getIdNota());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(String idNota) {
        String sql = "DELETE FROM nota WHERE id_nota = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idNota);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Nota mapear(ResultSet rs) throws SQLException {
        Nota n = new Nota();
        n.setIdNota(rs.getString("id_nota"));
        n.setIdUsuario(rs.getString("id_usuario"));
        n.setTitulo(rs.getString("titulo"));
        n.setCuerpo(rs.getString("cuerpo"));
        java.sql.Timestamp creacion = rs.getTimestamp("fecha_creacion");
        n.setFechaCreacion(creacion == null ? "" : creacion.toString());
        java.sql.Timestamp modificacion = rs.getTimestamp("fecha_modificacion");
        n.setFechaModificacion(modificacion == null ? "" : modificacion.toString());
        return n;
    }
}
