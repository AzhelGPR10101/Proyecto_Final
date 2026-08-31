package Vista.RECURSOSHUMANOS;

import Controladores.EmpleadoControlador;
import Modelo.Empleado;

public class PanelGenerarPago extends javax.swing.JPanel {

    public PanelGenerarPago() {
        initComponents();
        compactarEspaciado();
        componentes.EstiloTablaKrypton.aplicar(jTable1);
        cargarEmpleados();
        cargarTabla();
        FiltadoEmpleados.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { cargarTabla(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { cargarTabla(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { cargarTabla(); }
        });
        jcbOrden.addActionListener(e -> cargarTabla());
    }

    private void compactarEspaciado() {
        lblTitulo.setBounds(60, 15, 660, 30);
        panelRedondo1.setBounds(60, 60, 620, 620);
        panelRedondo3.setBounds(700, 60, 770, 70);
        panelRedondo2.setBounds(700, 140, 1160, 540);
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
        txtPeriodo.setText("");
        txtMonto.setText("");
        txtObservaciones.setText("");
        if (cmbEmpleado.getItemCount() > 0) {
            cmbEmpleado.setSelectedIndex(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        lblEmpleado = new javax.swing.JLabel();
        cmbEmpleado = new componentes.ComboBoxModerno();
        lblPeriodo = new javax.swing.JLabel();
        txtPeriodo = new componentes.TextFieldModerno();
        lblMonto = new javax.swing.JLabel();
        txtMonto = new componentes.TextFieldModerno();
        lblObservaciones = new javax.swing.JLabel();
        txtObservaciones = new componentes.TextFieldModerno();
        btnGuardarPago = new componentes.BotonModerno();
        btnLimpiar = new componentes.BotonModerno();
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
        panelRedondo1.add(txtPeriodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 500, 35));

        lblMonto.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblMonto.setForeground(new java.awt.Color(255, 255, 255));
        lblMonto.setText("Monto a pagar:");
        panelRedondo1.add(lblMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 300, 25));
        panelRedondo1.add(txtMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 500, 35));

        lblObservaciones.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblObservaciones.setForeground(new java.awt.Color(255, 255, 255));
        lblObservaciones.setText("Observaciones (opcional):");
        panelRedondo1.add(lblObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, 300, 25));
        panelRedondo1.add(txtObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, 500, 35));

        btnGuardarPago.setText("Guardar Pago");
        btnGuardarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPagoActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnGuardarPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 480, 230, 45));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 480, 230, 45));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 620, 700));

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
        boolean exito = new Controladores.ControladorPagoEmpleado().generarPago(
                this, empleado.getIdEmpleado(), txtPeriodo.getText(), txtMonto.getText(), txtObservaciones.getText());
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbOrden;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblMonto;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblPeriodo;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private componentes.PanelRedondo panelRedondo3;
    private componentes.TextFieldModerno txtMonto;
    private componentes.TextFieldModerno txtObservaciones;
    private componentes.TextFieldModerno txtPeriodo;
    // End of variables declaration//GEN-END:variables
}
