package Vista.ROLBODEGUERO;

import Controladores.ControladorInventarioBodega;
import Modelo.Producto;
import java.util.List;

public class PanelDialogAjustarInventario extends javax.swing.JPanel {

    private final javax.swing.DefaultListModel<Producto> modeloResultados = new javax.swing.DefaultListModel<>();
    private Producto productoSeleccionado;
    private boolean guardado = false;
    private javax.swing.JDialog ventana;

    public PanelDialogAjustarInventario() {
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
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> ventana.dispose());

        habilitarCamposAjuste(false);
    }

    public static boolean mostrar(java.awt.Frame parent) {
        PanelDialogAjustarInventario panel = new PanelDialogAjustarInventario();

        javax.swing.JDialog ventana = new javax.swing.JDialog(parent, "Ajustar Inventario", true);
        panel.ventana = ventana;
        ventana.add(panel);
        ventana.pack();
        ventana.setLocationRelativeTo(parent);
        ventana.setResizable(false);
        componentes.escalado.KryptonPanelScrollable.envolverJDialog(ventana);
        ventana.setVisible(true);

        return panel.guardado;
    }

    private void guardar() {
        if (!hayProductoSeleccionado()) {
            javax.swing.JOptionPane.showMessageDialog(ventana, "Busca y selecciona un producto antes de continuar.",
                    "Producto no seleccionado", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exito = ControladorInventarioBodega.ajustarInventario(ventana,
                getCodigoSeleccionado(), getUbicacion(), getLote(),
                getStockMinimoTexto(), getStockMaximoTexto(), getStockActualTexto());
        if (exito) {
            guardado = true;
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

        List<Producto> encontrados = ControladorInventarioBodega.buscarParaBodega(texto);

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
        lblInfoCodigo.setText(p.getCodigo());
        panelInfoProducto.setVisible(true);

        txtUbicacion.setText(p.getUbicacionPasillo() == null ? "" : p.getUbicacionPasillo());
        txtLote.setText(p.getLote() == null ? "" : p.getLote());
        txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
        txtStockMaximo.setText(String.valueOf(p.getStockMaximo()));
        txtStockActual.setText(String.valueOf(p.getCantidad()));

        habilitarCamposAjuste(true);
        btnGuardar.setEnabled(true);
    }

    private void limpiarSeleccion() {
        productoSeleccionado = null;
        panelInfoProducto.setVisible(false);
        txtUbicacion.setText("");
        txtLote.setText("");
        txtStockMinimo.setText("");
        txtStockMaximo.setText("");
        txtStockActual.setText("");
        habilitarCamposAjuste(false);
        btnGuardar.setEnabled(false);
    }

    private void habilitarCamposAjuste(boolean habilitado) {
        txtUbicacion.setEnabled(habilitado);
        txtLote.setEnabled(habilitado);
        txtStockMinimo.setEnabled(habilitado);
        txtStockMaximo.setEnabled(habilitado);
        txtStockActual.setEnabled(habilitado);
    }

    public boolean hayProductoSeleccionado() {
        return productoSeleccionado != null;
    }

    public String getCodigoSeleccionado() {
        return productoSeleccionado == null ? null : productoSeleccionado.getCodigo();
    }

    public String getUbicacion() {
        return txtUbicacion.getText();
    }

    public String getLote() {
        return txtLote.getText();
    }

    public String getStockMinimoTexto() {
        return txtStockMinimo.getText();
    }

    public String getStockMaximoTexto() {
        return txtStockMaximo.getText();
    }

    public String getStockActualTexto() {
        return txtStockActual.getText();
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
        lblEtiquetaCodigo = new javax.swing.JLabel();
        lblInfoCodigo = new javax.swing.JLabel();
        lblUbicacion = new javax.swing.JLabel();
        txtUbicacion = new javax.swing.JTextField();
        lblLote = new javax.swing.JLabel();
        txtLote = new javax.swing.JTextField();
        lblStockMinimo = new javax.swing.JLabel();
        txtStockMinimo = new javax.swing.JTextField();
        lblStockMaximo = new javax.swing.JLabel();
        txtStockMaximo = new javax.swing.JTextField();
        lblStockActual = new javax.swing.JLabel();
        txtStockActual = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(31, 11, 43));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("AJUSTAR INVENTARIO");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 530, 35));

        lblBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBuscar.setForeground(new java.awt.Color(255, 255, 255));
        lblBuscar.setText("Buscar producto (código o nombre):");
        add(lblBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 400, 25));

        add(txtBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 430, 30));

        btnBuscar.setBackground(new java.awt.Color(60, 45, 90));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
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
        panelInfoProducto.add(lblEtiquetaCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 35, 90, 22));

        lblInfoCategoria.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfoCategoria.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoCategoria.setText("-");
        panelInfoProducto.add(lblInfoCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 35, 390, 22));

        lblEtiquetaCodigo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEtiquetaCodigo.setForeground(new java.awt.Color(200, 190, 210));
        lblEtiquetaCodigo.setText("Código:");
        panelInfoProducto.add(lblEtiquetaCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60, 90, 22));

        lblInfoCodigo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInfoCodigo.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoCodigo.setText("-");
        panelInfoProducto.add(lblInfoCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 60, 390, 22));

        add(panelInfoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 530, 90));

        lblUbicacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUbicacion.setForeground(new java.awt.Color(255, 255, 255));
        lblUbicacion.setText("Ubicación (pasillo):");
        add(lblUbicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 345, 200, 25));

        add(txtUbicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 345, 320, 25));

        lblLote.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblLote.setForeground(new java.awt.Color(255, 255, 255));
        lblLote.setText("Lote:");
        add(lblLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 200, 25));

        add(txtLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 380, 320, 25));

        lblStockMinimo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStockMinimo.setForeground(new java.awt.Color(255, 255, 255));
        lblStockMinimo.setText("Stock mínimo*:");
        add(lblStockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 415, 200, 25));

        add(txtStockMinimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 415, 320, 25));

        lblStockMaximo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStockMaximo.setForeground(new java.awt.Color(255, 255, 255));
        lblStockMaximo.setText("Stock máximo*:");
        add(lblStockMaximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 200, 25));

        add(txtStockMaximo, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 450, 320, 25));

        lblStockActual.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStockActual.setForeground(new java.awt.Color(255, 255, 255));
        lblStockActual.setText("Stock actual*:");
        add(lblStockActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 485, 200, 25));

        add(txtStockActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 485, 320, 25));

        btnGuardar.setBackground(new java.awt.Color(85, 0, 102));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("GUARDAR CAMBIOS");
        btnGuardar.setEnabled(false);
        btnGuardar.setFocusPainted(false);
        add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 525, 180, 35));

        btnCancelar.setBackground(new java.awt.Color(85, 0, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("CANCELAR");
        btnCancelar.setFocusPainted(false);
        add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 525, 150, 35));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblEtiquetaCategoria;
    private javax.swing.JLabel lblEtiquetaCodigo;
    private javax.swing.JLabel lblEtiquetaNombre;
    private javax.swing.JLabel lblInfoCategoria;
    private javax.swing.JLabel lblInfoCodigo;
    private javax.swing.JLabel lblInfoNombre;
    private javax.swing.JLabel lblLote;
    private javax.swing.JLabel lblStockActual;
    private javax.swing.JLabel lblStockMaximo;
    private javax.swing.JLabel lblStockMinimo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUbicacion;
    private javax.swing.JList<Producto> listaResultados;
    private javax.swing.JPanel panelInfoProducto;
    private javax.swing.JScrollPane scrollResultados;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtLote;
    private javax.swing.JTextField txtStockActual;
    private javax.swing.JTextField txtStockMaximo;
    private javax.swing.JTextField txtStockMinimo;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration//GEN-END:variables
}
