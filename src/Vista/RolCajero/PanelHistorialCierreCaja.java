package Vista.RolCajero;

import Controladores.ControladorCierreCaja;
import Modelo.CierreCaja;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class PanelHistorialCierreCaja extends javax.swing.JPanel {

        private final ControladorCierreCaja controladorCierreCaja = new ControladorCierreCaja();
    private List<CierreCaja> listaCierres;
    private List<CierreCaja> listaMostrada;
    private DefaultTableModel modeloTabla;

    public PanelHistorialCierreCaja() {
        initComponents();

        modeloTabla = (DefaultTableModel) tabla.getModel();
        componentes.EstiloTablaKrypton.aplicar(tabla);
        cargarHistorial();

        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            public void removeUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            public void changedUpdate(DocumentEvent e) {
                aplicarFiltro();
            }
        });

        botonModerno1.setVisible(Modelo.Sesion.esDueno());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarHistorial();
        }
    }

    private void cargarHistorial() {
        listaCierres = controladorCierreCaja.listarHistorialDelNegocio();
        actualizarTabla(listaCierres);
    }

    private void aplicarFiltro() {
        String textoLower = txtFiltro.getText() == null ? "" : txtFiltro.getText().trim().toLowerCase();
        if (textoLower.isEmpty()) {
            actualizarTabla(listaCierres);
            return;
        }
        List<CierreCaja> filtrados = new ArrayList<>();
        if (listaCierres != null) {
            for (CierreCaja c : listaCierres) {
                if (c.getNombreEmpleado() != null && c.getNombreEmpleado().toLowerCase().contains(textoLower)) {
                    filtrados.add(c);
                }
            }
        }
        actualizarTabla(filtrados);
    }

       private void actualizarTabla(List<CierreCaja> lista) {
        listaMostrada = lista;
        modeloTabla.setRowCount(0);
        if (lista == null) {
            return;
        }
        for (CierreCaja c : lista) {
            String estado = c.estaAbierta() ? "Abierta" : "Cerrada";
            modeloTabla.addRow(new Object[]{
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
    private void reabrirSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || listaMostrada == null || fila >= listaMostrada.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona primero un cierre en la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CierreCaja seleccionado = listaMostrada.get(fila);
        if (seleccionado.estaAbierta()) {
            JOptionPane.showMessageDialog(this, "Ese cierre ya está abierto.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Reabrir la caja de " + seleccionado.getNombreEmpleado() + " del " + seleccionado.getFechaInicio() + "?",
                "Confirmar reapertura", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }
        boolean exito = controladorCierreCaja.reabrirCierre(seleccionado.getIdCierre());
        if (exito) {
            JOptionPane.showMessageDialog(this, "Caja reabierta correctamente.");
            cargarHistorial();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo reabrir la caja.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void exportarHistorialExcel() {
        if (listaCierres == null || listaCierres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay cierres de caja para exportar.", "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String[] encabezados = {"Empleado", "Apertura", "Cierre", "Monto Inicial", "Efectivo", "Tarjeta", "Transferencia", "Esperado", "Real", "Diferencia", "Estado"};
            List<String[]> filas = new ArrayList<>();
            for (CierreCaja c : listaCierres) {
                filas.add(new String[]{
                    c.getNombreEmpleado(),
                    c.getFechaInicio(),
                    c.getFechaFin() != null ? c.getFechaFin() : "-",
                    String.format("%.2f", c.getMontoInicial()),
                    String.format("%.2f", c.getTotalEfectivo()),
                    String.format("%.2f", c.getTotalTarjeta()),
                    String.format("%.2f", c.getTotalTransferencia()),
                    String.format("%.2f", c.getMontoEsperado()),
                    String.format("%.2f", c.getMontoReal()),
                    String.format("%.2f", c.getDiferencia()),
                    c.estaAbierta() ? "Abierta" : "Cerrada"
                });
            }
            String ruta = System.getProperty("user.home") + java.io.File.separator + "HistorialCierreCaja.xlsx";
            Reportes.GeneradorExcel.generar(ruta, "Historial Cierre Caja", encabezados, filas);
            JOptionPane.showMessageDialog(this, "Excel generado en:\n" + ruta);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        panelRedondo2 = new componentes.PanelRedondo();
        lblFiltro = new javax.swing.JLabel();
        txtFiltro = new componentes.TextFieldModerno();
        botonModerno1 = new componentes.BotonModerno();
        botonModerno2 = new componentes.BotonModerno();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(null);

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 32)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Historial de Cierre de Caja");
        add(lblTitulo);
        lblTitulo.setBounds(90, 60, 600, 40);

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabla.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollTabla.setViewportView(tabla);

        panelRedondo1.add(scrollTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1670, 760));

        add(panelRedondo1);
        panelRedondo1.setBounds(90, 120, 1710, 820);

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFiltro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFiltro.setForeground(new java.awt.Color(255, 255, 255));
        lblFiltro.setText("Filtrar por empleado:");
        panelRedondo2.add(lblFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, 30));
        panelRedondo2.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 220, 30));

        botonModerno1.setText("Reapertura de caja");
        botonModerno1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModerno1ActionPerformed(evt);
            }
        });
        panelRedondo2.add(botonModerno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 190, 50));

        botonModerno2.setText("Exportar en ECXEL");
        botonModerno2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModerno2ActionPerformed(evt);
            }
        });
        panelRedondo2.add(botonModerno2, new org.netbeans.lib.awtextra.AbsoluteConstraints(663, 8, 170, 50));

        add(panelRedondo2);
        panelRedondo2.setBounds(930, 40, 870, 70);
    }// </editor-fold>//GEN-END:initComponents

    private void botonModerno1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModerno1ActionPerformed
        reabrirSeleccionada();
    }//GEN-LAST:event_botonModerno1ActionPerformed

    private void botonModerno2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModerno2ActionPerformed
        exportarHistorialExcel();
    }//GEN-LAST:event_botonModerno2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno botonModerno1;
    private componentes.BotonModerno botonModerno2;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    private componentes.TextFieldModerno txtFiltro;
    // End of variables declaration//GEN-END:variables
}
