package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PanelImagen extends JPanel {

    private Color fondo = new Color(55, 25, 75);
    private Color borde = new Color(90, 75, 100);

    private Image imagen;
    private File archivoImagen;

    private boolean hover = false;

    public PanelImagen() {

        setOpaque(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setPreferredSize(new Dimension(250, 200));

        configurarEventos();
    }

    private void configurarEventos() {

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
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {

                    seleccionarImagen();
                }
            }
        });
    }

    private void seleccionarImagen() {

        JFileChooser selector = new JFileChooser();

        FileNameExtensionFilter filtro =
                new FileNameExtensionFilter(
                        "Imágenes (*.jpg, *.jpeg, *.png)",
                        "jpg",
                        "jpeg",
                        "png"
                );

        selector.setFileFilter(filtro);

        int resultado = selector.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            archivoImagen = selector.getSelectedFile();

            try {

                imagen = ImageIO.read(archivoImagen);

                if (imagen == null) {

                    throw new IOException(
                            "El archivo seleccionado no es una imagen válida."
                    );
                }

                repaint();

            } catch (IOException ex) {

                imagen = null;
                archivoImagen = null;

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo cargar la imagen.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        if (hover) {

            g2.setColor(new Color(65, 30, 85));

        } else {

            g2.setColor(fondo);
        }

        g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                25,
                25
        );

        if (imagen != null) {

            int anchoPanel = getWidth();
            int altoPanel = getHeight();

            int anchoImagen = imagen.getWidth(null);
            int altoImagen = imagen.getHeight(null);

            if (anchoImagen > 0 && altoImagen > 0) {

                double escala = Math.min(
                        (double) anchoPanel / anchoImagen,
                        (double) altoPanel / altoImagen
                );

                int nuevoAncho =
                        (int) (anchoImagen * escala);

                int nuevoAlto =
                        (int) (altoImagen * escala);

                int x =
                        (anchoPanel - nuevoAncho) / 2;

                int y =
                        (altoPanel - nuevoAlto) / 2;

                g2.drawImage(
                        imagen,
                        x,
                        y,
                        nuevoAncho,
                        nuevoAlto,
                        null
                );
            }

        } else {

            g2.setColor(Color.WHITE);

            g2.setFont(
                    new java.awt.Font(
                            "Segoe UI",
                            java.awt.Font.PLAIN,
                            16
                    )
            );

            String texto = "Agregar imagen";

            int anchoTexto =
                    g2.getFontMetrics().stringWidth(texto);

            int x =
                    (getWidth() - anchoTexto) / 2;

            int y =
                    (getHeight() / 2) + 6;

            g2.drawString(
                    texto,
                    x,
                    y
            );
        }

        g2.setColor(borde);

        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                25,
                25
        );

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public void paintBorder(Graphics g) {

    }

    public File getArchivoImagen() {

        return archivoImagen;
    }

    public Image getImagen() {

        return imagen;
    }

    public void setColorFondo(Color color) {

        fondo = color;
        repaint();
    }

    public void setColorBorde(Color color) {

        borde = color;
        repaint();
    }

    public void limpiarImagen() {

        imagen = null;
        archivoImagen = null;

        repaint();
    }

    public void cargarImagen(File archivo) {

        if (archivo == null || !archivo.exists()) {
            return;
        }

        try {

            Image cargada = ImageIO.read(archivo);

            if (cargada != null) {
                imagen = cargada;
                archivoImagen = archivo;
                repaint();
            }

        } catch (IOException ex) {

        }
    }
}