package componentes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class ComboBoxModerno extends JComboBox<String> {

    private Color fondo = new Color(55, 25, 75);
    private Color borde = new Color(90, 75, 100);
    private Color fondoSeleccionado = new Color(75, 35, 95);
    private Color colorTexto = Color.WHITE;

    public ComboBoxModerno() {

        super();

        configurar();

        addItem("USD - Dólar estadounidense");
        addItem("EUR - Euro");
        addItem("GBP - Libra esterlina");
        addItem("JPY - Yen japonés");
        addItem("CNY - Yuan chino");
    }

    private void configurar() {

        setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        setForeground(colorTexto);
        setBackground(fondo);

        setOpaque(false);
        setFocusable(false);

        setBorder(
                BorderFactory.createEmptyBorder(
                        0, 10, 0, 5
                )
        );

        setMaximumRowCount(5);

        setUI(new BasicComboBoxUI() {

            @Override
            protected JButton createArrowButton() {

                JButton boton = new JButton("▼");

                boton.setFont(
                        new Font("Segoe UI", Font.PLAIN, 12)
                );

                boton.setForeground(Color.WHITE);
                boton.setBackground(fondo);

                boton.setBorder(null);
                boton.setFocusPainted(false);
                boton.setContentAreaFilled(false);

                return boton;
            }
        });

        setRenderer(new ListCellRenderer<Object>() {

            @Override
            public Component getListCellRendererComponent(
                    JList<? extends Object> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                JLabel label = new JLabel(value == null ? "" : value.toString());

                label.setFont(
                        new Font(
                                "Segoe UI",
                                Font.PLAIN,
                                14
                        )
                );

                label.setForeground(Color.WHITE);
                label.setOpaque(true);

                if (isSelected) {

                    label.setBackground(
                            fondoSeleccionado
                    );

                } else {

                    label.setBackground(fondo);
                }

                label.setBorder(
                        BorderFactory.createEmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                );

                return label;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(fondo);

        int radio = Math.min(18, Math.min(getWidth(), getHeight()) / 2 - 1);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                radio,
                radio
        );

        g2.setColor(borde);

        g2.draw(new java.awt.geom.RoundRectangle2D.Float(
                0.5f,
                0.5f,
                getWidth() - 1,
                getHeight() - 1,
                radio,
                radio
        ));

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public void paintBorder(Graphics g) {
    }
}