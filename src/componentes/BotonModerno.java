package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class BotonModerno extends JButton {

    private Color colorNormal = new Color(55, 25, 75);
    private Color colorHover = new Color(70, 35, 95);
    private Color colorClick = new Color(90, 45, 120);

    private Color colorBorde = new Color(90, 75, 100);

    private boolean hover = false;
    private boolean presionado = false;

    public BotonModerno() {

        configurarBoton();

    }

    public BotonModerno(String texto) {

        super(texto);
        configurarBoton();

    }

    private void configurarBoton() {

        setForeground(Color.WHITE);

        setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        setFocusPainted(false);

        setBorderPainted(false);

        setContentAreaFilled(false);

        setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (presionado) {

            g2.setColor(colorClick);

        } else if (hover) {

            g2.setColor(colorHover);

        } else {

            g2.setColor(colorNormal);

        }

        g2.fillRoundRect(
                1,
                1,
                getWidth() - 3,
                getHeight() - 3,
                20,
                20
        );

        g2.setColor(colorBorde);

        g2.drawRoundRect(
                1,
                1,
                getWidth() - 3,
                getHeight() - 3,
                20,
                20
        );

        g2.dispose();

        super.paintComponent(g);

    }

    @Override
    protected void paintBorder(Graphics g) {

    }

    @Override
    public void setText(String text) {

        super.setText(text);

        repaint();

    }

    public void setColorNormal(Color color) {

        colorNormal = color;
        repaint();

    }

    public void setColorHover(Color color) {

        colorHover = color;
        repaint();

    }

    public void setColorClick(Color color) {

        colorClick = color;
        repaint();

    }

    public void setColorBorde(Color color) {

        colorBorde = color;
        repaint();

    }

    {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                hover = true;
                repaint();

            }

            @Override
            public void mouseExited(MouseEvent e) {

                hover = false;
                repaint();

            }

            @Override
            public void mousePressed(MouseEvent e) {

                presionado = true;
                repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                presionado = false;
                repaint();

            }

        });

    }

}