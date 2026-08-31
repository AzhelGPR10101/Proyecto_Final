package componentes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class LabelModerno extends JLabel {

    private Color fondo = new Color(55, 25, 75);
    private Color borde = new Color(90, 75, 100);

    public LabelModerno() {

        configurar();

    }

    private void configurar() {

        setOpaque(false);

        setForeground(Color.WHITE);

        setFont(new Font("Segoe UI", Font.PLAIN, 14));

        setHorizontalAlignment(CENTER);

        setVerticalAlignment(CENTER);

        setBorder(new EmptyBorder(
                new Insets(5, 15, 5, 15)));

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(fondo);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                30,
                30);

        g2.setColor(borde);

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                30,
                30);

        g2.dispose();

        super.paintComponent(g);

    }

    @Override
    protected void paintBorder(Graphics g) {

    }

    public void setColorFondo(Color color) {

        this.fondo = color;
        repaint();

    }

    public void setColorBorde(Color color) {

        this.borde = color;
        repaint();

    }

}