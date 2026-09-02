
package Vista.EMPLEADOS;

import java.util.List;

public class PanelEmpleadosModificar extends javax.swing.JPanel {

public PanelEmpleadosModificar() {
    initComponents();
    componentes.EstiloTablaKrypton.aplicar(jTable1);
    cargarTablaModificar(jTable1);
    FiltadoEmpleados.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
    });
    jcbOrden.addActionListener(e -> aplicarFiltro());
    jcbOrden1.addActionListener(e -> aplicarFiltro());
}

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && jTable1 != null) {
            cargarTablaModificar(jTable1);
        }
    }
private void aplicarFiltro() {
    String texto = FiltadoEmpleados.getText();
    String rol = (String) jcbOrden.getSelectedItem();
    String orden = (String) jcbOrden1.getSelectedItem();
    cargarTablaModificar(jTable1, texto, rol, orden);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel2 = new javax.swing.JLabel();
        FiltadoEmpleados = new componentes.TextFieldModerno();
        jcbOrden1 = new componentes.ComboBoxModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(31, 10, 48));
        jPanel1.setForeground(new java.awt.Color(36, 3, 38));
        jPanel1.setInheritsPopupMenu(true);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(51, 0, 102));
        jTable1.setForeground(new java.awt.Color(51, 0, 51));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero", "Nombre", "Apellido", "Cedula", "Area", "Accion"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1690, 660));

        jPanel1.add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 1760, 740));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FIltrar:");
        panelRedondo1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        FiltadoEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        FiltadoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoEmpleadosActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltadoEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 220, 30));

        jcbOrden1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden1.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre A-Z", "Nombre Z-A" }));
        panelRedondo1.add(jcbOrden1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 210, 30));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Cajero", "Bodeguero", "Recursos Humanos" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 230, 30));

        jPanel1.add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 960, 70));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MODIFICAR EMPLEADO");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 630, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));
    }// </editor-fold>//GEN-END:initComponents

    private void FiltadoEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltadoEmpleadosActionPerformed

    }//GEN-LAST:event_FiltadoEmpleadosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoEmpleados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbOrden;
    private javax.swing.JComboBox<String> jcbOrden1;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables

public static void cargarTablaModificar(javax.swing.JTable tabla) {
    cargarTablaModificar(tabla, null, null, null);
}

    public static void cargarTablaModificar(javax.swing.JTable tabla, String texto, String rol, String orden) {
    String[] columnas = {"N°", "Nombre", "Apellido", "Cedula", "Área", "Acción"};

    javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5;
        }
    };

    List<Modelo.Empleado> lista = Controladores.EmpleadoControlador.filtrarEmpleados(texto, rol, orden);
    int contador = 1;
    for (Modelo.Empleado emp : lista) {
        modelo.addRow(new Object[]{
            contador++,
            emp.getNombres(),
            emp.getApellidos(),
            emp.getCedula(),
            emp.getRol(),
            "Modificar"
        });
    }
    tabla.setModel(modelo);
    componentes.EstiloTablaKrypton.aplicar(tabla);

    tabla.getColumnModel().getColumn(5).setCellRenderer(
        new componentes.EstiloTablaKrypton.RenderBotonRedondeado("Modificar")
    );
    tabla.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new javax.swing.JCheckBox(), tabla));
}
}

    class ButtonEditor extends javax.swing.DefaultCellEditor {
        private javax.swing.JButton button;
        private String label;
        private boolean clicked;
        private javax.swing.JTable tabla;

        public ButtonEditor(javax.swing.JCheckBox checkBox, javax.swing.JTable tabla) {
            super(checkBox);
            this.tabla = tabla;
            button = new javax.swing.JButton() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                componentes.EstiloTablaKrypton.pintarFondoRedondeado(this, g);
                super.paintComponent(g);
            }
        };
        componentes.EstiloTablaKrypton.configurarBoton(button);
        button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
        if (clicked) {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                String cedula = tabla.getValueAt(filaSeleccionada, 3).toString();

                Modelo.Empleado empleado = Controladores.EmpleadoControlador.buscarEmpleadoPorCedula(cedula);
                if (empleado != null) {
                    Vista.EMPLEADOS.DialogModificarEmpleado dialogo = new Vista.EMPLEADOS.DialogModificarEmpleado(empleado);
                    dialogo.setVisible(true);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(null, "No se encontró el empleado con cédula: " + cedula, "Empleado no encontrado", javax.swing.JOptionPane.WARNING_MESSAGE);
                }

                javax.swing.SwingUtilities.invokeLater(() -> {
                    PanelEmpleadosModificar.cargarTablaModificar(tabla);
                });
            }
        }
        clicked = false;
        return label;
    }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }