package Vista.FACTURACION;

public class PanelDialogDetalleFactura extends javax.swing.JPanel {

    private javax.swing.JDialog ventana;

    public PanelDialogDetalleFactura() {
        initComponents();
        componentes.EstiloTablaKrypton.aplicar(tablaDetalle);
        tablaDetalle.setRowHeight(28);
        scrollTabla.getViewport().setBackground(componentes.EstiloTablaKrypton.FONDO_TABLA);
        btnCerrar.addActionListener(e -> ventana.dispose());
    }

    public static void mostrar(java.awt.Frame parent, Modelo.MovimientoFinanciero movimiento,
            java.util.List<Modelo.DetalleFactura> detalles) {
        PanelDialogDetalleFactura panel = new PanelDialogDetalleFactura();
        panel.cargarDatos(movimiento, detalles);

        javax.swing.JDialog ventana = new javax.swing.JDialog(parent, "Detalle factura " + safe(movimiento.getReferencia()), true);
        panel.ventana = ventana;
        ventana.add(panel);
        ventana.setResizable(false);
        ventana.pack();
        ventana.setLocationRelativeTo(parent);
        ventana.setVisible(true);
    }

    private static String safe(String texto) {
        return texto != null ? texto : "";
    }

    private void cargarDatos(Modelo.MovimientoFinanciero movimiento, java.util.List<Modelo.DetalleFactura> detalles) {
        lblTitulo.setText("Detalle factura " + safe(movimiento.getReferencia()));
        lblSubtitulo.setText(safe(movimiento.getDetalle()) + " · " + safe(movimiento.getFecha())
                + " · " + safe(movimiento.getEmpleado()));

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
                null, new String[]{"Producto", "Cant", "Subtotal"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (detalles != null) {
            for (Modelo.DetalleFactura d : detalles) {
                modelo.addRow(new Object[]{
                    d.getNombreProducto(),
                    d.getCantidad(),
                    String.format("$%.2f", d.getSubtotal())
                });
            }
        }
        tablaDetalle.setModel(modelo);

        lblTotal.setText("Total    " + String.format("$%.2f", movimiento.getMonto()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        scrollTabla = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Detalle factura");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 420, 26));

        lblSubtitulo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(200, 190, 210));
        lblSubtitulo.setText(" ");
        add(lblSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 45, 420, 20));

        tablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Producto", "Cant", "Subtotal" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTabla.setViewportView(tablaDetalle);

        add(scrollTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 72, 420, 180));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 255, 255));
        lblTotal.setText("Total    $0.00");
        add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 265, 230, 30));

        btnCerrar.setBackground(new java.awt.Color(165, 24, 139));
        btnCerrar.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrar.setText("Cerrar");
        add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 263, 110, 34));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaDetalle;
    // End of variables declaration//GEN-END:variables
}
