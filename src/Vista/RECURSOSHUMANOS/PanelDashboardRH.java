package Vista.RECURSOSHUMANOS;

import java.awt.CardLayout;

public class PanelDashboardRH extends javax.swing.JPanel {

    private final PanelHistorialPagos panelHistorialPagosInstancia = new PanelHistorialPagos();

    public PanelDashboardRH() {
        initComponents();
        PanelContenedorRH.setLayout(new CardLayout());

        PanelContenedorRH.add(new Vista.EMPLEADOS.PanelEmpleados(), "empleados");
        PanelContenedorRH.add(new PanelGenerarPago(), "generarPago");
        PanelContenedorRH.add(panelHistorialPagosInstancia, "historialPagos");

        CardLayout cardLayout = (CardLayout) PanelContenedorRH.getLayout();
        cardLayout.show(PanelContenedorRH, "empleados");

        if (Modelo.Sesion.esDueno()) {
            btnGestionarEmpleados.setVisible(false);
        }
    }

    private void mostrar(String nombreTarjeta) {
        CardLayout cardLayout = (CardLayout) PanelContenedorRH.getLayout();
        if ("historialPagos".equals(nombreTarjeta)) {
            panelHistorialPagosInstancia.cargarHistorial();
        }
        cardLayout.show(PanelContenedorRH, nombreTarjeta);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloRH = new javax.swing.JLabel();
        btnGestionarEmpleados = new componentes.BotonModerno();
        btnGenerarPago = new componentes.BotonModerno();
        btnHistorialPagos = new componentes.BotonModerno();
        PanelContenedorRH = new componentes.PanelRedondo();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloRH.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        lblTituloRH.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloRH.setText("RECURSOS HUMANOS");
        add(lblTituloRH, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 500, 40));

        btnGestionarEmpleados.setText("Gestionar Empleados");
        btnGestionarEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarEmpleadosActionPerformed(evt);
            }
        });
        add(btnGestionarEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 200, 50));

        btnGenerarPago.setText("Generar Pago");
        btnGenerarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPagoActionPerformed(evt);
            }
        });
        add(btnGenerarPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 200, 50));

        btnHistorialPagos.setText("Historial de Pagos");
        btnHistorialPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialPagosActionPerformed(evt);
            }
        });
        add(btnHistorialPagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 200, 50));

        PanelContenedorRH.setLayout(new java.awt.CardLayout());
        add(PanelContenedorRH, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 1920, 930));
    }// </editor-fold>//GEN-END:initComponents

    private void btnGestionarEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarEmpleadosActionPerformed
        mostrar("empleados");
    }//GEN-LAST:event_btnGestionarEmpleadosActionPerformed

    private void btnGenerarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPagoActionPerformed
        mostrar("generarPago");
    }//GEN-LAST:event_btnGenerarPagoActionPerformed

    private void btnHistorialPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialPagosActionPerformed
        mostrar("historialPagos");
    }//GEN-LAST:event_btnHistorialPagosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.PanelRedondo PanelContenedorRH;
    private componentes.BotonModerno btnGenerarPago;
    private componentes.BotonModerno btnGestionarEmpleados;
    private componentes.BotonModerno btnHistorialPagos;
    private javax.swing.JLabel lblTituloRH;
    // End of variables declaration//GEN-END:variables
}
