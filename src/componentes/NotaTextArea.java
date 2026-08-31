package componentes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class NotaTextArea extends JTextArea {

    private Color fondo = new Color(31, 10, 48);
    private Color colorLinea = new Color(90, 65, 100);
    private Color colorTexto = Color.WHITE;

    private int espacioLinea = 28;

    public NotaTextArea() {

        configurar();
    }

    private void configurar() {

        setForeground(colorTexto);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));

        setOpaque(false);

        setBorder(new EmptyBorder(
                8,
                12,
                8,
                12
        ));

        setLineWrap(true);
        setWrapStyleWord(true);

        setCaretColor(Color.WHITE);

        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(fondo);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                18,
                18
        );

        g2.setColor(colorLinea);

        int inicioY = 32;

        for (int y = inicioY; y < getHeight(); y += espacioLinea) {

            g2.drawLine(
                    10,
                    y,
                    getWidth() - 10,
                    y
            );
        }

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(new Color(80, 55, 90));

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                18,
                18
        );

        g2.dispose();
    }

    public void setColorFondo(Color color) {

        fondo = color;
        repaint();
    }

    public void setColorLinea(Color color) {

        colorLinea = color;
        repaint();
    }

    public void setColorTexto(Color color) {

        colorTexto = color;
        setForeground(color);
        repaint();
    }

    public void setEspacioLinea(int espacio) {

        espacioLinea = espacio;
        repaint();
    }
}
