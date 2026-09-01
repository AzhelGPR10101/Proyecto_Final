package componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class CheckBoxModerno extends JCheckBox {

    private Color colorFondo = new Color(31, 10, 48);
    private Color colorBorde = new Color(90, 75, 100);
    private Color colorMarcado = new Color(180, 90, 210);
    private static final int TAMANO_CAJA = 18;

    public CheckBoxModerno() {
        configurar();
    }

    public CheckBoxModerno(String texto) {
        super(texto);
        configurar();
    }

    private void configurar() {
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setIconTextGap(10);

        Icon caja = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(isSelected() ? colorMarcado.darker() : colorFondo);
                g2.fillRoundRect(x, y, TAMANO_CAJA, TAMANO_CAJA, 6, 6);

                g2.setColor(isSelected() ? colorMarcado : colorBorde);
                g2.drawRoundRect(x, y, TAMANO_CAJA - 1, TAMANO_CAJA - 1, 6, 6);

                if (isSelected()) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x + 4, y + TAMANO_CAJA / 2, x + TAMANO_CAJA / 2 - 1, y + TAMANO_CAJA - 5);
                    g2.drawLine(x + TAMANO_CAJA / 2 - 1, y + TAMANO_CAJA - 5, x + TAMANO_CAJA - 4, y + 4);
                }

                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return TAMANO_CAJA;
            }

            @Override
            public int getIconHeight() {
                return TAMANO_CAJA;
            }
        };

        setIcon(caja);
        setSelectedIcon(caja);
        setRolloverIcon(caja);
        setRolloverSelectedIcon(caja);
        setPressedIcon(caja);
        setDisabledIcon(caja);
        setDisabledSelectedIcon(caja);
    }

    public void setColorFondo(Color color) {
        colorFondo = color;
        repaint();
    }

    public void setColorBorde(Color color) {
        colorBorde = color;
        repaint();
    }

    public void setColorMarcado(Color color) {
        colorMarcado = color;
        repaint();
    }
}
