package provider;

import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;
import provider.reader.twitch.TwitchChatReader;
import provider.reader.youtube.YoutubeChatReader;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConcurrentLinkedQueue<ChatMessage> queue = new ConcurrentLinkedQueue<>();


        QueueFilterWrapper queueFilterWrapper = new QueueFilterWrapper(queue);

        TwitchChatReader bot = new TwitchChatReader("guzu", queueFilterWrapper);
        bot.registerFeatures();
        bot.start();

        YoutubeChatReader youtubeChatReader = new YoutubeChatReader("dvOJQba8VFs", queueFilterWrapper);
        youtubeChatReader.start();

        while (true) {
            if (queueFilterWrapper.hasElements()) {
                ChatMessage chatMessage = queueFilterWrapper.poll();
                System.out.println(chatMessage.getStreamingSite() + " " + chatMessage.getMessage());
            }
        }
    }
}
