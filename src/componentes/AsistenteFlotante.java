package componentes;

import javax.swing.*;
import java.awt.*;

public class AsistenteFlotante extends JPanel {

    private final JLabel circulo;
    private final PanelRedondo panelChat;
    private final JTextArea areaConversacion;
    private final JTextField campoTexto;
    private final JButton botonEnviar;
    private final JButton botonMicrofono;
    private final JLabel etiquetaEstado;
    private boolean abierto = false;
    public static boolean vozActiva = true;
    private boolean grabando = false;
    private javax.swing.JPanel panelContenido;
    private final java.util.List<String[]> historialConversacion = new java.util.ArrayList<>();
    private static final java.util.Map<String, String> MAPA_PANELES = new java.util.HashMap<>();

    static {
        MAPA_PANELES.put("REPORTES", "Reporte");
        MAPA_PANELES.put("PRODUCTOS", "Productos");
        MAPA_PANELES.put("CLIENTES", "Clientes");
        MAPA_PANELES.put("EMPLEADOS", "Empleados");
        MAPA_PANELES.put("PROVEEDORES", "Proveedores");
        MAPA_PANELES.put("FACTURACION", "PanelFacturacion");
        MAPA_PANELES.put("HISTORIAL_FACTURAS", "historialFacturas");
        MAPA_PANELES.put("COMPRAS", "PanelCompras");
        MAPA_PANELES.put("PAGARES", "PanelPagares");
        MAPA_PANELES.put("EGRESOS", "PanelHistorialEgresos");
        MAPA_PANELES.put("CONFIGURACION", "Configuracion");
    }

    public void setPanelContenido(javax.swing.JPanel panelContenido) {
        this.panelContenido = panelContenido;
    }

    public AsistenteFlotante() {
        setOpaque(false);
        setLayout(null);

        circulo = new JLabel("IA", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradiente = new GradientPaint(0, 0, new Color(255, 60, 90), getWidth(), getHeight(), new Color(150, 30, 90));
                g2.setPaint(gradiente);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        circulo.setForeground(Color.WHITE);
        circulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        circulo.setSize(60, 60);
        circulo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        circulo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                alternarChat();
            }
        });
        add(circulo);

        panelChat = new PanelRedondo();
        panelChat.setLayout(new BorderLayout());
        panelChat.setBackground(new Color(40, 15, 55));
        panelChat.setVisible(false);
        panelChat.setSize(430, 580);
        add(panelChat);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 10));
        JLabel titulo = new JLabel("Asistente IA");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton cerrar = new JButton("X");
        cerrar.setForeground(Color.WHITE);
        cerrar.setBackground(new Color(80, 20, 40));
        cerrar.setBorderPainted(false);
        cerrar.setFocusPainted(false);
        cerrar.addActionListener(e -> alternarChat());
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(cerrar, BorderLayout.EAST);
        panelChat.add(encabezado, BorderLayout.NORTH);

        areaConversacion = new JTextArea();
        areaConversacion.setEditable(false);
        areaConversacion.setLineWrap(true);
        areaConversacion.setWrapStyleWord(true);
        areaConversacion.setBackground(new Color(30, 10, 45));
        areaConversacion.setForeground(Color.WHITE);
        areaConversacion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        areaConversacion.setMargin(new Insets(8, 10, 8, 10));
        areaConversacion.setText("Asistente: Hola, soy tu asistente virtual del sistema KTP. ¿En que puedo ayudarte?\n\n");
        JScrollPane scroll = new JScrollPane(areaConversacion);
        scroll.setBorder(null);
        panelChat.add(scroll, BorderLayout.CENTER);

        etiquetaEstado = new JLabel(" ");
        etiquetaEstado.setForeground(Color.LIGHT_GRAY);
        etiquetaEstado.setFont(new Font("SansSerif", Font.ITALIC, 11));
        etiquetaEstado.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        campoTexto = new JTextField();
        botonEnviar = new JButton("Enviar");
        botonMicrofono = new JButton("Mic");
        botonMicrofono.setPreferredSize(new Dimension(50, 28));

        JPanel panelEntrada = new JPanel(new BorderLayout(5, 0));
        panelEntrada.setOpaque(false);
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(4, 10, 10, 10));
        panelEntrada.add(botonMicrofono, BorderLayout.WEST);
        panelEntrada.add(campoTexto, BorderLayout.CENTER);
        panelEntrada.add(botonEnviar, BorderLayout.EAST);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(etiquetaEstado, BorderLayout.NORTH);
        panelInferior.add(panelEntrada, BorderLayout.SOUTH);
        panelChat.add(panelInferior, BorderLayout.SOUTH);

        botonEnviar.addActionListener(this::enviarMensaje);
        campoTexto.addActionListener(this::enviarMensaje);
        botonMicrofono.addActionListener(this::escucharVoz);
    }

    @Override
    public boolean contains(int x, int y) {
        if (circulo.isVisible() && circulo.getBounds().contains(x, y)) {
            return true;
        }
        return panelChat.isVisible() && panelChat.getBounds().contains(x, y);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int margen = 30;
        circulo.setLocation(getWidth() - circulo.getWidth() - margen, getHeight() - circulo.getHeight() - margen);
        panelChat.setLocation(getWidth() - panelChat.getWidth() - margen,
                getHeight() - panelChat.getHeight() - circulo.getHeight() - margen - 15);
    }

    private void alternarChat() {
        abierto = !abierto;
        panelChat.setVisible(abierto);
        if (!abierto) {
            Vista.IA.VozAsistente.detener();
        }
    }

    private void enviarMensaje(java.awt.event.ActionEvent e) {
        String pregunta = campoTexto.getText().trim();
        if (pregunta.isEmpty()) {
            return;
        }
        campoTexto.setText("");
        procesarPregunta(pregunta);

    }

    private void escucharVoz(java.awt.event.ActionEvent e) {
        if (!grabando) {
            Vista.IA.VozAsistente.detener();
            grabando = true;
            botonMicrofono.setText("Stop");
            etiquetaEstado.setText("Grabando...");
            try {
                Vista.IA.AudioRecorder.iniciarGrabacion();
            } catch (Exception ex) {
                grabando = false;
                botonMicrofono.setText("Mic");
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        } else {
            grabando = false;
            botonMicrofono.setText("Mic");
            botonEnviar.setEnabled(false);
            botonMicrofono.setEnabled(false);
            etiquetaEstado.setText("Procesando audio...");

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    String archivo = Vista.IA.AudioRecorder.detenerGrabacion();
                    return Vista.IA.WhisperGroq.transcribir(archivo);
                }

                @Override
                protected void done() {
                    try {
                        String texto = get();
                        if (texto != null && !texto.isBlank()) {
                            procesarPregunta(texto);
                        } else {
                            etiquetaEstado.setText("No se entendio el audio.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    botonEnviar.setEnabled(true);
                    botonMicrofono.setEnabled(true);
                }
            }.execute();
        }
    }

    private void procesarPregunta(String pregunta) {
        areaConversacion.append("Tu: " + pregunta + "\n");
        scrollAbajo();
        botonEnviar.setEnabled(false);
        botonMicrofono.setEnabled(false);
        etiquetaEstado.setText("Pensando...");

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String idNegocio = Modelo.Sesion.getIdNegocio();
                String contexto = Vista.IA.ContextoNegocio.generar(idNegocio);
                String respuesta = Vista.IA.AsistenteIA.preguntar(contexto, historialConversacion, pregunta);
                return respuesta;
            }

            @Override
            protected void done() {
                try {
                    String respuesta = get();
                    String textoFinal = respuesta;
                    historialConversacion.add(new String[]{"user", pregunta});
                    historialConversacion.add(new String[]{"assistant", textoFinal});

                    java.util.regex.Matcher m = java.util.regex.Pattern
                            .compile("\\[IR_A:([A-Z_]+)\\]").matcher(respuesta);
                    if (m.find()) {
                        textoFinal = respuesta.substring(0, m.start()).trim();
                        String destino = MAPA_PANELES.get(m.group(1));
                        if (destino != null && panelContenido != null) {
                            CardLayout cl = (CardLayout) panelContenido.getLayout();
                            cl.show(panelContenido, destino);
                        }
                    }

                    areaConversacion.append("Asistente: " + textoFinal + "\n\n");
                    scrollAbajo();
                    etiquetaEstado.setText(" ");
                    if (vozActiva) {
                        etiquetaEstado.setText("Hablando...");
                        Vista.IA.VozAsistente.hablar(textoFinal);
                        new Timer(1000, ev -> {
                            etiquetaEstado.setText(" ");
                            ((Timer) ev.getSource()).stop();
                        }).start();
                    }
                } catch (Exception ex) {
                    areaConversacion.append("Asistente: Error: " + ex.getMessage() + "\n\n");
                    etiquetaEstado.setText(" ");
                }
                botonEnviar.setEnabled(true);
                botonMicrofono.setEnabled(true);
            }
        }.execute();
    }

    private void scrollAbajo() {
        SwingUtilities.invokeLater(() -> areaConversacion.setCaretPosition(areaConversacion.getDocument().getLength()));
    }
}
