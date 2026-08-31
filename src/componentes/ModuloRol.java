package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ModuloRol extends JPanel {

    private Color fondo = new Color(31, 10, 48);
    private Color borde = new Color(90, 75, 100);

    private Color fondoSeleccionado = new Color(45, 15, 65);

    private JLabel lblNombre;
    private JLabel lblDescripcion;
    private JLabel lblEstado;
    private JCheckBox checkActivo;

    private String nombreModulo = "";
    private String descripcion = "";
    private String estado = "Opcional";

    public ModuloRol() {

        configurarPanel();
        crearComponentes();
        configurarEventos();
    }

    private void configurarPanel() {

        setLayout(null);

        setOpaque(false);

        setPreferredSize(
                new java.awt.Dimension(350, 120)
        );

        setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    private void crearComponentes() {

        lblNombre = new JLabel("Nombre del módulo");

        lblNombre.setForeground(Color.WHITE);

        lblNombre.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        lblNombre.setBounds(
                16,
                14,
                250,
                25
        );

        add(lblNombre);

        lblDescripcion = new JLabel(
                "<html>Descripción del módulo</html>"
        );

        lblDescripcion.setForeground(
                new Color(220, 215, 225)
        );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblDescripcion.setVerticalAlignment(
                SwingConstants.TOP
        );

        lblDescripcion.setBounds(
                16,
                38,
                260,
                45
        );

        add(lblDescripcion);

        lblEstado = new JLabel("Opcional");

        lblEstado.setForeground(
                new Color(220, 215, 225)
        );

        lblEstado.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        lblEstado.setBounds(
                16,
                88,
                150,
                20
        );

        add(lblEstado);

        checkActivo = new JCheckBox();

        checkActivo.setOpaque(false);

        checkActivo.setFocusPainted(false);

        checkActivo.setBorderPainted(false);

        checkActivo.setContentAreaFilled(false);

        checkActivo.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        checkActivo.setBounds(
                300,
                84,
                25,
                25
        );

        add(checkActivo);
    }

    private void configurarEventos() {

        checkActivo.addActionListener(e -> {

            repaint();
        });

        MouseAdapter eventoPanel = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getSource() != checkActivo) {

                    checkActivo.setSelected(
                            !checkActivo.isSelected()
                    );

                    repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {

                repaint();
            }
        };

        addMouseListener(eventoPanel);

        lblNombre.addMouseListener(eventoPanel);

        lblDescripcion.addMouseListener(eventoPanel);

        lblEstado.addMouseListener(eventoPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (checkActivo != null &&
                checkActivo.isSelected()) {

            g2.setColor(
                    fondoSeleccionado
            );

        } else {

            g2.setColor(fondo);
        }

        g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                10,
                10
        );

        if (checkActivo != null &&
                checkActivo.isSelected()) {

            g2.setColor(
                    new Color(180, 90, 210)
            );

        } else {

            g2.setColor(borde);
        }

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                10,
                10
        );

        g2.dispose();

        super.paintComponent(g);
    }

    public String getNombreModulo() {

        return nombreModulo;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public String getEstado() {

        return estado;
    }

    public boolean isActivo() {

        return checkActivo.isSelected();
    }

    public void setNombreModulo(
            String nombreModulo) {

        this.nombreModulo =
                nombreModulo;

        lblNombre.setText(
                nombreModulo
        );
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion =
                descripcion;

        lblDescripcion.setText(
                "<html>" +
                descripcion +
                "</html>"
        );
    }

    public void setEstado(
            String estado) {

        this.estado = estado;

        lblEstado.setText(
                estado
        );
    }

    public void setActivo(
            boolean activo) {

        checkActivo.setSelected(
                activo
        );

        repaint();
    }

    public void setBloqueado(boolean bloqueado) {
        if (bloqueado) {
            checkActivo.setSelected(true);
        }
        checkActivo.setEnabled(!bloqueado);
    }

    public void setColorFondo(
            Color color) {

        fondo = color;

        repaint();
    }

    public void setColorBorde(
            Color color) {

        borde = color;

        repaint();
    }

    public void setColorFondoSeleccionado(
            Color color) {

        fondoSeleccionado = color;

        repaint();
    }
}