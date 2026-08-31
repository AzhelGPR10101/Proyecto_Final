
package Vista.CLIENTES;

import java.awt.CardLayout;

public class PanelClientes extends javax.swing.JPanel {

    public PanelClientes() {
        initComponents();
                PanelContenedor.setLayout(new CardLayout());

        PanelContenedor.add(new PanelClientesAgregar(), "clientesAgregar");
        PanelContenedor.add(new PanelClientesBuscar(), "clientesBuscar");
        PanelContenedor.add(new PanelClientesModificar(), "clientesModificar");
        PanelContenedor.add(new PanelClientesEliminar(), "clientesEliminar");
        CardLayout cardLayout = (CardLayout) PanelContenedor.getLayout();
        cardLayout.show(PanelContenedor, "clientesAgregar");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAgregarClientes = new componentes.BotonModerno();
        btnBuscarClientes = new componentes.BotonModerno();
        brnModificarClientes = new componentes.BotonModerno();
        btnEliminarClientes = new componentes.BotonModerno();
        PanelContenedor = new componentes.PanelRedondo();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregarClientes.setText("Agregar Cliente");
        btnAgregarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarClientesActionPerformed(evt);
            }
        });
        add(btnAgregarClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 38, 160, 50));

        btnBuscarClientes.setText("Buscar Cliente");
        btnBuscarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClientesActionPerformed(evt);
            }
        });
        add(btnBuscarClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 170, 50));

        brnModificarClientes.setText("Modificar Cliente");
        brnModificarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brnModificarClientesActionPerformed(evt);
            }
        });
        add(brnModificarClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 170, 50));

        btnEliminarClientes.setText("Eliminar Cliente");
        btnEliminarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClientesActionPerformed(evt);
            }
        });
        add(btnEliminarClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 40, 160, 50));

        PanelContenedor.setLayout(new java.awt.CardLayout());
        add(PanelContenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 1920, 1100));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarClientesActionPerformed
      CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "clientesAgregar");
    }//GEN-LAST:event_btnAgregarClientesActionPerformed

    private void btnBuscarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClientesActionPerformed
      CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "clientesBuscar");
    }//GEN-LAST:event_btnBuscarClientesActionPerformed

    private void brnModificarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brnModificarClientesActionPerformed
        CardLayout cl = (CardLayout) PanelContenedor.getLayout();
        cl.show(PanelContenedor, "clientesModificar");
    }//GEN-LAST:event_brnModificarClientesActionPerformed

    private void btnEliminarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClientesActionPerformed
       CardLayout cl = (CardLayout) PanelContenedor.getLayout();
    cl.show(PanelContenedor, "clientesEliminar");
    }//GEN-LAST:event_btnEliminarClientesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.PanelRedondo PanelContenedor;
    private componentes.BotonModerno brnModificarClientes;
    private componentes.BotonModerno btnAgregarClientes;
    private componentes.BotonModerno btnBuscarClientes;
    private componentes.BotonModerno btnEliminarClientes;
    // End of variables declaration//GEN-END:variables
}
