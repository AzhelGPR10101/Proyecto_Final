package Vista.PRINCIPAL;

import java.awt.CardLayout;

public class MenuPrincipal extends javax.swing.JFrame {

    public static String usuarioActivo;
    private final Vista.PanelNotas panelNotasInstancia = new Vista.PanelNotas();
    private final Vista.RolCajero.PanelAperturaCaja panelAperturaCajaInstancia = new Vista.RolCajero.PanelAperturaCaja();
    private final Vista.RolCajero.PanelCierreCaja panelCierreCajaInstancia = new Vista.RolCajero.PanelCierreCaja();
    private javax.swing.JDialog dialogoNotas;
    private java.util.List<Modelo.Nota> notasActuales = new java.util.ArrayList<>();
    private final Vista.REPORTES.PanelEstadistica panelEstadisticaInstancia = new Vista.REPORTES.PanelEstadistica();
    private final Controladores.ControladorEstadistica controladorEstadisticaInicio = new Controladores.ControladorEstadistica();
    private Controladores.ControladorEstadistica.TipoPeriodo periodoActualInicio = Controladores.ControladorEstadistica.TipoPeriodo.HOY;
    private final java.util.Map<String, java.util.function.Supplier<javax.swing.JComponent>> fabricasPanel = new java.util.HashMap<>();
    private final java.util.Set<String> panelesCargados = new java.util.HashSet<>();

    public MenuPrincipal() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        new Thread(() -> new Controladores.ControladorNotificacion().revisarYGenerarNotificaciones()).start();
        setLocationRelativeTo(null);
        estilizarActividadReciente();
        estilizarMenusDesplegables();

        jlbUsuarioActivo.setText(usuarioActivo);
        Panelcontenido.add(panelEstadisticaInstancia, "Estadisticas");
        registrarPanelDiferido("historialFacturas", Vista.FACTURACION.PanelHistorialFacturas::new);
        registrarPanelDiferido("PanelFacturacion", Vista.FACTURACION.PanelFacturacion::new);
        registrarPanelDiferido("Reporte", Vista.REPORTES.PanelReporte::new);
        registrarPanelDiferido("Clientes", Vista.CLIENTES.PanelClientes::new);
        registrarPanelDiferido("Empleados", Vista.EMPLEADOS.PanelEmpleados::new);
        registrarPanelDiferido("Productos", Vista.PRODUCTOS.PanelProductos::new);
        registrarPanelDiferido("Proveedores", Vista.PROVEEDORES.PanelProveedores::new);
        registrarPanelDiferido("Configuracion", Vista.CONFIGURACION.PanelConfiguracion::new);
        registrarPanelDiferido("PanelCompras", Vista.COMPRAS.PanelCompras::new);
        registrarPanelDiferido("PanelPagares", Vista.COMPRAS.PanelPagares::new);
        registrarPanelDiferido("PanelHistorialEgresos", Vista.COMPRAS.PanelHistorialEgresos::new);
        registrarPanelDiferido("DashboardBodeguero", Vista.ROLBODEGUERO.PanelRolBodeguero::new);
        registrarPanelDiferido("HistorialCierreCaja", Vista.RolCajero.PanelHistorialCierreCaja::new);
        registrarPanelDiferido("Cajero", Vista.RolCajero.PanelCajero::new);
        registrarPanelDiferido("DashboardRH", Vista.RECURSOSHUMANOS.PanelDashboardRH::new);
        panelNotasInstancia.setAlGuardarOEliminar(() -> {
            cargarListaNotas();
            if (dialogoNotas != null) {
                dialogoNotas.setVisible(false);
            }
        });

        habilitarScrollDelContenido();

        int anchoDiseno = getContentPane().getWidth();
        int altoDiseno = getContentPane().getHeight();
        componentes.escalado.KryptonAutoEscalador.activar(getContentPane(), anchoDiseno, altoDiseno);
        activarClickEnNombreDeApartado();
        activarNavegacionCaja();
        aplicarVisibilidadPorModulosYRol();
        activarNotas();

        componentes.AsistenteFlotante asistenteFlotante = new componentes.AsistenteFlotante();
        asistenteFlotante.setPanelContenido(Panelcontenido);
        javax.swing.JLayeredPane capas = getLayeredPane();
        capas.add(asistenteFlotante, javax.swing.JLayeredPane.PALETTE_LAYER);
        asistenteFlotante.setBounds(0, 0, capas.getWidth(), capas.getHeight());
        componentes.PanelNotificaciones panelNotificaciones = new componentes.PanelNotificaciones();
        capas.add(panelNotificaciones, javax.swing.JLayeredPane.PALETTE_LAYER);
        panelNotificaciones.setBounds(0, 0, capas.getWidth(), capas.getHeight());
        centrarCampanaEnTopbar(panelNotificaciones, capas);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                asistenteFlotante.setBounds(0, 0, capas.getWidth(), capas.getHeight());
                panelNotificaciones.setBounds(0, 0, capas.getWidth(), capas.getHeight());
                centrarCampanaEnTopbar(panelNotificaciones, capas);
            }
        });
        activarDashboardPrincipal();
        mostrarPantallaInicialSegunRol();
    }

    private void centrarCampanaEnTopbar(componentes.PanelNotificaciones panelNotificaciones, javax.swing.JLayeredPane capas) {
        java.awt.Point origenTopbar = javax.swing.SwingUtilities.convertPoint(jMenuBar1, 0, 0, capas);
        panelNotificaciones.centrarEnTopbar(origenTopbar.y, jMenuBar1.getHeight());
    }

    private void mostrarPantallaInicialSegunRol() {
        String rol = Modelo.Sesion.getRolUsuario();

        if ("Bodeguero".equalsIgnoreCase(rol)) {
            mostrarPanel("DashboardBodeguero");
        } else if ("Recursos Humanos".equalsIgnoreCase(rol)
                || "RRHH".equalsIgnoreCase(rol)
                || "Talento Humano".equalsIgnoreCase(rol)) {
            mostrarPanel("DashboardRH");
        } else if ("Vendedor".equalsIgnoreCase(rol) || "Cajero".equalsIgnoreCase(rol)) {
            mostrarPanel("PanelFacturacion");
        } else {
            cargarDashboardPrincipal();
            mostrarPanel("card2");
        }

        for (java.awt.Component tarjeta : Panelcontenido.getComponents()) {
            if (tarjeta.isVisible()) {
                ajustarAlturaScrollA(tarjeta);
                break;
            }
        }
    }

    private void activarDashboardPrincipal() {
        btnSemana.addActionListener(e -> seleccionarPeriodoInicio(Controladores.ControladorEstadistica.TipoPeriodo.SEMANA));
        BtnMes.addActionListener(e -> seleccionarPeriodoInicio(Controladores.ControladorEstadistica.TipoPeriodo.MES));
        BtnTrimestre.addActionListener(e -> seleccionarPeriodoInicio(Controladores.ControladorEstadistica.TipoPeriodo.TRIMESTRE));
        cargarDashboardPrincipal();

    }

    private void cargarActividadReciente() {
        java.util.List<Modelo.ActividadReciente> actividades
                = controladorEstadisticaInicio.listarActividadReciente(8);
        StringBuilder texto = new StringBuilder();

        if (actividades.isEmpty()) {
            texto.append("No hay actividades recientes registradas.");
        } else {
            java.time.format.DateTimeFormatter formato
                    = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Modelo.ActividadReciente actividad : actividades) {
                texto.append("• ")
                        .append(actividad.getDescripcion())
                        .append("  |  ")
                        .append(String.format("$%.2f", actividad.getMonto()))
                        .append("  |  ")
                        .append(actividad.getFecha().format(formato))
                        .append(System.lineSeparator());
            }
        }

        txtActividadReciente.setEnabled(true);
        txtActividadReciente.setEditable(false);
        txtActividadReciente.setText(texto.toString());
    }

    private void seleccionarPeriodoInicio(Controladores.ControladorEstadistica.TipoPeriodo tipo) {
        periodoActualInicio = tipo;
        cargarDashboardPrincipal();
    }

    private void cargarDashboardPrincipal() {
        Controladores.ControladorEstadistica.TipoPeriodo periodo = periodoActualInicio;
        new javax.swing.SwingWorker<Void, Void>() {
            Controladores.ControladorEstadistica.ResultadoEstadistica resultado;
            java.util.List<Modelo.Producto> stockBajo = new java.util.ArrayList<>();
            java.util.List<Modelo.ProductoVendido> masVendidos = new java.util.ArrayList<>();

            @Override
            protected Void doInBackground() {
                try {
                    resultado = controladorEstadisticaInicio.obtenerEstadisticas(periodo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    stockBajo = controladorEstadisticaInicio.listarStockBajo(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    masVendidos = controladorEstadisticaInicio.listarMasVendidos(periodo, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                if (resultado != null) {
                    lblganancias.setText(String.format("$%.2f", resultado.getTotalGanancias()));
                    lblgastos.setText(String.format("$%.2f", resultado.getTotalGastos()));

                    java.util.List<Modelo.EstadisticaPeriodo> filas = resultado.getFilas();
                    String[] periodos = new String[filas.size()];
                    double[] ganancias = new double[filas.size()];
                    double[] gastos = new double[filas.size()];
                    for (int i = 0; i < filas.size(); i++) {
                        Modelo.EstadisticaPeriodo f = filas.get(i);
                        periodos[i] = f.getPeriodo();
                        ganancias[i] = f.getGanancias();
                        gastos[i] = f.getGastos();
                    }
                    graficoEstadisticasInicio.setDatos(periodos, ganancias, gastos);
                    graficoEstadisticasInicio.mostrarSegunTipo(panelEstadisticaInstancia.getTipoGraficoActual());
                }

                lblEstockagotarseprimero.setText(stockBajo.size() > 0
                        ? stockBajo.get(0).getNombre() + " - " + stockBajo.get(0).getCantidad() + " uds" : "Sin datos");
                lblEstockagotarsesegundo.setText(stockBajo.size() > 1
                        ? stockBajo.get(1).getNombre() + " - " + stockBajo.get(1).getCantidad() + " uds" : "");

                lblMasvendidosPrimero.setText(masVendidos.size() > 0
                        ? masVendidos.get(0).getNombre() + "    " + masVendidos.get(0).getCantidadVendida() + "Vend." : "Sin datos");
                lblMasvendidosSegundo.setText(masVendidos.size() > 1
                        ? masVendidos.get(1).getNombre() + "    " + masVendidos.get(1).getCantidadVendida() + "Vend." : "");

                try {
                    cargarActividadReciente();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void estilizarActividadReciente() {
        java.awt.Color fondo = new java.awt.Color(28, 9, 40);
        txtActividadReciente.setBackground(fondo);
        txtActividadReciente.setForeground(java.awt.Color.WHITE);
        txtActividadReciente.setCaretColor(java.awt.Color.WHITE);
        txtActividadReciente.setFont(new java.awt.Font("Lucida Bright", java.awt.Font.PLAIN, 14));
        txtActividadReciente.setLineWrap(true);
        txtActividadReciente.setWrapStyleWord(true);
        jScrollPane2.setBorder(null);
        jScrollPane2.getViewport().setBackground(fondo);
    }

    private void estilizarMenusDesplegables() {
        javax.swing.JMenuItem[] items = {
            MenuFacturas, MenuVentas, Compras, HistorialEgresos, Pagares, jMenuItem1, HistorialCierreCaja
        };
        java.awt.Color fondo = new java.awt.Color(70, 20, 90);
        java.awt.Color fondoHover = new java.awt.Color(90, 45, 120);
        java.awt.Font fuente = new java.awt.Font("Lucida Bright", java.awt.Font.PLAIN, 15);
        javax.swing.UIManager.put("MenuItem.selectionBackground", fondoHover);
        javax.swing.UIManager.put("MenuItem.selectionForeground", java.awt.Color.WHITE);
        for (javax.swing.JMenuItem item : items) {
            item.setFont(fuente);
            item.setForeground(java.awt.Color.WHITE);
            item.setBackground(fondo);
            item.setOpaque(true);
            item.setBorderPainted(false);
            item.setPreferredSize(new java.awt.Dimension(260, 42));
            item.setIconTextGap(0);
        }
    }

    private void cargarListaNotas() {
        notasActuales = new Controladores.ControladorNota().listar();
        javax.swing.DefaultListModel<String> modelo = new javax.swing.DefaultListModel<>();
        for (Modelo.Nota n : notasActuales) {
            modelo.addElement(n.getTitulo());
        }
        JlstNotas.setModel(modelo);
    }

    private void abrirDialogoNotas() {
        if (dialogoNotas == null) {
            dialogoNotas = new javax.swing.JDialog(this, "Nota", true);
            dialogoNotas.getContentPane().add(panelNotasInstancia);
            dialogoNotas.setSize(430, 720);
            dialogoNotas.setResizable(false);
        }
        dialogoNotas.setLocationRelativeTo(this);
        dialogoNotas.setVisible(true);
    }

    private void activarNotas() {
        cargarListaNotas();

        JlstNotas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int indice = JlstNotas.getSelectedIndex();
                    if (indice >= 0 && indice < notasActuales.size()) {
                        panelNotasInstancia.cargarNota(notasActuales.get(indice));
                        abrirDialogoNotas();
                    }
                }
            }
        });
    }

    private void activarClickEnNombreDeApartado() {
        PRODUCTOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarPanel("Productos");
            }
        });
        CLIENTES.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarPanel("Clientes");
            }
        });
        EMPLEADOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarPanel("Empleados");
            }
        });
        PROVEEDORES.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarPanel("Proveedores");
            }
        });
        VENTASYFACTURAS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarPanel("PanelFacturacion");
            }
        });
        BODEGA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarPanel("DashboardBodeguero");
            }
        });

        RH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarPanel("DashboardRH");
            }
        });
    }

    private void activarNavegacionCaja() {
        panelAperturaCajaInstancia.setAlAbrirCaja(() -> {
            mostrarPanel("CierreCaja");
            panelCierreCajaInstancia.cargarDatosDeHoy();
        });
        panelAperturaCajaInstancia.setAlCancelar(() -> {
            mostrarPanel("card2");
        });
        panelCierreCajaInstancia.setAlCerrar(() -> {
            mostrarPanel("card2");
        });
        panelCierreCajaInstancia.setAlCancelar(() -> {
            mostrarPanel("card2");
        });
    }

    private void abrirPanelDeCaja() {
        Controladores.ControladorCierreCaja.EstadoTurno estado
                = new Controladores.ControladorCierreCaja().obtenerEstadoTurno();
        if (estado == Controladores.ControladorCierreCaja.EstadoTurno.SIN_ABRIR) {
            mostrarPanel("AperturaCaja");
            panelAperturaCajaInstancia.cargarDatos();
        } else {
            mostrarPanel("CierreCaja");
            panelCierreCajaInstancia.cargarDatosDeHoy();
        }
    }

    private void habilitarScrollDelContenido() {
        java.awt.Dimension preferido = Panelcontenido.getPreferredSize();
        int pixelesPorCm = (int) Math.round(java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / 2.54);
        Panelcontenido.setPreferredSize(new java.awt.Dimension(
                preferido.width, preferido.height + pixelesPorCm));

        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(Panelcontenido);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        java.awt.Container contenedor = getContentPane();
        contenedor.remove(Panelcontenido);
        contenedor.setLayout(new java.awt.BorderLayout());
        contenedor.add(scroll, java.awt.BorderLayout.CENTER);

        activarScrollDinamicoPorPagina();
    }

    /**
     * El JScrollPane de arriba se arma una sola vez, con el tamaño de la
     * pantalla mas grande de todas (asi funciona CardLayout.getPreferredSize).
     * Esto hace que en pantallas chicas el scroll se pase de largo. En vez
     * de tocar los ~30 lugares que hacen cl.show(Panelcontenido, "..."),
     * se le pone un listener a cada tarjeta ya agregada: cada vez que una
     * se hace visible, el area de scroll se ajusta a SU tamaño real.
     */
    private void activarScrollDinamicoPorPagina() {
        for (java.awt.Component tarjeta : Panelcontenido.getComponents()) {
            tarjeta.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentShown(java.awt.event.ComponentEvent e) {
                    ajustarAlturaScrollA(tarjeta);
                }
            });
        }
    }

    private void registrarPanelDiferido(String tag, java.util.function.Supplier<javax.swing.JComponent> fabrica) {
        fabricasPanel.put(tag, fabrica);
    }

    private void mostrarPanel(String tag) {
        if (!panelesCargados.contains(tag) && fabricasPanel.containsKey(tag)) {
            javax.swing.JComponent panel = fabricasPanel.get(tag).get();
            Panelcontenido.add(panel, tag);
            panel.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentShown(java.awt.event.ComponentEvent e) {
                    ajustarAlturaScrollA(panel);
                }
            });
            panelesCargados.add(tag);
        }
        CardLayout cl = (CardLayout) Panelcontenido.getLayout();
        cl.show(Panelcontenido, tag);
    }

    private void ajustarAlturaScrollA(java.awt.Component tarjeta) {
        int anchoActual = Panelcontenido.getWidth() > 0 ? Panelcontenido.getWidth() : Panelcontenido.getPreferredSize().width;
        int alturaTarjeta = Math.max(tarjeta.getPreferredSize().height, tarjeta.getHeight());
        int pixelesPorCm = (int) Math.round(java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / 2.54);
        Panelcontenido.setPreferredSize(new java.awt.Dimension(anchoActual, alturaTarjeta + pixelesPorCm));
        Panelcontenido.revalidate();
    }

    private void aplicarVisibilidadPorModulosYRol() {
        String idNegocio = Modelo.Sesion.getIdNegocio();
        if (idNegocio == null) {
            return;
        }

        Controladores.ControladorModulo controladorModulo = new Controladores.ControladorModulo();
        java.util.List<String> activos = controladorModulo.obtenerNombresModulosActivos(idNegocio);

        PRODUCTOS.setVisible(activos.contains("Catálogo"));
        PROVEEDORES.setVisible(activos.contains("Catálogo"));
        BODEGA.setVisible(activos.contains("Catálogo"));
        VENTASYFACTURAS.setVisible(activos.contains("Ventas y Facturación"));
        CAJA.setVisible(activos.contains("Ventas y Facturación") && !Modelo.Sesion.esDueno());
        CLIENTES.setVisible(activos.contains("Ventas y Facturación"));
        REPORTES.setVisible(activos.contains("Finanzas"));
        EMPLEADOS.setVisible(activos.contains("Recursos Humanos"));
        RH.setVisible(activos.contains("Recursos Humanos"));

        boolean puedeVerConfiguracion = Modelo.Sesion.esDueno() && activos.contains("Configuración");
        MASOPCIONES.setVisible(puedeVerConfiguracion);

        if (!Modelo.Sesion.esDueno()) {
            aplicarRestriccionesPorRol();
        }
    }

    private void aplicarRestriccionesPorRol() {
        String idRol = Modelo.Sesion.getIdRolUsuario();
        if (idRol == null) {
            return;
        }

        boolean esBodeguero = "Bodeguero".equalsIgnoreCase(Modelo.Sesion.getRolUsuario());
        PRODUCTOS.setVisible(PRODUCTOS.isVisible()
                && (Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_PRODUCTOS.name()) || esBodeguero));
        CLIENTES.setVisible(CLIENTES.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_CLIENTES.name()));
        PROVEEDORES.setVisible(PROVEEDORES.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_PROVEEDORES.name()));
        VENTASYFACTURAS.setVisible(VENTASYFACTURAS.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_VENTAS.name()));
        CAJA.setVisible(CAJA.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_VENTAS.name()));
        EMPLEADOS.setVisible(EMPLEADOS.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_EMPLEADOS.name()));
        RH.setVisible(RH.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_NOMINA.name()));
        REPORTES.setVisible(REPORTES.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_REPORTES.name()));
        BODEGA.setVisible(BODEGA.isVisible() && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_BODEGA.name()));
        boolean esCajero = "Cajero".equalsIgnoreCase(Modelo.Sesion.getRolUsuario())
                || "Vendedor".equalsIgnoreCase(Modelo.Sesion.getRolUsuario());
        EgresosProductos.setVisible(EgresosProductos.isVisible()
                && !esCajero
                && Controladores.ControladorPermiso.tienePermiso(idRol, Modelo.PermisoSistema.VER_EGRESOS.name()));
    }

    public void mostrarUsuario(String nombreUsuario) {

        jlbUsuarioActivo.setText(nombreUsuario);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        Panelcontenido = new javax.swing.JPanel();
        JPPANEL = new javax.swing.JPanel();
        PanelGastos = new componentes.PanelRedondo();
        JlbGastos = new javax.swing.JLabel();
        IconGastos = new javax.swing.JLabel();
        lblgastos = new javax.swing.JLabel();
        PanelGanancias = new componentes.PanelRedondo();
        JlbGanancias = new javax.swing.JLabel();
        lblganancias = new javax.swing.JLabel();
        IconGanancias = new javax.swing.JLabel();
        PanelStock = new componentes.PanelRedondo();
        Jlbstockdown = new javax.swing.JLabel();
        IconStock = new javax.swing.JLabel();
        lblEstockagotarseprimero = new javax.swing.JLabel();
        lblEstockagotarsesegundo = new javax.swing.JLabel();
        lblstockagotarse = new javax.swing.JLabel();
        PanelMasVendidos = new componentes.PanelRedondo();
        JlbMasvendidos = new javax.swing.JLabel();
        IconVendidos = new javax.swing.JLabel();
        lblMasvendidosPrimero = new javax.swing.JLabel();
        lblMasvendidosSegundo = new javax.swing.JLabel();
        lblmasvendidos = new javax.swing.JLabel();
        PanelGrafico = new componentes.PanelRedondo();
        JlbEdeventas = new javax.swing.JLabel();
        BtnVerStats = new componentes.BotonModerno();
        graficoEstadisticasInicio = new componentes.GraficoKrypton();
        PanelNotas = new componentes.PanelRedondo();
        jScrollPane1 = new javax.swing.JScrollPane();
        JlstNotas = new javax.swing.JList<>();
        JlbNotas = new javax.swing.JLabel();
        BtnAgregarNota = new componentes.BotonModerno();
        PanelActividad = new componentes.PanelRedondo();
        JlbActividad = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtActividadReciente = new javax.swing.JTextArea();
        Btnhoy = new componentes.BotonModerno();
        btnSemana = new componentes.BotonModerno();
        BtnMes = new componentes.BotonModerno();
        BtnTrimestre = new componentes.BotonModerno();
        JlbTituloPrincipal = new javax.swing.JLabel();
        JlbDescripcion = new javax.swing.JLabel();
        panelRedondo1 = new componentes.PanelRedondo();
        jlbUsuarioActivo = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        INICIO = new javax.swing.JMenu();
        VENTASYFACTURAS = new javax.swing.JMenu();
        MenuFacturas = new javax.swing.JMenuItem();
        MenuVentas = new javax.swing.JMenuItem();
        PRODUCTOS = new javax.swing.JMenu();
        EMPLEADOS = new javax.swing.JMenu();
        BODEGA = new javax.swing.JMenu();
        CAJA = new javax.swing.JMenu();
        RH = new javax.swing.JMenu();
        PROVEEDORES = new javax.swing.JMenu();
        CLIENTES = new javax.swing.JMenu();
        REPORTES = new javax.swing.JMenu();
        EgresosProductos = new javax.swing.JMenu();
        Compras = new javax.swing.JMenuItem();
        HistorialEgresos = new javax.swing.JMenuItem();
        Pagares = new javax.swing.JMenuItem();
        MASOPCIONES = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        HistorialCierreCaja = new javax.swing.JMenuItem();
        CERRARSESION = new javax.swing.JMenu();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Panelcontenido.setBackground(new java.awt.Color(15, 8, 20));
        Panelcontenido.setLayout(new java.awt.CardLayout());

        JPPANEL.setBackground(new java.awt.Color(31, 10, 48));
        JPPANEL.setForeground(new java.awt.Color(0, 0, 0));
        JPPANEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelGastos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JlbGastos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlbGastos.setForeground(new java.awt.Color(255, 255, 255));
        JlbGastos.setText("GASTOS");
        PanelGastos.add(JlbGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 232, -1));

        IconGastos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/grafico-de-barras.png"))); // NOI18N
        PanelGastos.add(IconGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, 38));

        lblgastos.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblgastos.setForeground(new java.awt.Color(255, 255, 255));
        PanelGastos.add(lblgastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 340, 44));

        JPPANEL.add(PanelGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 270, 430, 160));

        PanelGanancias.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JlbGanancias.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlbGanancias.setForeground(new java.awt.Color(255, 255, 255));
        JlbGanancias.setText("GANANCIAS");
        PanelGanancias.add(JlbGanancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 20, -1, -1));

        lblganancias.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblganancias.setForeground(new java.awt.Color(255, 255, 255));
        PanelGanancias.add(lblganancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 400, 50));

        IconGanancias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/bolsa-de-dinero.png"))); // NOI18N
        PanelGanancias.add(IconGanancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, 31));

        JPPANEL.add(PanelGanancias, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, 450, 160));

        PanelStock.setForeground(new java.awt.Color(255, 255, 255));
        PanelStock.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Jlbstockdown.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        Jlbstockdown.setForeground(new java.awt.Color(255, 255, 255));
        Jlbstockdown.setText("STOCK POR AGOTARSE");
        PanelStock.add(Jlbstockdown, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        IconStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/precaucion.png"))); // NOI18N
        PanelStock.add(IconStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, 33));

        lblEstockagotarseprimero.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblEstockagotarseprimero.setForeground(new java.awt.Color(255, 255, 255));
        PanelStock.add(lblEstockagotarseprimero, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 71, -1, 33));

        lblEstockagotarsesegundo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblEstockagotarsesegundo.setForeground(new java.awt.Color(255, 255, 255));
        PanelStock.add(lblEstockagotarsesegundo, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 110, -1, 33));

        lblstockagotarse.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblstockagotarse.setForeground(new java.awt.Color(255, 255, 255));
        PanelStock.add(lblstockagotarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 360, 40));

        JPPANEL.add(PanelStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 270, 420, 160));

        PanelMasVendidos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JlbMasvendidos.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlbMasvendidos.setForeground(new java.awt.Color(255, 255, 255));
        JlbMasvendidos.setText("MAS VENDIDOS");
        PanelMasVendidos.add(JlbMasvendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 186, -1));

        IconVendidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgKrypton/fuego.png"))); // NOI18N
        PanelMasVendidos.add(IconVendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, -1, 40));

        lblMasvendidosPrimero.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblMasvendidosPrimero.setForeground(new java.awt.Color(255, 255, 255));
        PanelMasVendidos.add(lblMasvendidosPrimero, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 101, -1, 33));

        lblMasvendidosSegundo.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        lblMasvendidosSegundo.setForeground(new java.awt.Color(255, 255, 255));
        PanelMasVendidos.add(lblMasvendidosSegundo, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 62, -1, 33));

        lblmasvendidos.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        lblmasvendidos.setForeground(new java.awt.Color(255, 255, 255));
        PanelMasVendidos.add(lblmasvendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 360, 40));

        JPPANEL.add(PanelMasVendidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(1400, 270, 450, 160));

        PanelGrafico.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JlbEdeventas.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlbEdeventas.setForeground(new java.awt.Color(255, 255, 255));
        JlbEdeventas.setText("EVOLUCION DE VENTAS");
        PanelGrafico.add(JlbEdeventas, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 27, 354, -1));

        BtnVerStats.setForeground(new java.awt.Color(255, 0, 51));
        BtnVerStats.setText("Ver Estadisticas --->");
        BtnVerStats.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnVerStatsMouseClicked(evt);
            }
        });
        BtnVerStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnVerStatsActionPerformed(evt);
            }
        });
        PanelGrafico.add(BtnVerStats, new org.netbeans.lib.awtextra.AbsoluteConstraints(843, 25, 192, -1));
        PanelGrafico.add(graficoEstadisticasInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 1010, 380));

        JPPANEL.add(PanelGrafico, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 440, 1077, 480));

        PanelNotas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JlstNotas.setBackground(new java.awt.Color(26, 16, 36));
        JlstNotas.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlstNotas.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(JlstNotas);

        PanelNotas.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 669, 82));

        JlbNotas.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlbNotas.setForeground(new java.awt.Color(255, 255, 255));
        JlbNotas.setText("NOTAS");
        PanelNotas.add(JlbNotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 14, 237, -1));

        BtnAgregarNota.setText("+");
        BtnAgregarNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarNotaActionPerformed(evt);
            }
        });
        PanelNotas.add(BtnAgregarNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 12, 44, -1));

        JPPANEL.add(PanelNotas, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 450, 690, 150));

        PanelActividad.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JlbActividad.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        JlbActividad.setForeground(new java.awt.Color(255, 255, 255));
        JlbActividad.setText("ACTIVIDAD RECIENTE");
        PanelActividad.add(JlbActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 19, 354, -1));

        txtActividadReciente.setColumns(20);
        txtActividadReciente.setRows(5);
        jScrollPane2.setViewportView(txtActividadReciente);

        PanelActividad.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 650, 210));

        JPPANEL.add(PanelActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 620, 690, 300));

        Btnhoy.setBackground(new java.awt.Color(31, 10, 66));
        Btnhoy.setText("Hoy");
        Btnhoy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnhoyActionPerformed(evt);
            }
        });
        JPPANEL.add(Btnhoy, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 104, 52));

        btnSemana.setBackground(new java.awt.Color(31, 10, 66));
        btnSemana.setText("Semana");
        JPPANEL.add(btnSemana, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 145, 52));

        BtnMes.setBackground(new java.awt.Color(31, 10, 66));
        BtnMes.setText("Mes");
        JPPANEL.add(BtnMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 190, 78, 52));

        BtnTrimestre.setBackground(new java.awt.Color(31, 10, 66));
        BtnTrimestre.setText("Trimestre");
        JPPANEL.add(BtnTrimestre, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, 151, 52));

        JlbTituloPrincipal.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 56)); // NOI18N
        JlbTituloPrincipal.setForeground(new java.awt.Color(255, 255, 255));
        JlbTituloPrincipal.setText("RESUMEN DEL NEGOCIO");
        JPPANEL.add(JlbTituloPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 867, 94));

        JlbDescripcion.setFont(new java.awt.Font("Lucida Sans", 0, 18)); // NOI18N
        JlbDescripcion.setForeground(new java.awt.Color(204, 0, 0));
        JlbDescripcion.setText("Esto es lo que está pasando con tu inventario y ventas.");
        JPPANEL.add(JlbDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 678, 31));

        panelRedondo1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlbUsuarioActivo.setBackground(new java.awt.Color(255, 255, 255));
        jlbUsuarioActivo.setFont(new java.awt.Font("Lucida Bright", 1, 24)); // NOI18N
        jlbUsuarioActivo.setForeground(new java.awt.Color(255, 255, 255));
        panelRedondo1.add(jlbUsuarioActivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 230, 50));

        JPPANEL.add(panelRedondo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1560, 60, 270, 70));

        Panelcontenido.add(JPPANEL, "card2");

        jMenuBar1.setBackground(new java.awt.Color(51, 0, 51));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenuBar1.setForeground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setBorderPainted(false);

        INICIO.setBackground(new java.awt.Color(51, 0, 51));
        INICIO.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        INICIO.setForeground(new java.awt.Color(255, 255, 255));
        INICIO.setText("Inicio");
        INICIO.setBorderPainted(false);
        INICIO.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        INICIO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        INICIO.setPreferredSize(new java.awt.Dimension(150, 50));
        INICIO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                INICIOMouseClicked(evt);
            }
        });
        INICIO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INICIOActionPerformed(evt);
            }
        });
        jMenuBar1.add(INICIO);

        VENTASYFACTURAS.setBackground(new java.awt.Color(51, 0, 51));
        VENTASYFACTURAS.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        VENTASYFACTURAS.setForeground(new java.awt.Color(255, 255, 255));
        VENTASYFACTURAS.setText("Ventas y Facturas");
        VENTASYFACTURAS.setBorderPainted(false);
        VENTASYFACTURAS.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        VENTASYFACTURAS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        VENTASYFACTURAS.setPreferredSize(new java.awt.Dimension(170, 50));
        VENTASYFACTURAS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                VENTASYFACTURASMouseEntered(evt);
            }
        });

        MenuFacturas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuFacturas.setText("Facturas");
        MenuFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFacturasActionPerformed(evt);
            }
        });
        VENTASYFACTURAS.add(MenuFacturas);

        MenuVentas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        MenuVentas.setText("Ventas");
        MenuVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuVentasActionPerformed(evt);
            }
        });
        VENTASYFACTURAS.add(MenuVentas);

        jMenuBar1.add(VENTASYFACTURAS);

        PRODUCTOS.setBackground(new java.awt.Color(51, 0, 51));
        PRODUCTOS.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PRODUCTOS.setForeground(new java.awt.Color(255, 255, 255));
        PRODUCTOS.setText("Productos");
        PRODUCTOS.setBorderPainted(false);
        PRODUCTOS.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        PRODUCTOS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PRODUCTOS.setPreferredSize(new java.awt.Dimension(150, 50));
        PRODUCTOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PRODUCTOSMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PRODUCTOSMouseEntered(evt);
            }
        });
        jMenuBar1.add(PRODUCTOS);

        EMPLEADOS.setBackground(new java.awt.Color(51, 0, 51));
        EMPLEADOS.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        EMPLEADOS.setForeground(new java.awt.Color(255, 255, 255));
        EMPLEADOS.setText("Empleados");
        EMPLEADOS.setBorderPainted(false);
        EMPLEADOS.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        EMPLEADOS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EMPLEADOS.setPreferredSize(new java.awt.Dimension(150, 50));
        EMPLEADOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EMPLEADOSMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                EMPLEADOSMouseEntered(evt);
            }
        });
        jMenuBar1.add(EMPLEADOS);

        BODEGA.setBackground(new java.awt.Color(51, 0, 51));
        BODEGA.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BODEGA.setForeground(new java.awt.Color(255, 255, 255));
        BODEGA.setText("Bodega");
        BODEGA.setBorderPainted(false);
        BODEGA.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        BODEGA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BODEGA.setPreferredSize(new java.awt.Dimension(150, 50));
        BODEGA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BODEGAMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BODEGAMouseEntered(evt);
            }
        });
        jMenuBar1.add(BODEGA);

        CAJA.setBackground(new java.awt.Color(51, 0, 51));
        CAJA.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CAJA.setForeground(new java.awt.Color(255, 255, 255));
        CAJA.setText("Caja");
        CAJA.setBorderPainted(false);
        CAJA.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        CAJA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CAJA.setPreferredSize(new java.awt.Dimension(150, 50));
        CAJA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CAJAMouseClicked(evt);
            }
        });
        jMenuBar1.add(CAJA);

        RH.setBackground(new java.awt.Color(51, 0, 51));
        RH.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        RH.setForeground(new java.awt.Color(255, 255, 255));
        RH.setText("RH");
        RH.setBorderPainted(false);
        RH.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        RH.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RH.setPreferredSize(new java.awt.Dimension(150, 50));
        RH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RHMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                RHMouseEntered(evt);
            }
        });
        jMenuBar1.add(RH);

        PROVEEDORES.setBackground(new java.awt.Color(51, 0, 51));
        PROVEEDORES.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PROVEEDORES.setForeground(new java.awt.Color(255, 255, 255));
        PROVEEDORES.setText("Proveedores");
        PROVEEDORES.setBorderPainted(false);
        PROVEEDORES.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        PROVEEDORES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PROVEEDORES.setPreferredSize(new java.awt.Dimension(150, 50));
        PROVEEDORES.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PROVEEDORESMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PROVEEDORESMouseEntered(evt);
            }
        });
        jMenuBar1.add(PROVEEDORES);

        CLIENTES.setBackground(new java.awt.Color(51, 0, 51));
        CLIENTES.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CLIENTES.setForeground(new java.awt.Color(255, 255, 255));
        CLIENTES.setText("Clientes");
        CLIENTES.setBorderPainted(false);
        CLIENTES.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        CLIENTES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CLIENTES.setPreferredSize(new java.awt.Dimension(150, 50));
        CLIENTES.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CLIENTESMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CLIENTESMouseEntered(evt);
            }
        });
        jMenuBar1.add(CLIENTES);

        REPORTES.setBackground(new java.awt.Color(51, 0, 51));
        REPORTES.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        REPORTES.setForeground(new java.awt.Color(255, 255, 255));
        REPORTES.setText("Reportes");
        REPORTES.setBorderPainted(false);
        REPORTES.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        REPORTES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        REPORTES.setPreferredSize(new java.awt.Dimension(150, 50));
        REPORTES.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                REPORTESMouseClicked(evt);
            }
        });
        jMenuBar1.add(REPORTES);

        EgresosProductos.setBackground(new java.awt.Color(51, 0, 51));
        EgresosProductos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        EgresosProductos.setForeground(new java.awt.Color(255, 255, 255));
        EgresosProductos.setText("Egresos");
        EgresosProductos.setBorderPainted(false);
        EgresosProductos.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        EgresosProductos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EgresosProductos.setPreferredSize(new java.awt.Dimension(150, 50));
        EgresosProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                EgresosProductosMouseEntered(evt);
            }
        });

        Compras.setText("Compra de productos");
        Compras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComprasActionPerformed(evt);
            }
        });
        EgresosProductos.add(Compras);

        HistorialEgresos.setText("Historial de Egresos");
        HistorialEgresos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistorialEgresosActionPerformed(evt);
            }
        });
        EgresosProductos.add(HistorialEgresos);

        Pagares.setText("Pagares");
        Pagares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagaresActionPerformed(evt);
            }
        });
        EgresosProductos.add(Pagares);

        jMenuBar1.add(EgresosProductos);

        MASOPCIONES.setBackground(new java.awt.Color(51, 0, 51));
        MASOPCIONES.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MASOPCIONES.setForeground(new java.awt.Color(255, 255, 255));
        MASOPCIONES.setText("Mas Opciones");
        MASOPCIONES.setBorderPainted(false);
        MASOPCIONES.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        MASOPCIONES.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MASOPCIONES.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem1.setText("Configuración");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        MASOPCIONES.add(jMenuItem1);

        HistorialCierreCaja.setText("Historial de Cierre de Caja");
        HistorialCierreCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistorialCierreCajaActionPerformed(evt);
            }
        });
        MASOPCIONES.add(HistorialCierreCaja);

        jMenuBar1.add(MASOPCIONES);

        CERRARSESION.setBackground(new java.awt.Color(51, 0, 51));
        CERRARSESION.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CERRARSESION.setForeground(new java.awt.Color(255, 255, 255));
        CERRARSESION.setText("Cerrar Sesion");
        CERRARSESION.setBorderPainted(false);
        CERRARSESION.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        CERRARSESION.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CERRARSESION.setPreferredSize(new java.awt.Dimension(150, 50));
        CERRARSESION.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CERRARSESIONMouseClicked(evt);
            }
        });
        jMenuBar1.add(CERRARSESION);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panelcontenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panelcontenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RHMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void RHMouseEntered(java.awt.event.MouseEvent evt) {
        RH.doClick();
    }

    private void CAJAMouseClicked(java.awt.event.MouseEvent evt) {
        mostrarPanel("Cajero");
    }

    private void BODEGAMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void BODEGAMouseEntered(java.awt.event.MouseEvent evt) {
        BODEGA.doClick();
    }

    private void EMPLEADOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EMPLEADOSMouseClicked
        mostrarPanel("Empleados");
    }//GEN-LAST:event_EMPLEADOSMouseClicked

    private void INICIOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INICIOActionPerformed

    }//GEN-LAST:event_INICIOActionPerformed

    private void BtnhoyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnhoyActionPerformed
        seleccionarPeriodoInicio(Controladores.ControladorEstadistica.TipoPeriodo.HOY);
    }//GEN-LAST:event_BtnhoyActionPerformed

    private void BtnAgregarNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAgregarNotaActionPerformed
        panelNotasInstancia.prepararNuevaNota();
        abrirDialogoNotas();
    }//GEN-LAST:event_BtnAgregarNotaActionPerformed

    private void BtnVerStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnVerStatsActionPerformed
        mostrarPanel("Estadisticas");
    }//GEN-LAST:event_BtnVerStatsActionPerformed

    private void INICIOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_INICIOMouseClicked
        mostrarPantallaInicialSegunRol();
    }//GEN-LAST:event_INICIOMouseClicked

    private void CERRARSESIONMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CERRARSESIONMouseClicked
        Vista.AUTENTICACION.Login login = new Vista.AUTENTICACION.Login();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        Vista.IA.VozAsistente.detener();
        this.dispose();
    }//GEN-LAST:event_CERRARSESIONMouseClicked

    private void EMPLEADOSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EMPLEADOSMouseEntered

        EMPLEADOS.doClick();

    }//GEN-LAST:event_EMPLEADOSMouseEntered

    private void PROVEEDORESMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROVEEDORESMouseEntered
        PROVEEDORES.doClick();
    }//GEN-LAST:event_PROVEEDORESMouseEntered

    private void VENTASYFACTURASMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VENTASYFACTURASMouseEntered
        VENTASYFACTURAS.doClick();
    }//GEN-LAST:event_VENTASYFACTURASMouseEntered

    private void BtnVerStatsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnVerStatsMouseClicked
        mostrarPanel("estadisticas");
    }//GEN-LAST:event_BtnVerStatsMouseClicked

    private void CLIENTESMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CLIENTESMouseEntered
        CLIENTES.doClick();
    }//GEN-LAST:event_CLIENTESMouseEntered

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        mostrarPanel("Configuracion");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void HistorialCierreCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistorialCierreCajaActionPerformed
        mostrarPanel("HistorialCierreCaja");
    }//GEN-LAST:event_HistorialCierreCajaActionPerformed

    private void PRODUCTOSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PRODUCTOSMouseEntered
        PRODUCTOS.doClick();
    }//GEN-LAST:event_PRODUCTOSMouseEntered

    private void PRODUCTOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PRODUCTOSMouseClicked
        mostrarPanel("Productos");
    }//GEN-LAST:event_PRODUCTOSMouseClicked

    private void PROVEEDORESMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROVEEDORESMouseClicked
        mostrarPanel("Proveedores");
    }//GEN-LAST:event_PROVEEDORESMouseClicked

    private void CLIENTESMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CLIENTESMouseClicked
        mostrarPanel("Clientes");
    }//GEN-LAST:event_CLIENTESMouseClicked

    private void MenuVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVentasActionPerformed
        mostrarPanel("historialFacturas");
    }//GEN-LAST:event_MenuVentasActionPerformed

    private void MenuFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFacturasActionPerformed
        mostrarPanel("PanelFacturacion");
    }//GEN-LAST:event_MenuFacturasActionPerformed

    private void ComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComprasActionPerformed
        mostrarPanel("PanelCompras");
    }//GEN-LAST:event_ComprasActionPerformed

    private void HistorialEgresosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistorialEgresosActionPerformed
        mostrarPanel("PanelHistorialEgresos");
    }//GEN-LAST:event_HistorialEgresosActionPerformed

    private void PagaresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagaresActionPerformed
        mostrarPanel("PanelPagares");
    }//GEN-LAST:event_PagaresActionPerformed

    private void EgresosProductosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EgresosProductosMouseEntered
        EgresosProductos.doClick();
    }//GEN-LAST:event_EgresosProductosMouseEntered

    private void REPORTESMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_REPORTESMouseClicked
        mostrarPanel("Reporte");
    }//GEN-LAST:event_REPORTESMouseClicked

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu BODEGA;
    private componentes.BotonModerno BtnAgregarNota;
    private componentes.BotonModerno BtnMes;
    private componentes.BotonModerno BtnTrimestre;
    private componentes.BotonModerno BtnVerStats;
    private componentes.BotonModerno Btnhoy;
    private javax.swing.JMenu CAJA;
    private javax.swing.JMenu CERRARSESION;
    private javax.swing.JMenu CLIENTES;
    private javax.swing.JMenuItem Compras;
    private javax.swing.JMenu EMPLEADOS;
    private javax.swing.JMenu EgresosProductos;
    private javax.swing.JMenuItem HistorialCierreCaja;
    private javax.swing.JMenuItem HistorialEgresos;
    private javax.swing.JMenu INICIO;
    private javax.swing.JLabel IconGanancias;
    private javax.swing.JLabel IconGastos;
    private javax.swing.JLabel IconStock;
    private javax.swing.JLabel IconVendidos;
    private javax.swing.JPanel JPPANEL;
    private javax.swing.JLabel JlbActividad;
    private javax.swing.JLabel JlbDescripcion;
    private javax.swing.JLabel JlbEdeventas;
    private javax.swing.JLabel JlbGanancias;
    private javax.swing.JLabel JlbGastos;
    private javax.swing.JLabel JlbMasvendidos;
    private javax.swing.JLabel JlbNotas;
    private javax.swing.JLabel JlbTituloPrincipal;
    private javax.swing.JLabel Jlbstockdown;
    private javax.swing.JList<String> JlstNotas;
    private javax.swing.JMenu MASOPCIONES;
    private javax.swing.JMenuItem MenuFacturas;
    private javax.swing.JMenuItem MenuVentas;
    private javax.swing.JMenu PRODUCTOS;
    private javax.swing.JMenu PROVEEDORES;
    private javax.swing.JMenuItem Pagares;
    private componentes.PanelRedondo PanelActividad;
    private componentes.PanelRedondo PanelGanancias;
    private componentes.PanelRedondo PanelGastos;
    private componentes.PanelRedondo PanelGrafico;
    private componentes.PanelRedondo PanelMasVendidos;
    private componentes.PanelRedondo PanelNotas;
    private componentes.PanelRedondo PanelStock;
    private javax.swing.JPanel Panelcontenido;
    private javax.swing.JMenu REPORTES;
    private javax.swing.JMenu RH;
    private javax.swing.JMenu VENTASYFACTURAS;
    private componentes.BotonModerno btnSemana;
    private componentes.GraficoKrypton graficoEstadisticasInicio;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlbUsuarioActivo;
    private javax.swing.JLabel lblEstockagotarseprimero;
    private javax.swing.JLabel lblEstockagotarsesegundo;
    private javax.swing.JLabel lblMasvendidosPrimero;
    private javax.swing.JLabel lblMasvendidosSegundo;
    private javax.swing.JLabel lblganancias;
    private javax.swing.JLabel lblgastos;
    private javax.swing.JLabel lblmasvendidos;
    private javax.swing.JLabel lblstockagotarse;
    private componentes.PanelRedondo panelRedondo1;
    private javax.swing.JTextArea txtActividadReciente;
    // End of variables declaration//GEN-END:variables
}
