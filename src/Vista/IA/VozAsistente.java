
package Vista.IA;

public class VozAsistente {

    private static Process procesoVozActivo = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(VozAsistente::detener));
    }
    public static void hablar(String texto) {
        new Thread(() -> {
            try {
                detener();
                String textoSeguro = texto.replace("'", " ").replace("\"", " ");

                String[] cmd = {
                    "powershell",
                    "-Command",
                    "Add-Type -AssemblyName System.Speech;" +
                    "$voz = New-Object System.Speech.Synthesis.SpeechSynthesizer;" +
                    "$voz.Rate = 1;" +
                    "$voz.Volume = 100;" +
                    "$voces = $voz.GetInstalledVoices() | Where-Object {$_.VoiceInfo.Culture.Name -like 'es*'};" +
                    "if($voces.Count -gt 0){$voz.SelectVoice($voces[0].VoiceInfo.Name)};" +
                    "$voz.Speak('" + textoSeguro + "');" +
                    "$voz.Dispose();"
                };

                procesoVozActivo = Runtime.getRuntime().exec(cmd);
                procesoVozActivo.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void detener() {
        try {
            if (procesoVozActivo != null) {
                procesoVozActivo.destroyForcibly();
                procesoVozActivo.waitFor();
                procesoVozActivo = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}