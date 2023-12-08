package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;

import java.time.LocalDateTime;

public class WriteChannelChatToQueue {

    private QueueFilterWrapper queueFilterWrapper;

    public WriteChannelChatToQueue(SimpleEventHandler eventHandler, QueueFilterWrapper queueFilterWrapper) {
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(event));
        this.queueFilterWrapper = queueFilterWrapper;
    }

    public void onChannelMessage(ChannelMessageEvent event) {
        boolean wasAdded = queueFilterWrapper.offer(ChatMessage.builder().streamingSite("Twitch").message(event.getMessage())
                .userName(event.getUser().getName()).timeCreated(LocalDateTime.now()).build());
        // TODO handle wasAdded when false, log?
    }
}
