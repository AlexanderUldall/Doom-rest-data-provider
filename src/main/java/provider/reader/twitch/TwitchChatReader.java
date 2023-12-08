package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import provider.filter.QueueFilterWrapper;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TwitchChatReader {

    private ITwitchClient twitchClient;
    private String channel;
    private QueueFilterWrapper queueFilterWrapper;

    public TwitchChatReader(String channel, QueueFilterWrapper queueFilterWrapper) {

        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
        this.channel = channel;
        this.queueFilterWrapper = queueFilterWrapper;

        twitchClient = clientBuilder
                .withClientId("justinfan1234") // jusinfanXXXX is used for read only access.
                .withClientSecret("NOTUSED") // Password not used for read only access
                .withEnableChat(true)
                .build();
    }

    public void registerFeatures() {
        SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);

        // Register Event-based features
        WriteChannelChatToQueue writeChannelChatToQueue = new WriteChannelChatToQueue(eventHandler, queueFilterWrapper);
    }

    public void start() {
        twitchClient.getChat().joinChannel(channel);
    }

    public ITwitchClient getTwitchClient() {
        return twitchClient;
    }
}
