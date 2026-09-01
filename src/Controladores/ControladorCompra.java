package Controladores;

import DAO.CompraDAO;
import DAO.ProductoDAO;
import DAO.ProveedorDAO;
import Modelo.Compra;
import Modelo.DetalleCompra;
import Modelo.Producto;
import Modelo.Proveedores;
import Modelo.Sesion;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorCompra {

    private final CompraDAO compraDAO = new CompraDAO();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    public double[] calcularTotales(List<DetalleCompra> detalles, double descuento) {
        double subtotal = 0;
        for (DetalleCompra d : detalles) {
            subtotal += d.getSubtotal();
        }
        double valorIva = subtotal * productoDAO.obtenerPorcentajeIvaVigente();
        double total = subtotal + valorIva - descuento;
        return new double[]{subtotal, valorIva, total};
    }

    public DetalleCompra crearDetalle(Producto producto, int cantidad, double costoUnitario) {
        double subtotal = cantidad * costoUnitario;
        return new DetalleCompra(producto.getCodigo(), producto.getNombre(), cantidad, costoUnitario, subtotal);
    }

    public static class ResultadoAgregarCarrito {
        public final int indiceActualizado;
        public final DetalleCompra detalle;

        private ResultadoAgregarCarrito(int indiceActualizado, DetalleCompra detalle) {
            this.indiceActualizado = indiceActualizado;
            this.detalle = detalle;
        }
    }

    public ResultadoAgregarCarrito agregarAlCarrito(List<DetalleCompra> listaActual, Producto producto,
            int cantidad, double costoUnitario) {
        int filaExistente = -1;
        for (int i = 0; i < listaActual.size(); i++) {
            if (listaActual.get(i).getIdProducto().equals(producto.getCodigo())) {
                filaExistente = i;
                break;
            }
        }

        if (filaExistente >= 0) {
            DetalleCompra existente = listaActual.get(filaExistente);
            int nuevaCantidad = existente.getCantidad() + cantidad;
            existente.setCantidad(nuevaCantidad);
            existente.setCostoUnitario(costoUnitario);
            existente.setSubtotal(nuevaCantidad * costoUnitario);
            return new ResultadoAgregarCarrito(filaExistente, existente);
        }
        return new ResultadoAgregarCarrito(-1, crearDetalle(producto, cantidad, costoUnitario));
    }

    public List<Proveedores> listarProveedores() {
        return proveedorDAO.listarTodos();
    }

    public List<Proveedores> filtrarProveedores(String texto) {
        List<Proveedores> todos = listarProveedores();
        List<Proveedores> filtrados = new ArrayList<>();
        String textoLower = texto == null ? "" : texto.trim().toLowerCase();
        for (Proveedores p : todos) {
            if (textoLower.isEmpty()
                    || p.getNombreEmpresa().toLowerCase().contains(textoLower)
                    || (p.getRuc() != null && p.getRuc().toLowerCase().contains(textoLower))) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public List<Producto> listarProductos() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return productoDAO.listarPorNegocio(idNegocio);
    }

    public boolean registrarCompra(java.awt.Component parent, String numFacturaProveedor, Proveedores proveedor,
            List<DetalleCompra> detalles, double descuento, boolean pagoContado, String metodoPago,
            String fechaVencimientoPagare) {

        if (numFacturaProveedor == null || numFacturaProveedor.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Debe ingresar el número de factura del proveedor.");
            return false;
        }
        if (proveedor == null) {
            JOptionPane.showMessageDialog(parent, "Debe seleccionar un proveedor.");
            return false;
        }
        if (detalles == null || detalles.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Debe agregar al menos un producto a la compra.");
            return false;
        }
        if (!pagoContado && (fechaVencimientoPagare == null || fechaVencimientoPagare.trim().isEmpty())) {
            JOptionPane.showMessageDialog(parent, "Debe indicar la fecha de vencimiento del pagaré para una compra a crédito.");
            return false;
        }

        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(parent, "No hay una sesión activa. Inicia sesión antes de registrar compras.", "Sesión requerida", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String idProveedor = proveedorDAO.obtenerIdPorRuc(proveedor.getRuc());
        if (idProveedor == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo resolver el proveedor seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        double[] totales = calcularTotales(detalles, descuento);

        String idCompra = compraDAO.registrar(idNegocio, idProveedor, numFacturaProveedor.trim(),
                totales[0], totales[1], descuento, totales[2], detalles,
                pagoContado, metodoPago, fechaVencimientoPagare, proveedor.getNombreEmpresa());

        boolean exito = idCompra != null;
        if (exito) {
            String mensaje = pagoContado
                    ? "Compra registrada y egreso generado exitosamente!"
                    : "Compra registrada. Se generó un pagaré pendiente de pago.";
            JOptionPane.showMessageDialog(parent, mensaje);
            String detalleNotif = pagoContado
                    ? "Se registro una compra de contado a " + proveedor.getNombreEmpresa() + " por $" + String.format("%.2f", totales[2]) + "."
                    : "Se registro una compra a credito con " + proveedor.getNombreEmpresa() + " por $" + String.format("%.2f", totales[2]) + ". Vence el " + fechaVencimientoPagare + ".";
            new ControladorNotificacion().notificarEvento(idNegocio, "COMPRA_" + idCompra, detalleNotif);
        } else {
            JOptionPane.showMessageDialog(parent, "Error al registrar la compra (revisa que los productos existan).");
        }
        return exito;
    }

    public List<Compra> listarTodas() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return compraDAO.listarPorNegocio(idNegocio);
    }
}
