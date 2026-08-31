/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista.ROLBODEGUERO;

import javax.swing.*;
import java.awt.*;

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
    private JTextField txtCodigo;
    private JTextField txtCantidad;
    private JTextField txtDestino;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    private String resultado;

    private static final Color COLOR_FONDO = new Color(31, 11, 43);
    private static final Color COLOR_BOTON = new Color(85, 0, 102);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOTON = new Font("Segoe UI", Font.BOLD, 14);

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
        gbc.insets = new Insets(10, 15, 10, 15);
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
        gbc.gridwidth = 1;
        row++;

        txtCodigo = new JTextField(16);
        txtCantidad = new JTextField(16);
        row = agregarFila(gbc, row, "Código de producto*:", txtCodigo);
        row = agregarFila(gbc, row, "Cantidad*:", txtCantidad);

        if (modo == Modo.TRANSFERENCIA) {
            txtDestino = new JTextField(16);
            row = agregarFila(gbc, row, "Destino*:", txtDestino);
        }

        btnConfirmar = new JButton(modo.titulo.toUpperCase());
        estilizarBoton(btnConfirmar);
        btnConfirmar.addActionListener(e -> confirmar());

        btnCancelar = new JButton("CANCELAR");
        estilizarBoton(btnCancelar);
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);

        pack();
        setLocationRelativeTo(getParent());
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

    private void confirmar() {
        String codigo = txtCodigo.getText();
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
