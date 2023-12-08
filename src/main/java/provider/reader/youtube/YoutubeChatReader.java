package provider.reader.youtube;

import com.google.gson.*;
import provider.model.ChatMessage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeChatReader {

    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36,gzip(gfe)";
    private static final String liveChatApi = "https://www.youtube.com/youtubei/v1/live_chat/get_live_chat?key="; // view live chat

    private boolean firstRun = true;
    private String liveVideoId;
    private String apiKey;
    private String continuation;
    private String clientVersion;
    private ConcurrentLinkedQueue queue;
    private HttpClient httpClient = HttpClient.newHttpClient();
    private ArrayList<String> chatMessages = new ArrayList<>();
    private Gson gson = new GsonBuilder().create();

    public YoutubeChatReader(String liveVideoId, ConcurrentLinkedQueue queue) throws IOException, InterruptedException {
        this.liveVideoId = liveVideoId;
        this.queue = queue;
        initialize();
    }

    public void initialize() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.youtube.com/watch?v=" + this.liveVideoId))
                .build();

        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Matcher innertubeApiKeyMatcher = Pattern.compile("\"innertubeApiKey\":\"([^\"]*)\"").matcher(response.body().toString());
        if (innertubeApiKeyMatcher.find()) {
            this.apiKey = innertubeApiKeyMatcher.group(1);
        }

        Matcher topOnlyContinuationMatcher = Pattern.compile("\"selected\":true,\"continuation\":\\{\"reloadContinuationData\":\\{\"continuation\":\"([^\"]*)").matcher(response.body().toString());
        if (topOnlyContinuationMatcher.find()) {
            this.continuation = topOnlyContinuationMatcher.group(1);
        }
    }


    public void update() throws IOException, InterruptedException {
        this.chatMessages.clear();

        HttpRequest httpRequest = buildRequest();

        HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonObject = JsonParser.parseString((String) response.body()).getAsJsonObject();

        JsonObject liveChatContinuationJson = jsonObject.getAsJsonObject("continuationContents").getAsJsonObject("liveChatContinuation");
        JsonArray continuations = liveChatContinuationJson.getAsJsonArray("continuations");
        JsonArray actions = liveChatContinuationJson.getAsJsonArray("actions");

        for (JsonElement continuation : continuations) {
            JsonElement ContinuationData = continuation.getAsJsonObject().get("invalidationContinuationData");
            if (ContinuationData == null) {
                ContinuationData = continuation.getAsJsonObject().get("timedContinuationData");
            }
            if (ContinuationData == null) {
                ContinuationData = continuation.getAsJsonObject().get("reloadContinuationData");
            }
            this.continuation = ((JsonObject) ContinuationData).get("continuation").getAsString();
        }

        // First pass through of youtube chat will get ALL messages from the chat
        // We only want the ones from after the program was started
        if (firstRun) {
            firstRun = false;
            return;
        }

        if (actions == null) {
            return;
        }

        for (JsonElement action : actions) {

            JsonObject messageObject = getJsonObjectWithNullGuard(action, "addChatItemAction", "item", "liveChatTextMessageRenderer", "message");
            if (messageObject == null) {
                continue;
            }
            JsonElement textElement = ((JsonArray) messageObject.get("runs")).get(0).getAsJsonObject().get("text");
            if (textElement != null) {
                chatMessages.add(textElement.getAsString());
            }
        }
    }


    public void start() throws IOException, InterruptedException {

        new Thread(() -> {
            try {
                while (true) {
                    update();
                    for (String message : getChatMessages()) {
//                System.out.println(message);
                        boolean wasAdded = queue.offer(ChatMessage.builder().streamingSite("Youtube").message(message).userName("none yet").build());
                        // TODO handle not added
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
            }
        }).start();

//        while (true) {
//
//        }
    }


    private JsonObject getJsonObjectWithNullGuard(JsonElement jsonElement, String... keys) {
        JsonObject jsonObject = (JsonObject) jsonElement;
        for (String key : keys) {

            if (jsonObject == null) {
                break;
            }
            jsonObject = jsonObject.getAsJsonObject(key);
        }
        return jsonObject;
    }

    private HttpRequest buildRequest() {
        //        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        this.clientVersion = format.format(new Date(System.currentTimeMillis() - (24 * 60 * 1000))) + ".06.00";
        this.clientVersion = "2.20231205.06.00"; // TODO above client versioon code does not work, hardcode rework to get it

        Map<String, Object> jsonMap = new LinkedHashMap<>();
        Map<String, Object> context = new LinkedHashMap<>();
        Map<String, Object> client = new LinkedHashMap<>();
        jsonMap.put("context", context);
        context.put("client", client);
        client.put("clientName", "WEB");
        client.put("clientVersion", clientVersion);
        jsonMap.put("continuation", this.continuation);

        String json = this.gson.toJson(jsonMap);

        HttpRequest.Builder postBuilder = HttpRequest.newBuilder()
                .uri(URI.create(this.liveChatApi + this.apiKey))
                .POST(HttpRequest.BodyPublishers.ofString(json));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept-Charset", "utf-8");
        headers.put("User-Agent", userAgent);

        headers.forEach((headerKey, headerValue) -> postBuilder.header(headerKey, headerValue));

        return postBuilder.build();
    }

    public ArrayList<String> getChatMessages() {
        return chatMessages;
    }
}
