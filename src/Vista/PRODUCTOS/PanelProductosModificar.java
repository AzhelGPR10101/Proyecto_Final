
package Vista.PRODUCTOS;

import Vista.PRODUCTOS.*;
import java.util.List;

public class PanelProductosModificar extends javax.swing.JPanel {

    public PanelProductosModificar() {
        initComponents();
        componentes.EstiloTablaKrypton.aplicar(jTable1);
        cargarTablaModificar(jTable1);

        FiltadoProductos.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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

        jcbOrden1.addActionListener(evt -> aplicarFiltro());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && jTable1 != null) {
            cargarTablaModificar(jTable1);
        }
    }

    private void aplicarFiltro() {
        String texto = FiltadoProductos.getText();
        String orden = (String) jcbOrden1.getSelectedItem();
        cargarTablaModificar(jTable1, texto, orden);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelRedondo1 = new componentes.PanelRedondo();
        jLabel2 = new javax.swing.JLabel();
        FiltadoProductos = new componentes.TextFieldModerno();
        jcbOrden1 = new componentes.ComboBoxModerno();
        jLabel1 = new javax.swing.JLabel();
        panelRedondo2 = new componentes.PanelRedondo();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(31, 10, 48));
        jPanel1.setForeground(new java.awt.Color(36, 3, 38));
        jPanel1.setInheritsPopupMenu(true);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FIltrar:");
        panelRedondo1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        FiltadoProductos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        FiltadoProductos.setForeground(new java.awt.Color(255, 255, 255));
        FiltadoProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltadoProductosActionPerformed(evt);
            }
        });
        panelRedondo1.add(FiltadoProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 300, 40));

        jcbOrden1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jcbOrden1.setForeground(new java.awt.Color(255, 255, 255));
        jcbOrden1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre A-Z", "Nombre Z-A." }));
        panelRedondo1.add(jcbOrden1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 350, 30));

        jPanel1.add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 860, 80));

        jLabel1.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MODIFICAR PRODUCTO");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 470, -1));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(28, 9, 40));
        jTable1.setForeground(new java.awt.Color(51, 0, 51));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N", "Codigo", "Nombre", "Categoria", "Cantidad", "Precio Unit.", "Accion"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        panelRedondo2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1720, 640));

        jPanel1.add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 1770, 710));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));
    }// </editor-fold>//GEN-END:initComponents

    private void FiltadoProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiltadoProductosActionPerformed

    }//GEN-LAST:event_FiltadoProductosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField FiltadoProductos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> jcbOrden1;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    // End of variables declaration//GEN-END:variables

    public static void cargarTablaModificar(javax.swing.JTable tabla) {
        cargarTablaModificar(tabla, null, null);
    }

    public static void cargarTablaModificar(javax.swing.JTable tabla, String texto, String orden) {
        String[] columnas = {"N°", "Código", "Nombre", "Categoría", "Cantidad", "Precio Unit.", "Acción"};

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        List<Modelo.Producto> lista = Controladores.ControladorProducto.filtrarProductos(texto, orden);
        int contador = 1;
        for (Modelo.Producto p : lista) {
            modelo.addRow(new Object[]{
                contador++,
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                p.getCantidad(),
                String.format("%.2f", p.getPrecioUnitario()),
                "Modificar"
            });
        }
        tabla.setModel(modelo);

        tabla.getColumnModel().getColumn(6).setCellRenderer(
                new componentes.EstiloTablaKrypton.RenderBotonRedondeado("Modificar")
        );
        tabla.getColumnModel().getColumn(6).setCellEditor(new ButtonEditorProducto(new javax.swing.JCheckBox(), tabla));
    }
}

class ButtonEditorProducto extends javax.swing.DefaultCellEditor {

    private javax.swing.JButton button;
    private String label;
    private boolean clicked;
    private javax.swing.JTable tabla;

    public ButtonEditorProducto(javax.swing.JCheckBox checkBox, javax.swing.JTable tabla) {
        super(checkBox);
        this.tabla = tabla;
        button = new javax.swing.JButton() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                componentes.EstiloTablaKrypton.pintarFondoRedondeado(this, g);
                super.paintComponent(g);
            }
        };
        componentes.EstiloTablaKrypton.configurarBoton(button);
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clicked = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (clicked) {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                String codigo = tabla.getValueAt(filaSeleccionada, 1).toString();

                List<Modelo.Producto> lista = Controladores.ControladorProducto.listarProductos();
                Modelo.Producto encontrado = null;
                for (Modelo.Producto p : lista) {
                    if (p.getCodigo().equalsIgnoreCase(codigo)) {
                        encontrado = p;
                        break;
                    }
                }

                if (encontrado != null) {
                    Vista.PRODUCTOS.PanelDialogModificarProducto.mostrar(encontrado);

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        Vista.PRODUCTOS.PanelProductosModificar.cargarTablaModificar(tabla);
                    });
                }
            }
        }
        clicked = false;
        return label;
    }

    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
