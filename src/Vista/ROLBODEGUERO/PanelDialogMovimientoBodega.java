package Vista.ROLBODEGUERO;

import Modelo.Producto;
import java.util.List;

public class PanelDialogMovimientoBodega extends javax.swing.JPanel {

    public enum Modo {
        ENTRADA("Registrar Entrada"),
        SALIDA("Confirmar Salida"),
        TRANSFERENCIA("Transferir Bodega");

        final String titulo;

        Modo(String titulo) {
            this.titulo = titulo;
        }
    }

    private final javax.swing.DefaultListModel<Producto> modeloResultados = new javax.swing.DefaultListModel<>();
    private Producto productoSeleccionado;
    private boolean esTransferencia;
    private Modo modo;
    private String resultado;
    private javax.swing.JDialog ventana;

    public PanelDialogMovimientoBodega() {
        initComponents();

        listaResultados.setModel(modeloResultados);
        listaResultados.setSelectionBackground(new java.awt.Color(85, 0, 102));
        listaResultados.setSelectionForeground(java.awt.Color.WHITE);
        listaResultados.setVisibleRowCount(4);
        listaResultados.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel lbl = new javax.swing.JLabel("  " + value.getNombre() + "   —   " + value.getCodigo());
            lbl.setOpaque(true);
            lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
            lbl.setBackground(isSelected ? new java.awt.Color(85, 0, 102) : new java.awt.Color(45, 16, 61));
            lbl.setForeground(java.awt.Color.WHITE);
            lbl.setBorder(new javax.swing.border.EmptyBorder(4, 4, 4, 4));
            return lbl;
        });
        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaResultados.getSelectedValue() != null) {
                seleccionarProducto(listaResultados.getSelectedValue());
            }
        });

        txtBusqueda.addActionListener(e -> buscarProductos());
        btnBuscar.addActionListener(e -> buscarProductos());
        btnConfirmar.addActionListener(e -> confirmar());
        btnCancelar.addActionListener(e -> ventana.dispose());

        limpiarSeleccion();
    }

    public static String mostrar(java.awt.Frame parent, Modo modo) {
        PanelDialogMovimientoBodega panel = new PanelDialogMovimientoBodega();
        panel.modo = modo;
        panel.configurarModo(modo.titulo);

        javax.swing.JDialog ventana = new javax.swing.JDialog(parent, modo.titulo, true);
        panel.ventana = ventana;
        ventana.add(panel);
        ventana.pack();
        ventana.setLocationRelativeTo(parent);
        ventana.setResizable(false);
        componentes.escalado.KryptonPanelScrollable.envolverJDialog(ventana);
        ventana.setVisible(true);

        return panel.resultado;
    }

    private void configurarModo(String tituloModo) {
        lblTitulo.setText(tituloModo.toUpperCase());
        btnConfirmar.setText(tituloModo.toUpperCase());
        esTransferencia = "Transferir Bodega".equals(tituloModo);
        lblDestino.setVisible(esTransferencia);
        txtDestino.setVisible(esTransferencia);
    }

    private void confirmar() {
        if (!hayProductoSeleccionado()) {
            javax.swing.JOptionPane.showMessageDialog(ventana, "Busca y selecciona un producto antes de continuar.",
                    "Producto no seleccionado", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = getCodigoSeleccionado();
        String cantidad = getCantidadTexto();

        switch (modo) {
            case ENTRADA:
                resultado = Controladores.ControladorInventarioBodega.registrarEntrada(ventana, codigo, cantidad);
                break;
            case SALIDA:
                resultado = Controladores.ControladorInventarioBodega.confirmarSalida(ventana, codigo, cantidad);
                break;
            default:
                break;
        }

        if (resultado != null) {
            ventana.dispose();
        }
    }

    private void buscarProductos() {
        String texto = txtBusqueda.getText();
        if (texto == null || texto.trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Escribe un código o nombre para buscar.",
                    "Búsqueda vacía", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        limpiarSeleccion();

        List<Producto> encontrados = Controladores.ControladorInventarioBodega.buscarParaBodega(texto);

        if (encontrados.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontró ningún producto con ese código o nombre.",
                    "Sin resultados", javax.swing.JOptionPane.WARNING_MESSAGE);
            scrollResultados.setVisible(false);
        } else if (encontrados.size() == 1) {
            scrollResultados.setVisible(false);
            seleccionarProducto(encontrados.get(0));
        } else {
            modeloResultados.clear();
            for (Producto p : encontrados) {
                modeloResultados.addElement(p);
            }
            scrollResultados.setVisible(true);
        }
    }

    private void seleccionarProducto(Producto p) {
        productoSeleccionado = p;

        lblInfoNombre.setText(p.getNombre());
        lblInfoCategoria.setText(p.getCategoria() == null ? "-" : p.getCategoria());
        lblInfoUbicacion.setText(p.getUbicacionPasillo() == null || p.getUbicacionPasillo().isEmpty()
                ? "N/A" : p.getUbicacionPasillo());
        lblInfoLote.setText(p.getLote() == null || p.getLote().isEmpty() ? "-" : p.getLote());
        lblInfoStock.setText(p.getCantidad() + " uds.");
        panelInfoProducto.setVisible(true);

        txtCantidad.setEnabled(true);
        if (esTransferencia) {
            txtDestino.setEnabled(true);
        }
        btnConfirmar.setEnabled(true);
    }

    private void limpiarSeleccion() {
        productoSeleccionado = null;
        panelInfoProducto.setVisible(false);
        txtCantidad.setEnabled(false);
        txtCantidad.setText("");
        txtDestino.setEnabled(false);
        txtDestino.setText("");
        btnConfirmar.setEnabled(false);
    }

    public boolean hayProductoSeleccionado() {
        return productoSeleccionado != null;
    }

    public String getCodigoSeleccionado() {
        return productoSeleccionado == null ? null : productoSeleccionado.getCodigo();
    }

    public String getCantidadTexto() {
        return txtCantidad.getText();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblBuscar = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        scrollResultados = new javax.swing.JScrollPane();
        listaResultados = new javax.swing.JList<>();
        panelInfoProducto = new javax.swing.JPanel();
        lblEtiquetaNombre = new javax.swing.JLabel();
        lblInfoNombre = new javax.swing.JLabel();
        lblEtiquetaCategoria = new javax.swing.JLabel();
        lblInfoCategoria = new javax.swing.JLabel();
        lblEtiquetaUbicacion = new javax.swing.JLabel();
        lblInfoUbicacion = new javax.swing.JLabel();
        lblEtiquetaLote = new javax.swing.JLabel();
        lblInfoLote = new javax.swing.JLabel();
        lblEtiquetaStock = new javax.swing.JLabel();
        lblInfoStock = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        lblDestino = new javax.swing.JLabel();
        txtDestino = new javax.swing.JTextField();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("MOVIMIENTO DE BODEGA");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 530, 35));

        lblBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBuscar.setForeground(new java.awt.Color(255, 255, 255));
        lblBuscar.setText("Buscar producto (código o nombre):");
        add(lblBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 400, 25));

        add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 430, 30));

        btnBuscar.setBackground(new java.awt.Color(60, 45, 90));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.setFocusPainted(false);
        add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, 90, 30));

        scrollResultados.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(110, 70, 140), 1, true));
        scrollResultados.setVisible(false);
        scrollResultados.setViewportView(listaResultados);

        add(scrollResultados, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 530, 100));

        panelInfoProducto.setBackground(new java.awt.Color(45, 16, 61));
        panelInfoProducto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(110, 70, 140), 1, true));
        panelInfoProducto.setVisible(false);
        panelInfoProducto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEtiquetaNombre.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEtiquetaNombre.setForeground(new java.awt.Color(200, 190, 210));
        lblEtiquetaNombre.setText("Producto:");
        panelInfoProducto.add(lblEtiquetaNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 10, 90, 22));

        lblInfoNombre.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfoNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoNombre.setText("-");
        panelInfoProducto.add(lblInfoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 10, 390, 22));

        lblEtiquetaCategoria.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEtiquetaCategoria.setForeground(new java.awt.Color(200, 190, 210));
        lblEtiquetaCategoria.setText("Categoría:");
        panelInfoProducto.add(lblEtiquetaCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 32, 90, 22));

        lblInfoCategoria.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfoCategoria.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoCategoria.setText("-");
        panelInfoProducto.add(lblInfoCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 32, 390, 22));

        lblEtiquetaUbicacion.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEtiquetaUbicacion.setForeground(new java.awt.Color(200, 190, 210));
        lblEtiquetaUbicacion.setText("Ubicación:");
        panelInfoProducto.add(lblEtiquetaUbicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 54, 90, 22));

        lblInfoUbicacion.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfoUbicacion.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoUbicacion.setText("-");
        panelInfoProducto.add(lblInfoUbicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 54, 390, 22));

        lblEtiquetaLote.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEtiquetaLote.setForeground(new java.awt.Color(200, 190, 210));
        lblEtiquetaLote.setText("Lote:");
        panelInfoProducto.add(lblEtiquetaLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 76, 90, 22));

        lblInfoLote.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfoLote.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoLote.setText("-");
        panelInfoProducto.add(lblInfoLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 76, 390, 22));

        lblEtiquetaStock.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEtiquetaStock.setForeground(new java.awt.Color(200, 190, 210));
        lblEtiquetaStock.setText("Stock actual:");
        panelInfoProducto.add(lblEtiquetaStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 98, 90, 22));

        lblInfoStock.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfoStock.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoStock.setText("-");
        panelInfoProducto.add(lblInfoStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 98, 390, 22));

        add(panelInfoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 530, 130));

        lblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCantidad.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidad.setText("Cantidad*:");
        add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 385, 200, 25));

        txtCantidad.setEnabled(false);
        add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 385, 320, 25));

        lblDestino.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDestino.setForeground(new java.awt.Color(255, 255, 255));
        lblDestino.setText("Destino*:");
        lblDestino.setVisible(false);
        add(lblDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 200, 25));

        txtDestino.setEnabled(false);
        txtDestino.setVisible(false);
        add(txtDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 420, 320, 25));

        btnConfirmar.setBackground(new java.awt.Color(85, 0, 102));
        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmar.setText("CONFIRMAR");
        btnConfirmar.setEnabled(false);
        btnConfirmar.setFocusPainted(false);
        add(btnConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 460, 180, 35));

        btnCancelar.setBackground(new java.awt.Color(85, 0, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.setFocusPainted(false);
        add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 460, 150, 35));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JLabel lblEtiquetaCategoria;
    private javax.swing.JLabel lblEtiquetaLote;
    private javax.swing.JLabel lblEtiquetaNombre;
    private javax.swing.JLabel lblEtiquetaStock;
    private javax.swing.JLabel lblEtiquetaUbicacion;
    private javax.swing.JLabel lblInfoCategoria;
    private javax.swing.JLabel lblInfoLote;
    private javax.swing.JLabel lblInfoNombre;
    private javax.swing.JLabel lblInfoStock;
    private javax.swing.JLabel lblInfoUbicacion;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JList<Producto> listaResultados;
    private javax.swing.JPanel panelInfoProducto;
    private javax.swing.JScrollPane scrollResultados;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDestino;
    // End of variables declaration//GEN-END:variables
}
