package Vista.FACTURACION;

public class PanelFacturacion extends javax.swing.JPanel {

    private final Controladores.ControladorFactura controladorFactura = new Controladores.ControladorFactura();
    private final Controladores.ControladorCliente controladorCliente = new Controladores.ControladorCliente();

    private java.util.List<Modelo.DetalleFactura> listaDetalles = new java.util.ArrayList<>();
    private javax.swing.table.DefaultTableModel modeloTabla;
    private final javax.swing.ButtonGroup grupoTipoCliente = new javax.swing.ButtonGroup();
    private static final Modelo.Cliente CONSUMIDOR_FINAL = new Modelo.Cliente("CF", "Consumidor", "Final", "9999999999", "", "");

    public PanelFacturacion() {
        initComponents();
        componentes.EstiloTablaKrypton.aplicar(jTable_Productos);
        componentes.EstiloTablaKrypton.aplicar(tablaProductosRegistrados);

        grupoTipoCliente.add(RbConsumidorFinal);
        grupoTipoCliente.add(RbConDatos);

        grupoTipoCliente.add(RbConsumidorFinal);
        grupoTipoCliente.add(RbConDatos);
        RbConsumidorFinal.setSelected(true);
        activarCamposCliente(false);
        cargarEmpleados();
        cargarClientes();

        cargarProductos();
        inicializarTabla();

        NumeroFactura.setText(controladorFactura.generarNumeroFactura());
        remove(NumeroFactura);
        NumeroFactura.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        panelRedondo3.add(NumeroFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 170, 30));
        panelRedondo3.setComponentZOrder(NumeroFactura, 0);
        JCCliente.addItemListener(evt -> llenarDatosCliente());
        cargarCatalogosLista();
        cargarListaProductos();
        JCCatalogoLista.addActionListener(evt -> cargarListaProductos());
        txtBuscarProductoLista.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cargarListaProductos();
            }
        });

    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            cargarClientes();
            cargarProductos();
        }
    }

    private void cargarClientes() {
        JCCliente.removeAllItems();
        JCCliente.addItem("Seleccione Cliente.");
        for (Modelo.Cliente c : controladorCliente.listarTodos()) {
            JCCliente.addItem(c);
        }
        JCCliente.setSelectedIndex(0);
    }

    private String idEmpleadoActual;

    private void cargarEmpleados() {
        Controladores.EmpleadoControlador.EmpleadoActual actual = Controladores.EmpleadoControlador.resolverEmpleadoDeSesion();
        idEmpleadoActual = actual.idEmpleado;
        LblEmpleadoFactura.setText(actual.nombreCompleto);
    }

    private void actualizarTotales() {
        double descuento = 0;
        try {
            descuento = Double.parseDouble(Descuento.getText().trim());
        } catch (NumberFormatException e) {
            descuento = 0;
        }

        double[] totales = controladorFactura.calcularTotales(listaDetalles, descuento);
        Subtotal.setText(String.format("%.2f", totales[0]));
        IVA.setText(String.format("%.2f", totales[1]));
        TOTALPAGAR.setText(String.format("%.2f", totales[2]));
    }

    private void cargarProductos() {
        JCProductos.removeAllItems();
        for (Modelo.Producto p : Controladores.ControladorProducto.listarProductos()) {
            JCProductos.addItem(p);
        }
    }

    private void inicializarTabla() {
        String[] columnas = {"N°", "Codigo", "Producto", "Cantidad", "Precio Unit.", "Iva", "Subtotal"};
        modeloTabla = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_Productos.setModel(modeloTabla);
    }

    private void activarCamposCliente(boolean activar) {
        JCCliente.setEnabled(activar);
        txt_cliente_buscar1.setEnabled(activar);
        BtnBuscar2.setEnabled(activar);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Productos = new javax.swing.JTable();
        NumeroFactura = new javax.swing.JLabel();
        BtnRegistrarFactura = new componentes.BotonModerno();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaProductosRegistrados = new javax.swing.JTable();
        panelRedondo1 = new componentes.PanelRedondo();
        TOTALPAGAR = new componentes.TextFieldModerno();
        IVA = new componentes.TextFieldModerno();
        Descuento = new componentes.TextFieldModerno();
        Subtotal = new componentes.TextFieldModerno();
        txtSubtotal = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JLabel();
        txtIVA = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JLabel();
        panelRedondo2 = new componentes.PanelRedondo();
        Cantidad = new javax.swing.JLabel();
        JCProductos = new componentes.ComboBoxModerno();
        txt_cantidad = new componentes.TextFieldModerno();
        javax.swing.JButton BtnAñadir = new componentes.BotonModerno();
        javax.swing.JButton BtnEliminarProducto = new componentes.BotonModerno();
        TxtProductos1 = new javax.swing.JLabel();
        txtBuscarProducto = new javax.swing.JLabel();
        JCCatalogoLista =  new componentes.ComboBoxModerno();
        txtBuscarProductoLista = new componentes.TextFieldModerno();
        TxtProductos3 = new javax.swing.JLabel();
        panelRedondo3 = new componentes.PanelRedondo();
        Titulo = new javax.swing.JLabel();
        txtClienteNombre = new componentes.TextFieldModerno();
        txtClienteCedula = new componentes.TextFieldModerno();
        txtClienteDireccion = new componentes.TextFieldModerno();
        txtClienteCorreo = new componentes.TextFieldModerno();
        txtClienteTelefono = new componentes.TextFieldModerno();
        NombreCliente = new javax.swing.JLabel();
        DireccionCliente = new javax.swing.JLabel();
        CorreoCliente = new javax.swing.JLabel();
        TelefonoCliente = new javax.swing.JLabel();
        CedulaCliente = new javax.swing.JLabel();
        panelRedondo4 = new componentes.PanelRedondo();
        txt_cliente_buscar1 = new componentes.TextFieldModerno();
        RbConsumidorFinal = new javax.swing.JRadioButton();
        RbConDatos = new javax.swing.JRadioButton();
        BtnBuscar2 = new componentes.BotonModerno();
        JCCliente = new componentes.ComboBoxModerno();
        TxtCliente3 = new javax.swing.JLabel();
        panelRedondo5 = new componentes.PanelRedondo();
        TxtEmpleado = new javax.swing.JLabel();
        LblEmpleadoFactura = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable_Productos.setBackground(new java.awt.Color(31, 11, 43));
        jTable_Productos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTable_Productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N°", "Codigo", "Producto", "Cantidad", "Precio Unit.", "Iva", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_Productos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 460));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 1090, 460));

        NumeroFactura.setBackground(new java.awt.Color(255, 255, 255));
        NumeroFactura.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        NumeroFactura.setForeground(new java.awt.Color(255, 255, 255));
        add(NumeroFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 50, -1, -1));

        BtnRegistrarFactura.setText("Registrar Factura");
        BtnRegistrarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRegistrarFacturaActionPerformed(evt);
            }
        });
        add(BtnRegistrarFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 960, 180, 140));

        tablaProductosRegistrados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tablaProductosRegistrados);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 410, 690, 460));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TOTALPAGAR.setForeground(new java.awt.Color(255, 255, 255));
        TOTALPAGAR.setEnabled(false);
        TOTALPAGAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TOTALPAGARActionPerformed(evt);
            }
        });
        panelRedondo1.add(TOTALPAGAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 160, 30));

        IVA.setForeground(new java.awt.Color(255, 255, 255));
        IVA.setEnabled(false);
        IVA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IVAActionPerformed(evt);
            }
        });
        panelRedondo1.add(IVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 160, 30));

        Descuento.setForeground(new java.awt.Color(255, 255, 255));
        Descuento.setEnabled(false);
        panelRedondo1.add(Descuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 160, 30));

        Subtotal.setForeground(new java.awt.Color(255, 255, 255));
        Subtotal.setEnabled(false);
        panelRedondo1.add(Subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 160, 30));

        txtSubtotal.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        txtSubtotal.setForeground(new java.awt.Color(255, 255, 255));
        txtSubtotal.setText("Subtotal:");
        panelRedondo1.add(txtSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, -1));

        txtDescuento.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        txtDescuento.setForeground(new java.awt.Color(255, 255, 255));
        txtDescuento.setText("Descuento");
        panelRedondo1.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        txtIVA.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        txtIVA.setForeground(new java.awt.Color(255, 255, 255));
        txtIVA.setText("IVA");
        panelRedondo1.add(txtIVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, -1));

        txtTotalPagar.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        txtTotalPagar.setForeground(new java.awt.Color(255, 255, 255));
        txtTotalPagar.setText("TOTAL A PAGAR");
        panelRedondo1.add(txtTotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 910, 380, 220));

        panelRedondo2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Cantidad.setBackground(new java.awt.Color(255, 255, 255));
        Cantidad.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        Cantidad.setForeground(new java.awt.Color(255, 255, 255));
        Cantidad.setText("Cantidad");
        panelRedondo2.add(Cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 90, 30));

        JCProductos.setBackground(new java.awt.Color(26, 16, 36));
        JCProductos.setForeground(new java.awt.Color(255, 255, 255));
        JCProductos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione Producto" }));
        JCProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCProductosActionPerformed(evt);
            }
        });
        panelRedondo2.add(JCProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 240, 30));

        txt_cantidad.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo2.add(txt_cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 160, 30));

        BtnAñadir.setBackground(new java.awt.Color(102, 102, 102));
        BtnAñadir.setForeground(new java.awt.Color(255, 255, 255));
        BtnAñadir.setText("Añadir Producto");
        BtnAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAñadirActionPerformed(evt);
            }
        });
        panelRedondo2.add(BtnAñadir, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 160, 35));

        BtnEliminarProducto.setBackground(new java.awt.Color(102, 102, 102));
        BtnEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        BtnEliminarProducto.setText("Eliminar Producto ");
        BtnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarProductoActionPerformed(evt);
            }
        });
        panelRedondo2.add(BtnEliminarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 170, 35));

        TxtProductos1.setBackground(new java.awt.Color(255, 255, 255));
        TxtProductos1.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        TxtProductos1.setForeground(new java.awt.Color(255, 255, 255));
        TxtProductos1.setText("Producto:");
        panelRedondo2.add(TxtProductos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 90, 30));

        txtBuscarProducto.setBackground(new java.awt.Color(255, 255, 255));
        txtBuscarProducto.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        txtBuscarProducto.setForeground(new java.awt.Color(255, 255, 255));
        txtBuscarProducto.setText("Buscar Producto");
        panelRedondo2.add(txtBuscarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 170, 30));

        JCCatalogoLista.setBackground(new java.awt.Color(26, 16, 36));
        JCCatalogoLista.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelRedondo2.add(JCCatalogoLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 240, 30));

        txtBuscarProductoLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProductoListaActionPerformed(evt);
            }
        });
        panelRedondo2.add(txtBuscarProductoLista, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 180, 270, 30));

        TxtProductos3.setBackground(new java.awt.Color(255, 255, 255));
        TxtProductos3.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        TxtProductos3.setForeground(new java.awt.Color(255, 255, 255));
        TxtProductos3.setText("Catalogo:");
        panelRedondo2.add(TxtProductos3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 90, 30));

        add(panelRedondo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 160, 690, 230));

        panelRedondo3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Titulo.setBackground(new java.awt.Color(255, 255, 255));
        Titulo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        Titulo.setText("FACTURACION");
        panelRedondo3.add(Titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 200, 30));

        txtClienteNombre.setEditable(false);
        panelRedondo3.add(txtClienteNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 230, -1));

        txtClienteCedula.setEditable(false);
        panelRedondo3.add(txtClienteCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 260, -1));

        txtClienteDireccion.setEditable(false);
        panelRedondo3.add(txtClienteDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, 260, -1));

        txtClienteCorreo.setEditable(false);
        panelRedondo3.add(txtClienteCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 230, -1));

        txtClienteTelefono.setEditable(false);
        txtClienteTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteTelefonoActionPerformed(evt);
            }
        });
        panelRedondo3.add(txtClienteTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 230, -1));

        NombreCliente.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        NombreCliente.setForeground(new java.awt.Color(255, 255, 255));
        NombreCliente.setText("Nombre:");
        panelRedondo3.add(NombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        DireccionCliente.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        DireccionCliente.setForeground(new java.awt.Color(255, 255, 255));
        DireccionCliente.setText("Direccion:");
        panelRedondo3.add(DireccionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, -1, -1));

        CorreoCliente.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        CorreoCliente.setForeground(new java.awt.Color(255, 255, 255));
        CorreoCliente.setText("Correo:");
        panelRedondo3.add(CorreoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        TelefonoCliente.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        TelefonoCliente.setForeground(new java.awt.Color(255, 255, 255));
        TelefonoCliente.setText("Telefono:");
        panelRedondo3.add(TelefonoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        CedulaCliente.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        CedulaCliente.setForeground(new java.awt.Color(255, 255, 255));
        CedulaCliente.setText("Cedula:");
        panelRedondo3.add(CedulaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, -1, -1));

        add(panelRedondo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 160, 600, 230));

        panelRedondo4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_cliente_buscar1.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo4.add(txt_cliente_buscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 260, 30));

        RbConsumidorFinal.setBackground(new java.awt.Color(31, 11, 43));
        RbConsumidorFinal.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        RbConsumidorFinal.setForeground(new java.awt.Color(255, 255, 255));
        RbConsumidorFinal.setText("Consumidor Final");
        RbConsumidorFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbConsumidorFinalActionPerformed(evt);
            }
        });
        panelRedondo4.add(RbConsumidorFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 160, 25));

        RbConDatos.setBackground(new java.awt.Color(31, 11, 43));
        RbConDatos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        RbConDatos.setForeground(new java.awt.Color(255, 255, 255));
        RbConDatos.setText("Con datos");
        RbConDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbConDatosActionPerformed(evt);
            }
        });
        panelRedondo4.add(RbConDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 120, 25));

        BtnBuscar2.setBackground(new java.awt.Color(102, 102, 102));
        BtnBuscar2.setForeground(new java.awt.Color(255, 255, 255));
        BtnBuscar2.setText("Buscar");
        BtnBuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscar2ActionPerformed(evt);
            }
        });
        panelRedondo4.add(BtnBuscar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 140, 30));

        JCCliente.setBackground(new java.awt.Color(26, 16, 36));
        JCCliente.setForeground(new java.awt.Color(255, 255, 255));
        JCCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione Cliente." }));
        JCCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCClienteActionPerformed(evt);
            }
        });
        panelRedondo4.add(JCCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 220, 30));

        TxtCliente3.setBackground(new java.awt.Color(255, 255, 255));
        TxtCliente3.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        TxtCliente3.setForeground(new java.awt.Color(255, 255, 255));
        TxtCliente3.setText("Cliente:");
        panelRedondo4.add(TxtCliente3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 80, 30));

        add(panelRedondo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 470, 150));

        panelRedondo5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TxtEmpleado.setBackground(new java.awt.Color(255, 255, 255));
        TxtEmpleado.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        TxtEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        TxtEmpleado.setText("Empleado:");
        panelRedondo5.add(TxtEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, 30));

        LblEmpleadoFactura.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        LblEmpleadoFactura.setForeground(new java.awt.Color(255, 255, 255));
        LblEmpleadoFactura.setText("Nombre");
        panelRedondo5.add(LblEmpleadoFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 200, 30));

        add(panelRedondo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 470, 70));

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 1, 48)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("FACTURACIÓN");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 460, 60));
    }// </editor-fold>//GEN-END:initComponents

    private void RbConsumidorFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbConsumidorFinalActionPerformed
        activarCamposCliente(false);
    }//GEN-LAST:event_RbConsumidorFinalActionPerformed

    private void RbConDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbConDatosActionPerformed
        activarCamposCliente(true);
    }//GEN-LAST:event_RbConDatosActionPerformed
    private void cargarCatalogosLista() {
        JCCatalogoLista.removeAllItems();
        JCCatalogoLista.addItem("Todos");
        for (String cat : Controladores.ControladorProducto.obtenerNombresCategorias()) {
            JCCatalogoLista.addItem(cat);
        }
    }

    private void cargarListaProductos() {
        String texto = txtBuscarProductoLista.getText().trim();
        String categoria = (String) JCCatalogoLista.getSelectedItem();

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
                new String[]{"Nombre", "Categoria", "Precio Unit."}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Modelo.Producto p : Controladores.ControladorProducto.filtrarProductos(texto, null)) {
            if (categoria == null || categoria.equals("Todos") || categoria.equals(p.getCategoria())) {
                modelo.addRow(new Object[]{
                    p.getNombre(),
                    p.getCategoria(),
                    String.format("%.2f", p.getPrecioUnitario())
                });
            }
        }

        tablaProductosRegistrados.setModel(modelo);
    }

    private void llenarDatosCliente() {
        Object seleccionado = JCCliente.getSelectedItem();
        if (seleccionado instanceof Modelo.Cliente) {
            Modelo.Cliente c = (Modelo.Cliente) seleccionado;
            txtClienteNombre.setText(c.getNombreCliente());
            txtClienteCedula.setText(c.getNumeroDocumento());
            txtClienteDireccion.setText(c.getDireccion());
            txtClienteCorreo.setText(c.getCorreo());
            txtClienteTelefono.setText(c.getTelefono());
        } else {
            txtClienteNombre.setText("");
            txtClienteCedula.setText("");
            txtClienteDireccion.setText("");
            txtClienteCorreo.setText("");
            txtClienteTelefono.setText("");
        }
    }
    private void BtnAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAñadirActionPerformed
        if (JCProductos.getSelectedItem() == null || !(JCProductos.getSelectedItem() instanceof Modelo.Producto)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un producto válido.");
            return;
        }

        Modelo.Producto seleccionado = (Modelo.Producto) JCProductos.getSelectedItem();

        int cantidad;
        try {
            cantidad = Integer.parseInt(txt_cantidad.getText().trim());
            if (cantidad <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.");
            return;
        }

        Controladores.ControladorFactura.ResultadoAgregarCarrito resultado
                = controladorFactura.agregarAlCarrito(listaDetalles, seleccionado, cantidad);

        if (!resultado.ok) {
            javax.swing.JOptionPane.showMessageDialog(this, resultado.error,
                    "Cantidad no disponible", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String textoIva = seleccionado.isTieneIva() ? "15%" : "0%";
        Modelo.DetalleFactura detalle = resultado.detalle;

        if (resultado.indiceActualizado != -1) {
            // El producto ya estaba en el carrito: sumamos cantidad en vez de duplicar fila
            listaDetalles.set(resultado.indiceActualizado, detalle);
            modeloTabla.setValueAt(detalle.getCantidad(), resultado.indiceActualizado, 3);
            modeloTabla.setValueAt(String.format("%.2f", detalle.getSubtotal()), resultado.indiceActualizado, 6);
        } else {
            listaDetalles.add(detalle);

            int numItem = modeloTabla.getRowCount() + 1;
            String codigoAuto = String.format("PROD-%03d", numItem);

            modeloTabla.addRow(new Object[]{
                numItem,
                codigoAuto,
                detalle.getNombreProducto(),
                detalle.getCantidad(),
                String.format("%.2f", detalle.getPrecioUnitario()),
                textoIva,
                String.format("%.2f", detalle.getSubtotal())
            });
        }

        txt_cantidad.setText("");
        actualizarTotales();
    }//GEN-LAST:event_BtnAñadirActionPerformed

    private void BtnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEliminarProductoActionPerformed
        int filaSeleccionada = jTable_Productos.getSelectedRow();

        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.");
            return;
        }

        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar este producto de la factura?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            listaDetalles.remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                int nuevoNum = i + 1;
                modeloTabla.setValueAt(nuevoNum, i, 0);
                modeloTabla.setValueAt(String.format("PROD-%03d", nuevoNum), i, 1);
            }

            actualizarTotales();
        }
    }//GEN-LAST:event_BtnEliminarProductoActionPerformed

    private void BtnRegistrarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRegistrarFacturaActionPerformed
        if (listaDetalles.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Agregue al menos un producto.");
            return;
        }

        double descuento;
        try {
            descuento = Double.parseDouble(Descuento.getText().trim());
        } catch (NumberFormatException e) {
            descuento = 0;
        }

        double[] totales = controladorFactura.calcularTotales(listaDetalles, descuento);

        java.awt.Frame ventanaPadre = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
        DialogCobro dialog = new DialogCobro(ventanaPadre, totales[2]);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (!dialog.isConfirmado()) {
            return;
        }

        String idEmpleadoSeleccionado = idEmpleadoActual;
        if (idEmpleadoSeleccionado == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Tu cuenta no tiene un registro de empleado asociado. Contacta al administrador.");
            return;
        }

        Modelo.Cliente clienteFinal = RbConsumidorFinal.isSelected() ? CONSUMIDOR_FINAL : (Modelo.Cliente) JCCliente.getSelectedItem();
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        boolean exito = controladorFactura.registrarFactura(
                this, NumeroFactura.getText(), fecha, clienteFinal,
                dialog.getMetodoPago(), listaDetalles, descuento, idEmpleadoSeleccionado
        );

        if (exito) {
            listaDetalles = new java.util.ArrayList<>();
            modeloTabla.setRowCount(0);
            Subtotal.setText("");
            IVA.setText("");
            TOTALPAGAR.setText("");
            NumeroFactura.setText(controladorFactura.generarNumeroFactura());
        }
    }//GEN-LAST:event_BtnRegistrarFacturaActionPerformed

    private void TOTALPAGARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TOTALPAGARActionPerformed

    }//GEN-LAST:event_TOTALPAGARActionPerformed

    private void IVAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IVAActionPerformed

    }//GEN-LAST:event_IVAActionPerformed

    private void BtnBuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscar2ActionPerformed
        String texto = txt_cliente_buscar1.getText().trim();
        JCCliente.removeAllItems();
        for (Modelo.Cliente c : controladorCliente.filtrarClientes(texto)) {
            JCCliente.addItem(c);
        }
    }//GEN-LAST:event_BtnBuscar2ActionPerformed

    private void txtClienteTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteTelefonoActionPerformed

    }//GEN-LAST:event_txtClienteTelefonoActionPerformed

    private void JCClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCClienteActionPerformed

    }//GEN-LAST:event_JCClienteActionPerformed

    private void JCProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCProductosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JCProductosActionPerformed

    private void txtBuscarProductoListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProductoListaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarProductoListaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBuscar2;
    private componentes.BotonModerno BtnRegistrarFactura;
    private javax.swing.JLabel Cantidad;
    private javax.swing.JLabel CedulaCliente;
    private javax.swing.JLabel CorreoCliente;
    private javax.swing.JTextField Descuento;
    private javax.swing.JLabel DireccionCliente;
    private javax.swing.JTextField IVA;
    private javax.swing.JComboBox<String> JCCatalogoLista;
    private javax.swing.JComboBox JCCliente;
    private javax.swing.JComboBox JCProductos;
    private javax.swing.JLabel LblEmpleadoFactura;
    private javax.swing.JLabel NombreCliente;
    private javax.swing.JLabel NumeroFactura;
    private javax.swing.JRadioButton RbConDatos;
    private javax.swing.JRadioButton RbConsumidorFinal;
    private javax.swing.JTextField Subtotal;
    private javax.swing.JTextField TOTALPAGAR;
    private javax.swing.JLabel TelefonoCliente;
    private javax.swing.JLabel Titulo;
    private javax.swing.JLabel TxtCliente3;
    private javax.swing.JLabel TxtEmpleado;
    private javax.swing.JLabel TxtProductos1;
    private javax.swing.JLabel TxtProductos3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable jTable_Productos;
    private javax.swing.JLabel lblTitulo;
    private componentes.PanelRedondo panelRedondo1;
    private componentes.PanelRedondo panelRedondo2;
    private componentes.PanelRedondo panelRedondo3;
    private componentes.PanelRedondo panelRedondo4;
    private componentes.PanelRedondo panelRedondo5;
    private javax.swing.JTable tablaProductosRegistrados;
    private javax.swing.JLabel txtBuscarProducto;
    private javax.swing.JTextField txtBuscarProductoLista;
    private javax.swing.JTextField txtClienteCedula;
    private javax.swing.JTextField txtClienteCorreo;
    private javax.swing.JTextField txtClienteDireccion;
    private javax.swing.JTextField txtClienteNombre;
    private javax.swing.JTextField txtClienteTelefono;
    private javax.swing.JLabel txtDescuento;
    private javax.swing.JLabel txtIVA;
    private javax.swing.JLabel txtSubtotal;
    private javax.swing.JLabel txtTotalPagar;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_cliente_buscar1;
    // End of variables declaration//GEN-END:variables
}
