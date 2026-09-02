
package Vista.CLIENTES;

import Controladores.ControladorCliente;
import Modelo.Cliente;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PanelClientesEliminar extends javax.swing.JPanel {

    private List<Cliente> listaClientes;

    public PanelClientesEliminar() {
        initComponents();
        configurarEstiloTabla();
        cargarClientes();
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
        if (visible) {
            aplicarFiltro();
        }
    }

    private void aplicarFiltro() {
        String texto = FiltadoClientes.getText();
        String orden = (String) jcbOrden.getSelectedItem();
        listaClientes = new ControladorCliente().filtrarClientes(texto, orden);
        actualizarTabla();
    }

    private void configurarEstiloTabla() {
        if (jTable1 == null) {
            return;
        }
        componentes.EstiloTablaKrypton.aplicar(jTable1);
    }

    private void cargarClientes() {
        listaClientes = new ControladorCliente().listarTodos();
        actualizarTabla();
    }

    public void actualizarTabla() {
        if (jTable1 == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        if (listaClientes != null) {
            for (int i = 0; i < listaClientes.size(); i++) {
                Cliente cli = listaClientes.get(i);

                modelo.addRow(new Object[]{
                    (i + 1),
                    cli.getNombre(),
                    cli.getApellido(),
                    cli.getCedula(),
                    cli.getTelefono(),
                    "Eliminar"
                });
            }
        }

        if (jTable1.getColumnModel().getColumnCount() >= 6) {
            jTable1.getColumnModel().getColumn(5).setCellRenderer(
                new componentes.EstiloTablaKrypton.RenderBotonRedondeado("Eliminar")
            );
            jTable1.getColumnModel().getColumn(5).setCellEditor(
                new EditorBotonTabla(new JCheckBox(), jTable1, this)
            );
        }
    }

    class EditorBotonTabla extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable tabla;
        private PanelClientesEliminar panel;

        public EditorBotonTabla(JCheckBox checkBox, JTable tabla, PanelClientesEliminar panel) {
            super(checkBox);
            this.tabla = tabla;
            this.panel = panel;

            button = new JButton() {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    componentes.EstiloTablaKrypton.pintarFondoRedondeado(this, g);
                    super.paintComponent(g);
                }
            };
            componentes.EstiloTablaKrypton.configurarBoton(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Eliminar" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada >= 0 && filaSeleccionada < panel.listaClientes.size()) {
                    Cliente cli = panel.listaClientes.get(filaSeleccionada);

                    int confirmacion = JOptionPane.showConfirmDialog(
                        button,
                        "¿Está seguro de eliminar a " + cli.getNombre() + " " + cli.getApellido() + "?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        boolean eliminado = new ControladorCliente().eliminar(cli.getCedula());
                        if (eliminado) {
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                 panel.aplicarFiltro();
                                JOptionPane.showMessageDialog(button, "Cliente eliminado con éxito.");
                            });
                        }
                    }
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

        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel1 = new javax.swing.JLabel();
        FiltadoClientes = new componentes.TextFieldModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        lblTitulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(28, 9, 40));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero", "Nombre", "Apellido", "Cedula", "Telefono", "Eliminar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 1550, 590));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 1620, 720));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        FiltadoClientes.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        panelRedondo1.add(FiltadoClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 300, 40));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nombre A-Z", "Apellido A-Z" }));
        jcbOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbOrdenActionPerformed(evt);
            }
        });
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 370, 30));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 880, 80));

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("ELIMINAR CLIENTES");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 450, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jcbOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbOrdenActionPerformed
    }//GEN-LAST:event_jcbOrdenActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoClientes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox jcbOrden;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables

}
