import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class VirusScanner {

    // signup here to get your API key: https://developers.virustotal.com/v3.0/reference#getting-started
    private static final String VIRUS_TOTAL_API_KEY_HEADER = "x-apikey";
    private static final String VIRUS_TOTAL_API_KEY = "YOUR_API_KEY_HERE";

    public static void main(String[] args) throws Exception {

        var client = HttpClient.newBuilder().build();

        Path localFile = Paths.get("7z.exe");
        if (!Files.exists(localFile)) {
            System.err.println("File does not exist: " + localFile.toAbsolutePath());
            return;
        }

        Map<Object, Object> data = new LinkedHashMap<>();
        data.put("file", localFile);
        String boundary = new BigInteger(256, new Random()).toString();

        var request = HttpRequest.newBuilder()
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .POST(ofMimeMultipartData(data, boundary))
                .uri(URI.create("https://www.virustotal.com/api/v3/files"))
                .header(VIRUS_TOTAL_API_KEY_HEADER, VIRUS_TOTAL_API_KEY)
                .build();

        HttpResponse<String> uploadResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(uploadResponse.body());

        Gson gson = new Gson();
        Map<?, ?> uploadResponseMap = gson.fromJson(uploadResponse.body(), Map.class);
        // The JSON for a successful file scan v3 response looks like:
        // {
        //   "data": {
        //     "type": "analysis",
        //     "id": "<analysis_id>"
        //   }
        // }

        Map<?, ?> dataMap = (Map<?, ?>) uploadResponseMap.get("data");
        String analysisId = (String) dataMap.get("id");

        URI uri = new URI("https", "www.virustotal.com", "/api/v3/analyses/" + analysisId,
                null, null);

        HttpResponse<String> status = client.send(HttpRequest.newBuilder(uri)
                        .header(VIRUS_TOTAL_API_KEY_HEADER, VIRUS_TOTAL_API_KEY)
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        System.out.println(status.body());
    }

    public static HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data,
                                                                String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + System.lineSeparator() + "Content-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                        + "\"" + System.lineSeparator() + "Content-Type: " + mimeType + System.lineSeparator() +
                        System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"" + System.lineSeparator() + System.lineSeparator() +
                        entry.getValue() + System.lineSeparator())
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

}
