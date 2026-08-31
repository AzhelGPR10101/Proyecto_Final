
package Vista.COMPRAS;

import Controladores.ControladorEgreso;
import Modelo.Pagare;
import java.awt.Frame;
import java.awt.Window;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PanelPagares extends javax.swing.JPanel {

    private final ControladorEgreso controladorEgreso = new ControladorEgreso();
    private List<Pagare> listaPagares;
    private DefaultTableModel modeloTabla;

    public PanelPagares() {
        initComponents();

        modeloTabla = (DefaultTableModel) tabla.getModel();
        componentes.EstiloTablaKrypton.aplicar(tabla);
        cargarPagares();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarPagares();
        }
    }

    private void cargarPagares() {
        listaPagares = controladorEgreso.listarPagaresPendientes();
        modeloTabla.setRowCount(0);
        for (Pagare p : listaPagares) {
            modeloTabla.addRow(new Object[]{
                p.getNumFacturaProveedor(), p.getNombreProveedor(),
                String.format("$%.2f", p.getMontoTotal()),
                String.format("$%.2f", p.getSaldoPendiente()),
                p.getFechaVencimiento(), p.getEstado()
            });
        }
    }

    private void registrarPago() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un pagaré de la tabla.");
            return;
        }
        Pagare seleccionado = listaPagares.get(fila);

        Window ventana = SwingUtilities.getWindowAncestor(this);
        Frame frame = (ventana instanceof Frame) ? (Frame) ventana : null;

        DialogPagoProveedor dialog = new DialogPagoProveedor(frame, seleccionado.getNombreProveedor(),
                seleccionado.getSaldoPendiente());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            boolean exito = controladorEgreso.registrarPago(this, seleccionado,
                    dialog.getMetodoPago(), dialog.getMontoIngresado());
            if (exito) {
                cargarPagares();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        btnActualizar = new componentes.BotonModerno();
        btnPagar = new componentes.BotonModerno();
        panelRedondo1 = new componentes.PanelRedondo();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setBackground(new java.awt.Color(28, 9, 40));
        setLayout(null);

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 32)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Pagarés Pendientes a Proveedores");
        add(lblTitulo);
        lblTitulo.setBounds(90, 60, 750, 40);

        btnActualizar.setText("Actualizar Lista");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        add(btnActualizar);
        btnActualizar.setBounds(1490, 960, 150, 45);

        btnPagar.setText("Registrar Pago");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });
        add(btnPagar);
        btnPagar.setBounds(1650, 960, 150, 45);

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Factura Proveedor", "Proveedor", "Monto Total", "Saldo Pendiente", "Vencimiento", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTabla.setViewportView(tabla);

        panelRedondo1.add(scrollTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1670, 760));

        add(panelRedondo1);
        panelRedondo1.setBounds(90, 120, 1710, 820);
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        cargarPagares();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        registrarPago();
    }//GEN-LAST:event_btnPagarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno btnActualizar;
    private componentes.BotonModerno btnPagar;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
