package Vista.RolCajero;

import Vista.RolCajero.PanelAperturaCaja;
import Vista.RolCajero.PanelCierreCaja;
import java.awt.CardLayout;

public class PanelCajero extends javax.swing.JPanel {

    private final PanelAperturaCaja panelApertura = new PanelAperturaCaja();
    private final PanelCierreCaja panelCierre = new PanelCierreCaja();
    private Runnable alCambiarEstadoCaja;

    public PanelCajero() {
        initComponents();
        PanelContenedor.setLayout(new CardLayout());

        PanelContenedor.add(panelApertura, "Apertura");
        PanelContenedor.add(panelCierre, "Cierre");
        CardLayout cardLayout = (CardLayout) PanelContenedor.getLayout();

        Controladores.ControladorCierreCaja.EstadoTurno estado
                = new Controladores.ControladorCierreCaja().obtenerEstadoTurno();
        if (estado == Controladores.ControladorCierreCaja.EstadoTurno.ABIERTO) {
            panelCierre.cargarDatosDeHoy();
            cardLayout.show(PanelContenedor, "Cierre");
        } else {
            panelApertura.cargarDatos();
            cardLayout.show(PanelContenedor, "Apertura");
        }

        panelApertura.setAlAbrirCaja(() -> {
            if (alCambiarEstadoCaja != null) {
                alCambiarEstadoCaja.run();
            }
        });
        panelCierre.setAlCerrar(() -> {
            if (alCambiarEstadoCaja != null) {
                alCambiarEstadoCaja.run();
            }
        });
    }

    public void setAlCambiarEstadoCaja(Runnable alCambiarEstadoCaja) {
        this.alCambiarEstadoCaja = alCambiarEstadoCaja;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CerrarCaja = new componentes.BotonModerno();
        btnAbriCaja = new componentes.BotonModerno();
        PanelContenedor = new javax.swing.JPanel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CerrarCaja.setText("CERRAR CAJA");
        CerrarCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CerrarCajaActionPerformed(evt);
            }
        });
        add(CerrarCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 240, 60));

        btnAbriCaja.setText("ABRIR CAJA");
        btnAbriCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbriCajaActionPerformed(evt);
            }
        });
        add(btnAbriCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 240, 60));

        javax.swing.GroupLayout PanelContenedorLayout = new javax.swing.GroupLayout(PanelContenedor);
        PanelContenedor.setLayout(PanelContenedorLayout);
        PanelContenedorLayout.setHorizontalGroup(
            PanelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1870, Short.MAX_VALUE)
        );
        PanelContenedorLayout.setVerticalGroup(
            PanelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 890, Short.MAX_VALUE)
        );

        add(PanelContenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 1870, 890));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAbriCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbriCajaActionPerformed
      CardLayout cl = (CardLayout) PanelContenedor.getLayout();
        panelApertura.cargarDatos();
        cl.show(PanelContenedor, "Apertura");

    }//GEN-LAST:event_btnAbriCajaActionPerformed

    private void CerrarCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CerrarCajaActionPerformed
         CardLayout cl = (CardLayout) PanelContenedor.getLayout();
        panelCierre.cargarDatosDeHoy();
        cl.show(PanelContenedor, "Cierre");

    }//GEN-LAST:event_CerrarCajaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CerrarCaja;
    private javax.swing.JPanel PanelContenedor;
    private javax.swing.JButton btnAbriCaja;
    // End of variables declaration//GEN-END:variables
}
