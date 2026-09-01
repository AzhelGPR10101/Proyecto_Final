package Vista.REPORTES;

import Controladores.ControladorReporte;
import Controladores.ControladorReporte.ResultadoReporte;
import Modelo.DetalleFactura;
import Modelo.MovimientoFinanciero;

import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PanelReporte extends javax.swing.JPanel {

    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final int COLUMNA_ACCION = 7;

    private final ControladorReporte controladorReporte = new ControladorReporte();

    private final List<MovimientoFinanciero> movimientosMostrados = new ArrayList<>();
    private componentes.TextFieldModerno txtBuscarTexto;

    public PanelReporte() {
        initComponents();

        componentes.EstiloTablaKrypton.aplicar(jTable1);

        jTable1.getColumnModel().getColumn(COLUMNA_ACCION).setCellRenderer(
                new componentes.EstiloTablaKrypton.RenderBotonRedondeado("Ver detalle"));

        jTable1.getColumnModel().getColumn(COLUMNA_ACCION).setCellEditor(
                new BotonVerDetalleEditor(new javax.swing.JCheckBox(), jTable1));

        lbltotalf.setText("Total Ingresos");
        lbltotalv.setText("Total Egresos");
        lblpromedio.setText("Balance Neto");
        lblcedula.setText("CLIENTE");

        establecerFechasPorDefecto();
        cargarCombos();
        aplicarFiltro();

        BtnexpExcel.addActionListener(e -> exportarReporteExcel());

        txtBuscarTexto = new componentes.TextFieldModerno();
        add(txtBuscarTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 190, 220, 30));
        // Filtrado en tiempo real: igual que en Productos/Clientes, cada
        // tecla vuelve a aplicar el filtro sin necesidad de un boton "Buscar".
        txtBuscarTexto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && jTable1 != null) {
            aplicarFiltro();
        }
    }

    private void establecerFechasPorDefecto() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        String hoy = sdf.format(new java.util.Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        String hace30Dias = sdf.format(cal.getTime());

        txtFechaDesde.setText(hace30Dias);
        txtFechaHasta.setText(hoy);
    }

    private void cargarCombos() {
        cbCedulaCliente.setModel(new javax.swing.DefaultComboBoxModel<>(
                controladorReporte.listarClientesParaFiltro().toArray(new String[0])));
        cbUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(
                controladorReporte.listarEmpleadosParaFiltro().toArray(new String[0])));

        cbCedulaCliente.addActionListener(e -> aplicarFiltro());
        cbUsuario.addActionListener(e -> aplicarFiltro());
    }

    private void aplicarFiltro() {
        String desde = txtFechaDesde.getText();
        String hasta = txtFechaHasta.getText();

        String error = controladorReporte.validarRango(desde, hasta);
        if (error != null) {
            javax.swing.JOptionPane.showMessageDialog(this, error, "Fecha inválida",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cliente = (String) cbCedulaCliente.getSelectedItem();
        String empleado = (String) cbUsuario.getSelectedItem();

        ResultadoReporte resultado = controladorReporte.filtrar(
                desde, hasta, ControladorReporte.OPCION_TODOS, cliente, empleado);

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
        movimientosMostrados.clear();

        String textoBusqueda = txtBuscarTexto == null ? "" : txtBuscarTexto.getText().trim().toLowerCase();

        for (MovimientoFinanciero m : resultado.movimientos) {
            if (!textoBusqueda.isEmpty()) {
                String referencia = m.getReferencia() == null ? "" : m.getReferencia().toLowerCase();
                String detalle = m.getDetalle() == null ? "" : m.getDetalle().toLowerCase();
                String empleadoMov = m.getEmpleado() == null ? "" : m.getEmpleado().toLowerCase();
                String tipoMov = m.getTipo() == null ? "" : m.getTipo().toLowerCase();
                boolean coincide = referencia.contains(textoBusqueda) || detalle.contains(textoBusqueda)
                        || empleadoMov.contains(textoBusqueda) || tipoMov.contains(textoBusqueda);
                if (!coincide) {
                    continue;
                }
            }
            modelo.addRow(new Object[]{
                m.getTipo(),
                m.getFecha(),
                m.getReferencia(),
                m.getDetalle(),
                m.getMetodoPago(),
                m.getEmpleado(),
                String.format("$%.2f", m.getMonto()),
                m.esIngreso() ? "Ver detalle" : "-"
            });
            movimientosMostrados.add(m);
        }

        if (textoBusqueda.isEmpty()) {
            txtCliente.setText(String.format("$ %,.2f", resultado.totalIngresos));
            txtCliente5.setText(String.format("$ %,.2f", resultado.totalEgresos));
            txtCliente2.setText(String.format("$ %,.2f", resultado.balanceNeto));
        } else {
            // Con busqueda activa, los totales reflejan solo lo que quedo visible.
            double ingresos = 0, egresos = 0;
            for (MovimientoFinanciero m : movimientosMostrados) {
                if (m.esIngreso()) ingresos += m.getMonto(); else egresos += m.getMonto();
            }
            txtCliente.setText(String.format("$ %,.2f", ingresos));
            txtCliente5.setText(String.format("$ %,.2f", egresos));
            txtCliente2.setText(String.format("$ %,.2f", ingresos - egresos));
        }
    }

    private void mostrarDetalle(MovimientoFinanciero movimiento) {
        if (!movimiento.esIngreso()) {
            return;
        }
        List<DetalleFactura> detalles = controladorReporte.obtenerDetallePorFactura(movimiento.getIdFactura());
        java.awt.Frame ventanaPadre = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        Vista.FACTURACION.DialogDetalleFactura dialog = new Vista.FACTURACION.DialogDetalleFactura(ventanaPadre, movimiento, detalles);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void exportarReporteExcel() {
    if (movimientosMostrados.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "No hay movimientos para exportar.", "Sin datos", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    try {
        String[] encabezados = {"Tipo", "Fecha", "Referencia", "Detalle", "Metodo Pago", "Empleado", "Monto"};
        List<String[]> filas = new ArrayList<>();
        for (MovimientoFinanciero m : movimientosMostrados) {
            filas.add(new String[]{
                m.getTipo(),
                m.getFecha(),
                m.getReferencia(),
                m.getDetalle(),
                m.getMetodoPago(),
                m.getEmpleado(),
                String.format("%.2f", m.getMonto())
            });
        }
        String ruta = Reportes.CarpetaExportacion.obtenerRuta("Reporte.xlsx");
        Reportes.GeneradorExcel.generar(ruta, "Reporte", encabezados, filas);
        javax.swing.JOptionPane.showMessageDialog(this, "Excel generado en:\n" + ruta);
    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "No se pudo exportar: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelRedondo2 = new componentes.PanelRedondo();
        lblusuario = new javax.swing.JLabel();
        lblhasta = new javax.swing.JLabel();
        lblpromedio = new javax.swing.JLabel();
        lblcedula = new javax.swing.JLabel();
        txtFechaDesde = new componentes.TextFieldModerno();
        txtFechaHasta = new componentes.TextFieldModerno();
        txtCliente2 = new componentes.TextFieldModerno();
        cbCedulaCliente = new componentes.ComboBoxModerno();
        txtCliente5 = new componentes.TextFieldModerno();
        lbldesde = new javax.swing.JLabel();
        lbltotalf = new javax.swing.JLabel();
        lbltotalv = new javax.swing.JLabel();
        cbUsuario = new componentes.ComboBoxModerno();
        txtCliente = new componentes.TextFieldModerno();
        BtnexpExcel = new componentes.BotonModerno();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("REPORTE DE VENTAS");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, -1, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(31, 10, 48));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tipo", "Fecha", "Referencia", "Detalle", "Metodo Pago", "Empleado", "Monto", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        panelRedondo1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1620, 450));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 470, 1680, 510));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblusuario.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblusuario.setForeground(new java.awt.Color(255, 255, 255));
        lblusuario.setText("USUARIO:");
        panelRedondo2.add(lblusuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 40, -1, -1));

        lblhasta.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblhasta.setForeground(new java.awt.Color(255, 255, 255));
        lblhasta.setText("HASTA:");
        panelRedondo2.add(lblhasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, -1, -1));

        lblpromedio.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblpromedio.setForeground(new java.awt.Color(255, 255, 255));
        lblpromedio.setText("Balance Neto");
        panelRedondo2.add(lblpromedio, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 130, -1, -1));

        lblcedula.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblcedula.setForeground(new java.awt.Color(255, 255, 255));
        lblcedula.setText("CLIENTE:");
        panelRedondo2.add(lblcedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, -1, -1));

        txtFechaDesde.setBackground(new java.awt.Color(31, 10, 60));
        txtFechaDesde.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtFechaDesde.setForeground(new java.awt.Color(255, 255, 255));
        txtFechaDesde.setText("--/--/----");
        txtFechaDesde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaDesdeActionPerformed(evt);
            }
        });
        txtFechaDesde.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFechaDesdeKeyTyped(evt);
            }
        });
        panelRedondo2.add(txtFechaDesde, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 150, 40));

        txtFechaHasta.setBackground(new java.awt.Color(31, 10, 60));
        txtFechaHasta.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtFechaHasta.setForeground(new java.awt.Color(255, 255, 255));
        txtFechaHasta.setText("--/--/----");
        txtFechaHasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaHastaActionPerformed(evt);
            }
        });
        txtFechaHasta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFechaHastaKeyTyped(evt);
            }
        });
        panelRedondo2.add(txtFechaHasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 150, 40));

        txtCliente2.setEditable(false);
        txtCliente2.setBackground(new java.awt.Color(31, 10, 60));
        txtCliente2.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtCliente2.setForeground(new java.awt.Color(255, 255, 255));
        txtCliente2.setText("$67.45");
        txtCliente2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliente2ActionPerformed(evt);
            }
        });
        txtCliente2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCliente2KeyTyped(evt);
            }
        });
        panelRedondo2.add(txtCliente2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 180, 410, 40));

        cbCedulaCliente.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 18)); // NOI18N
        cbCedulaCliente.setForeground(new java.awt.Color(255, 255, 255));
        cbCedulaCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecionar Cliente" }));
        cbCedulaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCedulaClienteActionPerformed(evt);
            }
        });
        panelRedondo2.add(cbCedulaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 30, 230, 40));

        txtCliente5.setEditable(false);
        txtCliente5.setBackground(new java.awt.Color(31, 10, 60));
        txtCliente5.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtCliente5.setForeground(new java.awt.Color(255, 255, 255));
        txtCliente5.setText("$ 3,214.50");
        txtCliente5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliente5ActionPerformed(evt);
            }
        });
        txtCliente5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCliente5KeyTyped(evt);
            }
        });
        panelRedondo2.add(txtCliente5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 180, 410, 40));

        lbldesde.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lbldesde.setForeground(new java.awt.Color(255, 255, 255));
        lbldesde.setText("DESDE:");
        panelRedondo2.add(lbldesde, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, -1));

        lbltotalf.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lbltotalf.setForeground(new java.awt.Color(255, 255, 255));
        lbltotalf.setText("Total Facturas ");
        panelRedondo2.add(lbltotalf, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, -1, -1));

        lbltotalv.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lbltotalv.setForeground(new java.awt.Color(255, 255, 255));
        lbltotalv.setText("Total Vendido");
        panelRedondo2.add(lbltotalv, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, -1, -1));

        cbUsuario.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 18)); // NOI18N
        cbUsuario.setForeground(new java.awt.Color(255, 255, 255));
        cbUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecionar Usuario" }));
        cbUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbUsuarioActionPerformed(evt);
            }
        });
        panelRedondo2.add(cbUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 30, 260, 40));

        txtCliente.setEditable(false);
        txtCliente.setBackground(new java.awt.Color(31, 10, 60));
        txtCliente.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        txtCliente.setForeground(new java.awt.Color(255, 255, 255));
        txtCliente.setText("00");
        txtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteActionPerformed(evt);
            }
        });
        panelRedondo2.add(txtCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 350, 40));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, 1680, 250));

        BtnexpExcel.setText("Exportar a EXCEL");
        add(BtnexpExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 90, 200, 50));
    }// </editor-fold>//GEN-END:initComponents

    private void txtFechaDesdeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaDesdeKeyTyped
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (txtFechaDesde.getText().length() == 10) {
                aplicarFiltro();
            }
        });
    }//GEN-LAST:event_txtFechaDesdeKeyTyped

    private void txtFechaHastaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaHastaKeyTyped
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (txtFechaHasta.getText().length() == 10) {
                aplicarFiltro();
            }
        });
    }//GEN-LAST:event_txtFechaHastaKeyTyped

    private void txtCliente5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliente5KeyTyped

    }//GEN-LAST:event_txtCliente5KeyTyped

    private void txtCliente5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliente5ActionPerformed

    }//GEN-LAST:event_txtCliente5ActionPerformed

    private void txtCliente2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliente2KeyTyped

    }//GEN-LAST:event_txtCliente2KeyTyped

    private void txtCliente2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliente2ActionPerformed

    }//GEN-LAST:event_txtCliente2ActionPerformed

    private void txtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteActionPerformed

    private void cbCedulaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCedulaClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCedulaClienteActionPerformed

    private void cbUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbUsuarioActionPerformed

    private void txtFechaHastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaHastaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaHastaActionPerformed

    private void txtFechaDesdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaDesdeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaDesdeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnexpExcel;
    private javax.swing.JComboBox<String> cbCedulaCliente;
    private javax.swing.JComboBox<String> cbUsuario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblcedula;
    private javax.swing.JLabel lbldesde;
    private javax.swing.JLabel lblhasta;
    private javax.swing.JLabel lblpromedio;
    private javax.swing.JLabel lbltotalf;
    private javax.swing.JLabel lbltotalv;
    private javax.swing.JLabel lblusuario;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCliente2;
    private javax.swing.JTextField txtCliente5;
    private javax.swing.JTextField txtFechaDesde;
    private javax.swing.JTextField txtFechaHasta;
    // End of variables declaration//GEN-END:variables

    private class BotonVerDetalleEditor extends javax.swing.DefaultCellEditor {

        private final javax.swing.JButton boton;
        private String etiqueta;
        private boolean clickeado;
        private final javax.swing.JTable tabla;

        public BotonVerDetalleEditor(javax.swing.JCheckBox checkBox, javax.swing.JTable tabla) {
            super(checkBox);
            this.tabla = tabla;
            boton = new javax.swing.JButton() {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    componentes.EstiloTablaKrypton.pintarFondoRedondeado(this, g);
                    super.paintComponent(g);
                }
            };
            componentes.EstiloTablaKrypton.configurarBoton(boton);
            boton.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value,
                boolean isSelected, int row, int column) {
            etiqueta = (value == null) ? "" : value.toString();
            boton.setText(etiqueta);
            clickeado = true;
            return boton;
        }

        @Override
        public Object getCellEditorValue() {
            if (clickeado) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0 && fila < movimientosMostrados.size()) {
                    mostrarDetalle(movimientosMostrados.get(fila));
                }
            }
            clickeado = false;
            return etiqueta;
        }

        @Override
        public boolean stopCellEditing() {
            clickeado = false;
            return super.stopCellEditing();
        }
    }
}
