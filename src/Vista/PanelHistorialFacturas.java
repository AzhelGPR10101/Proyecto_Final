package Vista;

import Controladores.ControladorFactura;
import Modelo.Factura;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultCellEditor;
import java.awt.Component;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PanelHistorialFacturas extends javax.swing.JPanel {

    private final ControladorFactura controladorFactura = new ControladorFactura();
    private List<Factura> listaFacturas;

    public PanelHistorialFacturas() {
        initComponents();
        configurarTablaBotones();
        cargarFacturas();
        componentes.EstiloTablaKrypton.aplicar(JThistorial);
        configurarTablaBotones();

        FiltroHistorial.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                aplicarFiltro();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                aplicarFiltro();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                aplicarFiltro();
            }
        });

        jcbTipoCliente.addActionListener(e -> aplicarFiltro());

        javax.swing.JButton btnExportarPdf = new javax.swing.JButton("Exportar / Enviar PDF");
        btnExportarPdf.setBackground(new java.awt.Color(80, 40, 110));
        btnExportarPdf.setForeground(java.awt.Color.WHITE);
        btnExportarPdf.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 12));
        btnExportarPdf.addActionListener(e -> exportarFacturaSeleccionada());
        add(btnExportarPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 70, 220, 30));
    }

    private void exportarVentasExcel() {
        if (listaFacturas == null || listaFacturas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ventas para exportar.", "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String[] encabezados = {"No. Factura", "Fecha", "Cliente", "Empleado", "Metodo Pago", "Total", "Estado"};
            List<String[]> filas = new java.util.ArrayList<>();
            for (Factura f : listaFacturas) {
                String nombreCliente = f.getCliente() != null ? (f.getCliente().getNombre() + " " + f.getCliente().getApellido()) : "";
                filas.add(new String[]{
                    f.getNumFactura(),
                    f.getFecha(),
                    nombreCliente,
                    f.getNombreEmpleado(),
                    f.getMetodoPago(),
                    String.format("%.2f", f.getTotal()),
                    f.getEstadoSri()
                });
            }
            String ruta = System.getProperty("user.home") + java.io.File.separator + "Ventas.xlsx";
            Reportes.GeneradorExcel.generar(ruta, "Ventas", encabezados, filas);
            JOptionPane.showMessageDialog(this, "Excel generado en:\n" + ruta);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarFacturaSeleccionada() {
        int row = JThistorial.getSelectedRow();
        if (row < 0 || listaFacturas == null || row >= listaFacturas.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura de la tabla primero.", "Ninguna factura seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Factura f = listaFacturas.get(row);
        try {
            String nombreArchivo = "Factura_" + f.getNumFactura() + ".pdf";
            String ruta = System.getProperty("user.home") + java.io.File.separator + nombreArchivo;
            Reportes.GeneradorPDFFactura.generar(f, ruta);

            String correoCliente = f.getCliente() != null ? f.getCliente().getCorreo() : null;
            if (correoCliente != null && !correoCliente.trim().isEmpty() && Correo.EmailService.hayCorreoConfigurado()) {
                Correo.EmailService.enviarFacturaPorCorreo(correoCliente, f.getCliente().getNombre(), f.getNumFactura(), ruta);
                JOptionPane.showMessageDialog(this, "PDF generado en:\n" + ruta + "\n\nEnviado también al correo del cliente.");
            } else {
                JOptionPane.showMessageDialog(this, "PDF generado en:\n" + ruta);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo generar/enviar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarFacturas();
        }
    }

    private void cargarFacturas() {
        listaFacturas = controladorFactura.listarTodas();
        actualizarTabla();
    }

    private void aplicarFiltro() {
        String texto = FiltroHistorial.getText();
        String tipoCliente = (String) jcbTipoCliente.getSelectedItem();
        listaFacturas = controladorFactura.filtrarFacturas(texto, tipoCliente);
        actualizarTabla();
    }

    public void actualizarTabla() {
        if (JThistorial == null) {
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) JThistorial.getModel();
        modelo.setRowCount(0);
        if (listaFacturas == null) {
            return;
        }
        for (Factura f : listaFacturas) {
            modelo.addRow(new Object[]{
                f.getNumFactura(),
                f.getFecha(),
                f.getCliente() != null ? (f.getCliente().getNombre() + " " + f.getCliente().getApellido()) : "",
                f.getNombreEmpleado(),
                f.getMetodoPago(),
                String.format("$%.2f", f.getTotal()),
                f.getEstadoSri(),
                "Ver Detalle"
            });
        }
    }

    private void configurarTablaBotones() {
        JThistorial.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        JThistorial.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new javax.swing.JCheckBox(), this));
        JThistorial.setRowHeight(35);
    }

    class ButtonRenderer extends javax.swing.JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new java.awt.Color(165, 24, 139));
            setForeground(java.awt.Color.WHITE);
            setFocusPainted(false);
            setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Ver Detalle" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected javax.swing.JButton button;
        private String label;
        private boolean isPushed;
        private PanelHistorialFacturas panel;

        public ButtonEditor(javax.swing.JCheckBox checkBox, PanelHistorialFacturas panel) {
            super(checkBox);
            this.panel = panel;
            button = new javax.swing.JButton();
            button.setOpaque(true);
            button.setBackground(new java.awt.Color(165, 24, 139));
            button.setForeground(java.awt.Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 12));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "Ver Detalle" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = JThistorial.getSelectedRow();
                if (row >= 0 && panel.listaFacturas != null && row < panel.listaFacturas.size()) {
                    Factura f = panel.listaFacturas.get(row);
                    java.util.List<Modelo.DetalleFactura> detalles = new DAO.FacturaDAO().obtenerDetallePorFactura(f.getIdFactura());
                    Modelo.MovimientoFinanciero movimiento = new Modelo.MovimientoFinanciero(
                            Modelo.MovimientoFinanciero.TIPO_INGRESO, f.getFecha(), f.getNumFactura(), "Factura",
                            f.getNombreEmpleado(), f.getMetodoPago(), f.getTotal(), f.getIdFactura());
                    java.awt.Frame ventanaPadre = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(panel);
                    DialogDetalleFactura dialog = new DialogDetalleFactura(ventanaPadre, movimiento, detalles);
                    dialog.setLocationRelativeTo(panel);
                    dialog.setVisible(true);
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlblHistorial = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        FiltroHistorial =  new componentes.TextFieldModerno();
        jLabel1 = new javax.swing.JLabel();
        jcbTipoCliente = new componentes.ComboBoxModerno();
        btnExportarPDF = new componentes.BotonModerno();
        BtnExportarExcel = new componentes.BotonModerno();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        JThistorial = new javax.swing.JTable();

        setBackground(new java.awt.Color(28, 9, 40));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlblHistorial.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        jlblHistorial.setForeground(new java.awt.Color(255, 255, 255));
        jlblHistorial.setText("Historial Facturas");
        add(jlblHistorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FiltroHistorial.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltroHistorial.setForeground(new java.awt.Color(255, 255, 255));
        FiltroHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltroHistorialActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltroHistorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 270, 40));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jcbTipoCliente.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbTipoCliente.setForeground(new java.awt.Color(255, 255, 255));
        jcbTipoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Consumidor Final", "Con Datos", "Fecha de factura" }));
        panelRedondo1.add(jcbTipoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 320, 30));

        btnExportarPDF.setText("Exportar / Enviar PDF");
        btnExportarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarPDFActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnExportarPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, 210, 40));

        BtnExportarExcel.setText("Exportar a EXCEL");
        BtnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnExportarExcelActionPerformed(evt);
            }
        });
        panelRedondo1.add(BtnExportarExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 20, 170, 40));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 1280, 80));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JThistorial.setBackground(new java.awt.Color(28, 9, 40));
        JThistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO. Factura", "Fecha", "Cliente", "Empleado", "Metodo Pago", "Total", "Estado", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(JThistorial);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1670, 740));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 1730, 800));
    }// </editor-fold>//GEN-END:initComponents

    private void FiltroHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltroHistorialActionPerformed

    }//GEN-LAST:event_FiltroHistorialActionPerformed

    private void btnExportarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarPDFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportarPDFActionPerformed

    private void BtnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnExportarExcelActionPerformed
        exportarVentasExcel();
    }//GEN-LAST:event_BtnExportarExcelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnExportarExcel;
    private javax.swing.JTextField FiltroHistorial;
    private javax.swing.JTable JThistorial;
    private componentes.BotonModerno btnExportarPDF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jcbTipoCliente;
    private javax.swing.JLabel jlblHistorial;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables
}
