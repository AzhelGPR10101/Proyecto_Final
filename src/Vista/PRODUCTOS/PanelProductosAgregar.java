package Vista.PRODUCTOS;

import Controladores.ControladorCategoriaProducto;
import Modelo.CategoriaProducto;
import Modelo.Sesion;
import Modelo.Producto;
import Controladores.ControladorProducto;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PanelProductosAgregar extends javax.swing.JPanel {

    private static final int CODIGO_MIN_DIGITOS = 8;
    private static final int CODIGO_MAX_DIGITOS = 13;
    private static final int NOMBRE_MAX_CARACTERES = 1000;
    private static final int STOCK_PRECIO_MAX_DIGITOS = 8;

    public PanelProductosAgregar() {
        initComponents();
        cargarCatalogos();
        configurarGrupoIva();
        configurarValidacionesCampos();

        conectarBotonAccion();

        componentes.EstiloTablaKrypton.aplicar(tablaProductosAgregados);
        cargarTablaProductos();
    }

    private void configurarValidacionesCampos() {
        componentes.FiltrosTexto.aplicarSoloNumeros(jtCodigo, CODIGO_MAX_DIGITOS);
        componentes.FiltrosTexto.aplicarLetrasYNumeros(jtNombre, NOMBRE_MAX_CARACTERES);
        componentes.FiltrosTexto.aplicarSoloNumeros(jtStockMinimo, STOCK_PRECIO_MAX_DIGITOS);
        componentes.FiltrosTexto.aplicarSoloDecimal(jtPU, STOCK_PRECIO_MAX_DIGITOS);
    }

    private void conectarBotonAccion() {
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                guardarProducto();
            }
        });
    }

    public void cargarCatalogos() {
        ControladorCategoriaProducto controladorCategoria = new ControladorCategoriaProducto();
        String idNegocio = Sesion.getIdNegocio();
        java.util.List<CategoriaProducto> categorias = controladorCategoria.listarCategorias(idNegocio);

        java.util.Vector<String> nombres = new java.util.Vector<>();
        for (CategoriaProducto c : categorias) {
            nombres.add(c.getNombreCategoria());
        }
        jtCatalogo.setModel(new javax.swing.DefaultComboBoxModel<>(nombres));
    }

    private void guardarProducto() {
        Object catalogoSeleccionado = jtCatalogo.getSelectedItem();
        String catalogo = (catalogoSeleccionado == null) ? "" : catalogoSeleccionado.toString();

        boolean exito = ControladorProducto.registrarProducto(
                this,
                jtCodigo.getText(),
                jtNombre.getText(),
                catalogo,
                jTextField6.getText(),
                jtPU.getText(),
                rbConIva.isSelected(),
                rbSinIva.isSelected(),
                jtStockMinimo.getText(),
                txtPasillo.getText()
        );

        if (exito) {
            limpiarCampos();
            cargarTablaProductos();
        }
    }

    public void cargarTablaProductos() {
        String[] columnas = {"CÓDIGO", "NOMBRE", "CATEGORÍA", "CANTIDAD", "PRECIO UNITARIO", "IVA", "STOCK MÍNIMO"};

        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Producto> lista = ControladorProducto.listarProductos();

        for (Producto p : lista) {
            Object[] fila = new Object[7];
            fila[0] = p.getCodigo();
            fila[1] = p.getNombre();
            fila[2] = p.getCategoria();
            fila[3] = p.getCantidad();
            fila[4] = "$" + String.format("%.2f", p.getPrecioUnitario());
            fila[5] = p.isTieneIva() ? "CON IVA" : "SIN IVA";
            fila[6] = p.getStockMinimo();

            modelo.addRow(fila);
        }

        tablaProductosAgregados.setModel(modelo);
    }

    private void configurarGrupoIva() {
        javax.swing.ButtonGroup grupoIva = new javax.swing.ButtonGroup();
        grupoIva.add(rbConIva);
        grupoIva.add(rbSinIva);
        rbConIva.setSelected(true);
    }

    private void limpiarCampos() {
        jtCodigo.setText("");
        jtNombre.setText("");
        jTextField6.setText("");
        if (jtCatalogo.getItemCount() > 0) {
            jtCatalogo.setSelectedIndex(0);
        }
        jtPU.setText("");
        jtStockMinimo.setText("");
        txtPasillo.setText("");
        rbConIva.setSelected(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GrupoIva = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        lblListaProductos = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jScrollPaneProductos = new javax.swing.JScrollPane();
        tablaProductosAgregados = new javax.swing.JTable();
        panelRedondo2 = new componentes.PanelRedondo();
        jbCodigo1 = new javax.swing.JLabel();
        jtCodigo = new componentes.TextFieldModerno();
        jbCI = new javax.swing.JLabel();
        jTextField6 = new componentes.TextFieldModerno();
        jtStockMinimo = new componentes.TextFieldModerno();
        lblStockMinimo = new javax.swing.JLabel();
        lblPasillo = new javax.swing.JLabel();
        txtPasillo = new componentes.TextFieldModerno();
        txtFechElavoracion = new componentes.TextFieldModerno();
        elaboracion = new javax.swing.JLabel();
        jtNombre = new componentes.TextFieldModerno();
        jbNombre = new javax.swing.JLabel();
        jtCatalogo = new componentes.ComboBoxModerno();
        jbCatalogo = new javax.swing.JLabel();
        vencimiento = new javax.swing.JLabel();
        txtFechaVencimiento = new componentes.TextFieldModerno();
        rbConIva = new javax.swing.JRadioButton();
        rbSinIva = new javax.swing.JRadioButton();
        jtPU = new componentes.TextFieldModerno();
        jbPU = new javax.swing.JLabel();
        jButton1 = new componentes.BotonModerno();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CREAR PRODUCTO ");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));

        lblListaProductos.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblListaProductos.setForeground(new java.awt.Color(255, 255, 255));
        lblListaProductos.setText("LISTA DE PRODUCTOS");
        add(lblListaProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 500, -1, -1));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaProductosAgregados.setBackground(new java.awt.Color(28, 9, 40));
        tablaProductosAgregados.setForeground(new java.awt.Color(255, 255, 255));
        tablaProductosAgregados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÓDIGO", "NOMBRE", "CATEGORÍA", "CANTIDAD", "PRECIO UNITARIO", "IVA", "STOCK MÍNIMO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneProductos.setViewportView(tablaProductosAgregados);

        panelRedondo1.add(jScrollPaneProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1740, 270));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 560, 1780, 330));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbCodigo1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jbCodigo1.setForeground(new java.awt.Color(255, 255, 255));
        jbCodigo1.setText("Codigo:");
        panelRedondo2.add(jbCodigo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));

        jtCodigo.setBackground(new java.awt.Color(31, 10, 60));
        jtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtCodigoActionPerformed(evt);
            }
        });
        panelRedondo2.add(jtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 330, 40));

        jbCI.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jbCI.setForeground(new java.awt.Color(255, 255, 255));
        jbCI.setText("stock Inicial:");
        panelRedondo2.add(jbCI, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, -1));

        jTextField6.setBackground(new java.awt.Color(31, 10, 60));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        panelRedondo2.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 330, 40));

        jtStockMinimo.setBackground(new java.awt.Color(31, 10, 60));
        jtStockMinimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtStockMinimoActionPerformed(evt);
            }
        });
        panelRedondo2.add(jtStockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 330, 40));

        lblStockMinimo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblStockMinimo.setForeground(new java.awt.Color(255, 255, 255));
        lblStockMinimo.setText("Stock minimo:");
        panelRedondo2.add(lblStockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, -1));

        lblPasillo.setBackground(new java.awt.Color(255, 255, 255));
        lblPasillo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblPasillo.setForeground(new java.awt.Color(255, 255, 255));
        lblPasillo.setText("Pasillo:");
        panelRedondo2.add(lblPasillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 230, -1, -1));

        txtPasillo.setBackground(new java.awt.Color(31, 10, 60));
        txtPasillo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasilloActionPerformed(evt);
            }
        });
        panelRedondo2.add(txtPasillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 260, 330, 40));

        txtFechElavoracion.setBackground(new java.awt.Color(31, 10, 60));
        panelRedondo2.add(txtFechElavoracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 170, 330, 40));

        elaboracion.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        elaboracion.setForeground(new java.awt.Color(255, 255, 255));
        elaboracion.setText("Elaboración:");
        panelRedondo2.add(elaboracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, -1, -1));

        jtNombre.setBackground(new java.awt.Color(31, 10, 60));
        panelRedondo2.add(jtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, 330, 40));

        jbNombre.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jbNombre.setForeground(new java.awt.Color(255, 255, 255));
        jbNombre.setText("Nombre:");
        panelRedondo2.add(jbNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, -1, -1));

        jtCatalogo.setBackground(new java.awt.Color(26, 16, 36));
        jtCatalogo.setForeground(new java.awt.Color(255, 255, 255));
        jtCatalogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtCatalogoActionPerformed(evt);
            }
        });
        panelRedondo2.add(jtCatalogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 80, 300, 40));

        jbCatalogo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jbCatalogo.setForeground(new java.awt.Color(255, 255, 255));
        jbCatalogo.setText("Categoria:");
        panelRedondo2.add(jbCatalogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 50, 140, -1));

        vencimiento.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        vencimiento.setForeground(new java.awt.Color(255, 255, 255));
        vencimiento.setText("Vencimiento:");
        panelRedondo2.add(vencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 140, 160, -1));

        txtFechaVencimiento.setBackground(new java.awt.Color(31, 10, 60));
        panelRedondo2.add(txtFechaVencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 170, 300, 40));

        rbConIva.setBackground(new java.awt.Color(31, 11, 43));
        GrupoIva.add(rbConIva);
        rbConIva.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        rbConIva.setForeground(new java.awt.Color(255, 255, 255));
        rbConIva.setText("CON IVA ");
        rbConIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbConIvaActionPerformed(evt);
            }
        });
        panelRedondo2.add(rbConIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 140, 180, -1));

        rbSinIva.setBackground(new java.awt.Color(31, 11, 43));
        GrupoIva.add(rbSinIva);
        rbSinIva.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        rbSinIva.setForeground(new java.awt.Color(255, 255, 255));
        rbSinIva.setText("SIN IVA ");
        rbSinIva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSinIvaActionPerformed(evt);
            }
        });
        panelRedondo2.add(rbSinIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 170, 170, -1));

        jtPU.setBackground(new java.awt.Color(31, 10, 60));
        jtPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtPUActionPerformed(evt);
            }
        });
        panelRedondo2.add(jtPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 80, 290, 40));

        jbPU.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jbPU.setForeground(new java.awt.Color(255, 255, 255));
        jbPU.setText("Precio Unitario:");
        panelRedondo2.add(jbPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 50, 200, -1));

        jButton1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        jButton1.setText("Registrar Producto");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelRedondo2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1370, 270, 220, 60));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 1780, 360));
    }// </editor-fold>//GEN-END:initComponents

    private void jtPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtPUActionPerformed

    }//GEN-LAST:event_jtPUActionPerformed

    private void jtCatalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtCatalogoActionPerformed

    }//GEN-LAST:event_jtCatalogoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rbConIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbConIvaActionPerformed

    }//GEN-LAST:event_rbConIvaActionPerformed

    private void rbSinIvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSinIvaActionPerformed

    }//GEN-LAST:event_rbSinIvaActionPerformed

    private void jtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtCodigoActionPerformed

    }//GEN-LAST:event_jtCodigoActionPerformed

    private void txtPasilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasilloActionPerformed

    }//GEN-LAST:event_txtPasilloActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed

    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jtStockMinimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtStockMinimoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtStockMinimoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GrupoIva;
    private javax.swing.JLabel elaboracion;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPaneProductos;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel jbCI;
    private javax.swing.JLabel jbCatalogo;
    private javax.swing.JLabel jbCodigo1;
    private javax.swing.JLabel jbNombre;
    private javax.swing.JLabel jbPU;
    private javax.swing.JComboBox jtCatalogo;
    private javax.swing.JTextField jtCodigo;
    private javax.swing.JTextField jtNombre;
    private javax.swing.JTextField jtPU;
    private javax.swing.JTextField jtStockMinimo;
    private javax.swing.JLabel lblListaProductos;
    private javax.swing.JLabel lblPasillo;
    private javax.swing.JLabel lblStockMinimo;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private javax.swing.JRadioButton rbConIva;
    private javax.swing.JRadioButton rbSinIva;
    private javax.swing.JTable tablaProductosAgregados;
    private javax.swing.JTextField txtFechElavoracion;
    private javax.swing.JTextField txtFechaVencimiento;
    private javax.swing.JTextField txtPasillo;
    private javax.swing.JLabel vencimiento;
    // End of variables declaration//GEN-END:variables

}
