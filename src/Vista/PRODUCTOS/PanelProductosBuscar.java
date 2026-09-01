
package Vista.PRODUCTOS;

import Controladores.ControladorProducto;
import Modelo.Producto;

import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;

public class PanelProductosBuscar extends javax.swing.JPanel {

    private List<Producto> listaProductos;

    public PanelProductosBuscar() {
        initComponents();
        componentes.EstiloTablaKrypton.aplicar(jTable1);
        cargarProductos();

        FiltradoProductos.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                aplicarFiltro();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                aplicarFiltro();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                aplicarFiltro();
            }
        });

        jcbOrden.addActionListener(e -> aplicarFiltro());

        Btnexpexel.addActionListener(e -> exportarProductosExcel());

        jTable1.getColumnModel().getColumn(3).setCellRenderer(new RenderizadorStock());
        }

    private class RenderizadorStock extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (listaProductos != null && row >= 0 && row < listaProductos.size()) {
                Producto p = listaProductos.get(row);
                if (p.getStockMinimo() > 0 && p.getCantidad() <= p.getStockMinimo()) {
                    c.setBackground(new java.awt.Color(180, 40, 40));
                    c.setForeground(java.awt.Color.WHITE);
                } else if (p.getStockMinimo() > 0 && p.getCantidad() <= p.getStockMinimo() * 1.5) {
                    c.setBackground(new java.awt.Color(200, 140, 30));
                    c.setForeground(java.awt.Color.WHITE);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }
            }
            return c;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarProductos();
        }
    }

public void cargarProductos() {
    listaProductos = ControladorProducto.listarProductos();
    actualizarTabla();
}

private void aplicarFiltro() {
String texto = FiltradoProductos.getText();
String orden = (String) jcbOrden.getSelectedItem();
    listaProductos = ControladorProducto.filtrarProductos(texto, orden);
    actualizarTabla();
}

private void actualizarTabla() {
    DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
    modelo.setRowCount(0);

    if (listaProductos == null) {
        return;
    }

    for (Producto prod : listaProductos) {
        String textoIva = prod.isTieneIva() ? " ($" + String.format("%.2f", prod.getPrecioFinal()) + " c/IVA)" : "";

        modelo.addRow(new Object[]{
            prod.getCodigo(),
            prod.getNombre(),
            prod.getCategoria(),
            prod.getCantidad(),
            "$" + String.format("%.2f", prod.getPrecioUnitario()) + textoIva,
            prod.getFechaElaboracion(),
            prod.getFechaVencimiento(),
            "Exportar"
        });
    }
}
private void exportarProductosExcel() {
    if (listaProductos == null || listaProductos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay productos para exportar.", "Sin datos", JOptionPane.WARNING_MESSAGE);
        return;
    }
    try {
        String[] encabezados = {"Codigo", "Producto", "Categoria", "Cantidad", "Precio Unitario", "Elaboracion", "Vencimiento"};
        List<String[]> filas = new ArrayList<>();
        for (Producto p : listaProductos) {
            filas.add(new String[]{
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                String.valueOf(p.getCantidad()),
                String.format("%.2f", p.getPrecioUnitario()),
                p.getFechaElaboracion(),
                p.getFechaVencimiento()
            });
        }
        String ruta = Reportes.CarpetaExportacion.obtenerRuta("Productos.xlsx");
        Reportes.GeneradorExcel.generar(ruta, "Productos", encabezados, filas);
        JOptionPane.showMessageDialog(this, "Excel generado en:\n" + ruta);
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "No se pudo exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel1 = new javax.swing.JLabel();
        FiltradoProductos = new componentes.TextFieldModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        Btnexpexel = new componentes.BotonModerno();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("BUSCAR PRODUCTO");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, 40));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(28, 9, 40));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Producto", "Categoria", "Cantidad", "Precio Unitario", "Elaboracion", "Vencimiento", "Exportar"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1720, 640));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 1770, 710));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        FiltradoProductos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        panelRedondo1.add(FiltradoProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 300, 40));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nombre A-Z", "Nombre Z-A" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 350, 30));

        Btnexpexel.setText("Exportar a EXCEL");
        panelRedondo1.add(Btnexpexel, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 20, 200, 40));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 1150, 80));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno Btnexpexel;
    private javax.swing.JTextField FiltradoProductos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox jcbOrden;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables

}
