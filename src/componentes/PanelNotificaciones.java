package componentes;

import Controladores.ControladorNotificacion;
import Modelo.Notificacion;
import Modelo.Sesion;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class PanelNotificaciones extends JPanel {

    private final JLabel campana;
    private final JLabel badge;
    private final PanelRedondo panelLista;
    private final JPanel contenedorItems;
    private final ControladorNotificacion controlador = new ControladorNotificacion();
    private final javax.swing.Timer timerAutoRefresco;
    private boolean abierto = false;
    private int centroYTopbar = -1;

    public PanelNotificaciones() {
        setOpaque(false);
        setLayout(null);

        campana = new JLabel("\uD83D\uDD14", SwingConstants.CENTER);
        campana.setFont(new Font("SansSerif", Font.PLAIN, 22));
        campana.setForeground(Color.WHITE);
        campana.setSize(42, 42);
        campana.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        campana.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                alternarLista();
            }
        });
        add(campana);

        badge = new JLabel("0", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 40, 40));
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setSize(16, 16);
        badge.setVisible(false);
        add(badge);

        panelLista = new PanelRedondo();
        panelLista.setLayout(new BorderLayout());
        panelLista.setBackground(new Color(40, 15, 55));
        panelLista.setVisible(false);
        panelLista.setSize(340, 380);
        add(panelLista);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 10));
        JLabel titulo = new JLabel("Notificaciones");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton marcarTodas = new JButton("Marcar todas");
        marcarTodas.setForeground(Color.WHITE);
        marcarTodas.setBackground(new Color(80, 40, 110));
        marcarTodas.setBorderPainted(false);
        marcarTodas.setFocusPainted(false);
        marcarTodas.setFont(new Font("SansSerif", Font.PLAIN, 10));
        marcarTodas.addActionListener(e -> {
            controlador.marcarTodasComoLeidas(Sesion.getIdUsuario());
            cargarNotificaciones();
        });
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(marcarTodas, BorderLayout.EAST);
        panelLista.add(encabezado, BorderLayout.NORTH);

        contenedorItems = new JPanel();
        contenedorItems.setOpaque(false);
        contenedorItems.setLayout(new BoxLayout(contenedorItems, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(contenedorItems);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        panelLista.add(scroll, BorderLayout.CENTER);

        revisarEnSegundoPlano();
        timerAutoRefresco = new javax.swing.Timer(60000, evt -> revisarEnSegundoPlano());
        timerAutoRefresco.start();
    }

    public void detener() {
        timerAutoRefresco.stop();
    }

    @Override
    public boolean contains(int x, int y) {
        if (campana.isVisible() && campana.getBounds().contains(x, y)) {
            return true;
        }
        if (badge.isVisible() && badge.getBounds().contains(x, y)) {
            return true;
        }
        return panelLista.isVisible() && panelLista.getBounds().contains(x, y);
    }

    public void centrarEnTopbar(int yTopbar, int alturaTopbar) {
        centroYTopbar = yTopbar + alturaTopbar / 2;
        revalidate();
        repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int margen = 20;
        int y = centroYTopbar >= 0 ? centroYTopbar - campana.getHeight() / 2 : margen;
        campana.setLocation(getWidth() - campana.getWidth() - margen, y);
        badge.setLocation(campana.getX() + campana.getWidth() - 12, campana.getY() - 4);
        panelLista.setLocation(getWidth() - panelLista.getWidth() - margen, campana.getY() + campana.getHeight() + 8);
    }

    private void alternarLista() {
    abierto = !abierto;
    panelLista.setVisible(abierto);
    if (abierto) {
        revisarEnSegundoPlano();
    }
}

    private void revisarEnSegundoPlano() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                controlador.revisarYGenerarNotificaciones();
                return null;
            }

            @Override
            protected void done() {
                actualizarContador();
                if (abierto) {
                    cargarNotificaciones();
                }
            }
        }.execute();
    }

    private void actualizarContador() {
        String idUsuario = Sesion.getIdUsuario();
        if (idUsuario == null) return;
        new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() {
                return controlador.contarNoLeidas(idUsuario);
            }

            @Override
            protected void done() {
                try {
                    int total = get();
                    badge.setVisible(total > 0);
                    badge.setText(total > 9 ? "9+" : String.valueOf(total));
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void cargarNotificaciones() {
        String idUsuario = Sesion.getIdUsuario();
        if (idUsuario == null) return;
        new SwingWorker<List<Notificacion>, Void>() {
            @Override
            protected List<Notificacion> doInBackground() {
                return controlador.listarTodas(idUsuario);
            }

            @Override
            protected void done() {
                try {
                    pintarLista(get());
                } catch (Exception ignored) {
                }
                actualizarContador();
            }
        }.execute();
    }

    private void pintarLista(List<Notificacion> lista) {
        contenedorItems.removeAll();
        if (lista.isEmpty()) {
            JLabel vacio = new JLabel("Sin notificaciones por ahora.");
            vacio.setForeground(Color.LIGHT_GRAY);
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 12));
            vacio.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            contenedorItems.add(vacio);
        } else {
            for (Notificacion n : lista) {
                contenedorItems.add(crearItem(n));
            }
        }
        contenedorItems.revalidate();
        contenedorItems.repaint();
    }

    private JPanel crearItem(Notificacion n) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(true);
        item.setBackground(n.isLeido() ? new Color(40, 15, 55) : new Color(60, 25, 80));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 40, 90)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JLabel texto = new JLabel("<html><body style='width:230px'>" + escaparHtml(n.getMensaje()) + "</body></html>");
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("SansSerif", n.isLeido() ? Font.PLAIN : Font.BOLD, 12));
        item.add(texto, BorderLayout.CENTER);

        JLabel fecha = new JLabel(formatearFecha(n.getFechaGeneracion()));
        fecha.setForeground(Color.LIGHT_GRAY);
        fecha.setFont(new Font("SansSerif", Font.PLAIN, 10));
        item.add(fecha, BorderLayout.SOUTH);

        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!n.isLeido()) {
                    controlador.marcarComoLeida(n.getIdNotificacion());
                    cargarNotificaciones();
                }
            }
        });
        return item;
    }

    private String formatearFecha(String fechaGeneracion) {
        if (fechaGeneracion == null || fechaGeneracion.isBlank()) return "";
        return fechaGeneracion.length() >= 16 ? fechaGeneracion.substring(0, 16) : fechaGeneracion;
    }

    private String escaparHtml(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}