package Controladores;

import DAO.ProductoDAO;
import Modelo.Producto;
import Modelo.Sesion;
import javax.swing.JOptionPane;
import java.util.List;

public class ControladorProducto {

    private static final ProductoDAO productoDAO = new ProductoDAO();

    private static String idNegocioSesion(java.awt.Component parent) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(parent, "No hay una sesion activa. Inicia sesion antes de gestionar productos.", "Sesion requerida", JOptionPane.WARNING_MESSAGE);
        }
        return idNegocio;
    }

        public static boolean registrarProducto(java.awt.Component parent,
            String codigo, String nombre, String catalogo,
            String cantidadStr, String precioStr, boolean conIva, boolean sinIva, String stockMinimoStr,
            String pasillo, String fechaVencimiento) {

        if (Validaciones.camposVacios(codigo, nombre, catalogo, cantidadStr, precioStr)) {
            JOptionPane.showMessageDialog(parent, "Por favor, complete todos los campos obligatorios (*).", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!conIva && !sinIva) {
            JOptionPane.showMessageDialog(parent, "Debe seleccionar una opcion de IVA (CON IVA / SIN IVA).", "IVA Requerido", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Validaciones.validarCodigoProducto(codigo)) {
            JOptionPane.showMessageDialog(parent, "El codigo debe ser numerico y tener entre 8 y 13 digitos.", "Codigo Invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validaciones.validarNombreProducto(nombre)) {
            JOptionPane.showMessageDialog(parent, "El nombre solo puede contener letras, numeros y un maximo de 15 caracteres.", "Nombre Invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validaciones.validarCantidadStock(cantidadStr)) {
            JOptionPane.showMessageDialog(parent, "La cantidad debe ser un numero entero no negativo de maximo 8 digitos.", "Cantidad Invalida", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int cantidad = Integer.parseInt(cantidadStr.trim());

        if (!Validaciones.validarPrecioUnitario(precioStr)) {
            JOptionPane.showMessageDialog(parent, "El precio unitario debe ser un numero no negativo de maximo 8 digitos.", "Precio Invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        double precio = Double.parseDouble(precioStr.trim());
        if (precio <= 0) {
            JOptionPane.showMessageDialog(parent, "El precio unitario debe ser mayor a 0.", "Precio Invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String idNegocio = idNegocioSesion(parent);
        if (idNegocio == null) {
            return false;
        }

        codigo = codigo.trim();

        if (productoDAO.existeCodigoBarras(codigo)) {
            JOptionPane.showMessageDialog(parent, "El codigo del producto ya esta registrado.", "Registro Duplicado", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String idCategoria = new DAO.CategoriaProductoDAO().obtenerIdPorNombre(idNegocio, catalogo.trim());
        if (idCategoria == null) {
            JOptionPane.showMessageDialog(parent, "Esa categoria ya no existe. Selecciona una categoria valida (o creala primero en Categorias).", "Categoria invalida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String idTasaIva = productoDAO.obtenerIdTasaIva(conIva);
        if (idTasaIva == null) {
            JOptionPane.showMessageDialog(parent, "No hay una tasa de IVA configurada en la base de datos. Ejecuta sql/03_seed_tasa_iva.sql.", "Configuracion faltante", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Producto nuevoProducto = new Producto(
                codigo, nombre.trim(), catalogo.trim(), cantidad, precio, conIva,
                java.time.LocalDate.now().toString(),
                fechaVencimiento == null ? "" : fechaVencimiento.trim()
        );

        int stockMinimo = 0;
        try {
            if (stockMinimoStr != null && !stockMinimoStr.trim().isEmpty()) {
                stockMinimo = Math.max(0, Integer.parseInt(stockMinimoStr.trim()));
            }
        } catch (NumberFormatException ignorado) {
            stockMinimo = 0;
        }

        nuevoProducto.setIdNegocio(idNegocio);
        nuevoProducto.setIdCategoria(idCategoria);
        nuevoProducto.setIdTasaIva(idTasaIva);
        nuevoProducto.setStockMinimo(stockMinimo);
        nuevoProducto.setUbicacionPasillo(pasillo == null ? "" : pasillo.trim());

        boolean exito = productoDAO.registrar(nuevoProducto);
        if (exito) {
            JOptionPane.showMessageDialog(parent, "Producto guardado exitosamente!", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Error al guardar el producto en la base de datos.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }

    public static List<Producto> listarProductos() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new java.util.ArrayList<>();
        }
        return productoDAO.listarPorNegocio(idNegocio);
    }

    public static String[] obtenerNombresCategorias() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new String[0];
        }
        java.util.List<Modelo.CategoriaProducto> categorias = new DAO.CategoriaProductoDAO().listarPorNegocio(idNegocio);
        String[] nombres = new String[categorias.size()];
        for (int i = 0; i < categorias.size(); i++) {
            nombres[i] = categorias.get(i).getNombreCategoria();
        }
        return nombres;
    }

    public static List<Producto> filtrarProductos(String texto, String orden) {
        List<Producto> lista = listarProductos();
        List<Producto> filtrada = new java.util.ArrayList<>();

        for (Producto prod : lista) {
            boolean coincideTexto = texto == null || texto.trim().isEmpty()
                    || prod.getNombre().toLowerCase().contains(texto.toLowerCase())
                    || prod.getCodigo().toLowerCase().contains(texto.toLowerCase())
                    || prod.getCategoria().toLowerCase().contains(texto.toLowerCase());

            if (coincideTexto) {
                filtrada.add(prod);
            }
        }

        if ("Nombre Z-A.".equals(orden) || "Nombre Z-A".equals(orden)) {
            filtrada.sort((a, b) -> b.getNombre().compareToIgnoreCase(a.getNombre()));
        } else {
            filtrada.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        }

        return filtrada;
    }

    public static boolean actualizarProducto(java.awt.Component parent,
            String codigo, String nombre, String catalogo,
            String cantidadStr, String precioStr, boolean conIva, String stockMinimoStr) {

        if (Validaciones.camposVacios(nombre, catalogo, cantidadStr, precioStr)) {
            JOptionPane.showMessageDialog(parent, "Por favor, complete todos los campos obligatorios.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Validaciones.validarNombreProducto(nombre)) {
            JOptionPane.showMessageDialog(parent, "El nombre solo puede contener letras y un maximo de 15 caracteres.", "Nombre Invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validaciones.validarCantidadStock(cantidadStr)) {
            JOptionPane.showMessageDialog(parent, "La cantidad debe ser un numero entero no negativo de maximo 8 digitos.", "Cantidad Invalida", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validaciones.validarPrecioUnitario(precioStr)) {
            JOptionPane.showMessageDialog(parent, "El precio unitario debe ser un numero no negativo de maximo 8 digitos.", "Precio Invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int cantidad;
        double precio;
        try {
            cantidad = Integer.parseInt(cantidadStr.trim());
            precio = Double.parseDouble(precioStr.trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Cantidad o precio en formato incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String idNegocio = idNegocioSesion(parent);
        if (idNegocio == null) {
            return false;
        }

        String idCategoria = new DAO.CategoriaProductoDAO().obtenerIdPorNombre(idNegocio, catalogo.trim());
        if (idCategoria == null) {
            JOptionPane.showMessageDialog(parent, "Esa categoria ya no existe. Selecciona una categoria valida (o creala primero en Categorias).", "Categoria invalida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String idTasaIva = productoDAO.obtenerIdTasaIva(conIva);
        if (idTasaIva == null) {
            JOptionPane.showMessageDialog(parent, "No hay una tasa de IVA configurada en la base de datos. Ejecuta sql/03_seed_tasa_iva.sql.", "Configuracion faltante", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int stockMinimo = 0;
        try {
            if (stockMinimoStr != null && !stockMinimoStr.trim().isEmpty()) {
                stockMinimo = Math.max(0, Integer.parseInt(stockMinimoStr.trim()));
            }
        } catch (NumberFormatException ignorado) {
            stockMinimo = 0;
        }

        Producto productoActualizado = new Producto();
        productoActualizado.setIdNegocio(idNegocio);
        productoActualizado.setIdCategoria(idCategoria);
        productoActualizado.setIdTasaIva(idTasaIva);
        productoActualizado.setCodigo(codigo);
        productoActualizado.setNombre(nombre.trim());
        productoActualizado.setCantidad(cantidad);
        productoActualizado.setPrecioUnitario(precio);
        productoActualizado.setStockMinimo(stockMinimo);
        boolean exito = productoDAO.actualizar(productoActualizado);
        if (exito) {
            JOptionPane.showMessageDialog(parent, "Producto actualizado exitosamente!", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "No se encontro el producto a modificar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }

    public static boolean eliminarProducto(java.awt.Component parent, String codigo) {
        String idNegocio = idNegocioSesion(parent);
        if (idNegocio == null) {
            return false;
        }

        boolean exito = productoDAO.eliminar(idNegocio, codigo);
        if (!exito) {
            JOptionPane.showMessageDialog(parent,
                    "No se pudo eliminar el producto. Puede que ya tenga compras, ventas o movimientos de inventario registrados.",
                    "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        }
        return exito;
    }
}
