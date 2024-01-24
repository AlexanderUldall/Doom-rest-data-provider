package provider;

import lombok.extern.slf4j.Slf4j;
import provider.configuration.Configuration;
import provider.consumer.QueueConsumer;
import provider.filter.QueueFilterWrapper;
import provider.reader.kick.Kick4JChatReader;
import provider.reader.twitch.TwitchChatReader;
import provider.reader.youtube.YoutubeChatReader;

import java.io.IOException;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        QueueFilterWrapper queueFilterWrapper = new QueueFilterWrapper();
        Configuration configuration = new Configuration();

        if (isNotBlank(configuration.getTwitchChannelId())) {
            TwitchChatReader twitchChatReader = new TwitchChatReader(configuration.getTwitchChannelId(), queueFilterWrapper);
            twitchChatReader.start();
        }

        if (isNotBlank(configuration.getYoutubeChannelId())) {
            YoutubeChatReader youtubeChatReader = new YoutubeChatReader(configuration.getYoutubeChannelId(), queueFilterWrapper);
            youtubeChatReader.start();
        }

        if (isNotBlank(configuration.getKickChannelId())) {
            Kick4JChatReader kick4JChatReader = new Kick4JChatReader(configuration.getKickChannelId(), queueFilterWrapper);
            kick4JChatReader.start(); //  TODO handle erros from gifted subs(?)
        }

        QueueConsumer queueConsumer = new QueueConsumer(queueFilterWrapper);
        queueConsumer.start();
    }

    static boolean isNotBlank(String string) {
        return !(string == null || string.isBlank());
    }
}
