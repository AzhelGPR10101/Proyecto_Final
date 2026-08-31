
package Vista.EMPLEADOS;

import java.awt.CardLayout;

public class PanelEmpleados extends javax.swing.JPanel {

    public PanelEmpleados() {
        initComponents();
                PanelContenedor.setLayout(new CardLayout());

        PanelContenedor.add(new PanelAceptarEmpledos(), "empleadosAgregar");
        PanelContenedor.add(new PanelEmpleadosBuscar(), "empleadosBuscar");
        PanelContenedor.add(new PanelEmpleadosModificar(), "empleadosModificar");
        PanelContenedor.add(new PanelEmpleadosEliminar(), "empleadosEliminar");

        CardLayout cardLayout = (CardLayout) PanelContenedor.getLayout();
        cardLayout.show(PanelContenedor, "empleadosAgregar");

        if (!Modelo.Sesion.esDueno()) {
            btnAgregarEmpleado.setVisible(false);
            cardLayout.show(PanelContenedor, "empleadosBuscar");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAgregarEmpleado = new componentes.BotonModerno();
        btnBuscarEmpleado = new componentes.BotonModerno();
        brnModificarEmpleado = new componentes.BotonModerno();
        btnEliminarEmpleado = new componentes.BotonModerno();
        PanelContenedor = new componentes.PanelRedondo();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregarEmpleado.setText("Agregar Empleado");
        btnAgregarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEmpleadoActionPerformed(evt);
            }
        });
        add(btnAgregarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 38, 170, 50));

        btnBuscarEmpleado.setText("Buscar Empleado");
        btnBuscarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEmpleadoActionPerformed(evt);
            }
        });
        add(btnBuscarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 170, 50));

        brnModificarEmpleado.setText("Modificar Empleado");
        brnModificarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brnModificarEmpleadoActionPerformed(evt);
            }
        });
        add(brnModificarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, 170, 50));

        btnEliminarEmpleado.setText("Eliminar Empleado");
        btnEliminarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEmpleadoActionPerformed(evt);
            }
        });
        add(btnEliminarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, 180, 50));

        PanelContenedor.setLayout(new java.awt.CardLayout());
        add(PanelContenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 1920, 970));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEmpleadoActionPerformed
       CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "empleadosAgregar");
    }//GEN-LAST:event_btnAgregarEmpleadoActionPerformed

    private void btnBuscarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEmpleadoActionPerformed
        CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "empleadosBuscar");
    }//GEN-LAST:event_btnBuscarEmpleadoActionPerformed

    private void brnModificarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brnModificarEmpleadoActionPerformed
       CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "empleadosModificar");
    }//GEN-LAST:event_brnModificarEmpleadoActionPerformed

    private void btnEliminarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEmpleadoActionPerformed
       CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "empleadosEliminar");
    }//GEN-LAST:event_btnEliminarEmpleadoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.PanelRedondo PanelContenedor;
    private componentes.BotonModerno brnModificarEmpleado;
    private componentes.BotonModerno btnAgregarEmpleado;
    private componentes.BotonModerno btnBuscarEmpleado;
    private componentes.BotonModerno btnEliminarEmpleado;
    // End of variables declaration//GEN-END:variables
}
