package Vista.IA;

import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class AsistenteIA {

    private static final String URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODELO = "openai/gpt-oss-120b";

    private static final String SYSTEM_PROMPT_BASE
            = "Eres el asistente virtual del sistema KTP, un ERP de gestion de negocios. "
            + "Responde siempre en espanol, de forma amable, clara y breve. "
            + "Puedes responder saludos y mantener conversacion basica. "
            + "SOLO evita responder preguntas completamente ajenas al sistema "
            + "(historia, geografia, noticias, etc.). Si te preguntan eso responde: "
            + "'Ese tema esta fuera de mis funciones, solo puedo ayudarte con el sistema KTP.'\n\n"
            + "Si el usuario te pide ir, abrir o mostrar una seccion del sistema, responde "
            + "confirmando de forma breve (ej: 'Listo, aqui tienes tus reportes') y agrega "
            + "al FINAL de tu respuesta, en su propia linea, una de estas etiquetas EXACTAS "
            + "segun corresponda (nunca inventes otras etiquetas, y nunca la agregues si no te pidieron navegar a ninguna parte):\n"
            + "[IR_A:REPORTES] para reportes o estadisticas\n"
            + "[IR_A:PRODUCTOS] para productos o inventario\n"
            + "[IR_A:CLIENTES] para clientes\n"
            + "[IR_A:EMPLEADOS] para empleados\n"
            + "[IR_A:PROVEEDORES] para proveedores\n"
            + "[IR_A:FACTURACION] para facturar o crear una factura\n"
            + "[IR_A:HISTORIAL_FACTURAS] para el historial de facturas\n"
            + "[IR_A:COMPRAS] para compras\n"
            + "[IR_A:PAGARES] para pagares\n"
            + "[IR_A:EGRESOS] para egresos\n"
            + "[IR_A:CONFIGURACION] para configuracion\n\n"
            + "Se te va a entregar debajo un bloque con datos REALES y actuales del negocio "
            + "(productos y compras a proveedores). Usa SIEMPRE esos datos para responder "
            + "preguntas como 'que productos tenemos', 'que stock hay', 'a que proveedor le "
            + "compramos mas' o similares. Nunca inventes productos, cantidades ni montos que "
            + "no esten en esos datos. Si preguntan algo que esos datos no cubren, dilo con "
            + "honestidad en vez de inventar. Cuando encuentres algo relevante (stock bajo, "
            + "producto por vencer, proveedor con compras muy altas o muy bajas), puedes "
            + "sugerir brevemente una mejora o recomendacion para el negocio, sin exagerar ni "
            + "alargarte,"
            + "No te olvides de lo que se habla; si el usuario sigue en el mismo tema no saludes de nuevo "
            + "ni cambies de tema, continua la conversacion. "
            + "Responde siempre en texto plano, SIN markdown: nunca uses asteriscos (**), guiones de lista, "
            + "numerales (#) ni backticks. Si necesitas listar productos u opciones, usa lineas simples "
            + "separadas por saltos de linea, sin simbolos decorativos.";

    public static String obtenerApiKey() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (Exception e) {
            return null;
        }
        return props.getProperty("ia.groq.apikey");
    }

    public static String preguntar(String contextoSistema, java.util.List<String[]> historial, String preguntaUsuario) throws Exception {
        String apiKey = obtenerApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            return "Falta configurar ia.groq.apikey en config.properties.";
        }

        String systemPrompt = SYSTEM_PROMPT_BASE + "\n\nDatos actuales del sistema:\n" + contextoSistema;

        StringBuilder mensajes = new StringBuilder();
        mensajes.append("{\"role\":\"system\",\"content\":\"").append(escapar(systemPrompt)).append("\"}");
        for (String[] turno : historial) {
            mensajes.append(",{\"role\":\"").append(turno[0]).append("\",\"content\":\"")
                    .append(escapar(turno[1])).append("\"}");
        }
        mensajes.append(",{\"role\":\"user\",\"content\":\"").append(escapar(preguntaUsuario)).append("\"}");

        String jsonBody = "{\"model\":\"" + MODELO + "\",\"messages\":[" + mensajes + "]}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return "Error (codigo " + response.statusCode() + "). Revisa tu API KEY.";
        }

        return limpiarMarkdown(extraerContenido(response.body()));
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private static String extraerContenido(String json) {
        String marcador = "\"content\":\"";
        int inicio = json.lastIndexOf(marcador);
        if (inicio == -1) {
            return "No se pudo leer la respuesta.";
        }
        inicio += marcador.length();
        int fin = inicio;
        while (fin < json.length()) {
            if (json.charAt(fin) == '"' && json.charAt(fin - 1) != '\\') {
                break;
            }
            fin++;
        }
        return json.substring(inicio, fin)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    private static String limpiarMarkdown(String texto) {
        if (texto == null) {
            return null;
        }
        return texto.replace("**", "").replace("__", "")
                .replace("### ", "").replace("## ", "").replace("# ", "")
                .replace("`", "");
    }
}
