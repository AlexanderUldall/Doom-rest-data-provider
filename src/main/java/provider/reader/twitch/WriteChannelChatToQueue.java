package provider.reader.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import provider.model.ChatMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WriteChannelChatToQueue {

    private ConcurrentLinkedQueue queue;

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public WriteChannelChatToQueue(SimpleEventHandler eventHandler, ConcurrentLinkedQueue queue) {
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(event));
        this.queue = queue;
    }

    /**
     * Subscribe to the ChannelMessage Event and write the output to the console
     */
    public void onChannelMessage(ChannelMessageEvent event) {
        boolean wasAdded = queue.offer(ChatMessage.builder().streamingSite("Twitch").message(event.getMessage()).userName(event.getUser().getName()).build());
        // TODO handle wasAdded when false, log?
    }

}
