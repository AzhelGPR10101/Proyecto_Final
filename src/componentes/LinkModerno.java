package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class LinkModerno extends JButton {

    private Color colorNormal = new Color(236, 64, 122);
    private Color colorHover = new Color(255, 105, 180);

    private boolean hover = false;

    public LinkModerno() {
        configurar();
    }

    public LinkModerno(String texto) {
        super(texto);
        configurar();
    }

    private void configurar() {

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setForeground(colorNormal);

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setFont(new Font("Segoe UI", Font.BOLD, 15));

        setHorizontalAlignment(CENTER);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                hover = true;
                setForeground(colorHover);
                setFont(new Font("Segoe UI", Font.BOLD, 16));
                repaint();

            }

            @Override
            public void mouseExited(MouseEvent e) {

                hover = false;
                setForeground(colorNormal);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
                repaint();

            }

        });

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        super.paintComponent(g2);

        if (hover) {

            FontMetrics fm = g2.getFontMetrics();

            int anchoTexto = fm.stringWidth(getText());

            int x = (getWidth() - anchoTexto) / 2;

            int y = (getHeight() + fm.getAscent()) / 2;

            g2.setColor(getForeground());

            g2.drawLine(
                    x,
                    y + 2,
                    x + anchoTexto,
                    y + 2
            );

        }

        g2.dispose();

    }

    @Override
    protected void paintBorder(Graphics g) {

    }

}