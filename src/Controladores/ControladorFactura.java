package Controladores;

import DAO.FacturaDAO;
import Modelo.Cliente;
import Modelo.DetalleFactura;
import Modelo.Factura;
import Modelo.Producto;
import Modelo.Sesion;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class ControladorFactura {

    private final FacturaDAO facturaDAO = new FacturaDAO();
    private final DAO.ProductoDAO productoDAO = new DAO.ProductoDAO();

    public double[] calcularTotales(List<DetalleFactura> detalles, double descuento) {
        double subtotal = 0;
        double valorIva = 0;
        for (DetalleFactura d : detalles) {
            subtotal += d.getSubtotal();
            valorIva += d.getValorIva();
        }
        double total = subtotal + valorIva - descuento;
        return new double[]{subtotal, valorIva, total};
    }

    public DetalleFactura crearDetalle(String idProducto, String nombreProducto,
            int cantidad, double precioUnitario, boolean tieneIva) {
        double subtotal = cantidad * precioUnitario;
        double valorIva = tieneIva ? subtotal * productoDAO.obtenerPorcentajeIvaVigente() : 0;
        return new DetalleFactura(idProducto, nombreProducto, cantidad, precioUnitario, subtotal, valorIva);
    }

    public DetalleFactura crearDetalle(Producto producto, int cantidad) {
        return crearDetalle(
                producto.getCodigo(),
                producto.getNombre(),
                cantidad,
                producto.getPrecioUnitario(),
                producto.isTieneIva()
        );
    }

    public String generarNumeroFactura() {
        return "F-" + System.currentTimeMillis();
    }

    public static class ResultadoAgregarCarrito {
        public final boolean ok;
        public final String error;
        public final int indiceActualizado;
        public final DetalleFactura detalle;

        private ResultadoAgregarCarrito(boolean ok, String error, int indiceActualizado, DetalleFactura detalle) {
            this.ok = ok;
            this.error = error;
            this.indiceActualizado = indiceActualizado;
            this.detalle = detalle;
        }

        static ResultadoAgregarCarrito error(String mensaje) {
            return new ResultadoAgregarCarrito(false, mensaje, -1, null);
        }

        static ResultadoAgregarCarrito exito(int indiceActualizado, DetalleFactura detalle) {
            return new ResultadoAgregarCarrito(true, null, indiceActualizado, detalle);
        }
    }

    public ResultadoAgregarCarrito agregarAlCarrito(List<DetalleFactura> listaActual, Producto producto, int cantidad) {
        int yaAgregado = 0;
        int filaExistente = -1;
        for (int i = 0; i < listaActual.size(); i++) {
            if (listaActual.get(i).getIdProducto().equals(producto.getCodigo())) {
                yaAgregado += listaActual.get(i).getCantidad();
                filaExistente = i;
            }
        }

        if (cantidad + yaAgregado > producto.getCantidad()) {
            int disponibleReal = producto.getCantidad() - yaAgregado;
            return ResultadoAgregarCarrito.error(
                    "No hay suficiente stock de \"" + producto.getNombre() + "\". Disponible: "
                    + (disponibleReal < 0 ? 0 : disponibleReal) + " unidades.");
        }

        if (filaExistente != -1) {
            int nuevaCantidad = listaActual.get(filaExistente).getCantidad() + cantidad;
            return ResultadoAgregarCarrito.exito(filaExistente, crearDetalle(producto, nuevaCantidad));
        }
        return ResultadoAgregarCarrito.exito(-1, crearDetalle(producto, cantidad));
    }

    public List<DetalleFactura> obtenerDetallePorFactura(String idFactura) {
        return facturaDAO.obtenerDetallePorFactura(idFactura);
    }

    public boolean registrarFactura(java.awt.Component parent, String numFactura,
            String fecha, Cliente cliente, String metodoPago,
            List<DetalleFactura> detalles, double descuento, String idEmpleado) {

        if (!new ControladorCierreCaja().facturacionHabilitadaParaSesionActual()) {
            JOptionPane.showMessageDialog(parent,
                    "No puedes registrar la venta: la caja está cerrada. Abre caja antes de facturar.",
                    "Caja cerrada", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cliente == null) {
            JOptionPane.showMessageDialog(parent, "Debe seleccionar un cliente.");
            return false;
        }
        if (detalles == null || detalles.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Debe agregar al menos un producto.");
            return false;
        }

        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            JOptionPane.showMessageDialog(parent, "No hay una sesion activa. Inicia sesion antes de facturar.", "Sesion requerida", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String idCliente = cliente.getIdCliente();
        if (idCliente == null) {
            idCliente = facturaDAO.obtenerOCrearClienteConsumidorFinal();
        }
        if (idCliente == null) {
            JOptionPane.showMessageDialog(parent, "No se pudo resolver el cliente de la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (idEmpleado == null) {
            JOptionPane.showMessageDialog(parent, "Seleccione el empleado que factura.", "Falta un empleado", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        double[] totales = calcularTotales(detalles, descuento);

        cliente.setIdCliente(idCliente);

        Factura nuevaFactura = new Factura();
        nuevaFactura.setIdNegocio(idNegocio);
        nuevaFactura.setIdEmpleado(idEmpleado);
        nuevaFactura.setCliente(cliente);
        nuevaFactura.setMetodoPago(metodoPago);
        nuevaFactura.setNumFactura(numFactura);
        nuevaFactura.setDetalles(detalles);
        nuevaFactura.setSubtotal(totales[0]);
        nuevaFactura.setValorIva(totales[1]);
        nuevaFactura.setDescuento(descuento);
        nuevaFactura.setTotal(totales[2]);

        String idFactura = facturaDAO.registrar(nuevaFactura);

        boolean exito = idFactura != null;
        if (exito) {
            nuevaFactura.setIdFactura(idFactura);
            nuevaFactura.setFecha(fecha);

            String avisoCorreo = enviarFacturaPorCorreoSiCorresponde(nuevaFactura);

            JOptionPane.showMessageDialog(parent, "Factura guardada exitosamente!" + avisoCorreo);
            new ControladorNotificacion().notificarEvento(idNegocio,
                    "VENTA_" + idFactura,
                    "Se registro una venta (factura " + numFactura + ") por $" + String.format("%.2f", totales[2]) + ".");
        } else {
            JOptionPane.showMessageDialog(parent, "Error al guardar la factura (revisa que haya stock suficiente).");
        }
        return exito;
    }

    private String enviarFacturaPorCorreoSiCorresponde(Factura factura) {
        String correo = factura.getCliente() != null ? factura.getCliente().getCorreo() : null;
        if (correo == null || correo.trim().isEmpty() || !Correo.EmailService.hayCorreoConfigurado()) {
            return "";
        }
        try {
            String ruta = Reportes.CarpetaExportacion.obtenerRuta("Factura_" + factura.getNumFactura() + ".pdf");
            Reportes.GeneradorPDFFactura.generar(factura, ruta);
            Correo.EmailService.enviarFacturaPorCorreo(factura, ruta);
            return "\nSe envio la factura al correo del cliente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "\nNo se pudo enviar la factura por correo (revisa la conexion a internet). Puedes reenviarla despues desde Historial de Facturas.";
        }
    }

    public List<Factura> listarTodas() {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }

        String rol = Sesion.getRolUsuario();
        if ("Cajero".equalsIgnoreCase(rol) || "Vendedor".equalsIgnoreCase(rol)) {
            return facturaDAO.listarPorEmpleadoEnTurno(Sesion.getIdUsuario(), idNegocio);
        }

        return facturaDAO.listarPorNegocio(idNegocio);
    }

    public List<Factura> filtrarFacturas(String texto, String tipoCliente) {
        List<Factura> todas = listarTodas();
        List<Factura> filtradas = new ArrayList<>();
        String textoLower = texto == null ? "" : texto.trim().toLowerCase();

        for (Factura f : todas) {
            boolean coincideTexto = textoLower.isEmpty()
                    || f.getNumFactura().toLowerCase().contains(textoLower)
                    || (f.getCliente() != null && (f.getCliente().getNombre() + " " + f.getCliente().getApellido()).toLowerCase().contains(textoLower));

            boolean coincideTipo;
            if ("Todos".equals(tipoCliente) || tipoCliente == null) {
                coincideTipo = true;
            } else if ("Consumidor Final".equals(tipoCliente)) {
                coincideTipo = f.getCliente() != null && "Consumidor Final".equalsIgnoreCase(f.getCliente().getNombreCliente());
            } else if ("Con Datos".equals(tipoCliente)) {
                coincideTipo = f.getCliente() != null && !"Consumidor Final".equalsIgnoreCase(f.getCliente().getNombreCliente());
            } else {
                coincideTipo = true;
            }

            if (coincideTexto && coincideTipo) {
                filtradas.add(f);
            }
        }
        return filtradas;
    }

    public double calcularCambio(double efectivo, double total) {
        return efectivo - total;
    }

    public boolean efectivoAlcanza(double efectivo, double total) {
        return efectivo >= total;
    }

    public boolean eliminarFactura(java.awt.Component parent, String idFactura) {
        if (idFactura == null) {
            JOptionPane.showMessageDialog(parent, "Seleccione una factura de la lista.");
            return false;
        }
        int confirmar = JOptionPane.showConfirmDialog(parent,
                "¿Seguro que deseas eliminar esta factura? Esta acción no se puede deshacer\ny el stock de los productos se repondrá.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) {
            return false;
        }
        boolean exito = facturaDAO.eliminar(Sesion.getIdNegocio(), idFactura);
        if (exito) {
            JOptionPane.showMessageDialog(parent, "Factura eliminada correctamente.");
        } else {
            JOptionPane.showMessageDialog(parent, "No se pudo eliminar la factura.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }

    public boolean eliminarSinConfirmar(String idFactura) {
        if (idFactura == null) {
            return false;
        }
        return facturaDAO.eliminar(Sesion.getIdNegocio(), idFactura);
    }
}
