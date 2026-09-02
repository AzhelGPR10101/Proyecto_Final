
package Vista.ROLBODEGUERO;

import Modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class DialogMovimientoBodega extends JDialog {

    public enum Modo {
        ENTRADA("Registrar Entrada"),
        SALIDA("Confirmar Salida"),
        TRANSFERENCIA("Transferir Bodega");

        final String titulo;

        Modo(String titulo) {
            this.titulo = titulo;
        }
    }

    private final Modo modo;

    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private DefaultListModel<Producto> modeloResultados;
    private JList<Producto> listaResultados;
    private JScrollPane scrollResultados;

    private JPanel panelInfoProducto;
    private JLabel lblInfoNombre;
    private JLabel lblInfoCategoria;
    private JLabel lblInfoUbicacion;
    private JLabel lblInfoLote;
    private JLabel lblInfoStock;

    private JTextField txtCantidad;
    private JTextField txtDestino;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    private Producto productoSeleccionado;
    private String resultado;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_PANEL = new Color(45, 16, 61);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_BOTON_BUSCAR = new Color(60, 45, 90);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Color COLOR_TEXTO_TENUE = new Color(200, 190, 210);
    private static final Color COLOR_BORDE = new Color(110, 70, 140);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_INFO_ETIQUETA = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_INFO_VALOR = new Font("Segoe UI", Font.PLAIN, 13);

    public DialogMovimientoBodega(Frame parent, Modo modo) {
        super(parent, modo.titulo, true);
        this.modo = modo;
        initComponents();
        componentes.escalado.KryptonPanelScrollable.envolverJDialog(this);
    }

    public String getResultado() {
        return resultado;
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 18, 8, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel(modo.titulo.toUpperCase());
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
                new EmptyBorder(12, 16, 12, 16)));

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(3, 4, 3, 4);
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;
        gbcInfo.anchor = GridBagConstraints.WEST;

        lblInfoNombre = crearValorInfo("-");
        lblInfoCategoria = crearValorInfo("-");
        lblInfoUbicacion = crearValorInfo("-");
        lblInfoLote = crearValorInfo("-");
        lblInfoStock = crearValorInfo("-");

        agregarFilaInfo(panelInfoProducto, gbcInfo, 0, "Producto:", lblInfoNombre);
        agregarFilaInfo(panelInfoProducto, gbcInfo, 1, "Categoría:", lblInfoCategoria);
        agregarFilaInfo(panelInfoProducto, gbcInfo, 2, "Ubicación:", lblInfoUbicacion);
        agregarFilaInfo(panelInfoProducto, gbcInfo, 3, "Lote:", lblInfoLote);
        agregarFilaInfo(panelInfoProducto, gbcInfo, 4, "Stock actual:", lblInfoStock);

        panelInfoProducto.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelInfoProducto, gbc);
        row++;

        txtCantidad = new JTextField(16);
        txtCantidad.setEnabled(false);
        row = agregarFila(gbc, row, "Cantidad*:", txtCantidad);

        if (modo == Modo.TRANSFERENCIA) {
            txtDestino = new JTextField(16);
            txtDestino.setEnabled(false);
            row = agregarFila(gbc, row, "Destino*:", txtDestino);
        }

        btnConfirmar = new JButton(modo.titulo.toUpperCase());
        estilizarBoton(btnConfirmar, COLOR_BOTON);
        btnConfirmar.setEnabled(false);
        btnConfirmar.addActionListener(e -> confirmar());

        btnCancelar = new JButton("CANCELAR");
        estilizarBoton(btnCancelar, COLOR_BOTON);
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);

        setPreferredSize(new Dimension(560, 430));
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

    private int agregarFila(GridBagConstraints gbc, int row, String etiqueta, JTextField campo) {
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

        List<Producto> encontrados = Controladores.ControladorInventarioBodega.buscarParaBodega(texto);

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
        lblInfoUbicacion.setText(p.getUbicacionPasillo() == null || p.getUbicacionPasillo().isEmpty()
                ? "N/A" : p.getUbicacionPasillo());
        lblInfoLote.setText(p.getLote() == null || p.getLote().isEmpty() ? "-" : p.getLote());
        lblInfoStock.setText(p.getCantidad() + " uds.");
        panelInfoProducto.setVisible(true);

        txtCantidad.setEnabled(true);
        if (txtDestino != null) {
            txtDestino.setEnabled(true);
        }
        btnConfirmar.setEnabled(true);

        pack();
    }

    private void limpiarSeleccion() {
        productoSeleccionado = null;
        panelInfoProducto.setVisible(false);
        txtCantidad.setEnabled(false);
        txtCantidad.setText("");
        if (txtDestino != null) {
            txtDestino.setEnabled(false);
            txtDestino.setText("");
        }
        btnConfirmar.setEnabled(false);
    }

    private void confirmar() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Busca y selecciona un producto antes de continuar.",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = productoSeleccionado.getCodigo();
        String cantidad = txtCantidad.getText();

        switch (modo) {
            case ENTRADA:
                resultado = Controladores.ControladorInventarioBodega.registrarEntrada(this, codigo, cantidad);
                break;
            case SALIDA:
                resultado = Controladores.ControladorInventarioBodega.confirmarSalida(this, codigo, cantidad);
                break;

        }

        if (resultado != null) {
            dispose();
        }
    }
}
