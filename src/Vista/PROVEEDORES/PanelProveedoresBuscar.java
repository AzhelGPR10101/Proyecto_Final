
package Vista.PROVEEDORES;

import Controladores.ControladorProveedor;
import Modelo.Proveedores;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

public class PanelProveedoresBuscar extends javax.swing.JPanel {

    private List<Proveedores> listaProveedores;

    public PanelProveedoresBuscar() {
    initComponents();
    configurarEstiloTabla();
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

        jTable1.getColumnModel().getColumn(5).setCellRenderer(new componentes.EstiloTablaKrypton.RowRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setForeground(new Color(50, 205, 50));
                setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });
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

        if (listaProveedores == null) {
            return;
        }

        for (int i = 0; i < listaProveedores.size(); i++) {
            Proveedores prov = listaProveedores.get(i);

            modelo.addRow(new Object[]{
                (i + 1),
                prov.getNombreEmpresa(),
                prov.getNombreContacto(),
                prov.getRuc(),
                prov.getTelefono(),
                prov.getCorreo(),
                prov.getDireccion()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelRedondo1 = new componentes.PanelRedondo();
        jcbOrden = new componentes.ComboBoxModerno();
        FiltadoEmpleados = new componentes.TextFieldModerno();
        jLabel1 = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
                "Numero", "Empresa", "Dueño", "Ruc", "Contacto", "Correo", "Direccion"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1550, 630));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 1590, 680));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre de Empresa A-Z", "Nombre del dueño A-Z" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 350, 30));

        FiltadoEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        FiltadoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoEmpleadosActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltadoEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 300, 30));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, -1));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 900, 60));

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("LISTA DE PROVEEDOR/ES");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 590, 30));
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
