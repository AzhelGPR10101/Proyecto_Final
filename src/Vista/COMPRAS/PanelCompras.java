package Vista.COMPRAS;
 
import Controladores.ControladorCompra;
import Modelo.DetalleCompra;
import Modelo.Producto;
import Modelo.Proveedores;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
 
public class PanelCompras extends javax.swing.JPanel {
 
    private java.util.List<Proveedores> listaProveedores;
    private java.util.List<Producto> listaProductos;
    private final ControladorCompra controladorCompra = new ControladorCompra();
    private final List<DetalleCompra> listaDetalles = new ArrayList<>();
    private DefaultTableModel modeloTabla;
    private final javax.swing.ButtonGroup grupoFormaPago = new javax.swing.ButtonGroup();
 
    public PanelCompras() {
        initComponents();
        grupoFormaPago.add(rbContado);
        grupoFormaPago.add(rbCredito);
 
        modeloTabla = (DefaultTableModel) tablaDetalle.getModel();
 
        componentes.EstiloTablaKrypton.aplicar(tablaDetalle);
 
        cbProducto.addItemListener(e -> {
            int idx = cbProducto.getSelectedIndex();
            if (idx > 0 && listaProductos != null && idx - 1 < listaProductos.size()) {
                Producto seleccionado = listaProductos.get(idx - 1);
                txtCostoUnitario.setText(String.format("%.2f", seleccionado.getPrecioUnitario()));
            }
        });
 
        cargarProveedores();
 
        txtBuscarProveedor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarProveedores();
            }
 
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarProveedores();
            }
 
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarProveedores();
            }
        });
 
        cargarProductos();
        actualizarModoPago();
    }
 
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarProveedores();
            cargarProductos();
        }
    }
 
    private void actualizarModoPago() {
        boolean contado = rbContado.isSelected();
        cbMetodoPago.setEnabled(contado);
        txtFechaVencimiento.setEnabled(!contado);
        if (contado) {
            txtFechaVencimiento.setText("");
        }
    }
 
    private void cargarProveedores() {
        listaProveedores = controladorCompra.listarProveedores();
        llenarComboProveedores(listaProveedores);
    }
 
    private void llenarComboProveedores(List<Proveedores> lista) {
        cbProveedor.removeAllItems();
        cbProveedor.addItem("Seleccione Proveedor.");
        for (Proveedores p : lista) {
            cbProveedor.addItem(p.toString());
        }
    }
 
    private void filtrarProveedores() {
        String texto = txtBuscarProveedor.getText().trim();
        listaProveedores = controladorCompra.filtrarProveedores(texto);
        llenarComboProveedores(listaProveedores);

        if (!texto.isEmpty() && listaProveedores.size() == 1) {
            cbProveedor.setSelectedIndex(1);
        }
    }
 
    private void cargarProductos() {
        cbProducto.removeAllItems();
        cbProducto.addItem("Seleccione Producto.");
        listaProductos = controladorCompra.listarProductos();
        for (Producto p : listaProductos) {
            cbProducto.addItem(p.toString());
        }
    }
 
    private void agregarProducto() {
        int idxProducto = cbProducto.getSelectedIndex();
        Producto seleccionado = (idxProducto > 0) ? listaProductos.get(idxProducto - 1) : null;
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto válido.");
            return;
        }
        int cantidad;
        double costo;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.");
            return;
        }
        try {
            costo = Double.parseDouble(txtCostoUnitario.getText().trim());
            if (costo < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un costo unitario válido.");
            return;
        }
 
        Controladores.ControladorCompra.ResultadoAgregarCarrito resultado
                = controladorCompra.agregarAlCarrito(listaDetalles, seleccionado, cantidad, costo);
        DetalleCompra detalle = resultado.detalle;

        if (resultado.indiceActualizado >= 0) {
            modeloTabla.setValueAt(detalle.getCantidad(), resultado.indiceActualizado, 1);
            modeloTabla.setValueAt(String.format("$%.2f", detalle.getCostoUnitario()), resultado.indiceActualizado, 2);
            modeloTabla.setValueAt(String.format("$%.2f", detalle.getSubtotal()), resultado.indiceActualizado, 3);

            JOptionPane.showMessageDialog(this, "Ese producto ya estaba en la lista, se sumó la cantidad.");
        } else {
            listaDetalles.add(detalle);
            modeloTabla.addRow(new Object[]{
                detalle.getNombreProducto(), detalle.getCantidad(),
                String.format("$%.2f", detalle.getCostoUnitario()),
                String.format("$%.2f", detalle.getSubtotal())
            });
        }

        txtCantidad.setText("");
        actualizarTotales();
    }
 
    private void quitarProductoSeleccionado() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila de la tabla para quitarla.");
            return;
        }
        listaDetalles.remove(fila);
        modeloTabla.removeRow(fila);
        actualizarTotales();
    }
 
    private void actualizarTotales() {
        double descuento;
        try {
            descuento = Double.parseDouble(txtDescuento.getText().trim());
        } catch (NumberFormatException ex) {
            descuento = 0;
        }
        double[] totales = controladorCompra.calcularTotales(listaDetalles, descuento);
        lblSubtotal.setText(String.format("$%.2f", totales[0]));
        lblIva.setText(String.format("$%.2f", totales[1]));
        lblTotal.setText(String.format("$%.2f", totales[2]));
    }
 
    private void guardarCompra() {
        double descuento;
        try {
            descuento = Double.parseDouble(txtDescuento.getText().trim());
        } catch (NumberFormatException ex) {
            descuento = 0;
        }
 
        boolean pagoContado = rbContado.isSelected();
        String metodoPago = (String) cbMetodoPago.getSelectedItem();
        String fechaVencimiento = txtFechaVencimiento.getText().trim();
 
        if (!pagoContado && !fechaVencimiento.isEmpty()) {
            try {
                LocalDate.parse(fechaVencimiento, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "La fecha de vencimiento no es válida. Usa el formato aaaa-mm-dd, por ejemplo 2026-08-24.",
                        "Fecha inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
 
       int idxProveedor = cbProveedor.getSelectedIndex();
Proveedores proveedor = (idxProveedor > 0) ? listaProveedores.get(idxProveedor - 1) : null;
 
        boolean exito = controladorCompra.registrarCompra(this, txtNumFacturaProveedor.getText(),
                proveedor, listaDetalles, descuento, pagoContado, metodoPago, fechaVencimiento);
 
        if (exito) {
            listaDetalles.clear();
            modeloTabla.setRowCount(0);
            txtNumFacturaProveedor.setText("");
            txtDescuento.setText("0");
            txtFechaVencimiento.setText("");
            actualizarTotales();
            cargarProductos();
        }
    }
 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        lblDescuento = new javax.swing.JLabel();
        btnQuitar = new componentes.BotonModerno();
        lblQuitar = new javax.swing.JLabel();
        txtDescuento = new componentes.TextFieldModerno();
        lblSubtotalTxt = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblIvaTxt = new javax.swing.JLabel();
        lblIva = new javax.swing.JLabel();
        lblTotalTxt = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        btnGuardar = new componentes.BotonModerno();
        lblFormaPago = new javax.swing.JLabel();
        rbContado = new javax.swing.JRadioButton();
        rbCredito = new javax.swing.JRadioButton();
        lblMetodoPago = new javax.swing.JLabel();
        cbMetodoPago = new componentes.ComboBoxModerno();
        lblFechaVencimiento = new javax.swing.JLabel();
        txtFechaVencimiento = new componentes.FechaModerna();
        panelRedondo2 = new componentes.PanelRedondo();
        scrollTabla = new javax.swing.JScrollPane();
        tablaDetalle = new javax.swing.JTable();
        panelRedondo3 = new componentes.PanelRedondo();
        lblProducto = new javax.swing.JLabel();
        lblProveedor = new javax.swing.JLabel();
        cbProveedor = new componentes.ComboBoxModerno();
        cbProducto = new componentes.ComboBoxModerno();
        lblFactura = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new componentes.TextFieldModerno();
        txtNumFacturaProveedor = new componentes.TextFieldModerno();
        lblCosto = new javax.swing.JLabel();
        txtCostoUnitario = new componentes.TextFieldModerno();
        txtBuscarProveedor = new componentes.TextFieldModerno();
        txBuscar = new javax.swing.JLabel();
        btnAgregar = new componentes.BotonModerno();

        setBackground(new java.awt.Color(31, 10, 48));
        setLayout(null);

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 32)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Registrar Compra a Proveedor");
        add(lblTitulo);
        lblTitulo.setBounds(90, 40, 700, 45);

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDescuento.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDescuento.setForeground(new java.awt.Color(255, 255, 255));
        lblDescuento.setText("Descuento:");
        panelRedondo1.add(lblDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120, 100, 25));

        btnQuitar.setText("Quitar Producto");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnQuitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 160, 35));

        lblQuitar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblQuitar.setForeground(new java.awt.Color(255, 255, 255));
        lblQuitar.setText("Quitar seleccionado:");
        panelRedondo1.add(lblQuitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 170, 20));

        txtDescuento.setText("0");
        txtDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescuentoActionPerformed(evt);
            }
        });
        panelRedondo1.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 120, 90, 30));

        lblSubtotalTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSubtotalTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblSubtotalTxt.setText("Subtotal:");
        panelRedondo1.add(lblSubtotalTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 120, 90, 30));

        lblSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(255, 255, 255));
        lblSubtotal.setText("$0.00");
        panelRedondo1.add(lblSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 120, 90, 30));

        lblIvaTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblIvaTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblIvaTxt.setText("IVA (15%):");
        panelRedondo1.add(lblIvaTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 125, 100, 20));

        lblIva.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblIva.setForeground(new java.awt.Color(255, 255, 255));
        lblIva.setText("$0.00");
        panelRedondo1.add(lblIva, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 120, 70, 30));

        lblTotalTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalTxt.setText("TOTAL:");
        panelRedondo1.add(lblTotalTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 125, 80, -1));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(0, 230, 150));
        lblTotal.setText("$0.00");
        panelRedondo1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 120, 100, 32));

        btnGuardar.setText("Guardar Compra");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        panelRedondo1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 70, 190, 40));

        lblFormaPago.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFormaPago.setForeground(new java.awt.Color(255, 255, 255));
        lblFormaPago.setText("Forma de pago:");
        panelRedondo1.add(lblFormaPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 130, 25));

        rbContado.setBackground(new java.awt.Color(26, 16, 36));
        rbContado.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rbContado.setForeground(new java.awt.Color(255, 255, 255));
        rbContado.setSelected(true);
        rbContado.setText("Contado");
        rbContado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbContadoActionPerformed(evt);
            }
        });
        panelRedondo1.add(rbContado, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 110, 30));

        rbCredito.setBackground(new java.awt.Color(26, 16, 36));
        rbCredito.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rbCredito.setForeground(new java.awt.Color(255, 255, 255));
        rbCredito.setText("Crédito (genera pagaré)");
        rbCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCreditoActionPerformed(evt);
            }
        });
        panelRedondo1.add(rbCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 260, 30));

        lblMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMetodoPago.setForeground(new java.awt.Color(255, 255, 255));
        lblMetodoPago.setText("Método de pago:");
        panelRedondo1.add(lblMetodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 140, 25));

        cbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Efectivo" }));
        cbMetodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMetodoPagoActionPerformed(evt);
            }
        });
        panelRedondo1.add(cbMetodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 30, 160, 30));

        lblFechaVencimiento.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFechaVencimiento.setForeground(new java.awt.Color(255, 255, 255));
        lblFechaVencimiento.setText("Vence pagaré (AAAA-MM-DD):");
        panelRedondo1.add(lblFechaVencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 30, 250, 25));

        panelRedondo1.add(txtFechaVencimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 30, 150, 30));

        add(panelRedondo1);
        panelRedondo1.setBounds(90, 750, 1710, 170);

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Costo Unit.", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTabla.setViewportView(tablaDetalle);

        panelRedondo2.add(scrollTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 1660, 450));

        add(panelRedondo2);
        panelRedondo2.setBounds(90, 240, 1710, 490);

        panelRedondo3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblProducto.setText("Producto:");
        panelRedondo3.add(lblProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 100, 25));

        lblProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblProveedor.setForeground(new java.awt.Color(255, 255, 255));
        lblProveedor.setText("Proveedor:");
        panelRedondo3.add(lblProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 100, 25));

        cbProveedor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecionar Proveedor" }));
        cbProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProveedorActionPerformed(evt);
            }
        });
        panelRedondo3.add(cbProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 260, 30));

        cbProducto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecionar Producto" }));
        cbProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProductoActionPerformed(evt);
            }
        });
        panelRedondo3.add(cbProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 260, 30));

        lblFactura.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFactura.setForeground(new java.awt.Color(255, 255, 255));
        lblFactura.setText("N° Factura Proveedor:");
        panelRedondo3.add(lblFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 190, 25));

        lblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCantidad.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidad.setText("Cantidad:");
        panelRedondo3.add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, 90, 25));

        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });
        panelRedondo3.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 20, 240, 30));

        txtNumFacturaProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumFacturaProveedorActionPerformed(evt);
            }
        });
        panelRedondo3.add(txtNumFacturaProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 270, 30));

        lblCosto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCosto.setForeground(new java.awt.Color(255, 255, 255));
        lblCosto.setText("Costo Unit.:");
        panelRedondo3.add(lblCosto, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 70, 90, 25));

        txtCostoUnitario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoUnitarioActionPerformed(evt);
            }
        });
        panelRedondo3.add(txtCostoUnitario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 70, 240, 30));

        txtBuscarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProveedorActionPerformed(evt);
            }
        });
        panelRedondo3.add(txtBuscarProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, 270, 30));

        txBuscar.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        txBuscar.setForeground(new java.awt.Color(255, 255, 255));
        txBuscar.setText("Buscar:");
        panelRedondo3.add(txBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, 60, -1));

        btnAgregar.setText("Agregar Producto");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        panelRedondo3.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 40, 200, 38));

        add(panelRedondo3);
        panelRedondo3.setBounds(90, 100, 1710, 120);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarProducto();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        quitarProductoSeleccionado();
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void txtDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescuentoActionPerformed
        actualizarTotales();
    }//GEN-LAST:event_txtDescuentoActionPerformed

    private void rbContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbContadoActionPerformed
        actualizarModoPago();
    }//GEN-LAST:event_rbContadoActionPerformed

    private void rbCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCreditoActionPerformed
        actualizarModoPago();
    }//GEN-LAST:event_rbCreditoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarCompra();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtBuscarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProveedorActionPerformed
    }//GEN-LAST:event_txtBuscarProveedorActionPerformed

    private void txtNumFacturaProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumFacturaProveedorActionPerformed
    }//GEN-LAST:event_txtNumFacturaProveedorActionPerformed

    private void txtCostoUnitarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoUnitarioActionPerformed
    }//GEN-LAST:event_txtCostoUnitarioActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void cbProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProveedorActionPerformed
    }//GEN-LAST:event_cbProveedorActionPerformed

    private void cbProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProductoActionPerformed
    }//GEN-LAST:event_cbProductoActionPerformed

    private void cbMetodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMetodoPagoActionPerformed
    }//GEN-LAST:event_cbMetodoPagoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private componentes.BotonModerno btnAgregar;
    private componentes.BotonModerno btnGuardar;
    private componentes.BotonModerno btnQuitar;
    private componentes.ComboBoxModerno cbMetodoPago;
    private componentes.ComboBoxModerno cbProducto;
    private componentes.ComboBoxModerno cbProveedor;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCosto;
    private javax.swing.JLabel lblDescuento;
    private javax.swing.JLabel lblFactura;
    private javax.swing.JLabel lblFechaVencimiento;
    private javax.swing.JLabel lblFormaPago;
    private javax.swing.JLabel lblIva;
    private javax.swing.JLabel lblIvaTxt;
    private javax.swing.JLabel lblMetodoPago;
    private javax.swing.JLabel lblProducto;
    private javax.swing.JLabel lblProveedor;
    private javax.swing.JLabel lblQuitar;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblSubtotalTxt;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalTxt;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private componentes.PanelRedondo panelRedondo3;
    private javax.swing.JRadioButton rbContado;
    private javax.swing.JRadioButton rbCredito;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaDetalle;
    private javax.swing.JLabel txBuscar;
    private javax.swing.JTextField txtBuscarProveedor;
    private componentes.TextFieldModerno txtCantidad;
    private componentes.TextFieldModerno txtCostoUnitario;
    private componentes.TextFieldModerno txtDescuento;
    private componentes.FechaModerna txtFechaVencimiento;
    private componentes.TextFieldModerno txtNumFacturaProveedor;
    // End of variables declaration//GEN-END:variables
}
