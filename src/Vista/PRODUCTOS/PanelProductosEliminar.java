
package Vista.PRODUCTOS;

import Controladores.ControladorProducto;
import Modelo.Producto;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.DefaultCellEditor;
import java.awt.Component;
import java.util.List;

public class PanelProductosEliminar extends javax.swing.JPanel {

   public PanelProductosEliminar() {
    initComponents();
    configurarTablaBotones();
    componentes.EstiloTablaKrypton.aplicar(jTable1);
    cargarProductos();

    FiltradoProductos.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { aplicarFiltro(); }
    });

    jcbOrden.addActionListener(e -> aplicarFiltro());
}

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarProductos();
        }
    }

  public void cargarProductos() {
    List<Producto> lista = ControladorProducto.listarProductos();
    pintarTabla(lista);
}

private void aplicarFiltro() {
    String texto = FiltradoProductos.getText();
    String orden = (String) jcbOrden.getSelectedItem();
    List<Producto> lista = ControladorProducto.filtrarProductos(texto, orden);
    pintarTabla(lista);
}

private void pintarTabla(List<Producto> lista) {
    DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
    modelo.setRowCount(0);

    if (lista != null) {
        for (Producto prod : lista) {
            modelo.addRow(new Object[]{
                prod.getCodigo(),
                prod.getNombre(),
                prod.getCategoria(),
                prod.getCantidad(),
                "$" + String.format("%.2f", prod.getPrecioUnitario()),
                prod.getFechaElaboracion(),
                prod.getFechaVencimiento(),
                "Eliminar"
            });
        }
    }
}

    private void configurarTablaBotones() {

        jTable1.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new javax.swing.JCheckBox(), this));
        jTable1.setRowHeight(35);
    }

    class ButtonRenderer extends javax.swing.JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new java.awt.Color(204, 0, 51));
            setForeground(java.awt.Color.WHITE);
            setFocusPainted(false);
            setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Eliminar" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

    protected javax.swing.JButton button;
    private String label;
    private PanelProductosEliminar panel;

    public ButtonEditor(javax.swing.JCheckBox checkBox, PanelProductosEliminar panel) {
        super(checkBox);
        this.panel = panel;

        button = new javax.swing.JButton();
        button.setOpaque(true);
        button.setBackground(new java.awt.Color(204, 0, 51));
        button.setForeground(java.awt.Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new java.awt.Font("sansserif", java.awt.Font.BOLD, 12));

        button.addActionListener(e -> {

            int row = jTable1.getEditingRow();

            if (row < 0) {
                return;
            }

            String codigo = jTable1.getValueAt(row, 0).toString();

            int confirmacion = JOptionPane.showConfirmDialog(
                    panel,
                    "¿Está seguro de eliminar permanentemente el producto con código: " + codigo + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {

                System.out.println("Intentando eliminar codigo: [" + codigo + "]");

                boolean eliminado = ControladorProducto.eliminarProducto(panel, codigo);

                System.out.println("Resultado eliminar: " + eliminado);

                fireEditingStopped();

                if (eliminado) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        panel.cargarProductos();
                    });
                }

            } else {

                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        label = (value == null) ? "Eliminar" : value.toString();
        button.setText(label);

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel1 = new javax.swing.JLabel();
        FiltradoProductos = new componentes.TextFieldModerno();
        jcbOrden = new componentes.ComboBoxModerno();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ELIMINAR PRODUCTO");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FIltrar:");
        panelRedondo1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        FiltradoProductos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        panelRedondo1.add(FiltradoProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 300, 40));

        jcbOrden.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nombre A-Z", "Nombre Z-A" }));
        panelRedondo1.add(jcbOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 350, 30));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 860, 80));

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
                "Codigo", "Producto", "Categoria", "Cantidad", "Precio Unitario", "Elavoracion", "Vencimiento", ""
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1720, 640));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 1770, 710));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
