package componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class EstiloTablaKrypton {

    public static final Color FONDO_TABLA   = new Color(28, 9, 40);
    public static final Color FONDO_FILA_ALT = new Color(36, 16, 54);
    public static final Color FONDO_HEADER  = new Color(24, 8, 36);
    public static final Color TEXTO         = Color.WHITE;
    public static final Color TEXTO_HEADER  = new Color(220, 210, 230);
    public static final Color BORDE_SUAVE   = new Color(60, 40, 80);
    public static final Color BORDE_ROJO    = new Color(220, 20, 60);
    public static final Color SELECCION     = new Color(60, 30, 80);

    private EstiloTablaKrypton() { }

    public static void aplicar(JTable tabla) {
        if (tabla == null) return;

        tabla.setBackground(FONDO_TABLA);
        tabla.setForeground(TEXTO);
        tabla.setFont(new Font("Lucida Bright", Font.BOLD, 18));
        tabla.setRowHeight(52);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(SELECCION);
        tabla.setSelectionForeground(TEXTO);
        tabla.setFillsViewportHeight(true);
        tabla.setFocusable(false);

        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setBackground(FONDO_HEADER);
            header.setForeground(TEXTO_HEADER);
            header.setFont(new Font("Lucida Bright", Font.BOLD, 16));
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 48));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDE_SUAVE));
            header.setReorderingAllowed(false);
            header.setResizingAllowed(false);
            header.setDefaultRenderer(new HeaderRenderer());
        }

        RowRenderer renderer = new RowRenderer();
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    public static class RowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE_SUAVE),
                    BorderFactory.createEmptyBorder(0, 22, 0, 22)
            ));
            setHorizontalAlignment(SwingConstants.LEFT);
            setVerticalAlignment(SwingConstants.CENTER);
            setFont(new Font("Lucida Bright", Font.BOLD, 18));
            setForeground(TEXTO);

            if (isSelected) {
                setBackground(SELECCION);
            } else {
                setBackground(row % 2 == 0 ? FONDO_TABLA : FONDO_FILA_ALT);
            }
            return c;
        }
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.LEFT);
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setFont(new Font("Lucida Bright", Font.BOLD, 16));
            setBackground(FONDO_HEADER);
            setForeground(TEXTO_HEADER);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, BORDE_SUAVE),
                    BorderFactory.createEmptyBorder(0, 22, 0, 22)
            ));
            return this;
        }
    }

    public static class RenderBotonRedondeado extends JButton implements TableCellRenderer {
        private final String textoDefecto;

        public RenderBotonRedondeado(String textoDefecto) {
            this.textoDefecto = textoDefecto;
            configurarBoton(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? textoDefecto : value.toString());
            setBackground(table.getModel().getRowCount() > 0 && row % 2 == 0 ? FONDO_TABLA : FONDO_FILA_ALT);
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            pintarFondoRedondeado(this, g);
            super.paintComponent(g);
        }
    }

    public static void configurarBoton(JButton boton) {
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setForeground(TEXTO);
        boton.setFont(new Font("Lucida Bright", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    public static void pintarFondoRedondeado(JButton boton, Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(FONDO_HEADER);
        g2.fillRoundRect(0, 0, boton.getWidth() - 1, boton.getHeight() - 1, 14, 14);

        g2.setColor(BORDE_ROJO);
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawRoundRect(1, 1, boton.getWidth() - 3, boton.getHeight() - 3, 14, 14);

        g2.dispose();
    }
}