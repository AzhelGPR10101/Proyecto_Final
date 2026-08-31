package Vista.RECURSOSHUMANOS;

import Controladores.ControladorPagoEmpleado;
import Modelo.PagoEmpleado;
import javax.swing.table.DefaultTableModel;

public class PanelHistorialPagos extends javax.swing.JPanel {

    private DefaultTableModel modeloTabla;
    private final ControladorPagoEmpleado controlador = new ControladorPagoEmpleado();

    public PanelHistorialPagos() {
        initComponents();
        modeloTabla = (DefaultTableModel) tablaPagos.getModel();
        componentes.EstiloTablaKrypton.aplicar(tablaPagos);
        cargarHistorial();
    }

    public void cargarHistorial() {
        modeloTabla.setRowCount(0);
        java.util.List<PagoEmpleado> pagos = controlador.listarHistorial();
        for (PagoEmpleado pago : pagos) {
            modeloTabla.addRow(new Object[]{
                pago.getNombreEmpleado(),
                pago.getPeriodo(),
                pago.getFechaPago(),
                String.format("$%.2f", pago.getMonto()),
                pago.getObservaciones()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        btnActualizar = new componentes.BotonModerno();
        panelRedondo1 = new componentes.PanelRedondo();
        scrollTabla = new javax.swing.JScrollPane();
        tablaPagos = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("HISTORIAL DE PAGOS");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 500, 40));

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1630, 50, 160, 40));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Empleado", "Periodo", "Fecha de Pago", "Monto", "Observaciones"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTabla.setViewportView(tablaPagos);

        panelRedondo1.add(scrollTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 1690, 740));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 1750, 800));
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        cargarHistorial();
    }//GEN-LAST:event_btnActualizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno btnActualizar;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaPagos;
    // End of variables declaration//GEN-END:variables
}
