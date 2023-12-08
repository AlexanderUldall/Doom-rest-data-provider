package provider;

import provider.model.ChatMessage;
import provider.reader.twitch.TwitchChatReader;
import provider.reader.youtube.YoutubeChatReader;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConcurrentLinkedQueue<ChatMessage> queue = new ConcurrentLinkedQueue<>();

        TwitchChatReader bot = new TwitchChatReader("guzu", queue);
        bot.registerFeatures();
        bot.start();

        YoutubeChatReader youtubeChatReader = new YoutubeChatReader("dvOJQba8VFs", queue);
        youtubeChatReader.start();

        while (true) {
            if (!queue.isEmpty()) {
                ChatMessage chatMessage = queue.poll();
                System.out.println(chatMessage.getStreamingSite() + " " + chatMessage.getMessage());
            }
        }
    }
}
