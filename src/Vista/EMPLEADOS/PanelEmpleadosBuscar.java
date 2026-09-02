
package Vista.EMPLEADOS;

import Controladores.EmpleadoControlador;
import Modelo.Empleado;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

public class PanelEmpleadosBuscar extends javax.swing.JPanel {

    private List<Empleado> listaEmpleados;

    public PanelEmpleadosBuscar() {
    initComponents();
    configurarEstiloTabla();
    cargarEmpleados();
    FiltadoEmpleados.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
    });

    jcbOrden.addActionListener(e -> aplicarFiltro());
    iniciarAutoRefresco();
}

    private void iniciarAutoRefresco() {
        javax.swing.Timer timer = new javax.swing.Timer(60000, evt -> {
            if (isShowing()) {
                aplicarFiltro();
            }
        });
        timer.start();
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

    private void cargarEmpleados() {
    listaEmpleados = EmpleadoControlador.listarEmpleados();
    actualizarTabla();
}
private void aplicarFiltro() {
    String texto = FiltadoEmpleados.getText();
    String orden = (String) jcbOrden.getSelectedItem();
    listaEmpleados = EmpleadoControlador.filtrarEmpleados(texto, orden);
    actualizarTabla();
}
    public void actualizarTabla() {
        if (jTable1 == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        if (listaEmpleados == null) {
            return;
        }

        for (int i = 0; i < listaEmpleados.size(); i++) {
            Empleado emp = listaEmpleados.get(i);

            modelo.addRow(new Object[]{
                (i + 1),
                emp.getNombres(),
                emp.getApellidos(),
                emp.getCedula(),
                emp.getRol(),
                String.format("$%.2f", emp.getSueldo()),
                emp.getFechaContratacion()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlblEmpleados = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel1 = new javax.swing.JLabel();
        FiltadoEmpleados = new componentes.TextFieldModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlblEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jlblEmpleados.setForeground(new java.awt.Color(255, 255, 255));
        jlblEmpleados.setText("LISTA DE EMPLEADOS");
        add(jlblEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 600, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        FiltadoEmpleados.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoEmpleados.setForeground(new java.awt.Color(0, 0, 0));
        FiltadoEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoEmpleadosActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltadoEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 240, 30));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre A-Z", "Nombre Z-A", "Fecha_Contratacion" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 290, 30));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 770, 70));

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

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1690, 660));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 1760, 740));
    }// </editor-fold>//GEN-END:initComponents

    private void FiltadoEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltadoEmpleadosActionPerformed

    }//GEN-LAST:event_FiltadoEmpleadosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoEmpleados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbOrden;
    private javax.swing.JLabel jlblEmpleados;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables
}
