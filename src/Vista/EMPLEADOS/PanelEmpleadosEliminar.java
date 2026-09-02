
package Vista.EMPLEADOS;

import Controladores.EmpleadoControlador;
import Modelo.Empleado;

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

public class PanelEmpleadosEliminar extends javax.swing.JPanel {

    private List<Empleado> listaEmpleados;
    private boolean mostrandoInactivos = false;

   public PanelEmpleadosEliminar() {
    initComponents();
    configurarEstiloTabla();
    cargarEmpleados();
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
        if (visible) {
            cargarEmpleados();
        }
    }

    private void configurarEstiloTabla() {
        if (jTable1 == null) {
            return;
        }
        componentes.EstiloTablaKrypton.aplicar(jTable1);
    }

       private void cargarEmpleados() {
        mostrandoInactivos = false;
        listaEmpleados = EmpleadoControlador.listarEmpleados();
        actualizarTabla();
    }
private void aplicarFiltro() {
    String texto = FiltadoEmpleados.getText();
    String rol = (String) jcbOrden.getSelectedItem();
    String orden = (String) jcbOrden1.getSelectedItem();

    mostrandoInactivos = "Inactivo".equals(rol);

    if (mostrandoInactivos) {
        listaEmpleados = new java.util.ArrayList<>();
        for (Empleado emp : EmpleadoControlador.listarEmpleadosInactivos()) {
            boolean coincideTexto = texto == null || texto.trim().isEmpty()
                    || emp.getNombres().toLowerCase().contains(texto.toLowerCase())
                    || emp.getApellidos().toLowerCase().contains(texto.toLowerCase())
                    || emp.getCedula().toLowerCase().contains(texto.toLowerCase());
            if (coincideTexto) {
                listaEmpleados.add(emp);
            }
        }
    } else {
        listaEmpleados = EmpleadoControlador.filtrarEmpleados(texto, rol, orden);
    }
    actualizarTabla();
}
        public void actualizarTabla() {
        if (jTable1 == null) {
            return;
        }

        String etiquetaBoton = mostrandoInactivos ? "Activar" : "Desactivar";

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        if (listaEmpleados != null) {
            for (int i = 0; i < listaEmpleados.size(); i++) {
                Empleado emp = listaEmpleados.get(i);

                modelo.addRow(new Object[]{
                    (i + 1),
                    emp.getNombres(),
                    emp.getApellidos(),
                    emp.getCedula(),
                    emp.getRol(),
                    etiquetaBoton
                });
            }
        }

        if (jTable1.getColumnModel().getColumnCount() >= 6) {
            jTable1.getColumnModel().getColumn(5).setCellRenderer(
                new componentes.EstiloTablaKrypton.RenderBotonRedondeado(etiquetaBoton)
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
        private PanelEmpleadosEliminar panel;

        public EditorBotonTabla(JCheckBox checkBox, JTable tabla, PanelEmpleadosEliminar panel) {
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
            label = (value == null) ? "Desactivar" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
public Object getCellEditorValue() {
    if (isPushed) {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0 && filaSeleccionada < panel.listaEmpleados.size()) {
            Empleado emp = panel.listaEmpleados.get(filaSeleccionada);

            if (panel.mostrandoInactivos) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    button,
                    "¿Está seguro de reactivar a " + emp.getNombres() + " " + emp.getApellidos() + "?",
                    "Confirmar Reactivación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean reactivado = EmpleadoControlador.reactivarEmpleado(panel, emp.getCedula());
                    if (reactivado) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            panel.aplicarFiltro();
                            JOptionPane.showMessageDialog(button, "Empleado reactivado con éxito.");
                        });
                    }
                }
            } else {
                int confirmacion = JOptionPane.showConfirmDialog(
                    button,
                    "¿Está seguro de desactivar a " + emp.getNombres() + " " + emp.getApellidos() + "?\n"
                    + "El empleado ya no podrá iniciar sesión, pero su historial de facturas y pagos se conservará.",
                    "Confirmar Desactivación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean eliminado = EmpleadoControlador.eliminarEmpleado(panel, emp.getCedula());
                    if (eliminado) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            panel.cargarEmpleados();
                            JOptionPane.showMessageDialog(button, "Empleado desactivado con éxito.");
                        });
                    }
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
        jLabel2 = new javax.swing.JLabel();
        FiltadoEmpleados = new componentes.TextFieldModerno();
        jcbOrden1 = new componentes.ComboBoxModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        jLabel1 = new javax.swing.JLabel();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        JlbDescripcion = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FIltrar:");
        panelRedondo1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        FiltadoEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        FiltadoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoEmpleadosActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltadoEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 220, 30));

        jcbOrden1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden1.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre A-Z", "Nombre Z-A" }));
        panelRedondo1.add(jcbOrden1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 230, 30));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Cajero", "Bodeguero", "Recursos Humanos", "Inactivo" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 230, 30));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 950, 70));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DESACTIVAR EMPLEADO/S");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 690, -1));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(28, 9, 40));
        jTable1.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jTable1.setForeground(new java.awt.Color(31, 10, 48));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero", "Nombre", "Apellido", "Cedula", "Area", "Eliminar"
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

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1690, 660));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 1760, 740));

        JlbDescripcion.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        JlbDescripcion.setForeground(new java.awt.Color(255, 255, 255));
        JlbDescripcion.setText("Nota: Al desactivar un empleado, pierde el acceso a la plataforma pero su historial se conserva");
        add(JlbDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 970, -1, 40));
    }// </editor-fold>//GEN-END:initComponents

    private void FiltadoEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltadoEmpleadosActionPerformed

    }//GEN-LAST:event_FiltadoEmpleadosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoEmpleados;
    private javax.swing.JLabel JlbDescripcion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbOrden;
    private javax.swing.JComboBox<String> jcbOrden1;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables

}
