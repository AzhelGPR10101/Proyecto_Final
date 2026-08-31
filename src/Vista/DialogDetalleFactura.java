package Vista;

import Modelo.DetalleFactura;
import Modelo.MovimientoFinanciero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DialogDetalleFactura extends JDialog {

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Color COLOR_TEXTO_SUAVE = new Color(200, 190, 210);
    private static final Color COLOR_BOTON = new Color(165, 24, 139);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TOTAL = new Font("Segoe UI", Font.BOLD, 18);

    public DialogDetalleFactura(Frame parent, MovimientoFinanciero movimiento, List<DetalleFactura> detalles) {
        super(parent, "Detalle factura " + safe(movimiento.getReferencia()), true);
        initComponents(movimiento, detalles);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private static String safe(String texto) {
        return texto != null ? texto : "";
    }

    private void initComponents(MovimientoFinanciero movimiento, List<DetalleFactura> detalles) {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 12));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Detalle factura " + safe(movimiento.getReferencia()));
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel(safe(movimiento.getDetalle()) + " · " + safe(movimiento.getFecha())
                + " · " + safe(movimiento.getEmpleado()));
        subtitulo.setFont(FONT_SUBTITULO);
        subtitulo.setForeground(COLOR_TEXTO_SUAVE);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(subtitulo);
        add(encabezado, BorderLayout.NORTH);

        String[] columnas = {"Producto", "Cant", "Subtotal"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (detalles != null) {
            for (DetalleFactura d : detalles) {
                modelo.addRow(new Object[]{
                    d.getNombreProducto(),
                    d.getCantidad(),
                    String.format("$%.2f", d.getSubtotal())
                });
            }
        }
        JTable tabla = new JTable(modelo);
        componentes.EstiloTablaKrypton.aplicar(tabla);
        tabla.setRowHeight(28);
        tabla.setPreferredScrollableViewportSize(new Dimension(420, Math.min(180, 28 * Math.max(1, modelo.getRowCount()))));
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(componentes.EstiloTablaKrypton.FONDO_TABLA);
        add(scroll, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JLabel lblTotal = new JLabel("Total    " + String.format("$%.2f", movimiento.getMonto()));
        lblTotal.setFont(FONT_TOTAL);
        lblTotal.setForeground(COLOR_TEXTO);
        pie.add(lblTotal, BorderLayout.WEST);

        JButton btnCerrar = new componentes.BotonModerno();
        btnCerrar.setText("Cerrar");
        btnCerrar.setForeground(COLOR_TEXTO);
        btnCerrar.setBackground(COLOR_BOTON);
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBoton.setOpaque(false);
        panelBoton.add(btnCerrar);
        pie.add(panelBoton, BorderLayout.EAST);

        add(pie, BorderLayout.SOUTH);
    }
}