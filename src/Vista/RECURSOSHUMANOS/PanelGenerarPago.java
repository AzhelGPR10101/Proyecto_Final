package Vista.RECURSOSHUMANOS;

import Controladores.EmpleadoControlador;
import Modelo.Empleado;

public class PanelGenerarPago extends javax.swing.JPanel {

    public PanelGenerarPago() {
        initComponents();
        compactarEspaciado();
        componentes.EstiloTablaKrypton.aplicar(jTable1);
        txtMonto.setEditable(false);
        txtTotal.setEditable(false);
        cargarEmpleados();
        cargarTabla();
        actualizarSueldoDesdeSeleccion();
        FiltadoEmpleados.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { cargarTabla(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { cargarTabla(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { cargarTabla(); }
        });
        jcbOrden.addActionListener(e -> cargarTabla());

        cmbEmpleado.addActionListener(e -> actualizarSueldoDesdeSeleccion());

        javax.swing.event.DocumentListener recalculoListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { recalcularTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { recalcularTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { recalcularTotal(); }
        };
        txtBonificacion.getDocument().addDocumentListener(recalculoListener);
        txtDescuento.getDocument().addDocumentListener(recalculoListener);
    }

    private void compactarEspaciado() {
        lblTitulo.setBounds(60, 15, 660, 30);
        panelRedondo1.setBounds(60, 60, 620, 780);
        panelRedondo3.setBounds(700, 60, 770, 70);
        panelRedondo2.setBounds(700, 140, 1160, 540);
    }

    private Empleado obtenerEmpleadoSeleccionado() {
        Object seleccionado = cmbEmpleado.getSelectedItem();
        return (seleccionado instanceof Empleado) ? (Empleado) seleccionado : null;
    }

    private void actualizarSueldoDesdeSeleccion() {
        Empleado empleado = obtenerEmpleadoSeleccionado();
        if (empleado != null) {
            txtMonto.setText(String.format("$%.2f", empleado.getSueldo()));
        } else {
            txtMonto.setText("$0.00");
        }
        recalcularTotal();
    }

    private double parsearMontoAdicional(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return 0.0;
        }
        try {
            double valor = Double.parseDouble(texto.trim().replace("$", ""));
            return valor < 0 ? 0.0 : valor;
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private void recalcularTotal() {
        Empleado empleado = obtenerEmpleadoSeleccionado();
        double sueldo = empleado != null ? empleado.getSueldo() : 0.0;
        double bonificacion = parsearMontoAdicional(txtBonificacion.getText());
        double descuento = parsearMontoAdicional(txtDescuento.getText());
        double total = sueldo + bonificacion - descuento;
        txtTotal.setText(String.format("$%.2f", total));
    }

    private void cargarTabla() {
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
                new String[]{"Numero", "Nombre", "Apellido", "Cedula", "Area", "Sueldo", "Fecha_Contratacion"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        java.util.List<Empleado> lista = new java.util.ArrayList<>(EmpleadoControlador.listarEmpleados());

        String texto = FiltadoEmpleados.getText() == null ? "" : FiltadoEmpleados.getText().trim().toLowerCase();
        if (!texto.isEmpty()) {
            lista.removeIf(emp -> !(emp.getNombres() + " " + emp.getApellidos() + " " + emp.getCedula())
                    .toLowerCase().contains(texto));
        }

        Object orden = jcbOrden.getSelectedItem();
        if ("Nombre A-Z".equals(orden)) {
            lista.sort(java.util.Comparator.comparing(Empleado::getNombres, String.CASE_INSENSITIVE_ORDER));
        } else if ("Nombre Z-A".equals(orden)) {
            lista.sort(java.util.Comparator.comparing(Empleado::getNombres, String.CASE_INSENSITIVE_ORDER).reversed());
        } else if ("Fecha_Contratacion".equals(orden)) {
            lista.sort(java.util.Comparator.comparing(Empleado::getFechaContratacion));
        }

        int contador = 1;
        for (Empleado emp : lista) {
            modelo.addRow(new Object[]{
                contador++,
                emp.getNombres(),
                emp.getApellidos(),
                emp.getCedula(),
                emp.getRol(),
                emp.getSueldo(),
                emp.getFechaContratacion()
            });
        }
        jTable1.setModel(modelo);
    }

    private void cargarEmpleados() {
        cmbEmpleado.removeAllItems();
        java.util.List<Empleado> empleados = EmpleadoControlador.listarEmpleados();
        for (Empleado empleado : empleados) {
            cmbEmpleado.addItem(empleado);
        }
    }

    private void limpiarFormulario() {
        if (cmbPeriodo.getItemCount() > 0) {
            cmbPeriodo.setSelectedIndex(0);
        }
        txtBonificacion.setText("0.00");
        txtDescuento.setText("0.00");
        txtObservaciones.setText("");
        if (cmbEmpleado.getItemCount() > 0) {
            cmbEmpleado.setSelectedIndex(0);
        }
        actualizarSueldoDesdeSeleccion();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        lblEmpleado = new javax.swing.JLabel();
        cmbEmpleado = new componentes.ComboBoxModerno();
        lblPeriodo = new javax.swing.JLabel();
        lblMonto = new javax.swing.JLabel();
        txtMonto = new componentes.TextFieldModerno();
        lblBonificacion = new javax.swing.JLabel();
        txtBonificacion = new componentes.TextFieldModerno();
        lblDescuento = new javax.swing.JLabel();
        txtDescuento = new componentes.TextFieldModerno();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new componentes.TextFieldModerno();
        lblObservaciones = new javax.swing.JLabel();
        txtObservaciones = new componentes.TextFieldModerno();
        btnGuardarPago = new componentes.BotonModerno();
        btnLimpiar = new componentes.BotonModerno();
        cmbPeriodo = new javax.swing.JComboBox<>();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelRedondo3 = new componentes.PanelRedondo();
        jLabel1 = new javax.swing.JLabel();
        FiltadoEmpleados = new componentes.TextFieldModerno();
        jcbOrden = new componentes.ComboBoxModerno();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("GENERAR PAGO A EMPLEADO");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 660, 30));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        lblEmpleado.setText("Empleado:");
        panelRedondo1.add(lblEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 200, 25));

        panelRedondo1.add(cmbEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 500, 35));

        lblPeriodo.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblPeriodo.setForeground(new java.awt.Color(255, 255, 255));
        lblPeriodo.setText("Periodo (ej: Octubre 2026):");
        panelRedondo1.add(lblPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 300, 25));

        lblMonto.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblMonto.setForeground(new java.awt.Color(255, 255, 255));
        lblMonto.setText("Sueldo base (bloqueado):");
        panelRedondo1.add(lblMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 300, 25));

        txtMonto.setEditable(false);
        panelRedondo1.add(txtMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 500, 35));

        lblBonificacion.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblBonificacion.setForeground(new java.awt.Color(255, 255, 255));
        lblBonificacion.setText("Bonificaciones:");
        panelRedondo1.add(lblBonificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 370, 300, 25));

        txtBonificacion.setText("0.00");
        panelRedondo1.add(txtBonificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 400, 500, 35));

        lblDescuento.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblDescuento.setForeground(new java.awt.Color(255, 255, 255));
        lblDescuento.setText("Descuentos:");
        panelRedondo1.add(lblDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 450, 300, 25));

        txtDescuento.setText("0.00");
        panelRedondo1.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 480, 500, 35));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 255, 255));
        lblTotal.setText("Total a pagar:");
        panelRedondo1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 530, 300, 25));

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        panelRedondo1.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 560, 500, 35));

        lblObservaciones.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblObservaciones.setForeground(new java.awt.Color(255, 255, 255));
        lblObservaciones.setText("Observaciones (opcional):");
        panelRedondo1.add(lblObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 610, 300, 25));
        panelRedondo1.add(txtObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 640, 500, 35));

        btnGuardarPago.setText("Guardar Pago");
        btnGuardarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPagoActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnGuardarPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 700, 230, 45));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 700, 230, 45));

        cmbPeriodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));
        panelRedondo1.add(cmbPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 500, 40));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 620, 780));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(28, 9, 40));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Numero", "Nombre", "Apellido", "Cedula", "Area", "Sueldo", "Fecha_Contratacion"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1120, 530));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 220, 1160, 610));

        panelRedondo3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        FiltadoEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        FiltadoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoEmpleadosActionPerformed(evt);
            }
        });
        panelRedondo3.add(FiltadoEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 240, 30));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre A-Z", "Nombre Z-A", "Fecha_Contratacion" }));
        panelRedondo3.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 290, 30));

        add(panelRedondo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 770, 70));
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarPagoActionPerformed
        Object seleccionado = cmbEmpleado.getSelectedItem();
        if (!(seleccionado instanceof Empleado)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un empleado.",
                    "Sin empleado", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        Empleado empleado = (Empleado) seleccionado;
        Object periodoSel = cmbPeriodo.getSelectedItem();
        String periodo = periodoSel != null ? periodoSel.toString() : "";
        boolean exito = new Controladores.ControladorPagoEmpleado().generarPago(
                this, empleado.getIdEmpleado(), periodo,
                txtBonificacion.getText(), txtDescuento.getText(), txtObservaciones.getText());
        if (exito) {
            limpiarFormulario();
        }
    }//GEN-LAST:event_btnGuardarPagoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void FiltadoEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltadoEmpleadosActionPerformed

    }//GEN-LAST:event_FiltadoEmpleadosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoEmpleados;
    private componentes.BotonModerno btnGuardarPago;
    private componentes.BotonModerno btnLimpiar;
    private javax.swing.JComboBox cmbEmpleado;
    private javax.swing.JComboBox<String> cmbPeriodo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbOrden;
    private javax.swing.JLabel lblBonificacion;
    private javax.swing.JLabel lblDescuento;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblMonto;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblPeriodo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private componentes.PanelRedondo panelRedondo3;
    private componentes.TextFieldModerno txtBonificacion;
    private componentes.TextFieldModerno txtDescuento;
    private componentes.TextFieldModerno txtMonto;
    private componentes.TextFieldModerno txtObservaciones;
    private componentes.TextFieldModerno txtTotal;
    // End of variables declaration//GEN-END:variables
}
