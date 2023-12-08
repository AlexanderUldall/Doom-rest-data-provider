package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import provider.filter.QueueFilterWrapper;

public class TwitchChatReader {

    private ITwitchClient twitchClient;
    private String channel;

    public TwitchChatReader(String channel, QueueFilterWrapper queueFilterWrapper) {

        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
        this.channel = channel;

        twitchClient = clientBuilder
                .withClientId("justinfan1234") // jusinfanXXXX is used for read only access.
                .withClientSecret("NOTUSED") // Password not used for read only access
                .withEnableChat(true)
                .build();

        SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);

        // Register Event-based features
        WriteChannelChatToQueue writeChannelChatToQueue = new WriteChannelChatToQueue(eventHandler, queueFilterWrapper);
    }

    public void start() {
        twitchClient.getChat().joinChannel(channel);
    }
}
