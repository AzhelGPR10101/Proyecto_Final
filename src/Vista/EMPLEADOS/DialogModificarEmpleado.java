
package Vista.EMPLEADOS;

import javax.swing.*;
import java.awt.*;

public class DialogModificarEmpleado extends JDialog {

    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCedula;
    private JTextField txtSueldo;
    private JTextField txtTelefono;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private final String cedulaOriginal;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public DialogModificarEmpleado(Modelo.Empleado empleado) {
        super((Frame) null, "Modificar Empleado", true);
        this.cedulaOriginal = empleado.getCedula();
        initComponents();
        cargarDatos(empleado);
    }

    private void initComponents() {
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titulo = new JLabel("MODIFICAR EMPLEADO");
        titulo.setFont(FONT_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(titulo, gbc);
        gbc.gridwidth = 1;
        row++;

        txtNombres = new JTextField(18);
        txtApellidos = new JTextField(18);
        txtCedula = new JTextField(18);
        txtCedula.setEditable(false);
        txtCedula.setBackground(new Color(60, 60, 60));
        txtCedula.setForeground(COLOR_TEXTO);
        txtSueldo = new JTextField(18);
        txtTelefono = new JTextField(18);
        txtUsuario = new JTextField(18);
        txtPassword = new JPasswordField(18);

        row = agregarFila(gbc, row, "Nombres*:", txtNombres);
        row = agregarFila(gbc, row, "Apellidos*:", txtApellidos);
        row = agregarFila(gbc, row, "Cédula:", txtCedula);
        row = agregarFila(gbc, row, "Sueldo*:", txtSueldo);
        row = agregarFila(gbc, row, "Teléfono*:", txtTelefono);
        row = agregarFila(gbc, row, "Usuario*:", txtUsuario);
        row = agregarFila(gbc, row, "Nueva Contraseña:", txtPassword);

        JLabel notaPassword = new JLabel("(dejar en blanco para no cambiarla)");
        notaPassword.setForeground(new Color(220, 220, 220));
        notaPassword.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        gbc.gridx = 1;
        gbc.gridy = row;
        add(notaPassword, gbc);
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

        setSize(430, 500);
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

    private void cargarDatos(Modelo.Empleado empleado) {
        txtNombres.setText(empleado.getNombres());
        txtApellidos.setText(empleado.getApellidos());
        txtCedula.setText(empleado.getCedula());
        txtSueldo.setText(String.valueOf(empleado.getSueldo()));
        txtTelefono.setText(empleado.getTelefono());
        txtUsuario.setText(empleado.getUsername());
    }

    private void guardarCambios() {
        String password = new String(txtPassword.getPassword());

        boolean exito = Controladores.EmpleadoControlador.actualizarEmpleado(
                this,
                cedulaOriginal,
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtSueldo.getText().trim(),
                txtTelefono.getText().trim(),
                txtUsuario.getText().trim(),
                password
        );

        if (exito) {
            dispose();
        }
    }
}
