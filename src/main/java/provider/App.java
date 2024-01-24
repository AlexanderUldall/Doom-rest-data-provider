package provider;

import provider.consumer.QueueConsumer;
import provider.filter.QueueFilterWrapper;
import provider.reader.kick.Kick4JChatReader;
import provider.reader.twitch.TwitchChatReader;
import provider.reader.youtube.YoutubeChatReader;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        QueueFilterWrapper queueFilterWrapper = new QueueFilterWrapper();

        TwitchChatReader twitchChatReader = new TwitchChatReader("xaryu", queueFilterWrapper);
        twitchChatReader.start();

//        YoutubeChatReader youtubeChatReader = new YoutubeChatReader("-J3jCkIIN2U", queueFilterWrapper);
//        youtubeChatReader.start();

//        Kick4JChatReader kick4JChatReader = new Kick4JChatReader("1563861",queueFilterWrapper);
//        kick4JChatReader.start();

        QueueConsumer queueConsumer = new QueueConsumer(queueFilterWrapper);
        queueConsumer.start();

    }
}
