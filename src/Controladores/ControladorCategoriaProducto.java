package Controladores;

import DAO.CategoriaProductoDAO;
import Modelo.CategoriaProducto;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorCategoriaProducto {

    private final CategoriaProductoDAO categoriaDAO = new CategoriaProductoDAO();

    public String registrarCategoria(java.awt.Component parent, String idNegocio, String nombreCategoria) {
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(parent, "No hay un negocio activo en la sesión.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Escribe el nombre del catálogo antes de guardar.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        nombreCategoria = nombreCategoria.trim();
        if (categoriaDAO.existeNombre(idNegocio, nombreCategoria)) {
            JOptionPane.showMessageDialog(parent, "Ya existe un catálogo con ese nombre.", "Catálogo Duplicado", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String idCategoria = categoriaDAO.registrar(new CategoriaProducto(idNegocio, nombreCategoria));
        if (idCategoria == null) {
            JOptionPane.showMessageDialog(parent, "Ocurrió un error al guardar el catálogo.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return idCategoria;
    }

    public boolean eliminarCategoria(java.awt.Component parent, String idCategoria) {
        if (idCategoria == null) {
            return false;
        }
        if (categoriaDAO.estaEnUso(idCategoria)) {
            JOptionPane.showMessageDialog(parent,
                    "No se puede eliminar: hay productos registrados con esta categoría.\nReasigna o elimina esos productos primero.",
                    "Categoría en uso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        boolean eliminado = categoriaDAO.eliminar(idCategoria);
        if (!eliminado) {
            JOptionPane.showMessageDialog(parent, "No se pudo eliminar el catálogo (puede que ya tenga productos asignados).", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return eliminado;
    }

    public List<CategoriaProducto> listarCategorias(String idNegocio) {
        if (idNegocio == null) {
            return new java.util.ArrayList<>();
        }
        return categoriaDAO.listarPorNegocio(idNegocio);
    }
}
