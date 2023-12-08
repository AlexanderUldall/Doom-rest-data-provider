package provider;

import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;
import provider.reader.twitch.TwitchChatReader;
import provider.reader.youtube.YoutubeChatReader;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        QueueFilterWrapper queueFilterWrapper = new QueueFilterWrapper();

        TwitchChatReader bot = new TwitchChatReader("zackrawrr", queueFilterWrapper);
        bot.start();

        YoutubeChatReader youtubeChatReader = new YoutubeChatReader("-J3jCkIIN2U", queueFilterWrapper);
        youtubeChatReader.start();

        while (true) {
            if (queueFilterWrapper.hasElements()) {
                ChatMessage chatMessage = queueFilterWrapper.poll();
                System.out.println(chatMessage.getStreamingSite() + " " + chatMessage.getMessage());
            }
        }
    }
}
