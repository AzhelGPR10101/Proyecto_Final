package Controladores;

import DAO.EstadisticaDAO;
import Modelo.EstadisticaPeriodo;
import Modelo.MovimientoDiario;
import Modelo.MovimientoHorario;
import Modelo.Sesion;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ControladorEstadistica {

    public enum TipoPeriodo {
        HOY, SEMANA, MES, TRIMESTRE
    }

    private static final Locale ESPANOL = new Locale("es", "ES");

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAO();

    public static class ResultadoEstadistica {
        private final List<EstadisticaPeriodo> filas;
        private final double totalGanancias;
        private final double totalGastos;
        private final int totalProductosVendidos;
        private final int productosActivos;
        private final String categoriaMasVendida;
        private final String etiquetaPeriodo;

        public ResultadoEstadistica(List<EstadisticaPeriodo> filas, double totalGanancias, double totalGastos,
                int totalProductosVendidos, int productosActivos, String categoriaMasVendida, String etiquetaPeriodo) {
            this.filas = filas;
            this.totalGanancias = totalGanancias;
            this.totalGastos = totalGastos;
            this.totalProductosVendidos = totalProductosVendidos;
            this.productosActivos = productosActivos;
            this.categoriaMasVendida = categoriaMasVendida;
            this.etiquetaPeriodo = etiquetaPeriodo;
        }

        public List<EstadisticaPeriodo> getFilas() {
            return filas;
        }

        public double getTotalGanancias() {
            return totalGanancias;
        }

        public double getTotalGastos() {
            return totalGastos;
        }

        public double getBalanceNeto() {
            return totalGanancias - totalGastos;
        }

        public int getTotalProductosVendidos() {
            return totalProductosVendidos;
        }

        public int getProductosActivos() {
            return productosActivos;
        }

        public String getCategoriaMasVendida() {
            return categoriaMasVendida == null ? "Sin ventas" : categoriaMasVendida;
        }

        public String getEtiquetaPeriodo() {
            return etiquetaPeriodo;
        }
    }

    public ResultadoEstadistica obtenerEstadisticas(TipoPeriodo tipo) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ResultadoEstadistica(new ArrayList<>(), 0, 0, 0, 0, null, etiquetaDe(tipo));
        }

        LocalDate[] rango = rangoDe(tipo);
        LocalDate desde = rango[0];
        LocalDate hasta = rango[1];
        List<EstadisticaPeriodo> filas;

        switch (tipo) {
            case HOY:
                filas = filasPorHora(idNegocio, desde);
                break;
            case SEMANA:
                filas = filasPorNombreDia(idNegocio, desde, hasta);
                break;
            default:
                filas = filasPorSemana(idNegocio, desde, hasta);
                break;
        }

        double totalGanancias = 0;
        double totalGastos = 0;
        int totalVendidos = 0;
        for (EstadisticaPeriodo f : filas) {
            totalGanancias += f.getGanancias();
            totalGastos += f.getGastos();
            totalVendidos += f.getProductosVendidos();
        }

        String categoriaMasVendida = estadisticaDAO.obtenerCategoriaMasVendida(idNegocio, desde, hasta);
        int productosActivos = estadisticaDAO.contarProductosActivos(idNegocio);

        return new ResultadoEstadistica(filas, totalGanancias, totalGastos, totalVendidos,
                productosActivos, categoriaMasVendida, etiquetaDe(tipo));
    }

    public List<Modelo.ProductoVendido> listarMasVendidos(TipoPeriodo tipo, int limite) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        LocalDate[] rango = rangoDe(tipo);
        return estadisticaDAO.listarMasVendidos(idNegocio, rango[0], rango[1], limite);
    }

    public List<Modelo.Producto> listarStockBajo(int limite) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return estadisticaDAO.listarStockBajo(idNegocio, limite);
    }

    public List<Modelo.ActividadReciente> listarActividadReciente(int limite) {
        String idNegocio = Sesion.getIdNegocio();
        if (idNegocio == null) {
            return new ArrayList<>();
        }
        return estadisticaDAO.listarActividadReciente(idNegocio, limite);
    }

    private LocalDate[] rangoDe(TipoPeriodo tipo) {
        LocalDate hoy = LocalDate.now();
        switch (tipo) {
            case HOY:
                return new LocalDate[]{hoy, hoy};
            case SEMANA: {
                LocalDate lunes = hoy.with(DayOfWeek.MONDAY);
                return new LocalDate[]{lunes, lunes.plusDays(6)};
            }
            case MES: {
                YearMonth mesActual = YearMonth.from(hoy);
                return new LocalDate[]{mesActual.atDay(1), mesActual.atEndOfMonth()};
            }
            case TRIMESTRE:
            default: {
                int mesInicio = ((hoy.getMonthValue() - 1) / 3) * 3 + 1;
                LocalDate desde = LocalDate.of(hoy.getYear(), mesInicio, 1);
                return new LocalDate[]{desde, desde.plusMonths(3).minusDays(1)};
            }
        }
    }

    private String etiquetaDe(TipoPeriodo tipo) {
        switch (tipo) {
            case HOY: return "Hoy";
            case SEMANA: return "Esta semana";
            case MES: return "Este mes";
            case TRIMESTRE: return "Este trimestre";
            default: return "";
        }
    }

    private List<EstadisticaPeriodo> filasPorHora(String idNegocio, LocalDate hoy) {
        List<MovimientoHorario> horas = estadisticaDAO.listarPorHora(idNegocio, hoy);
        List<EstadisticaPeriodo> filas = new ArrayList<>();
        for (MovimientoHorario h : horas) {
            String etiqueta = String.format("%02d:00", h.getHora());
            filas.add(new EstadisticaPeriodo(etiqueta, h.getGanancias(), h.getGastos(), h.getProductosVendidos()));
        }
        if (filas.isEmpty()) {

            filas.add(new EstadisticaPeriodo("Hoy", 0, 0, 0));
        }
        return filas;
    }

    private List<EstadisticaPeriodo> filasPorNombreDia(String idNegocio, LocalDate lunes, LocalDate domingo) {
        List<MovimientoDiario> diarios = estadisticaDAO.listarPorDia(idNegocio, lunes, domingo);

        List<EstadisticaPeriodo> filas = new ArrayList<>();
        for (LocalDate dia = lunes; !dia.isAfter(domingo); dia = dia.plusDays(1)) {
            MovimientoDiario m = buscarPorFecha(diarios, dia);
            String nombreDia = capitalizar(dia.getDayOfWeek().getDisplayName(TextStyle.FULL, ESPANOL));
            if (m == null) {
                filas.add(new EstadisticaPeriodo(nombreDia, 0, 0, 0));
            } else {
                filas.add(new EstadisticaPeriodo(nombreDia, m.getGanancias(), m.getGastos(), m.getProductosVendidos()));
            }
        }
        return filas;
    }

    private List<EstadisticaPeriodo> filasPorSemana(String idNegocio, LocalDate desde, LocalDate hasta) {
        List<MovimientoDiario> diarios = estadisticaDAO.listarPorDia(idNegocio, desde, hasta);

        long totalDias = java.time.temporal.ChronoUnit.DAYS.between(desde, hasta) + 1;
        int totalSemanas = (int) Math.ceil(totalDias / 7.0);

        double[] gananciasPorSemana = new double[totalSemanas];
        double[] gastosPorSemana = new double[totalSemanas];
        int[] vendidosPorSemana = new int[totalSemanas];

        for (MovimientoDiario m : diarios) {
            int indiceSemana = (int) (java.time.temporal.ChronoUnit.DAYS.between(desde, m.getFecha()) / 7);
            if (indiceSemana >= 0 && indiceSemana < totalSemanas) {
                gananciasPorSemana[indiceSemana] += m.getGanancias();
                gastosPorSemana[indiceSemana] += m.getGastos();
                vendidosPorSemana[indiceSemana] += m.getProductosVendidos();
            }
        }

        List<EstadisticaPeriodo> filas = new ArrayList<>();
        for (int i = 0; i < totalSemanas; i++) {
            filas.add(new EstadisticaPeriodo("Semana " + (i + 1),
                    gananciasPorSemana[i], gastosPorSemana[i], vendidosPorSemana[i]));
        }
        return filas;
    }

    private MovimientoDiario buscarPorFecha(List<MovimientoDiario> lista, LocalDate fecha) {
        for (MovimientoDiario m : lista) {
            if (m.getFecha().equals(fecha)) {
                return m;
            }
        }
        return null;
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }
}
