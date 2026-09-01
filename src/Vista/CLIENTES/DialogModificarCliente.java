
package Vista.CLIENTES;

import javax.swing.*;
import java.awt.*;

public class DialogModificarCliente extends JDialog {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCedula;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private final String cedulaOriginal;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public DialogModificarCliente(Modelo.Cliente cliente) {
        super((Frame) null, "Modificar Cliente", true);
        this.cedulaOriginal = cliente.getCedula();
        initComponents();
        cargarDatos(cliente);
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("MODIFICAR CLIENTE");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        gbc.gridwidth = 1;
        row++;

        txtNombre = new JTextField(18);
        txtApellido = new JTextField(18);
        txtCedula = new JTextField(18);
        txtCedula.setEditable(false);
        txtCedula.setBackground(new Color(60, 60, 60));
        txtCedula.setForeground(COLOR_TEXTO);
        txtDireccion = new JTextField(18);
        txtTelefono = new JTextField(18);
        componentes.FiltrosTexto.aplicarSoloNumeros(txtTelefono, 10);
        txtCorreo = new JTextField(18);

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });

        row = agregarFila(gbc, row, "Nombre*:", txtNombre);
        row = agregarFila(gbc, row, "Apellido*:", txtApellido);
        row = agregarFila(gbc, row, "Cédula:", txtCedula);
        row = agregarFila(gbc, row, "Dirección:", txtDireccion);
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

    private void cargarDatos(Modelo.Cliente cliente) {
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtCedula.setText(cliente.getCedula());
        txtDireccion.setText(cliente.getDireccion());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getCorreo());
    }

    private void guardarCambios() {
        String nombre = Controladores.Validaciones.aMayusculas(txtNombre.getText());
        String apellido = Controladores.Validaciones.aMayusculas(txtApellido.getText());
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim().toLowerCase();
        String direccion = Controladores.Validaciones.aMayusculas(txtDireccion.getText());

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Los campos Nombre y Apellido son obligatorios.",
                "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El campo Dirección es obligatorio.",
                "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Controladores.Validaciones.validarTelefono(telefono)) {
            JOptionPane.showMessageDialog(this,
                "El número de teléfono debe tener exactamente 10 dígitos numéricos.",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Controladores.Validaciones.validarCorreo(correo)) {
            JOptionPane.showMessageDialog(this,
                "Por favor, ingrese un correo electrónico válido (ej: usuario@ejemplo.com).",
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Modelo.Cliente cliente = new Modelo.Cliente(
                cedulaOriginal,
                nombre,
                apellido,
                telefono,
                correo,
                direccion
        );

        boolean exito = new Controladores.ControladorCliente().modificar(cliente);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Cliente modificado con éxito.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo modificar el cliente.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
