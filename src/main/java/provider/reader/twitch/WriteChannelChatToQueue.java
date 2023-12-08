package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;

public class WriteChannelChatToQueue {

    private QueueFilterWrapper queueFilterWrapper;

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public WriteChannelChatToQueue(SimpleEventHandler eventHandler, QueueFilterWrapper queueFilterWrapper) {
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(event));
        this.queueFilterWrapper = queueFilterWrapper;
    }

    /**
     * Subscribe to the ChannelMessage Event and write the output to the console
     */
    public void onChannelMessage(ChannelMessageEvent event) {
        boolean wasAdded = queueFilterWrapper.offer(ChatMessage.builder().streamingSite("Twitch").message(event.getMessage()).userName(event.getUser().getName()).build());
        // TODO handle wasAdded when false, log?
    }

}
