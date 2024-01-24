package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import provider.Enums.StreamingSite;
import provider.filter.QueueFilterWrapper;
import provider.reader.StreamReader;

public class TwitchChatReader extends StreamReader {

    private ITwitchClient twitchClient;
    private String channel;

    public TwitchChatReader(String channel, QueueFilterWrapper queueFilterWrapper) {
        super(queueFilterWrapper);

        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
        this.channel = channel;
        this.twitchClient = clientBuilder
                .withClientId("justinfan1234") // justinfanXXXX is used for read only access.
                .withClientSecret("NOTUSED") // Password not used for read only access
                .withEnableChat(true)
                .build();

        SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);

        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(StreamingSite.TWITCH, event.getUser().getName(), event.getMessage()));
    }

    @Override
    public void start() {
        twitchClient.getChat().joinChannel(channel);
    }
}
