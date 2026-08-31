
package Vista.PROVEEDORES;

import javax.swing.*;
import java.awt.*;

public class DialogModificarProveedor extends JDialog {

    private JTextField txtNombreEmpresa;
    private JTextField txtNombreContacto;
    private JTextField txtRuc;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private final String rucOriginal;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public DialogModificarProveedor(Modelo.Proveedores proveedor) {
        super((Frame) null, "Modificar Proveedor", true);
        this.rucOriginal = proveedor.getRuc();
        initComponents();
        cargarDatos(proveedor);
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("MODIFICAR PROVEEDOR");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        gbc.gridwidth = 1;
        row++;

        txtNombreEmpresa = new JTextField(18);
        txtNombreContacto = new JTextField(18);
        txtRuc = new JTextField(18);
        txtRuc.setEditable(false);
        txtRuc.setBackground(new Color(60, 60, 60));
        txtRuc.setForeground(COLOR_TEXTO);
        txtDireccion = new JTextField(18);
        txtTelefono = new JTextField(18);
        txtCorreo = new JTextField(18);

        row = agregarFila(gbc, row, "Empresa*:", txtNombreEmpresa);
        row = agregarFila(gbc, row, "Contacto*:", txtNombreContacto);
        row = agregarFila(gbc, row, "RUC:", txtRuc);
        row = agregarFila(gbc, row, "Dirección*:", txtDireccion);
        row = agregarFila(gbc, row, "Teléfono*:", txtTelefono);
        row = agregarFila(gbc, row, "Correo*:", txtCorreo);

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

    private void estilizarBoton(JButton boton) {
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(FONT_BOTON);
        boton.setFocusPainted(false);
    }

    private void cargarDatos(Modelo.Proveedores proveedor) {
        txtNombreEmpresa.setText(proveedor.getNombreEmpresa());
        txtNombreContacto.setText(proveedor.getNombreContacto());
        txtRuc.setText(proveedor.getRuc());
        txtDireccion.setText(proveedor.getDireccion());
        txtTelefono.setText(proveedor.getTelefono());
        txtCorreo.setText(proveedor.getCorreo());
    }

    private void guardarCambios() {
        Modelo.Proveedores proveedor = new Modelo.Proveedores(
                rucOriginal,
                txtNombreEmpresa.getText().trim(),
                txtNombreContacto.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtDireccion.getText().trim()
        );

        boolean exito = new Controladores.ControladorProveedor().modificar(proveedor);

        if (exito) {
            dispose();
        }
    }
}