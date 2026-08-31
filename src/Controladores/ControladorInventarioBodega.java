package Controladores;

import DAO.ProductoDAO;
import Modelo.Producto;
import Modelo.Sesion;
import javax.swing.JOptionPane;
import java.util.List;

public class ControladorInventarioBodega {

    private static final ProductoDAO productoDAO = new ProductoDAO();

    private static String idNegocioSesion(java.awt.Component parent) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(parent, "No hay una sesion activa. Inicia sesion antes de gestionar la bodega.", "Sesion requerida", JOptionPane.WARNING_MESSAGE);
        }
        return idNegocio;
    }

    public static List<Producto> listarInventario() {
        return ControladorProducto.listarProductos();
    }

    public static List<Producto> listarBajoStock(int limite) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new java.util.ArrayList<>();
        }
        return productoDAO.listarBajoStock(idNegocio, limite);
    }

    public static List<Object[]> listarMovimientosRecientes(int limite) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new java.util.ArrayList<>();
        }
        return productoDAO.listarMovimientosRecientes(idNegocio, limite);
    }

    public static List<Object[]> listarMasSolicitados(int limite) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new java.util.ArrayList<>();
        }
        return productoDAO.listarMasSolicitados(idNegocio, limite);
    }

    public static Producto buscarPorCodigo(java.awt.Component parent, String codigo) {
        String idNegocio = idNegocioSesion(parent);
        if (idNegocio == null || codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        Producto p = productoDAO.obtenerPorCodigo(idNegocio, codigo.trim());
        if (p == null) {
            JOptionPane.showMessageDialog(parent, "No se encontro ningun producto con ese codigo.", "No encontrado", JOptionPane.WARNING_MESSAGE);
        }
        return p;
    }

    public static String registrarEntrada(java.awt.Component parent, String codigo, String cantidadStr) {
        if (Validaciones.camposVacios(codigo, cantidadStr)) {
            JOptionPane.showMessageDialog(parent, "Ingresa el codigo del producto y la cantidad.", "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Validaciones.validarCantidadStock(cantidadStr)) {
            JOptionPane.showMessageDialog(parent, "La cantidad debe ser un numero entero positivo.", "Cantidad invalida", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        int cantidad = Integer.parseInt(cantidadStr.trim());
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(parent, "La cantidad debe ser mayor a 0.", "Cantidad invalida", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String idNegocio = idNegocioSesion(parent);
        if (idNegocio == null) {
            return null;
        }
        Producto producto = productoDAO.obtenerPorCodigo(idNegocio, codigo.trim());
        if (producto == null) {
            JOptionPane.showMessageDialog(parent, "No se encontro ningun producto con ese codigo.", "No encontrado", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        boolean exito = productoDAO.sumarStock(idNegocio, codigo.trim(), cantidad);
        if (!exito) {
            JOptionPane.showMessageDialog(parent, "No se pudo registrar la entrada.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        JOptionPane.showMessageDialog(parent, "Entrada registrada: +" + cantidad + " uds. de " + producto.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
        return "+ " + producto.getNombre() + "  +" + cantidad;
    }

    public static String confirmarSalida(java.awt.Component parent, String codigo, String cantidadStr) {
        if (Validaciones.camposVacios(codigo, cantidadStr)) {
            JOptionPane.showMessageDialog(parent, "Ingresa el codigo del producto y la cantidad.", "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (!Validaciones.validarCantidadStock(cantidadStr)) {
            JOptionPane.showMessageDialog(parent, "La cantidad debe ser un numero entero positivo.", "Cantidad invalida", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        int cantidad = Integer.parseInt(cantidadStr.trim());
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(parent, "La cantidad debe ser mayor a 0.", "Cantidad invalida", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String idNegocio = idNegocioSesion(parent);
        if (idNegocio == null) {
            return null;
        }
        Producto producto = productoDAO.obtenerPorCodigo(idNegocio, codigo.trim());
        if (producto == null) {
            JOptionPane.showMessageDialog(parent, "No se encontro ningun producto con ese codigo.", "No encontrado", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        boolean exito = productoDAO.descontarStockPorCodigo(idNegocio, codigo.trim(), cantidad);
        if (!exito) {
            JOptionPane.showMessageDialog(parent, "No hay stock suficiente de " + producto.getNombre() + " para esa salida.", "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        JOptionPane.showMessageDialog(parent, "Salida confirmada: -" + cantidad + " uds. de " + producto.getNombre(), "Exito", JOptionPane.INFORMATION_MESSAGE);
        return "- " + producto.getNombre() + "  -" + cantidad;
    }

    public static boolean ajustarInventario(java.awt.Component parent, String codigo, String ubicacion,
            String lote, String stockMinStr, String stockMaxStr, String stockActualStr) {

        if (Validaciones.camposVacios(codigo, stockMinStr, stockMaxStr, stockActualStr)) {
            JOptionPane.showMessageDialog(parent, "El codigo, stock minimo, stock maximo y stock actual son obligatorios.", "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Validaciones.validarCantidadStock(stockMinStr) || !Validaciones.validarCantidadStock(stockMaxStr)
                || !Validaciones.validarCantidadStock(stockActualStr)) {
            JOptionPane.showMessageDialog(parent, "Stock minimo, maximo y actual deben ser numeros enteros no negativos.", "Datos invalidos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int stockMin = Integer.parseInt(stockMinStr.trim());
        int stockMax = Integer.parseInt(stockMaxStr.trim());
        int stockActual = Integer.parseInt(stockActualStr.trim());
        if (stockMax > 0 && stockMin > stockMax) {
            JOptionPane.showMessageDialog(parent, "El stock minimo no puede ser mayor al stock maximo.", "Datos invalidos", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String idNegocio = idNegocioSesion(parent);
        if (idNegocio == null) {
            return false;
        }

        boolean exito = productoDAO.actualizarInventarioBodega(idNegocio, codigo.trim(),
                ubicacion == null ? null : ubicacion.trim(),
                lote == null ? null : lote.trim(),
                stockMin, stockMax, stockActual);

        if (exito) {
            JOptionPane.showMessageDialog(parent, "Inventario actualizado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "No se encontro el producto con codigo: " + codigo, "No encontrado", JOptionPane.WARNING_MESSAGE);
        }
        return exito;
    }
}
