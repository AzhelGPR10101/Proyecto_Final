package Controladores;

import DAO.NotaDAO;
import Modelo.Nota;
import Modelo.Sesion;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class ControladorNota {

    private final NotaDAO notaDAO = new NotaDAO();

    public List<Nota> listar() {
        String idUsuario = Sesion.getIdUsuario();
        if (idUsuario == null) {
            return new ArrayList<>();
        }
        return notaDAO.listarPorUsuario(idUsuario);
    }

    public String registrar(java.awt.Component parent, String titulo, String cuerpo) {
        String idUsuario = Sesion.getIdUsuario();
        if (idUsuario == null) {
            JOptionPane.showMessageDialog(parent, "No hay una sesión activa.", "Sesión requerida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "El título de la nota es obligatorio.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String idNota = notaDAO.registrar(nuevaNota(idUsuario, titulo.trim(), cuerpo));
        if (idNota == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo guardar la nota.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return idNota;
    }

    public boolean actualizar(java.awt.Component parent, String idNota, String titulo, String cuerpo) {
        String idUsuario = Sesion.getIdUsuario();
        if (idUsuario == null) {
            JOptionPane.showMessageDialog(parent, "No hay una sesión activa.", "Sesión requerida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "El título de la nota es obligatorio.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        Nota nota = new Nota();
        nota.setIdNota(idNota);
        nota.setTitulo(titulo.trim());
        nota.setCuerpo(cuerpo);
        boolean exito = notaDAO.actualizar(nota, idUsuario);
        if (!exito) {
            JOptionPane.showMessageDialog(parent, "No se pudo actualizar la nota.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }

    private Nota nuevaNota(String idUsuario, String titulo, String cuerpo) {
        Nota nota = new Nota();
        nota.setIdUsuario(idUsuario);
        nota.setTitulo(titulo);
        nota.setCuerpo(cuerpo);
        return nota;
    }

    public boolean eliminar(java.awt.Component parent, String idNota) {
        String idUsuario = Sesion.getIdUsuario();
        if (idUsuario == null) {
            JOptionPane.showMessageDialog(parent, "No hay una sesión activa.", "Sesión requerida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        boolean exito = notaDAO.eliminar(idNota, idUsuario);
        if (!exito) {
            JOptionPane.showMessageDialog(parent, "No se pudo eliminar la nota.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }
}
