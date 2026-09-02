
package Vista.CLIENTES;
import java.util.List;

public class PanelClientesModificar extends javax.swing.JPanel {

    public PanelClientesModificar() {
        initComponents();
        componentes.EstiloTablaKrypton.aplicar(jTable1);
        cargarTablaModificar(jTable1);
        FiltadoClientes.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
        String texto = FiltadoClientes.getText();
        String orden = (String) jcbOrden.getSelectedItem();
        cargarTablaModificar(jTable1, texto, orden);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        FiltadoClientes = new componentes.TextFieldModerno();
        jLabel2 = new javax.swing.JLabel();
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
        jLabel1.setText("MODIFICAR CLIENTES");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 500, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FiltadoClientes.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        panelRedondo1.add(FiltadoClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 310, 40));

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FIltrar:");
        panelRedondo1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 140, -1));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre A-Z", "Apellido A-Z" }));
        jcbOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbOrdenActionPerformed(evt);
            }
        });
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 360, 30));

        jPanel1.add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 880, 80));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(28, 9, 40));
        jTable1.setForeground(new java.awt.Color(51, 0, 51));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero", "Nombre", "Apellido", "Cedula", "Telefono", "Accion"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 1540, 550));

        jPanel1.add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 1620, 640));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1930, 1090));
    }// </editor-fold>//GEN-END:initComponents

    private void jcbOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbOrdenActionPerformed
    }//GEN-LAST:event_jcbOrdenActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoClientes;
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
        String[] columnas = {"N°", "Nombre", "Apellido", "Cedula", "Telefono", "Acción"};

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        try {
            List<Modelo.Cliente> lista = new Controladores.ControladorCliente().listarTodos();

            int contador = 1;
            for (Modelo.Cliente cli : lista) {
                modelo.addRow(new Object[]{
                    contador++,
                    cli.getNombre(),
                    cli.getApellido(),
                    cli.getCedula(),
                    cli.getTelefono(),
                    "Modificar"
                });
            }
            tabla.setModel(modelo);
            componentes.EstiloTablaKrypton.aplicar(tabla);

            tabla.getColumnModel().getColumn(5).setCellRenderer(
                new componentes.EstiloTablaKrypton.RenderBotonRedondeado("Modificar")
            );
            tabla.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new javax.swing.JCheckBox(), tabla));

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        }
    }

    public static void cargarTablaModificar(javax.swing.JTable tabla, String texto, String orden) {
        String[] columnas = {"N°", "Nombre", "Apellido", "Cedula", "Telefono", "Acción"};

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        List<Modelo.Cliente> lista = new Controladores.ControladorCliente().filtrarClientes(texto, orden);
        int contador = 1;
        for (Modelo.Cliente cli : lista) {
            modelo.addRow(new Object[]{
                contador++,
                cli.getNombre(),
                cli.getApellido(),
                cli.getCedula(),
                cli.getTelefono(),
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

    static class ButtonEditor extends javax.swing.DefaultCellEditor {
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

                    Modelo.Cliente cliente = new Controladores.ControladorCliente().buscarPorCedula(cedula);
                    if (cliente != null) {
                        Vista.CLIENTES.PanelDialogModificarCliente.mostrar(cliente);
                    }

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        PanelClientesModificar.cargarTablaModificar(tabla);
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
}
