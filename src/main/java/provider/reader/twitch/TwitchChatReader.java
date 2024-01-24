package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;
import provider.reader.StreamReaderI;

import java.time.LocalDateTime;

public class TwitchChatReader implements StreamReaderI {

    private ITwitchClient twitchClient;
    private String channel;

    public TwitchChatReader(String channel, QueueFilterWrapper queueFilterWrapper) {

        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
        this.channel = channel;

        twitchClient = clientBuilder
                .withClientId("justinfan1234") // justinfanXXXX is used for read only access.
                .withClientSecret("NOTUSED") // Password not used for read only access
                .withEnableChat(true)
                .build();

        SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);

        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(event, queueFilterWrapper));
    }

    public void onChannelMessage(ChannelMessageEvent event, QueueFilterWrapper queueFilterWrapper) {
        boolean wasAdded = queueFilterWrapper.offer(ChatMessage.builder().streamingSite("Twitch").message(event.getMessage())
                .userName(event.getUser().getName()).timeCreated(LocalDateTime.now()).build());
        // TODO handle wasAdded when false, log?
    }

    public void start() {
        twitchClient.getChat().joinChannel(channel);
    }
}
