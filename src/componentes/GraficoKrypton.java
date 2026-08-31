package componentes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.labels.ItemLabelAnchor;

public class GraficoKrypton extends JPanel {

    private static final Color COLOR_FONDO = new Color(31, 10, 48);
    private static final Color COLOR_GANANCIAS = new Color(76, 175, 80);
    private static final Color COLOR_GASTOS = new Color(229, 62, 90);
    private static final Color COLOR_GRILLA = new Color(80, 50, 100);
    private static final Color COLOR_TEXTO = new Color(230, 220, 240);
    private static final Font FUENTE_EJES = new Font("SansSerif", Font.PLAIN, 12);

    private enum TipoGrafico {
        LINEAS, BARRAS, BARRAS_H, AREA
    }

    private ChartPanel chartPanel;
    private DefaultCategoryDataset datasetActual;
    private TipoGrafico ultimoTipo = TipoGrafico.BARRAS;
    private String nombreSerieGanancias = "Ganancias";
    private String nombreSerieGastos = "Gastos";

    public GraficoKrypton() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public String getTipoActual() {
        return ultimoTipo.name();
    }

    public void mostrarSegunTipo(String tipo) {
        switch (TipoGrafico.valueOf(tipo)) {
            case LINEAS:
                mostrarLineas();
                break;
            case BARRAS:
                mostrarBarras();
                break;
            case BARRAS_H:
                mostrarBarrasHorizontal();
                break;
            case AREA:
                mostrarArea();
                break;
        }
    }

    public void setDatos(String[] periodos, double[] ganancias, double[] gastos) {
        datasetActual = new DefaultCategoryDataset();
        for (int i = 0; i < periodos.length; i++) {
            datasetActual.addValue(ganancias[i], nombreSerieGanancias, periodos[i]);
            datasetActual.addValue(gastos[i], nombreSerieGastos, periodos[i]);
        }
        if (chartPanel != null) {
            mostrarSegunUltimoTipo();
        }
    }

    public void mostrarLineas() {
        ultimoTipo = TipoGrafico.LINEAS;
        JFreeChart chart = ChartFactory.createLineChart(null, null, null, datasetActual,
                PlotOrientation.VERTICAL, false, true, false);
        aplicarEstiloLineas(chart);
        renderizar(chart);
    }

    public void mostrarBarras() {
        ultimoTipo = TipoGrafico.BARRAS;
        JFreeChart chart = ChartFactory.createBarChart(null, null, null, datasetActual,
                PlotOrientation.VERTICAL, false, true, false);
        aplicarEstiloBarras(chart);
        renderizar(chart);
    }

    public void mostrarBarrasHorizontal() {
        ultimoTipo = TipoGrafico.BARRAS_H;
        JFreeChart chart = ChartFactory.createBarChart(null, null, null, datasetActual,
                PlotOrientation.HORIZONTAL, false, true, false);
        aplicarEstiloBarras(chart);
        renderizar(chart);
    }

    public void mostrarArea() {
        ultimoTipo = TipoGrafico.AREA;
        JFreeChart chart = ChartFactory.createAreaChart(null, null, null, datasetActual,
                PlotOrientation.VERTICAL, false, true, false);
        aplicarEstiloArea(chart);
        renderizar(chart);
    }

    private void mostrarSegunUltimoTipo() {
        switch (ultimoTipo) {
            case LINEAS:
                mostrarLineas();
                break;
            case BARRAS:
                mostrarBarras();
                break;
            case BARRAS_H:
                mostrarBarrasHorizontal();
                break;
            case AREA:
                mostrarArea();
                break;
        }
    }

    private void renderizar(JFreeChart chart) {
        removeAll();
        chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(COLOR_FONDO);
        chartPanel.setMouseWheelEnabled(false);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void estiloBase(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_FONDO);
        if (chart.getLegend() != null) {
            chart.removeLegend();
        }
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(COLOR_FONDO);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(COLOR_GRILLA);
        plot.setDomainGridlinePaint(COLOR_GRILLA);

        CategoryAxis ejeCategorias = plot.getDomainAxis();
        ejeCategorias.setLabelPaint(COLOR_TEXTO);
        ejeCategorias.setTickLabelPaint(COLOR_TEXTO);
        ejeCategorias.setTickLabelFont(FUENTE_EJES);
        ejeCategorias.setAxisLinePaint(COLOR_GRILLA);

        NumberAxis ejeValores = (NumberAxis) plot.getRangeAxis();
        ejeValores.setLabelPaint(COLOR_TEXTO);
        ejeValores.setTickLabelPaint(COLOR_TEXTO);
        ejeValores.setTickLabelFont(FUENTE_EJES);
        ejeValores.setAxisLinePaint(COLOR_GRILLA);
        ejeValores.setNumberFormatOverride(new java.text.DecimalFormat("$#,##0"));
        ejeValores.setAutoRangeIncludesZero(true);
        if (todosLosValoresSonCero()) {
            ejeValores.setRange(0, 10);
        }
    }

    private boolean todosLosValoresSonCero() {
        if (datasetActual == null) {
            return true;
        }
        for (int fila = 0; fila < datasetActual.getRowCount(); fila++) {
            for (int col = 0; col < datasetActual.getColumnCount(); col++) {
                Number valor = datasetActual.getValue(fila, col);
                if (valor != null && valor.doubleValue() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

       private void aplicarEstiloBarras(JFreeChart chart) {
        estiloBase(chart);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setSeriesPaint(0, COLOR_GANANCIAS);
        renderer.setSeriesPaint(1, COLOR_GASTOS);
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setBarPainter(new GradientBarPainter(0.10, 0.12, 0.85));
        renderer.setMaximumBarWidth(0.05);
        renderer.setItemMargin(0.15);

        renderer.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator("{2}", new java.text.DecimalFormat("$#,##0")));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelPaint(COLOR_TEXTO);
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
    }

    private void aplicarEstiloLineas(JFreeChart chart) {
        estiloBase(chart);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setSeriesPaint(0, COLOR_GANANCIAS);
        renderer.setSeriesPaint(1, COLOR_GASTOS);
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesStroke(1, new BasicStroke(2.5f));
    }

        private void aplicarEstiloArea(JFreeChart chart) {
        estiloBase(chart);
        AreaRenderer renderer = (AreaRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setSeriesPaint(0, new Color(76, 175, 80, 130));
        renderer.setSeriesPaint(1, new Color(229, 62, 90, 130));
        renderer.setSeriesOutlinePaint(0, COLOR_GANANCIAS);
        renderer.setSeriesOutlinePaint(1, COLOR_GASTOS);
        renderer.setSeriesOutlineStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesOutlineStroke(1, new BasicStroke(2.5f));
    }
}
