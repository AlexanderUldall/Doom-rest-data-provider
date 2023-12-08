package provider;

import provider.consumer.QueueConsumer;
import provider.filter.QueueFilterWrapper;
import provider.reader.twitch.TwitchChatReader;
import provider.reader.youtube.YoutubeChatReader;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        QueueFilterWrapper queueFilterWrapper = new QueueFilterWrapper();

        TwitchChatReader twitchChatReader = new TwitchChatReader("zackrawrr", queueFilterWrapper);
        twitchChatReader.start();

        YoutubeChatReader youtubeChatReader = new YoutubeChatReader("-J3jCkIIN2U", queueFilterWrapper);
        youtubeChatReader.start();

        QueueConsumer queueConsumer = new QueueConsumer(queueFilterWrapper);
        queueConsumer.start();
    }
}
