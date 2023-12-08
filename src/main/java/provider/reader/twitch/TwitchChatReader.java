package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TwitchChatReader {

    private ITwitchClient twitchClient;
    private String channel;
    private ConcurrentLinkedQueue queue;

    public TwitchChatReader(String channel, ConcurrentLinkedQueue queue) {

        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
        this.channel = channel;
        this.queue = queue;

        twitchClient = clientBuilder
                .withClientId("justinfan1234") // jusinfanXXXX is used for read only access.
                .withClientSecret("NOTUSED") // Password not used for read only access
                .withEnableChat(true)
                .build();
    }

    public void registerFeatures() {
        SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);

        // Register Event-based features
        WriteChannelChatToQueue writeChannelChatToQueue = new WriteChannelChatToQueue(eventHandler, queue);
    }

    public void start() {
        twitchClient.getChat().joinChannel(channel);
    }

    public ITwitchClient getTwitchClient() {
        return twitchClient;
    }
}
