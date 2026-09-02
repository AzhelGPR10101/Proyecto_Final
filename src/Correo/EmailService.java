package Correo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    private static final String NOMBRE_REMITENTE = "Krypton";

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    public static boolean hayCorreoConfigurado() {
        try {
            Properties config = cargarConfiguracion();
            String remitente = config.getProperty("correo.remitente");
            return remitente != null && !remitente.trim().isEmpty()
                    && !remitente.contains("tu_correo");
        } catch (Exception e) {
            return false;
        }
    }

    private static Properties cargarConfiguracion() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("No se encontró config.properties en la raíz del proyecto. "
                    + "Copia config.properties.example, renómbralo a config.properties y coloca ahí "
                    + "tu correo remitente y tu contraseña de aplicación de Gmail.", e);
        }
        return props;
    }

    public static void enviarCodigoRecuperacion(String correoRemitente, String claveAplicacion, String correoDestino, String nombreUsuario, String codigo) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        props.put("mail.smtp.connectiontimeout", "8000");
        props.put("mail.smtp.timeout", "8000");
        props.put("mail.smtp.writetimeout", "8000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(correoRemitente, NOMBRE_REMITENTE));
        } catch (UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(correoRemitente));
        }

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
        message.setSubject("Código de Recuperación de Contraseña");

        String saludo = (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) ? nombreUsuario : "Estimado Usuario";

        String contenidoHtml = "<h2>Recuperación de Contraseña</h2>"
                + "<p>Hola <b>" + saludo + "</b>,</p>"
                + "<p>Tu código de verificación es: <b style='font-size:18px; color:#6B21A8;'>" + codigo + "</b></p>"
                + "<p>Si no solicitaste este cambio, ignora este mensaje.</p>";

        String contenidoTexto = "Recuperación de Contraseña\n\n"
                + "Hola " + saludo + ",\n\n"
                + "Tu código de verificación es: " + codigo + "\n\n"
                + "Si no solicitaste este cambio, ignora este mensaje.";

        javax.mail.internet.MimeBodyPart parteTexto = new javax.mail.internet.MimeBodyPart();
        parteTexto.setText(contenidoTexto, "utf-8");

        javax.mail.internet.MimeBodyPart parteHtml = new javax.mail.internet.MimeBodyPart();
        parteHtml.setContent(contenidoHtml, "text/html; charset=utf-8");

        javax.mail.internet.MimeMultipart contenidoAlternativo = new javax.mail.internet.MimeMultipart("alternative");
        contenidoAlternativo.addBodyPart(parteTexto);
        contenidoAlternativo.addBodyPart(parteHtml);

        message.setContent(contenidoAlternativo);

        Transport.send(message);
    }

    public static void enviarCodigoRecuperacion(String correoDestino, String nombreUsuario, String codigo) throws MessagingException {
        Properties config = cargarConfiguracion();
        String correoRemitente = config.getProperty("correo.remitente");
        String claveAplicacion = config.getProperty("correo.clave");
        enviarCodigoRecuperacion(correoRemitente, claveAplicacion, correoDestino, nombreUsuario, codigo);
    }

    public static void enviarFacturaPorCorreo(Modelo.Factura factura, String rutaArchivoPdf) throws MessagingException, java.io.IOException {
        Properties config = cargarConfiguracion();
        String correoRemitente = config.getProperty("correo.remitente");
        String claveAplicacion = config.getProperty("correo.clave");

        String correoDestino = factura.getCliente() != null ? factura.getCliente().getCorreo() : null;
        if (correoDestino == null || correoDestino.trim().isEmpty()) {
            throw new MessagingException("El cliente no tiene correo registrado.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.connectiontimeout", "8000");
        props.put("mail.smtp.timeout", "8000");
        props.put("mail.smtp.writetimeout", "8000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(correoRemitente, NOMBRE_REMITENTE));
        } catch (UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(correoRemitente));
        }
        message.setReplyTo(InternetAddress.parse(correoRemitente));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
        message.setSubject("Tu factura " + factura.getNumFactura());

        javax.mail.internet.MimeBodyPart parteHtml = new javax.mail.internet.MimeBodyPart();
        parteHtml.setContent(construirHtmlFactura(factura), "text/html; charset=utf-8");

        javax.mail.internet.MimeBodyPart parteTexto = new javax.mail.internet.MimeBodyPart();
        parteTexto.setText(construirTextoPlanoFactura(factura), "utf-8");

        javax.mail.internet.MimeMultipart contenidoAlternativo = new javax.mail.internet.MimeMultipart("alternative");
        contenidoAlternativo.addBodyPart(parteTexto);
        contenidoAlternativo.addBodyPart(parteHtml);

        javax.mail.internet.MimeBodyPart parteCuerpo = new javax.mail.internet.MimeBodyPart();
        parteCuerpo.setContent(contenidoAlternativo);

        javax.mail.internet.MimeBodyPart parteAdjunto = new javax.mail.internet.MimeBodyPart();
        parteAdjunto.attachFile(new java.io.File(rutaArchivoPdf));

        javax.mail.internet.MimeMultipart multipart = new javax.mail.internet.MimeMultipart("mixed");
        multipart.addBodyPart(parteCuerpo);
        multipart.addBodyPart(parteAdjunto);

        message.setContent(multipart);
        Transport.send(message);
    }

    private static String construirTextoPlanoFactura(Modelo.Factura factura) {
        String nombreCliente = factura.getCliente() != null
                ? (factura.getCliente().getNombre() + " " + factura.getCliente().getApellido()) : "Cliente";
        StringBuilder sb = new StringBuilder();
        sb.append("Factura ").append(factura.getNumFactura()).append("\n");
        sb.append("Hola ").append(nombreCliente).append(", gracias por tu compra.\n\n");
        if (factura.getDetalles() != null) {
            for (Modelo.DetalleFactura d : factura.getDetalles()) {
                sb.append(String.format("%dx %s - $%.2f%n", d.getCantidad(), d.getNombreProducto(), d.getSubtotal()));
            }
        }
        sb.append(String.format("%nTotal: $%.2f%n", factura.getTotal()));
        sb.append("Adjuntamos el PDF de tu factura.");
        return sb.toString();
    }

    private static String construirHtmlFactura(Modelo.Factura factura) {
        String nombreCliente = factura.getCliente() != null
                ? (factura.getCliente().getNombre() + " " + factura.getCliente().getApellido()) : "Cliente";

        StringBuilder filas = new StringBuilder();
        if (factura.getDetalles() != null) {
            for (Modelo.DetalleFactura d : factura.getDetalles()) {
                filas.append("<tr>")
                     .append("<td style='padding:10px 8px;border-bottom:1px solid #eee;text-align:center;'>").append(d.getCantidad()).append("</td>")
                     .append("<td style='padding:10px 8px;border-bottom:1px solid #eee;'>").append(d.getNombreProducto()).append("</td>")
                     .append("<td style='padding:10px 8px;border-bottom:1px solid #eee;text-align:right;'>$").append(String.format("%.2f", d.getPrecioUnitario())).append("</td>")
                     .append("<td style='padding:10px 8px;border-bottom:1px solid #eee;text-align:right;'>$").append(String.format("%.2f", d.getSubtotal())).append("</td>")
                     .append("</tr>");
            }
        }

        return "<html><body style='margin:0;padding:0;background:#f4f4f7;font-family:Segoe UI,Arial,sans-serif;'>"
            + "<table width='100%' cellpadding='0' cellspacing='0'><tr><td align='center' style='padding:30px 10px;'>"
            + "<table width='600' cellpadding='0' cellspacing='0' style='background:#ffffff;border-radius:8px;overflow:hidden;'>"
            + "<tr><td style='background:#1e2a4a;padding:28px 30px;'>"
            + "<span style='color:#ffffff;font-size:26px;font-weight:bold;letter-spacing:1px;'>FACTURA</span>"
            + "<br><span style='color:#c9d3e6;font-size:13px;'>N&deg; " + factura.getNumFactura() + "</span>"
            + "</td></tr>"
            + "<tr><td style='padding:25px 30px 10px 30px;'>"
            + "<p style='margin:0 0 4px 0;color:#333;font-size:14px;'>Hola <b>" + nombreCliente + "</b>,</p>"
            + "<p style='margin:0;color:#666;font-size:13px;'>Gracias por tu compra. Aqu&iacute; tienes el detalle de tu factura:</p>"
            + "</td></tr>"
            + "<tr><td style='padding:15px 30px;'>"
            + "<table width='100%' cellpadding='0' cellspacing='0' style='border-collapse:collapse;'>"
            + "<tr style='background:#f4f4f7;'>"
            + "<th style='padding:10px 8px;text-align:center;font-size:12px;color:#555;'>CANT</th>"
            + "<th style='padding:10px 8px;text-align:left;font-size:12px;color:#555;'>PRODUCTO</th>"
            + "<th style='padding:10px 8px;text-align:right;font-size:12px;color:#555;'>P. UNIT</th>"
            + "<th style='padding:10px 8px;text-align:right;font-size:12px;color:#555;'>SUBTOTAL</th>"
            + "</tr>"
            + filas.toString()
            + "</table>"
            + "</td></tr>"
            + "<tr><td style='padding:5px 30px 25px 30px;'>"
            + "<table width='100%' cellpadding='0' cellspacing='0'>"
            + "<tr><td align='right' style='padding:4px 8px;color:#666;font-size:13px;'>Subtotal</td><td align='right' style='padding:4px 8px;width:100px;color:#333;font-size:13px;'>$" + String.format("%.2f", factura.getSubtotal()) + "</td></tr>"
            + "<tr><td align='right' style='padding:4px 8px;color:#666;font-size:13px;'>IVA</td><td align='right' style='padding:4px 8px;color:#333;font-size:13px;'>$" + String.format("%.2f", factura.getValorIva()) + "</td></tr>"
            + "<tr><td align='right' style='padding:4px 8px;color:#666;font-size:13px;'>Descuento</td><td align='right' style='padding:4px 8px;color:#333;font-size:13px;'>$" + String.format("%.2f", factura.getDescuento()) + "</td></tr>"
            + "<tr><td align='right' style='padding:10px 8px 0 8px;color:#1e2a4a;font-size:16px;font-weight:bold;'>TOTAL</td><td align='right' style='padding:10px 8px 0 8px;color:#1e2a4a;font-size:16px;font-weight:bold;'>$" + String.format("%.2f", factura.getTotal()) + "</td></tr>"
            + "</table>"
            + "</td></tr>"
            + "<tr><td style='background:#f4f4f7;padding:18px 30px;text-align:center;'>"
            + "<span style='color:#999;font-size:12px;'>Factura tambi&eacute;n adjunta en PDF. Gracias por confiar en nosotros.</span>"
            + "</td></tr>"
            + "</table></td></tr></table></body></html>";
    }
}