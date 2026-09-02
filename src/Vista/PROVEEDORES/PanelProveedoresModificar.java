
package Vista.PROVEEDORES;
import java.util.List;

public class PanelProveedoresModificar extends javax.swing.JPanel {

    public PanelProveedoresModificar() {
    initComponents();
    componentes.EstiloTablaKrypton.aplicar(jTable1);
    cargarTablaModificar(jTable1);
    FiltadoEmpleados.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
    });
    jcbOrden.addActionListener(e -> aplicarFiltro());
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
    String orden = (String) jcbOrden.getSelectedItem();
    cargarTablaModificar(jTable1, texto, orden);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel2 = new javax.swing.JLabel();
        FiltadoEmpleados = new componentes.TextFieldModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(31, 10, 48));
        jPanel1.setForeground(new java.awt.Color(36, 3, 38));
        jPanel1.setInheritsPopupMenu(true);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MODIFICAR PROVEEDOR/ES");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 630, 30));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FIltrar:");
        panelRedondo1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, -1));

        FiltadoEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        FiltadoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoEmpleadosActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltadoEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 300, 30));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre de Empresa A-Z", "Nombre del dueño A-Z" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 350, 30));

        jPanel1.add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 900, 60));

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
                "Numero", "Empresa", "Contacto", "Ruc", "Telefono", "Accion"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1580, 640));

        jPanel1.add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 1620, 690));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1930, 1090));
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
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables
    public static void cargarTablaModificar(javax.swing.JTable tabla) {
        cargarTablaModificar(tabla, null, null);
    }
    public static void cargarTablaModificar(javax.swing.JTable tabla, String texto, String orden) {
    String[] columnas = {"N°", "Empresa", "Contacto", "Ruc", "Telefono", "Acción"};

    javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5;
        }
    };

    List<Modelo.Proveedores> lista = new Controladores.ControladorProveedor().filtrarProveedores(texto, orden);
    int contador = 1;
    for (Modelo.Proveedores prov : lista) {
        modelo.addRow(new Object[]{
            contador++,
            prov.getNombreEmpresa(),
            prov.getNombreContacto(),
            prov.getRuc(),
            prov.getTelefono(),
            "Modificar"
        });
    }
    tabla.setModel(modelo);

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
                String ruc = tabla.getValueAt(filaSeleccionada, 3).toString();

                Modelo.Proveedores proveedor = new Controladores.ControladorProveedor().buscarPorRuc(ruc);
                if (proveedor != null) {
                    Vista.PROVEEDORES.PanelDialogModificarProveedor.mostrar(proveedor);
                }

                javax.swing.SwingUtilities.invokeLater(() -> {
                    PanelProveedoresModificar.cargarTablaModificar(tabla);
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