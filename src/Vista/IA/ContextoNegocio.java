package Vista.IA;

import DAO.CompraDAO;
import DAO.ProductoDAO;
import Modelo.Compra;
import Modelo.Producto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextoNegocio {

    private static final int STOCK_MINIMO = 5;

    public static String generar(String idNegocio) {
        if (idNegocio == null || idNegocio.isBlank()) {
            return "No hay una empresa/negocio activo en la sesion todavia.";
        }

        StringBuilder sb = new StringBuilder();

        try {
            agregarProductos(sb, idNegocio);
        } catch (Exception e) {
            sb.append("(No se pudo leer productos: ").append(e.getMessage()).append(")\n");
        }

        try {
            agregarCompras(sb, idNegocio);
        } catch (Exception e) {
            sb.append("(No se pudo leer compras: ").append(e.getMessage()).append(")\n");
        }

        try {
            agregarVentas(sb, idNegocio);
        } catch (Exception e) {
            sb.append("(No se pudo leer ventas: ").append(e.getMessage()).append(")\n");
        }

        try {
            agregarEgresos(sb, idNegocio);
        } catch (Exception e) {
            sb.append("(No se pudo leer egresos: ").append(e.getMessage()).append(")\n");
        }

        return sb.toString();
    }

    private static void agregarProductos(StringBuilder sb, String idNegocio) {
        List<Producto> productos = new ProductoDAO().listarPorNegocio(idNegocio);

        sb.append("=== PRODUCTOS (").append(productos.size()).append(" en total) ===\n");
        if (productos.isEmpty()) {
            sb.append("No hay productos registrados todavia.\n");
            return;
        }

        for (Producto p : productos) {
            sb.append("- ").append(p.getNombre())
                    .append(" | categoria: ").append(p.getCategoria())
                    .append(" | stock: ").append(p.getCantidad())
                    .append(" | precio: $").append(String.format("%.2f", p.getPrecioUnitario()));
            if (p.getFechaVencimiento() != null && !p.getFechaVencimiento().isBlank()) {
                sb.append(" | vence: ").append(p.getFechaVencimiento());
            }
            sb.append("\n");
        }

        sb.append("Productos con stock bajo (menos de ").append(STOCK_MINIMO).append(" unidades): ");
        boolean hayBajos = false;
        for (Producto p : productos) {
            if (p.getCantidad() < STOCK_MINIMO) {
                sb.append(p.getNombre()).append(" (").append(p.getCantidad()).append("), ");
                hayBajos = true;
            }
        }
        sb.append(hayBajos ? "\n" : "Ninguno.\n");
    }

    private static void agregarCompras(StringBuilder sb, String idNegocio) {
        List<Compra> compras = new CompraDAO().listarPorNegocio(idNegocio);

        sb.append("\n=== COMPRAS A PROVEEDORES (").append(compras.size()).append(" en total) ===\n");
        if (compras.isEmpty()) {
            sb.append("No hay compras registradas todavia.\n");
            return;
        }

        Map<String, Double> totalPorProveedor = new HashMap<>();
        Map<String, Integer> cantidadPorProveedor = new HashMap<>();
        double totalGeneral = 0;

        for (Compra c : compras) {
            String nombreProv = c.getProveedor() == null ? "Sin proveedor"
                    : (c.getProveedor().getNombreEmpresa() + " " + c.getProveedor().getNombreContacto()).trim();
            totalPorProveedor.merge(nombreProv, c.getTotal(), Double::sum);
            cantidadPorProveedor.merge(nombreProv, 1, Integer::sum);
            totalGeneral += c.getTotal();
        }

        sb.append("Total comprado en general: $").append(String.format("%.2f", totalGeneral)).append("\n");
        sb.append("Detalle por proveedor (monto total comprado y numero de compras):\n");
        for (Map.Entry<String, Double> entry : totalPorProveedor.entrySet()) {
            sb.append("- ").append(entry.getKey())
                    .append(": $").append(String.format("%.2f", entry.getValue()))
                    .append(" en ").append(cantidadPorProveedor.get(entry.getKey())).append(" compra(s)\n");
        }

        Compra ultima = compras.get(0);
        String nombreUltimo = ultima.getProveedor() == null ? "Sin proveedor"
                : (ultima.getProveedor().getNombreEmpresa() + " " + ultima.getProveedor().getNombreContacto()).trim();
        sb.append("Ultima compra registrada: a ").append(nombreUltimo)
                .append(" el ").append(ultima.getFecha())
                .append(" por $").append(String.format("%.2f", ultima.getTotal())).append("\n");
    }

    // NUEVO metodo en ContextoNegocio.java
    private static void agregarVentas(StringBuilder sb, String idNegocio) {
        List<Modelo.Factura> facturas = new DAO.FacturaDAO().listarPorNegocio(idNegocio);
        sb.append("\n=== VENTAS / FACTURAS (").append(facturas.size()).append(" en total) ===\n");
        if (facturas.isEmpty()) {
            sb.append("No hay ventas registradas todavia.\n");
            return;
        }
        double totalVendido = 0;
        for (Modelo.Factura f : facturas) {
            totalVendido += f.getTotal();
        }
        sb.append("Total vendido en general: $").append(String.format("%.2f", totalVendido)).append("\n");
        sb.append("Numero de facturas emitidas: ").append(facturas.size()).append("\n");
    }

    private static void agregarEgresos(StringBuilder sb, String idNegocio) {
        List<Modelo.Egreso> egresos = new DAO.EgresoDAO().listarPorNegocio(idNegocio);
        sb.append("\n=== EGRESOS (").append(egresos.size()).append(" en total) ===\n");
        if (egresos.isEmpty()) {
            sb.append("No hay egresos registrados todavia.\n");
            return;
        }
        double totalEgresos = 0;
        for (Modelo.Egreso e : egresos) {
            totalEgresos += e.getMonto(); 
        }
        sb.append("Total de egresos: $").append(String.format("%.2f", totalEgresos)).append("\n");
    }
}
