package Vista;

import Controladores.RecuperacionControlador;
import componentes.BotonModerno;
import componentes.PasswordModerno;
import componentes.TextFieldModerno;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DialogRecuperarContrasena extends JDialog {

    private static final String PASO_CORREO = "PASO_CORREO";
    private static final String PASO_CODIGO = "PASO_CODIGO";
    private static final String PASO_NUEVA = "PASO_NUEVA";

    private static final Color FONDO = new Color(31, 10, 48);
    private static final Color BLANCO = Color.WHITE;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenedor = new JPanel(cardLayout);

    private TextFieldModerno txtCorreo;
    private TextFieldModerno txtCodigo;
    private PasswordModerno txtNuevaPassword;
    private PasswordModerno txtConfirmarPassword;

    private String correoValidado;

    public DialogRecuperarContrasena(java.awt.Frame owner) {
        super(owner, "Recuperar contraseña", true);

        setSize(460, 380);
        setLocationRelativeTo(owner);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        panelContenedor.setBackground(FONDO);
        panelContenedor.add(crearPanelCorreo(), PASO_CORREO);
        panelContenedor.add(crearPanelCodigo(), PASO_CODIGO);
        panelContenedor.add(crearPanelNuevaContrasena(), PASO_NUEVA);

        getContentPane().add(panelContenedor, BorderLayout.CENTER);

        cardLayout.show(panelContenedor, PASO_CORREO);
    }

    private JPanel crearPanelCorreo() {
        JPanel panel = construirBase();

        JLabel titulo = crearTitulo("Recuperar contraseña");
        JLabel subtitulo = crearSubtitulo("Ingrese el correo asociado a su cuenta");

        txtCorreo = new TextFieldModerno();

        BotonModerno btnEnviar = new BotonModerno("ENVIAR CÓDIGO");
        btnEnviar.addActionListener(e -> {
            String correo = txtCorreo.getText().trim();

            btnEnviar.setText("ENVIANDO...");
            btnEnviar.setEnabled(false);

            new Thread(() -> {
                boolean enviado = RecuperacionControlador.solicitarCodigo(this, correo);

                SwingUtilities.invokeLater(() -> {
                    btnEnviar.setText("ENVIAR CÓDIGO");
                    btnEnviar.setEnabled(true);

                    if (enviado) {
                        correoValidado = correo;
                        cardLayout.show(panelContenedor, PASO_CODIGO);
                    }
                });
            }).start();
        });

        BotonModerno btnCancelar = new BotonModerno("CANCELAR");
        btnCancelar.addActionListener(e -> dispose());

        agregar(panel, titulo, 0);
        agregar(panel, subtitulo, 1);
        agregar(panel, etiquetaCampo("CORREO ELECTRÓNICO"), 2);
        agregar(panel, envolver(txtCorreo), 3);
        agregar(panel, btnEnviar, 4);
        agregar(panel, btnCancelar, 5);

        return panel;
    }

    private JPanel crearPanelCodigo() {
        JPanel panel = construirBase();

        JLabel titulo = crearTitulo("Verificar código");
        JLabel subtitulo = crearSubtitulo("Ingrese el código enviado a su correo");

        txtCodigo = new TextFieldModerno();

        BotonModerno btnValidar = new BotonModerno("VALIDAR CÓDIGO");
        btnValidar.addActionListener(e -> {
            String codigoIngresado = txtCodigo.getText().trim();

            if (RecuperacionControlador.validarCodigo(correoValidado, codigoIngresado)) {
                txtCodigo.setText("");
                cardLayout.show(panelContenedor, PASO_NUEVA);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El código es incorrecto o ya expiró (máximo 10 minutos).",
                        "Código inválido", JOptionPane.ERROR_MESSAGE);
            }
        });

        BotonModerno btnReenviar = new BotonModerno("REENVIAR CÓDIGO");
        btnReenviar.addActionListener(e -> {
            btnReenviar.setEnabled(false);
            new Thread(() -> {
                RecuperacionControlador.solicitarCodigo(this, correoValidado);
                SwingUtilities.invokeLater(() -> btnReenviar.setEnabled(true));
            }).start();
        });

        BotonModerno btnCancelar = new BotonModerno("CANCELAR");
        btnCancelar.addActionListener(e -> {
            RecuperacionControlador.limpiarSesionRecuperacion();
            dispose();
        });

        agregar(panel, titulo, 0);
        agregar(panel, subtitulo, 1);
        agregar(panel, etiquetaCampo("CÓDIGO DE VERIFICACIÓN"), 2);
        agregar(panel, envolver(txtCodigo), 3);
        agregar(panel, btnValidar, 4);
        agregar(panel, btnReenviar, 5);
        agregar(panel, btnCancelar, 6);

        return panel;
    }

    private JPanel crearPanelNuevaContrasena() {
        JPanel panel = construirBase();

        JLabel titulo = crearTitulo("Nueva contraseña");
        JLabel subtitulo = crearSubtitulo("Ingrese y confirme su nueva contraseña");

        txtNuevaPassword = new PasswordModerno();
        txtConfirmarPassword = new PasswordModerno();

        BotonModerno btnAceptar = new BotonModerno("ACEPTAR");
        btnAceptar.addActionListener(e -> {
            String nueva = new String(txtNuevaPassword.getPassword());
            String confirmacion = new String(txtConfirmarPassword.getPassword());

            if (RecuperacionControlador.validarNuevaPassword(this, nueva, confirmacion)) {
                if (RecuperacionControlador.actualizarPassword(this, correoValidado, nueva)) {
                    dispose();
                }
            }
        });

        BotonModerno btnCancelar = new BotonModerno("CANCELAR");
        btnCancelar.addActionListener(e -> {
            RecuperacionControlador.limpiarSesionRecuperacion();
            dispose();
        });

        agregar(panel, titulo, 0);
        agregar(panel, subtitulo, 1);
        agregar(panel, etiquetaCampo("NUEVA CONTRASEÑA"), 2);
        agregar(panel, envolver(txtNuevaPassword), 3);
        agregar(panel, etiquetaCampo("CONFIRMAR CONTRASEÑA"), 4);
        agregar(panel, envolver(txtConfirmarPassword), 5);
        agregar(panel, btnAceptar, 6);
        agregar(panel, btnCancelar, 7);

        return panel;
    }

    private JPanel construirBase() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        return panel;
    }

    private void agregar(JPanel panel, java.awt.Component componente, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        panel.add(componente, gbc);
    }

    private JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Lucida Bright", Font.BOLD, 22));
        lbl.setForeground(BLANCO);
        return lbl;
    }

    private JLabel crearSubtitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(220, 220, 220));
        return lbl;
    }

    private JLabel etiquetaCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Lucida Bright", Font.BOLD, 12));
        lbl.setForeground(BLANCO);
        return lbl;
    }

    private JPanel envolver(java.awt.Component campo) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(55, 25, 75));
        contenedor.setBorder(BorderFactory.createLineBorder(BLANCO, 1));
        contenedor.add(campo, BorderLayout.CENTER);
        contenedor.setPreferredSize(new java.awt.Dimension(100, 36));
        return contenedor;
    }

    public static void mostrar(java.awt.Frame owner) {
        SwingUtilities.invokeLater(() -> {
            DialogRecuperarContrasena dialogo = new DialogRecuperarContrasena(owner);
            dialogo.setVisible(true);
        });
    }
}