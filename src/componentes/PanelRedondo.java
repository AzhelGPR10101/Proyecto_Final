package componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PanelRedondo extends JPanel {

    private int radio = 20;
    private Color colorBorde = new Color(90, 75, 100);
    private float grosor = 1.4f;

    public PanelRedondo() {
        setOpaque(false);
        setBackground(new Color(26, 16, 36));
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);

        float mitad = grosor / 2f;
        java.awt.geom.RoundRectangle2D.Float borde = new java.awt.geom.RoundRectangle2D.Float(
                mitad, mitad, getWidth() - grosor, getHeight() - grosor, radio, radio);
        g2.setColor(colorBorde);
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(borde);

        g2.dispose();
    }

    public void setRadio(int radio) {
        this.radio = radio;
        repaint();
    }

    public int getRadio() {
        return radio;
    }

    public void setColorBorde(Color colorBorde) {
        this.colorBorde = colorBorde;
        repaint();
    }

    public Color getColorBorde() {
        return colorBorde;
    }

    public void setGrosor(float grosor) {
        this.grosor = grosor;
        repaint();
    }

    public float getGrosor() {
        return grosor;
    }
}