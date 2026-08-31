
package Vista.PROVEEDORES;

import java.awt.CardLayout;

public class PanelProveedores extends javax.swing.JPanel {

    public PanelProveedores() {
        initComponents();
        PanelContenedor.setLayout(new CardLayout());

        PanelContenedor.add(new PanelProveedoresAgregar(), "proveedoresAgregar");
        PanelContenedor.add(new PanelProveedoresBuscar(), "proveedoresBuscar");
        PanelContenedor.add(new PanelProveedoresModificar(), "proveedoresModificar");
        PanelContenedor.add(new PanelProveedoresEliminar(), "proveedoresEliminar");
        CardLayout cardLayout = (CardLayout) PanelContenedor.getLayout();
        cardLayout.show(PanelContenedor, "proveedoresAgregar");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAgregarProveedores = new componentes.BotonModerno();
        btnBuscarProveedores = new componentes.BotonModerno();
        btnModificarProveedores = new componentes.BotonModerno();
        EliminarProveedores = new componentes.BotonModerno();
        PanelContenedor = new componentes.PanelRedondo();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregarProveedores.setText("Agregar Proveedores");
        btnAgregarProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProveedoresActionPerformed(evt);
            }
        });
        add(btnAgregarProveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 200, 50));

        btnBuscarProveedores.setText("Buscar Proveedores");
        btnBuscarProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProveedoresActionPerformed(evt);
            }
        });
        add(btnBuscarProveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 190, 50));

        btnModificarProveedores.setText("Modificar Proveedores");
        btnModificarProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarProveedoresActionPerformed(evt);
            }
        });
        add(btnModificarProveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 180, 50));

        EliminarProveedores.setText("Eliminar Proveedores");
        EliminarProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarProveedoresActionPerformed(evt);
            }
        });
        add(EliminarProveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 200, 50));

        PanelContenedor.setLayout(new java.awt.CardLayout());
        add(PanelContenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1920, 980));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProveedoresActionPerformed
       CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "proveedoresAgregar");
    }//GEN-LAST:event_btnAgregarProveedoresActionPerformed

    private void btnBuscarProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProveedoresActionPerformed
 CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "proveedoresBuscar");
    }//GEN-LAST:event_btnBuscarProveedoresActionPerformed

    private void btnModificarProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarProveedoresActionPerformed
 CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "proveedoresModificar");
    }//GEN-LAST:event_btnModificarProveedoresActionPerformed

    private void EliminarProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarProveedoresActionPerformed
 CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "proveedoresEliminar");
    }//GEN-LAST:event_EliminarProveedoresActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno EliminarProveedores;
    private componentes.PanelRedondo PanelContenedor;
    private componentes.BotonModerno btnAgregarProveedores;
    private componentes.BotonModerno btnBuscarProveedores;
    private componentes.BotonModerno btnModificarProveedores;
    // End of variables declaration//GEN-END:variables
}
