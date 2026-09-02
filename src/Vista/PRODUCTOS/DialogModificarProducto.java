package Vista.PRODUCTOS;

import javax.swing.*;
import java.awt.*;

public class DialogModificarProducto extends JDialog {

    private JTextField txtNombre;
    private JComboBox<String> txtCategoria;
    private JTextField txtCodigo;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JTextField txtStockMinimo;
    private JCheckBox chkTieneIva;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private final String codigoOriginal;
    private boolean guardado = false;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public DialogModificarProducto(Modelo.Producto producto) {
        super((Frame) null, "Modificar Producto", true);
        this.codigoOriginal = producto.getCodigo();
        initComponents();
        cargarDatos(producto);
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("MODIFICAR PRODUCTO");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        gbc.gridwidth = 1;
        row++;

        txtCodigo = new JTextField(18);
        txtCodigo.setEditable(false);
        txtCodigo.setBackground(new Color(60, 60, 60));
        txtCodigo.setForeground(COLOR_TEXTO);
        txtNombre = new JTextField(18);
        txtCategoria = new JComboBox<>(Controladores.ControladorProducto.obtenerNombresCategorias());
        txtCantidad = new JTextField(18);
        txtPrecio = new JTextField(18);
        txtStockMinimo = new JTextField(18);

        componentes.FiltrosTexto.aplicarLetrasYNumeros(txtNombre, 150);
        componentes.FiltrosTexto.aplicarSoloNumeros(txtCantidad, 8);
        componentes.FiltrosTexto.aplicarSoloDecimal(txtPrecio, 8);

        chkTieneIva = new JCheckBox("Tiene IVA");
        chkTieneIva.setForeground(COLOR_TEXTO);
        chkTieneIva.setBackground(COLOR_FONDO);
        chkTieneIva.setFont(FONT_LABEL);

        row = agregarFila(gbc, row, "Código:", txtCodigo);
        row = agregarFila(gbc, row, "Nombre*:", txtNombre);
        row = agregarFila(gbc, row, "Categoría*:", txtCategoria);
        row = agregarFila(gbc, row, "Cantidad*:", txtCantidad);
        row = agregarFila(gbc, row, "Precio Unit.*:", txtPrecio);
        componentes.FiltrosTexto.aplicarSoloNumeros(txtStockMinimo, 8);
        row = agregarFila(gbc, row, "Stock minimo:", txtStockMinimo);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(chkTieneIva, gbc);
        gbc.gridwidth = 1;
        row++;

        btnGuardar = new JButton("GUARDAR CAMBIOS");
        estilizarBoton(btnGuardar);
        btnGuardar.addActionListener(e -> guardarCambios());

        btnCancelar = new JButton("CANCELAR");
        estilizarBoton(btnCancelar);
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);

        setSize(430, 430);
        setLocationRelativeTo(null);
        setResizable(false);
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

    private void estilizarBoton(JButton boton) {
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(FONT_BOTON);
        boton.setFocusPainted(false);
    }

          private void cargarDatos(Modelo.Producto producto) {
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        txtCategoria.setSelectedItem(producto.getCategoria());
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtPrecio.setText(String.valueOf(producto.getPrecioUnitario()));
        txtStockMinimo.setText(String.valueOf(producto.getStockMinimo()));
        chkTieneIva.setSelected(producto.isTieneIva());
    }

    private void guardarCambios() {
        boolean exito = Controladores.ControladorProducto.actualizarProducto(
                this,
                codigoOriginal,
                txtNombre.getText(),
                (txtCategoria.getSelectedItem() == null) ? "" : txtCategoria.getSelectedItem().toString(),
                txtCantidad.getText(),
                txtPrecio.getText(),
                chkTieneIva.isSelected(),
                txtStockMinimo.getText()
        );

        if (exito) {
            guardado = true;
            dispose();
        }
    }

    public boolean isGuardado() {
        return guardado;
    }
        }
