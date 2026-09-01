package Controladores;

import DAO.FacturaDAO;
import DAO.ReporteDAO;
import Modelo.DetalleFactura;
import Modelo.MovimientoFinanciero;
import Modelo.Sesion;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ControladorReporte {

    public static final String OPCION_TODOS = "Todos";
    private static final String FORMATO_FECHA = "yyyy-MM-dd";

    private final ReporteDAO reporteDAO = new ReporteDAO();
    private final FacturaDAO facturaDAO = new FacturaDAO();

    public List<DetalleFactura> obtenerDetallePorFactura(String idFactura) {
        return facturaDAO.obtenerDetallePorFactura(idFactura);
    }

    public static class ResultadoReporte {
        public final List<MovimientoFinanciero> movimientos;
        public final double totalIngresos;
        public final double totalEgresos;
        public final double balanceNeto;

        public ResultadoReporte(List<MovimientoFinanciero> movimientos) {
            this.movimientos = movimientos;
            double ingresos = 0, egresos = 0;
            for (MovimientoFinanciero m : movimientos) {
                if (m.esIngreso()) ingresos += m.getMonto(); else egresos += m.getMonto();
            }
            this.totalIngresos = ingresos;
            this.totalEgresos = egresos;
            this.balanceNeto = ingresos - egresos;
        }
    }

    public String validarRango(String desdeStr, String hastaStr) {
        if (!Validaciones.validarFecha(desdeStr) || !Validaciones.validarFecha(hastaStr)) {
            return "Ingrese fechas válidas con formato aaaa-mm-dd.";
        }
        Date desde = aSqlDate(desdeStr);
        Date hasta = aSqlDate(hastaStr);
        if (desde.after(hasta)) {
            return "La fecha 'Desde' no puede ser posterior a la fecha 'Hasta'.";
        }
        return null;
    }

    private Date aSqlDate(String fechaStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        sdf.setLenient(false);
        try {
            return new Date(sdf.parse(fechaStr.trim()).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public ResultadoReporte filtrar(String desdeStr, String hastaStr, String tipo,
            String clienteFiltro, String empleadoFiltro) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ResultadoReporte(new ArrayList<>());
        }
        Date desde = aSqlDate(desdeStr);
        Date hasta = aSqlDate(hastaStr);

        List<MovimientoFinanciero> movimientos = new ArrayList<>();
        if (tipo == null || OPCION_TODOS.equals(tipo) || "Ingresos".equals(tipo)) {
            movimientos.addAll(reporteDAO.listarIngresos(idNegocio, desde, hasta));
        }
        if (tipo == null || OPCION_TODOS.equals(tipo) || "Egresos".equals(tipo)) {
            movimientos.addAll(reporteDAO.listarEgresos(idNegocio, desde, hasta));
        }

        List<MovimientoFinanciero> filtrados = new ArrayList<>();
        for (MovimientoFinanciero m : movimientos) {
            boolean coincideCliente = clienteFiltro == null || OPCION_TODOS.equals(clienteFiltro)
                    || (m.esIngreso() && clienteFiltro.equalsIgnoreCase(m.getDetalle()));
            boolean coincideEmpleado = empleadoFiltro == null || OPCION_TODOS.equals(empleadoFiltro)
                    || (m.esIngreso() && empleadoFiltro.equalsIgnoreCase(m.getEmpleado()));
            if (coincideCliente && coincideEmpleado) {
                filtrados.add(m);
            }
        }

        filtrados.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));
        return new ResultadoReporte(filtrados);
    }

    public List<String> listarClientesParaFiltro() {
        List<String> lista = new ArrayList<>();
        lista.add(OPCION_TODOS);
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio != null) lista.addAll(reporteDAO.listarClientesParaFiltro(idNegocio));
        return lista;
    }

    public List<String> listarEmpleadosParaFiltro() {
        List<String> lista = new ArrayList<>();
        lista.add(OPCION_TODOS);
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio != null) lista.addAll(reporteDAO.listarEmpleadosParaFiltro(idNegocio));
        return lista;
    }
}