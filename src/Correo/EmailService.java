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

    private static final String NOMBRE_REMITENTE = "Sistema de Seguridad";

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

        message.setContent(contenidoHtml, "text/html; charset=utf-8");

        Transport.send(message);
    }

    public static void enviarCodigoRecuperacion(String correoDestino, String nombreUsuario, String codigo) throws MessagingException {
        Properties config = cargarConfiguracion();
        String correoRemitente = config.getProperty("correo.remitente");
        String claveAplicacion = config.getProperty("correo.clave");
        enviarCodigoRecuperacion(correoRemitente, claveAplicacion, correoDestino, nombreUsuario, codigo);
    }

    public static void enviarFacturaPorCorreo(String correoDestino, String nombreCliente, String numFactura, String rutaArchivoPdf) throws MessagingException, java.io.IOException {
        Properties config = cargarConfiguracion();
        String correoRemitente = config.getProperty("correo.remitente");
        String claveAplicacion = config.getProperty("correo.clave");

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
        message.setSubject("Factura " + numFactura);

        String saludo = (nombreCliente != null && !nombreCliente.trim().isEmpty()) ? nombreCliente : "Estimado cliente";

        javax.mail.internet.MimeBodyPart parteTexto = new javax.mail.internet.MimeBodyPart();
        parteTexto.setContent("<h2>Factura " + numFactura + "</h2><p>Hola <b>" + saludo + "</b>, adjuntamos tu factura en PDF.</p>", "text/html; charset=utf-8");

        javax.mail.internet.MimeBodyPart parteAdjunto = new javax.mail.internet.MimeBodyPart();
        parteAdjunto.attachFile(new java.io.File(rutaArchivoPdf));

        javax.mail.internet.MimeMultipart multipart = new javax.mail.internet.MimeMultipart();
        multipart.addBodyPart(parteTexto);
        multipart.addBodyPart(parteAdjunto);

        message.setContent(multipart);
        Transport.send(message);
    }
}