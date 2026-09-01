package Vista.IA;

import Controladores.ControladorPermiso;
import DAO.CompraDAO;
import DAO.EstadisticaDAO;
import DAO.ProductoDAO;
import Modelo.Compra;
import Modelo.MovimientoDiario;
import Modelo.PermisoSistema;
import Modelo.Producto;
import Modelo.Sesion;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ContextoNegocio {

    private static final int STOCK_MINIMO = 5;

    public static String generar(String idNegocio) {
        if (idNegocio == null || idNegocio.isBlank()) {
            return "No hay una empresa/negocio activo en la sesion todavia.";
        }

        String idRol = Sesion.getIdRolUsuario();
        StringBuilder sb = new StringBuilder();

        if (idRol == null || ControladorPermiso.tienePermiso(idRol, PermisoSistema.VER_PRODUCTOS.name())) {
            try {
                agregarProductos(sb, idNegocio);
            } catch (Exception e) {
                sb.append("(No se pudo leer productos: ").append(e.getMessage()).append(")\n");
            }
        }

        if (idRol == null || ControladorPermiso.tienePermiso(idRol, PermisoSistema.VER_PROVEEDORES.name())) {
            try {
                agregarCompras(sb, idNegocio);
            } catch (Exception e) {
                sb.append("(No se pudo leer compras: ").append(e.getMessage()).append(")\n");
            }
        }

        if (idRol == null || ControladorPermiso.tienePermiso(idRol, PermisoSistema.VER_VENTAS.name())) {
            try {
                agregarVentas(sb, idNegocio);
            } catch (Exception e) {
                sb.append("(No se pudo leer ventas: ").append(e.getMessage()).append(")\n");
            }
        }

        if (idRol == null || ControladorPermiso.tienePermiso(idRol, PermisoSistema.VER_EGRESOS.name())) {
            try {
                agregarEgresos(sb, idNegocio);
            } catch (Exception e) {
                sb.append("(No se pudo leer egresos: ").append(e.getMessage()).append(")\n");
            }
        }

        if (idRol == null || ControladorPermiso.tienePermiso(idRol, PermisoSistema.VER_REPORTES.name())) {
            try {
                agregarResumenMensual(sb, idNegocio);
            } catch (Exception e) {
                sb.append("(No se pudo leer el resumen mensual: ").append(e.getMessage()).append(")\n");
            }
        }

        if (sb.length() == 0) {
            sb.append("Tu rol actual no tiene permisos para consultar informacion del negocio con el asistente.");
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

        sb.append("Productos AGOTADOS (stock en 0, URGENTE): ");
        boolean hayAgotados = false;
        for (Producto p : productos) {
            if (p.getCantidad() <= 0) {
                sb.append(p.getNombre()).append(", ");
                hayAgotados = true;
            }
        }
        sb.append(hayAgotados ? "\n" : "Ninguno.\n");

        sb.append("Productos con stock bajo (por debajo de su stock minimo configurado): ");
        boolean hayBajos = false;
        for (Producto p : productos) {
            int minimo = p.getStockMinimo() > 0 ? p.getStockMinimo() : STOCK_MINIMO;
            if (p.getCantidad() > 0 && p.getCantidad() < minimo) {
                sb.append(p.getNombre()).append(" (").append(p.getCantidad())
                        .append(" de minimo ").append(minimo).append("), ");
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

    private static void agregarResumenMensual(StringBuilder sb, String idNegocio) {
        EstadisticaDAO estadisticaDAO = new EstadisticaDAO();
        Locale espanol = new Locale("es", "ES");
        LocalDate hoy = LocalDate.now();

        sb.append("\n=== RESUMEN POR MES (ultimos 6 meses, ganancias/gastos/productos vendidos) ===\n");

        for (int i = 5; i >= 0; i--) {
            YearMonth ym = YearMonth.from(hoy).minusMonths(i);
            LocalDate desde = ym.atDay(1);
            LocalDate hasta = ym.equals(YearMonth.from(hoy)) ? hoy : ym.atEndOfMonth();

            List<MovimientoDiario> dias = estadisticaDAO.listarPorDia(idNegocio, desde, hasta);
            double ganancias = 0, gastos = 0;
            int vendidos = 0;
            for (MovimientoDiario m : dias) {
                ganancias += m.getGanancias();
                gastos += m.getGastos();
                vendidos += m.getProductosVendidos();
            }

            String nombreMes = ym.getMonth().getDisplayName(TextStyle.FULL, espanol);
            nombreMes = Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1);

            sb.append("- ").append(nombreMes).append(" ").append(ym.getYear())
                    .append(": ganancias $").append(String.format("%.2f", ganancias))
                    .append(", gastos $").append(String.format("%.2f", gastos))
                    .append(", productos vendidos: ").append(vendidos).append("\n");
        }
    }
}
