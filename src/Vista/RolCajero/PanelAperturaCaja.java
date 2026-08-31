
package Vista.RolCajero;

import Controladores.ControladorCierreCaja;
import Modelo.CierreCaja;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PanelAperturaCaja extends javax.swing.JPanel {

    private final ControladorCierreCaja controlador = new ControladorCierreCaja();
    private Runnable alAbrirCaja;
    private Runnable alCancelar;
    private DefaultTableModel modeloHistorialApertura;

    public PanelAperturaCaja() {
        initComponents();

        txtFechaHora.setEditable(false);
        txtCajeroResponsable.setEditable(false);

        modeloHistorialApertura = (DefaultTableModel) tablaHistorialApertura.getModel();
        componentes.EstiloTablaKrypton.aplicar(tablaHistorialApertura);

        btnAbrirCaja.addActionListener(evt -> confirmarApertura());
    }

    public void setAlAbrirCaja(Runnable alAbrirCaja) {
        this.alAbrirCaja = alAbrirCaja;
    }

    public void setAlCancelar(Runnable alCancelar) {
        this.alCancelar = alCancelar;
    }

    public void cargarDatos() {
        txtCajeroResponsable.setText(Vista.MenuPrincipal.usuarioActivo);
        txtFechaHora.setText(java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        txtMontoInicial.setText("");
        txtNotasApertura.setText("");

        CierreCaja turnoDeHoy = controlador.obtenerTurnoDeHoy();

        boolean yaAbrioHoy = turnoDeHoy != null;
        txtMontoInicial.setEnabled(!yaAbrioHoy);
        txtNotasApertura.setEnabled(!yaAbrioHoy);
        btnAbrirCaja.setEnabled(!yaAbrioHoy);

        cargarHistorialApertura();

        if (yaAbrioHoy) {
            JOptionPane.showMessageDialog(this,
                    "Ya abriste caja hoy. Si necesitas cerrarla, usa la opción \"Cerrar Caja\".",
                    "Caja ya abierta", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarHistorialApertura() {
        if (modeloHistorialApertura == null) {
            return;
        }
        modeloHistorialApertura.setRowCount(0);
        List<CierreCaja> historial = controlador.listarHistorialDelNegocio();
        if (historial == null) {
            return;
        }
        for (CierreCaja c : historial) {
            String estado = c.estaAbierta() ? "Abierta" : "Cerrada";
            String notas = c.getNotasApertura() != null && !c.getNotasApertura().isEmpty()
                    ? c.getNotasApertura() : "-";
            modeloHistorialApertura.addRow(new Object[]{
                c.getNombreEmpleado(),
                c.getFechaInicio(),
                String.format("$%.2f", c.getMontoInicial()),
                notas,
                estado
            });
        }
    }

    private void confirmarApertura() {
        double montoInicial;
        try {
            montoInicial = Double.parseDouble(txtMontoInicial.getText().trim());
            if (montoInicial < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un monto inicial válido (0 o mayor).",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String notas = txtNotasApertura.getText().trim();

        CierreCaja turno = controlador.abrirTurno(montoInicial, notas);
        if (turno == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir la caja (puede que ya exista un turno abierto hoy).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                String.format("Caja abierta con un fondo de $%.2f.", montoInicial),
                "Caja abierta", JOptionPane.INFORMATION_MESSAGE);

        cargarHistorialApertura();

        if (alAbrirCaja != null) {
            alAbrirCaja.run();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCrearcuenta1 = new javax.swing.JLabel();
        lblHistorialApertura = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        lblCrearcuenta5 = new javax.swing.JLabel();
        txtCajeroResponsable = new componentes.TextFieldModerno();
        lblCrearcuenta2 = new javax.swing.JLabel();
        txtFechaHora = new componentes.TextFieldModerno();
        lblCrearcuenta3 = new javax.swing.JLabel();
        txtMontoInicial = new componentes.TextFieldModerno();
        lblCrearcuenta4 = new javax.swing.JLabel();
        txtNotasApertura = new componentes.TextFieldModerno();
        btnAbrirCaja = new componentes.BotonModerno();
        panelRedondo2 = new componentes.PanelRedondo();
        scrollHistorialApertura = new javax.swing.JScrollPane();
        tablaHistorialApertura = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCrearcuenta1.setFont(new java.awt.Font("Lucida Bright", 1, 28)); // NOI18N
        lblCrearcuenta1.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCrearcuenta1.setText("APERTURA DE CAJA");
        add(lblCrearcuenta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 300, 45));

        lblHistorialApertura.setFont(new java.awt.Font("Lucida Bright", 1, 28)); // NOI18N
        lblHistorialApertura.setForeground(new java.awt.Color(255, 255, 255));
        lblHistorialApertura.setText("HISTORIAL DE APERTURA DE CAJA");
        add(lblHistorialApertura, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 60, 540, 35));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCrearcuenta5.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCrearcuenta5.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta5.setText("Cajero Responsable:");
        panelRedondo1.add(lblCrearcuenta5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 510, 25));

        txtCajeroResponsable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCajeroResponsableActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtCajeroResponsable, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 510, 42));

        lblCrearcuenta2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCrearcuenta2.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta2.setText("Fecha y Hora:");
        panelRedondo1.add(lblCrearcuenta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 510, 25));

        txtFechaHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaHoraActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtFechaHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 510, 42));

        lblCrearcuenta3.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCrearcuenta3.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta3.setText("Monto Inicial en Caja (Fondo / Base):");
        panelRedondo1.add(lblCrearcuenta3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 510, 25));

        txtMontoInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoInicialActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtMontoInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 510, 45));

        lblCrearcuenta4.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCrearcuenta4.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta4.setText("Observaciones / Notas de Apertura (Opcional):");
        panelRedondo1.add(lblCrearcuenta4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 510, 25));

        txtNotasApertura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNotasAperturaActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtNotasApertura, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 510, 45));

        btnAbrirCaja.setText("ABRIR CAJA");
        btnAbrirCaja.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        btnAbrirCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirCajaActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnAbrirCaja, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 460, 320, 55));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 720, 600));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaHistorialApertura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Empleado", "Fecha de Apertura", "Monto Inicial", "Notas", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollHistorialApertura.setViewportView(tablaHistorialApertura);

        panelRedondo2.add(scrollHistorialApertura, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 920, 570));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 130, 960, 610));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAbrirCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirCajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAbrirCajaActionPerformed

    private void txtNotasAperturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotasAperturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNotasAperturaActionPerformed

    private void txtMontoInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoInicialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMontoInicialActionPerformed

    private void txtFechaHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaHoraActionPerformed

    private void txtCajeroResponsableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCajeroResponsableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCajeroResponsableActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno btnAbrirCaja;
    private javax.swing.JLabel lblCrearcuenta1;
    private javax.swing.JLabel lblCrearcuenta2;
    private javax.swing.JLabel lblCrearcuenta3;
    private javax.swing.JLabel lblCrearcuenta4;
    private javax.swing.JLabel lblCrearcuenta5;
    private javax.swing.JLabel lblHistorialApertura;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private javax.swing.JScrollPane scrollHistorialApertura;
    private javax.swing.JTable tablaHistorialApertura;
    private componentes.TextFieldModerno txtCajeroResponsable;
    private componentes.TextFieldModerno txtFechaHora;
    private componentes.TextFieldModerno txtMontoInicial;
    private componentes.TextFieldModerno txtNotasApertura;
    // End of variables declaration//GEN-END:variables
}
