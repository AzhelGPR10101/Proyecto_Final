
package Vista.PROVEEDORES;

import Controladores.ControladorProveedor;
import Modelo.Proveedores;

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

public class PanelProveedoresEliminar extends javax.swing.JPanel {

    private List<Proveedores> listaProveedores;

    public PanelProveedoresEliminar() {
    initComponents();
    configurarEstiloTabla();
    componentes.EstiloTablaKrypton.aplicar(jTable1);
    cargarProveedores();
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
        if (visible) {
            cargarProveedores();
        }
    }
private void aplicarFiltro() {
    String texto = FiltadoEmpleados.getText();
    String orden = (String) jcbOrden.getSelectedItem();
    listaProveedores = new ControladorProveedor().filtrarProveedores(texto, orden);
    actualizarTabla();
}
    private void configurarEstiloTabla() {
        if (jTable1 == null) {
            return;
        }
        componentes.EstiloTablaKrypton.aplicar(jTable1);
    }

    private void cargarProveedores() {
        listaProveedores = new ControladorProveedor().listarTodos();
        actualizarTabla();
    }

    public void actualizarTabla() {
        if (jTable1 == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        if (listaProveedores != null) {
            for (int i = 0; i < listaProveedores.size(); i++) {
                Proveedores prov = listaProveedores.get(i);

                modelo.addRow(new Object[]{
                    (i + 1),
                    prov.getNombreEmpresa(),
                    prov.getNombreContacto(),
                    prov.getRuc(),
                    prov.getTelefono(),
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
        private PanelProveedoresEliminar panel;

        public EditorBotonTabla(JCheckBox checkBox, JTable tabla, PanelProveedoresEliminar panel) {
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
        if (filaSeleccionada >= 0 && filaSeleccionada < panel.listaProveedores.size()) {
            Proveedores prov = panel.listaProveedores.get(filaSeleccionada);

            int confirmacion = JOptionPane.showConfirmDialog(
                button,
                "¿Está seguro de eliminar a " + prov.getNombreEmpresa() + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminado = new ControladorProveedor().eliminar(prov.getRuc());
                if (eliminado) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        panel.cargarProveedores();
                        JOptionPane.showMessageDialog(button, "Proveedor eliminado con éxito.");
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

        panelRedondo1 = new componentes.PanelRedondo();
        jcbOrden = new componentes.ComboBoxModerno();
        FiltadoEmpleados = new componentes.TextFieldModerno();
        jLabel1 = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre de Empresa A-Z", "Nombre del dueño A-Z" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 340, 30));

        FiltadoEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        FiltadoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoEmpleadosActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltadoEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 300, 40));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 140, -1));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 890, 70));

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("ELIMINAR PROVEEDORE/S");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 700, 30));

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
                "Numero", "Empresa", "Dueño", "Ruc", "Contacto", "Eliminar"
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

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1580, 640));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 1620, 690));
    }// </editor-fold>//GEN-END:initComponents

    private void FiltadoEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltadoEmpleadosActionPerformed

    }//GEN-LAST:event_FiltadoEmpleadosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoEmpleados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbOrden;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables

}
