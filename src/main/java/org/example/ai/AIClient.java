package org.example.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIClient {


    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    public static String API_KEY = System.getenv("GROQ_API");

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String ask(String systemPrompt, String userPrompt) throws Exception {

        String sys = escape(systemPrompt);
        String usr = escape(userPrompt);

        String json =
                "{"
                        + "\"model\":\"llama-3.3-70b-versatile\","
                        + "\"messages\":["
                        + "{\"role\":\"system\",\"content\":\"" + sys + "\"},"
                        + "{\"role\":\"user\",\"content\":\"" + usr + "\"}"
                        + "],"
                        + "\"temperature\":0.1"
                        + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return extract(response.body()).trim();
    }

    private static String escape(String s) {
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    private static String extract(String json) {
        int i = json.indexOf("\"content\":");
        if (i == -1) return json;
        i = json.indexOf("\"", i + 10) + 1;
        int j = json.indexOf("\"", i);
        if (j == -1) return json;
        return json.substring(i, j)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
