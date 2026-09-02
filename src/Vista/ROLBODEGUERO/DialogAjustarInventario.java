
package Vista.ROLBODEGUERO;

import Controladores.ControladorInventarioBodega;
import Modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class DialogAjustarInventario extends JDialog {

    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private DefaultListModel<Producto> modeloResultados;
    private JList<Producto> listaResultados;
    private JScrollPane scrollResultados;

    private JPanel panelInfoProducto;
    private JLabel lblInfoNombre;
    private JLabel lblInfoCategoria;
    private JLabel lblInfoCodigo;

    private JTextField txtUbicacion;
    private JTextField txtLote;
    private JTextField txtStockMinimo;
    private JTextField txtStockMaximo;
    private JTextField txtStockActual;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Producto productoSeleccionado;
    private boolean guardado = false;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_PANEL = new Color(45, 16, 61);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_BOTON_BUSCAR = new Color(60, 45, 90);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Color COLOR_TEXTO_TENUE = new Color(200, 190, 210);
    private static final Color COLOR_BORDE = new Color(110, 70, 140);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_INFO_ETIQUETA = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_INFO_VALOR = new Font("Segoe UI", Font.PLAIN, 13);

    public DialogAjustarInventario(Frame parent) {
        super(parent, "Ajustar Inventario", true);
        initComponents();
        componentes.escalado.KryptonPanelScrollable.envolverJDialog(this);
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 18, 8, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("AJUSTAR INVENTARIO");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        row++;

        JLabel lblBuscar = new JLabel("Buscar producto (código o nombre):");
        lblBuscar.setFont(FONT_LABEL);
        lblBuscar.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(lblBuscar, gbc);
        row++;

        txtBusqueda = new JTextField();
        txtBusqueda.setFont(FONT_INFO_VALOR);
        txtBusqueda.addActionListener(e -> buscarProductos());

        btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar, COLOR_BOTON_BUSCAR);
        btnBuscar.addActionListener(e -> buscarProductos());

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(COLOR_FONDO);
        panelBusqueda.add(txtBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelBusqueda, gbc);
        row++;

        modeloResultados = new DefaultListModel<>();
        listaResultados = new JList<>(modeloResultados);
        listaResultados.setBackground(COLOR_PANEL);
        listaResultados.setForeground(COLOR_TEXTO);
        listaResultados.setSelectionBackground(COLOR_BOTON);
        listaResultados.setSelectionForeground(COLOR_TEXTO);
        listaResultados.setFont(FONT_INFO_VALOR);
        listaResultados.setVisibleRowCount(4);
        listaResultados.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel lbl = new JLabel("  " + value.getNombre() + "   —   " + value.getCodigo());
            lbl.setOpaque(true);
            lbl.setFont(FONT_INFO_VALOR);
            lbl.setBackground(isSelected ? COLOR_BOTON : COLOR_PANEL);
            lbl.setForeground(COLOR_TEXTO);
            lbl.setBorder(new EmptyBorder(4, 4, 4, 4));
            return lbl;
        });
        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaResultados.getSelectedValue() != null) {
                seleccionarProducto(listaResultados.getSelectedValue());
            }
        });

        scrollResultados = new JScrollPane(listaResultados);
        scrollResultados.setBorder(new LineBorder(COLOR_BORDE, 1));
        scrollResultados.setPreferredSize(new Dimension(100, 100));
        scrollResultados.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(scrollResultados, gbc);
        row++;

        panelInfoProducto = new JPanel(new GridBagLayout());
        panelInfoProducto.setBackground(COLOR_PANEL);
        panelInfoProducto.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1),
                new EmptyBorder(10, 16, 10, 16)));

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(3, 4, 3, 4);
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;
        gbcInfo.anchor = GridBagConstraints.WEST;

        lblInfoNombre = crearValorInfo("-");
        lblInfoCategoria = crearValorInfo("-");
        lblInfoCodigo = crearValorInfo("-");

        agregarFilaInfo(panelInfoProducto, gbcInfo, 0, "Producto:", lblInfoNombre);
        agregarFilaInfo(panelInfoProducto, gbcInfo, 1, "Categoría:", lblInfoCategoria);
        agregarFilaInfo(panelInfoProducto, gbcInfo, 2, "Código:", lblInfoCodigo);

        panelInfoProducto.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelInfoProducto, gbc);
        row++;

        txtUbicacion = new JTextField(16);
        txtLote = new JTextField(16);
        txtStockMinimo = new JTextField(16);
        txtStockMaximo = new JTextField(16);
        txtStockActual = new JTextField(16);

        row = agregarFila(gbc, row, "Ubicación (pasillo):", txtUbicacion);
        row = agregarFila(gbc, row, "Lote:", txtLote);
        row = agregarFila(gbc, row, "Stock mínimo*:", txtStockMinimo);
        row = agregarFila(gbc, row, "Stock máximo*:", txtStockMaximo);
        row = agregarFila(gbc, row, "Stock actual*:", txtStockActual);

        habilitarCamposAjuste(false);

        btnGuardar = new JButton("GUARDAR CAMBIOS");
        estilizarBoton(btnGuardar, COLOR_BOTON);
        btnGuardar.setEnabled(false);
        btnGuardar.addActionListener(e -> guardar());

        btnCancelar = new JButton("CANCELAR");
        estilizarBoton(btnCancelar, COLOR_BOTON);
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);

        setPreferredSize(new Dimension(560, 560));
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private JLabel crearValorInfo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONT_INFO_VALOR);
        lbl.setForeground(COLOR_TEXTO);
        return lbl;
    }

    private void agregarFilaInfo(JPanel panel, GridBagConstraints gbc, int row, String etiqueta, JLabel valor) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(FONT_INFO_ETIQUETA);
        lbl.setForeground(COLOR_TEXTO_TENUE);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(valor, gbc);
    }

    private int agregarFila(GridBagConstraints gbc, int row, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(COLOR_TEXTO);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        add(lbl, gbc);

        gbc.gridx = 1;
        add(campo, gbc);

        return row + 1;
    }

    private void estilizarBoton(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(FONT_BOTON);
        boton.setFocusPainted(false);
    }

    private void buscarProductos() {
        String texto = txtBusqueda.getText();
        if (texto == null || texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un código o nombre para buscar.",
                    "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        limpiarSeleccion();

        List<Producto> encontrados = ControladorInventarioBodega.buscarParaBodega(texto);

        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún producto con ese código o nombre.",
                    "Sin resultados", JOptionPane.WARNING_MESSAGE);
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
        pack();
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

        pack();
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

    private void guardar() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Busca y selecciona un producto antes de continuar.",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exito = ControladorInventarioBodega.ajustarInventario(this,
                productoSeleccionado.getCodigo(), txtUbicacion.getText(), txtLote.getText(),
                txtStockMinimo.getText(), txtStockMaximo.getText(), txtStockActual.getText());
        if (exito) {
            guardado = true;
            dispose();
        }
    }
}
