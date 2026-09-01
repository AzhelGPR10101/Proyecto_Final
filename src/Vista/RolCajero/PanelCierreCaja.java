
package Vista.RolCajero;

import Controladores.ControladorCierreCaja;
import Modelo.CierreCaja;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PanelCierreCaja extends javax.swing.JPanel {

    private static final Color COLOR_VERDE = new Color(0, 200, 130);
    private static final Color COLOR_ROJO = new Color(220, 70, 70);
    private static final Color COLOR_BLANCO = Color.WHITE;

    private final ControladorCierreCaja controlador = new ControladorCierreCaja();
    private CierreCaja turno;
    private Runnable alCerrar;
    private Runnable alCancelar;
    private DefaultTableModel modeloHistorialCierre;

    public PanelCierreCaja() {
        initComponents();

        modeloHistorialCierre = (DefaultTableModel) tablaHistorialCierre.getModel();
        componentes.EstiloTablaKrypton.aplicar(tablaHistorialCierre);

        jButton2.addActionListener(evt -> calcularDiferencia());
        jButton1.addActionListener(evt -> confirmarCierre());
    }

    public void setAlCerrar(Runnable alCerrar) {
        this.alCerrar = alCerrar;
    }

    public void setAlCancelar(Runnable alCancelar) {
        this.alCancelar = alCancelar;
    }

    public void cargarDatosDeHoy() {
        jTextField1.setText("");
        lblCrearcuenta4.setText("$0.00");
        lblCrearcuenta4.setForeground(COLOR_BLANCO);

        turno = controlador.obtenerTurnoDeHoy();

        cargarHistorialCierre();

        if (turno == null) {
            JOptionPane.showMessageDialog(this,
                    "Todavía no has abierto caja hoy. Usa \"Apertura de Caja\" primero.",
                    "Caja no abierta", JOptionPane.WARNING_MESSAGE);
            habilitarFormulario(false);
            lblCrearcuenta8.setText("$0.00");
            lblCrearcuenta9.setText("$0.00");
            lblCrearcuenta10.setText("$0.00");
            lblCrearcuenta11.setText("$0.00");
            return;
        }

        boolean yaCerrado = !turno.estaAbierta();
        habilitarFormulario(!yaCerrado);

        turno = controlador.calcularEsperado(turno);
        lblCrearcuenta8.setText(String.format("$%.2f", turno.getTotalEfectivo()));
        lblCrearcuenta9.setText(String.format("$%.2f", turno.getTotalTarjeta()));
        lblCrearcuenta10.setText(String.format("$%.2f", turno.getTotalTransferencia()));
        lblCrearcuenta11.setText(String.format("$%.2f", turno.getMontoEsperado()));

        if (yaCerrado) {
            JOptionPane.showMessageDialog(this, "La caja de hoy ya fue cerrada.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarHistorialCierre() {
        if (modeloHistorialCierre == null) {
            return;
        }
        modeloHistorialCierre.setRowCount(0);
        List<CierreCaja> historial = controlador.listarHistorialDelNegocio();
        if (historial == null) {
            return;
        }
        for (CierreCaja c : historial) {
            String estado = c.estaAbierta() ? "Abierta" : "Cerrada";
            modeloHistorialCierre.addRow(new Object[]{
                c.getNombreEmpleado(),
                c.getFechaInicio(),
                c.getFechaFin() != null ? c.getFechaFin() : "-",
                String.format("$%.2f", c.getMontoInicial()),
                String.format("$%.2f", c.getTotalEfectivo()),
                String.format("$%.2f", c.getTotalTarjeta()),
                String.format("$%.2f", c.getTotalTransferencia()),
                String.format("$%.2f", c.getMontoEsperado()),
                String.format("$%.2f", c.getMontoReal()),
                String.format("$%.2f", c.getDiferencia()),
                estado
            });
        }
    }

    private void habilitarFormulario(boolean habilitado) {
        jTextField1.setEnabled(habilitado);
        jButton2.setEnabled(habilitado);
        jButton1.setEnabled(habilitado);
    }

    private void calcularDiferencia() {
        Double montoContado = leerMontoContado();
        if (montoContado == null || turno == null) {
            return;
        }
        double diferencia = controlador.calcularDiferencia(montoContado, turno);
        lblCrearcuenta4.setText(String.format("$%.2f", diferencia));
        if (diferencia > 0) {
            lblCrearcuenta4.setForeground(COLOR_VERDE);
        } else if (diferencia < 0) {
            lblCrearcuenta4.setForeground(COLOR_ROJO);
        } else {
            lblCrearcuenta4.setForeground(COLOR_BLANCO);
        }
    }

    private void confirmarCierre() {
        Double montoContado = leerMontoContado();
        if (montoContado == null || turno == null) {
            return;
        }

        double diferencia = controlador.calcularDiferencia(montoContado, turno);
        String mensaje = diferencia == 0
                ? "La caja cuadra exactamente. ¿Confirmar cierre?"
                : (diferencia > 0
                    ? String.format("Hay un SOBRANTE de $%.2f. ¿Confirmar cierre?", diferencia)
                    : String.format("Hay un FALTANTE de $%.2f. ¿Confirmar cierre?", Math.abs(diferencia)));

        int confirmacion = JOptionPane.showConfirmDialog(this, mensaje,
                "Confirmar cierre de caja", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = controlador.cerrarTurno(turno, montoContado);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el cierre de caja.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        habilitarFormulario(false);
        JOptionPane.showMessageDialog(this, "Caja cerrada correctamente.",
                "Cierre exitoso", JOptionPane.INFORMATION_MESSAGE);

        cargarHistorialCierre();

        if (alCerrar != null) {
            alCerrar.run();
        }
    }

    private Double leerMontoContado() {
        try {
            return Double.parseDouble(jTextField1.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa el monto contado en caja.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCrearcuenta1 = new javax.swing.JLabel();
        lblHistorialCierre = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        lblCrearcuenta2 = new javax.swing.JLabel();
        lblCrearcuenta3 = new javax.swing.JLabel();
        lblCrearcuenta5 = new javax.swing.JLabel();
        lblCrearcuenta = new javax.swing.JLabel();
        lblCrearcuenta6 = new javax.swing.JLabel();
        jTextField1 = new componentes.TextFieldModerno();
        jButton2 = new componentes.BotonModerno();
        lblCrearcuenta7 = new javax.swing.JLabel();
        lblCrearcuenta10 = new javax.swing.JLabel();
        lblCrearcuenta9 = new javax.swing.JLabel();
        lblCrearcuenta8 = new javax.swing.JLabel();
        lblCrearcuenta11 = new javax.swing.JLabel();
        lblCrearcuenta4 = new javax.swing.JLabel();
        jButton1 = new componentes.BotonModerno();
        panelRedondo2 = new componentes.PanelRedondo();
        scrollHistorialCierre = new javax.swing.JScrollPane();
        tablaHistorialCierre = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCrearcuenta1.setFont(new java.awt.Font("Lucida Bright", 1, 28)); // NOI18N
        lblCrearcuenta1.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCrearcuenta1.setText("CIERRE DE CAJA DEL DIA");
        add(lblCrearcuenta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 380, 50));

        lblHistorialCierre.setFont(new java.awt.Font("Lucida Bright", 1, 28)); // NOI18N
        lblHistorialCierre.setForeground(new java.awt.Color(255, 255, 255));
        lblHistorialCierre.setText("HISTORIAL DE CIERRE DE CAJA");
        add(lblHistorialCierre, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 40, 480, 40));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCrearcuenta2.setFont(new java.awt.Font("Lucida Bright", 1, 20)); // NOI18N
        lblCrearcuenta2.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta2.setText("Efectivo Esperado");
        panelRedondo1.add(lblCrearcuenta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 220, 28));

        lblCrearcuenta3.setFont(new java.awt.Font("Lucida Bright", 1, 20)); // NOI18N
        lblCrearcuenta3.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta3.setText("Tarjeta Esperado");
        panelRedondo1.add(lblCrearcuenta3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 220, 28));

        lblCrearcuenta5.setFont(new java.awt.Font("Lucida Bright", 1, 20)); // NOI18N
        lblCrearcuenta5.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta5.setText("Transferencia Esperado");
        panelRedondo1.add(lblCrearcuenta5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 260, 30));

        lblCrearcuenta.setFont(new java.awt.Font("Lucida Bright", 1, 26)); // NOI18N
        lblCrearcuenta.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta.setText("Total Esperado");
        panelRedondo1.add(lblCrearcuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 260, 42));

        lblCrearcuenta6.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblCrearcuenta6.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta6.setText("Monto Contado en Caja");
        panelRedondo1.add(lblCrearcuenta6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 240, 25));
        panelRedondo1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 600, 44));

        jButton2.setText("CALCULAR DIFERENCIA");
        jButton2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        panelRedondo1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, 600, 44));

        lblCrearcuenta7.setFont(new java.awt.Font("Lucida Bright", 1, 26)); // NOI18N
        lblCrearcuenta7.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta7.setText("Diferencia");
        panelRedondo1.add(lblCrearcuenta7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 550, 450, 42));

        lblCrearcuenta10.setFont(new java.awt.Font("Lucida Bright", 1, 20)); // NOI18N
        lblCrearcuenta10.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCrearcuenta10.setText("$0.00");
        panelRedondo1.add(lblCrearcuenta10, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, 160, 28));

        lblCrearcuenta9.setFont(new java.awt.Font("Lucida Bright", 1, 20)); // NOI18N
        lblCrearcuenta9.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCrearcuenta9.setText("$0.00");
        panelRedondo1.add(lblCrearcuenta9, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 160, 28));

        lblCrearcuenta8.setFont(new java.awt.Font("Lucida Bright", 1, 20)); // NOI18N
        lblCrearcuenta8.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCrearcuenta8.setText("$0.00");
        panelRedondo1.add(lblCrearcuenta8, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 160, 28));

        lblCrearcuenta11.setFont(new java.awt.Font("Lucida Bright", 1, 34)); // NOI18N
        lblCrearcuenta11.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCrearcuenta11.setText("$0.00");
        panelRedondo1.add(lblCrearcuenta11, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 160, 48));

        lblCrearcuenta4.setFont(new java.awt.Font("Lucida Bright", 1, 34)); // NOI18N
        lblCrearcuenta4.setForeground(new java.awt.Color(255, 255, 255));
        lblCrearcuenta4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCrearcuenta4.setText("$0.00");
        panelRedondo1.add(lblCrearcuenta4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 550, 160, 48));

        jButton1.setText("CONFIRMAR CIERRE");
        jButton1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        panelRedondo1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 670, 320, 55));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 690, 790));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaHistorialCierre.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Empleado", "Apertura", "Cierre", "Monto Inicial", "Efectivo", "Tarjeta", "Transferencia", "Esperado", "Real", "Diferencia", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollHistorialCierre.setViewportView(tablaHistorialCierre);

        panelRedondo2.add(scrollHistorialCierre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 990, 730));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 100, 1030, 790));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno jButton1;
    private componentes.BotonModerno jButton2;
    private componentes.TextFieldModerno jTextField1;
    private javax.swing.JLabel lblCrearcuenta;
    private javax.swing.JLabel lblCrearcuenta1;
    private javax.swing.JLabel lblCrearcuenta10;
    private javax.swing.JLabel lblCrearcuenta11;
    private javax.swing.JLabel lblCrearcuenta2;
    private javax.swing.JLabel lblCrearcuenta3;
    private javax.swing.JLabel lblCrearcuenta4;
    private javax.swing.JLabel lblCrearcuenta5;
    private javax.swing.JLabel lblCrearcuenta6;
    private javax.swing.JLabel lblCrearcuenta7;
    private javax.swing.JLabel lblCrearcuenta8;
    private javax.swing.JLabel lblCrearcuenta9;
    private javax.swing.JLabel lblHistorialCierre;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private javax.swing.JScrollPane scrollHistorialCierre;
    private javax.swing.JTable tablaHistorialCierre;
    // End of variables declaration//GEN-END:variables
}
