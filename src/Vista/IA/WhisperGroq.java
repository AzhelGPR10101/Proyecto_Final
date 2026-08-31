
package Vista.IA;

import okhttp3.*;
import java.io.File;
import java.io.IOException;

public class WhisperGroq {

    private static final String URL = "https://api.groq.com/openai/v1/audio/transcriptions";

    public static String transcribir(String archivo) throws IOException {
        OkHttpClient client = new OkHttpClient();
        File audio = new File(archivo);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", audio.getName(),
                        RequestBody.create(MediaType.parse("audio/wav"), audio))
                .addFormDataPart("model", "whisper-large-v3-turbo")
                .build();

        Request request = new Request.Builder()
                .url(URL)
                .header("Authorization", "Bearer " + AsistenteIA.obtenerApiKey())
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error " + response.code() + "\n" + response.body().string());
        }

        String json = response.body().string();
        int i = json.indexOf("\"text\":\"");
        if (i == -1) return "";
        i += 8;
        int j = json.indexOf("\"", i);
        return json.substring(i, j);
    }
}
