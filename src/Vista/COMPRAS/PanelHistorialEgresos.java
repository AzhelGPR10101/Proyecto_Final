
package Vista.COMPRAS;
import Controladores.ControladorEgreso;
import Modelo.Egreso;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class PanelHistorialEgresos extends javax.swing.JPanel {

    private final ControladorEgreso controladorEgreso = new ControladorEgreso();
    private List<Egreso> listaEgresos;
    private DefaultTableModel modeloTabla;

    public PanelHistorialEgresos() {
        initComponents();

        modeloTabla = (DefaultTableModel) tabla.getModel();
        componentes.EstiloTablaKrypton.aplicar(tabla);
        cargarEgresos();

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
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarEgresos();
        }
    }

    private void cargarEgresos() {
        listaEgresos = controladorEgreso.listarEgresos();
        actualizarTabla();
        lblTotal.setText(String.format("$%.2f", controladorEgreso.totalEgresos()));
    }

    private void aplicarFiltro() {
        listaEgresos = controladorEgreso.filtrarEgresos(txtFiltro.getText());
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        if (listaEgresos == null) {
            return;
        }
        for (Egreso e : listaEgresos) {
            modeloTabla.addRow(new Object[]{
                e.getFecha(), e.getConcepto(), e.getNumFacturaProveedor(), e.getProveedor(),
                e.getMetodoPago(), String.format("$%.2f", e.getMonto())
            });
        }
    }
    private void exportarEgresosExcel() {
    if (listaEgresos == null || listaEgresos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay egresos para exportar.", "Sin datos", JOptionPane.WARNING_MESSAGE);
        return;
    }
    try {
        String[] encabezados = {"Fecha", "Concepto", "N Factura Proveedor", "Proveedor", "Metodo de Pago", "Monto"};
        List<String[]> filas = new ArrayList<>();
        for (Egreso e : listaEgresos) {
            filas.add(new String[]{
                e.getFecha(),
                e.getConcepto(),
                e.getNumFacturaProveedor(),
                e.getProveedor(),
                e.getMetodoPago(),
                String.format("%.2f", e.getMonto())
            });
        }
        String ruta = Reportes.CarpetaExportacion.obtenerRuta("Egresos.xlsx");
        Reportes.GeneradorExcel.generar(ruta, "Egresos", encabezados, filas);
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
        BtnExpExcel = new componentes.BotonModerno();
        panelRedondo3 = new componentes.PanelRedondo();
        lblTotalTexto = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(null);

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 32)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Historial de Egresos");
        add(lblTitulo);
        lblTitulo.setBounds(90, 60, 600, 40);

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Concepto", "N° Factura Proveedor", "Proveedor", "Método de Pago", "Monto"
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

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFiltro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFiltro.setForeground(new java.awt.Color(255, 255, 255));
        lblFiltro.setText("Filtrar:");
        panelRedondo2.add(lblFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 30));
        panelRedondo2.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 290, 30));

        BtnExpExcel.setText("Exportar a EXCEL");
        BtnExpExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnExpExcelActionPerformed(evt);
            }
        });
        panelRedondo2.add(BtnExpExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 220, 30));

        add(panelRedondo2);
        panelRedondo2.setBounds(1080, 40, 720, 70);

        panelRedondo3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalTexto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalTexto.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalTexto.setText("Total egresos:");
        panelRedondo3.add(lblTotalTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, 30));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 100, 100));
        lblTotal.setText("$0.00");
        panelRedondo3.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 150, 30));

        add(panelRedondo3);
        panelRedondo3.setBounds(1510, 950, 290, 50);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnExpExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnExpExcelActionPerformed
        exportarEgresosExcel();
    }//GEN-LAST:event_BtnExpExcelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno BtnExpExcel;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalTexto;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private componentes.PanelRedondo panelRedondo3;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    private componentes.TextFieldModerno txtFiltro;
    // End of variables declaration//GEN-END:variables
}
