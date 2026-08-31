package Vista.PRODUCTOS;

import java.awt.CardLayout;

public class PanelProductos extends javax.swing.JPanel {

    private final PanelProductosAgregar panelAgregar = new PanelProductosAgregar();

    public PanelProductos() {
        initComponents();
        PanelContenedor.setLayout(new CardLayout());

        PanelContenedor.add(panelAgregar, "productosAgregar");
        PanelContenedor.add(new PanelProductosBuscar(), "productosBuscar");
        PanelContenedor.add(new PanelProductosModificar(), "productosModificar");
        PanelContenedor.add(new PanelProductosEliminar(), "productosEliminar");
        CardLayout cardLayout = (CardLayout) PanelContenedor.getLayout();
        cardLayout.show(PanelContenedor, "productosAgregar");

        String rol = Modelo.Sesion.getRolUsuario();
        boolean esVendedor = "Vendedor".equalsIgnoreCase(rol) || "Cajero".equalsIgnoreCase(rol);
        if (esVendedor) {
            btnAgregarProducto.setVisible(false);
            btnModificarProducto.setVisible(false);
            btnEliminarProducto.setVisible(false);
            add(btnBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 38, 160, 50));
            cardLayout.show(PanelContenedor, "productosBuscar");
            addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentShown(java.awt.event.ComponentEvent e) {
                    panelAgregar.cargarCatalogos();
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAgregarProducto = new componentes.BotonModerno();
        btnBuscarProducto = new componentes.BotonModerno();
        btnModificarProducto = new componentes.BotonModerno();
        btnEliminarProducto = new componentes.BotonModerno();
        PanelContenedor = new componentes.PanelRedondo();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAgregarProducto.setText("Agregar Producto");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });
        add(btnAgregarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 170, 50));

        btnBuscarProducto.setText("Buscar Producto");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });
        add(btnBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 160, 50));

        btnModificarProducto.setText("Modificar Producto");
        btnModificarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarProductoActionPerformed(evt);
            }
        });
        add(btnModificarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 200, 50));

        btnEliminarProducto.setText("Eliminar Producto");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });
        add(btnEliminarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, 160, 50));

        PanelContenedor.setLayout(new java.awt.CardLayout());
        add(PanelContenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 1920, 970));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        panelAgregar.cargarCatalogos();
        panelAgregar.cargarTablaProductos();
        CardLayout cl = (CardLayout) PanelContenedor.getLayout();
        cl.show(PanelContenedor, "productosAgregar");
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        CardLayout cl = (CardLayout) PanelContenedor.getLayout();
        cl.show(PanelContenedor, "productosBuscar");
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnModificarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarProductoActionPerformed
        CardLayout cl = (CardLayout) PanelContenedor.getLayout();
        cl.show(PanelContenedor, "productosModificar");
    }//GEN-LAST:event_btnModificarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        CardLayout cl = (CardLayout) PanelContenedor.getLayout();
        cl.show(PanelContenedor, "productosEliminar");
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.PanelRedondo PanelContenedor;
    private componentes.BotonModerno btnAgregarProducto;
    private componentes.BotonModerno btnBuscarProducto;
    private componentes.BotonModerno btnEliminarProducto;
    private componentes.BotonModerno btnModificarProducto;
    // End of variables declaration//GEN-END:variables
}
