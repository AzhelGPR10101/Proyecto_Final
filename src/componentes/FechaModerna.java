package componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class FechaModerna extends JLabel {

    private Color fondo = new Color(55, 25, 75);
    private Color borde = new Color(90, 75, 100);

    private Color fondoCalendario = new Color(31, 10, 48);
    private Color fondoBotones = new Color(55, 25, 75);
    private Color fondoHover = new Color(80, 35, 100);
    private Color colorSeleccionado = new Color(180, 60, 180);

    private final SimpleDateFormat formato =
            new SimpleDateFormat("yyyy-MM-dd");

    private Calendar fechaActual =
            Calendar.getInstance();

    private Runnable alSeleccionarFecha;

    public void setAlSeleccionarFecha(Runnable listener) {
        this.alSeleccionarFecha = listener;
    }

    public FechaModerna() {

        configurar();

    }

    private void configurar() {

        setOpaque(false);

        setForeground(Color.WHITE);

        setFont(new Font(
                "Lucida Bright",
                Font.BOLD,
                14
        ));

        setHorizontalAlignment(
                SwingConstants.CENTER
        );

        setVerticalAlignment(
                SwingConstants.CENTER
        );

        setBorder(new EmptyBorder(
                new Insets(5, 15, 5, 15)
        ));

        setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        setText("");

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (isEnabled()) {
                    mostrarCalendario();
                }

            }

        });

    }

    private void mostrarCalendario() {

        final JDialog dialogo =
                new JDialog();

        dialogo.setTitle(
                "Seleccionar fecha"
        );

        dialogo.setModal(true);

        dialogo.setSize(
                420,
                390
        );

        dialogo.setResizable(false);

        dialogo.setLocationRelativeTo(this);

        dialogo.getContentPane().setBackground(
                fondoCalendario
        );

        JPanel principal =
                new JPanel(new BorderLayout());

        principal.setBackground(
                fondoCalendario
        );

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JPanel cabecera =
                new JPanel(new BorderLayout());

        cabecera.setBackground(
                fondoCalendario
        );

        JButton anterior =
                crearBoton("<");

        JButton siguiente =
                crearBoton(">");

        JLabel titulo =
                new JLabel(
                        "",
                        SwingConstants.CENTER
                );

        titulo.setForeground(
                Color.WHITE
        );

        titulo.setFont(new Font(
                "Lucida Bright",
                Font.BOLD,
                20
        ));

        cabecera.add(
                anterior,
                BorderLayout.WEST
        );

        cabecera.add(
                titulo,
                BorderLayout.CENTER
        );

        cabecera.add(
                siguiente,
                BorderLayout.EAST
        );

        principal.add(
                cabecera,
                BorderLayout.NORTH
        );

        JPanel calendario =
                new JPanel(
                        new GridLayout(
                                7,
                                7,
                                5,
                                5
                        )
                );

        calendario.setBackground(
                fondoCalendario
        );

        principal.add(
                calendario,
                BorderLayout.CENTER
        );

        Runnable actualizar =
                () -> {

            calendario.removeAll();

            String[] dias = {
                "L", "M", "X", "J",
                "V", "S", "D"
            };

            for (String dia : dias) {

                JLabel etiqueta =
                        new JLabel(
                                dia,
                                SwingConstants.CENTER
                        );

                etiqueta.setForeground(
                        new Color(210, 170, 220)
                );

                etiqueta.setFont(
                        new Font(
                                "Lucida Bright",
                                Font.BOLD,
                                13
                        )
                );

                calendario.add(
                        etiqueta
                );

            }

            Calendar cal =
                    (Calendar) fechaActual.clone();

            cal.set(
                    Calendar.DAY_OF_MONTH,
                    1
            );

            int primerDia =
                    cal.get(Calendar.DAY_OF_WEEK);

            int posicionInicial =
                    primerDia - 2;

            if (posicionInicial < 0) {

                posicionInicial = 6;

            }

            for (int i = 0;
                    i < posicionInicial;
                    i++) {

                calendario.add(
                        new JLabel("")
                );

            }

            int cantidadDias =
                    cal.getActualMaximum(
                            Calendar.DAY_OF_MONTH
                    );

            for (int dia = 1;
                    dia <= cantidadDias;
                    dia++) {

                final int diaSeleccionado =
                        dia;

                JButton boton =
                        crearBotonDia(
                                String.valueOf(dia)
                        );

                boton.addActionListener(e -> {

                    Calendar seleccion =
                            (Calendar)
                            fechaActual.clone();

                    seleccion.set(
                            Calendar.DAY_OF_MONTH,
                            diaSeleccionado
                    );

                    setText(
                            formato.format(
                                    seleccion.getTime()
                            )
                    );

                    dialogo.dispose();

                    if (alSeleccionarFecha != null) {
                        alSeleccionarFecha.run();
                    }

                });

                calendario.add(
                        boton
                );

            }

            int celdasUsadas =
                    posicionInicial + cantidadDias;

            int celdasRestantes =
                    42 - celdasUsadas;

            for (int i = 0;
                    i < celdasRestantes;
                    i++) {

                calendario.add(
                        new JLabel("")
                );

            }

            titulo.setText(
                    new SimpleDateFormat(
                            "MMMM yyyy"
                    ).format(
                            fechaActual.getTime()
                    )
            );

            calendario.revalidate();

            calendario.repaint();

        };

        anterior.addActionListener(e -> {

            fechaActual.add(
                    Calendar.MONTH,
                    -1
            );

            actualizar.run();

        });

        siguiente.addActionListener(e -> {

            fechaActual.add(
                    Calendar.MONTH,
                    1
            );

            actualizar.run();

        });

        actualizar.run();

        dialogo.add(
                principal
        );

        dialogo.setVisible(true);

    }

    private JButton crearBoton(
            String texto
    ) {

        JButton boton =
                new JButton(texto);

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(new Font(
                "Lucida Bright",
                Font.BOLD,
                18
        ));

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setContentAreaFilled(false);

        boton.setOpaque(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setPreferredSize(
                new Dimension(50, 40)
        );

        return boton;

    }

    private JButton crearBotonDia(
            String texto
    ) {

        JButton boton =
                new JButton(texto);

        boton.setForeground(
                Color.WHITE
        );

        boton.setFont(new Font(
                "Lucida Bright",
                Font.PLAIN,
                14
        ));

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setContentAreaFilled(false);

        boton.setOpaque(false);

        boton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseEntered(
                    MouseEvent e
            ) {

                boton.setOpaque(true);

                boton.setBackground(
                        fondoHover
                );

            }

            @Override
            public void mouseExited(
                    MouseEvent e
            ) {

                boton.setOpaque(false);

            }

        });

        return boton;

    }

    @Override
    protected void paintComponent(
            Graphics g
    ) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(
                fondo
        );

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                30,
                30
        );

        g2.setColor(
                borde
        );

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                30,
                30
        );

        g2.dispose();

        super.paintComponent(g);

    }

    @Override
    protected void paintBorder(
            Graphics g
    ) {

    }

    public String getFecha() {

        return getText();

    }

    public java.sql.Date getFechaSQL() {

        if (getText() == null
                || getText().isEmpty()) {

            return null;

        }

        try {

            return java.sql.Date.valueOf(
                    getText()
            );

        } catch (Exception e) {

            return null;

        }

    }

    public void setColorFondo(
            Color color
    ) {

        fondo = color;

        repaint();

    }

    public void setColorBorde(
            Color color
    ) {

        borde = color;

        repaint();

    }

}