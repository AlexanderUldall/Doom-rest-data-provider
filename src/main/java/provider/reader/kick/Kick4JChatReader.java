package provider.reader.kick;

import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;
import provider.reader.StreamReaderI;
import uk.co.mistyknives.kick4j.Kick4J;
import uk.co.mistyknives.kick4j.Kick4JBuilder;
import uk.co.mistyknives.kick4j.events.EventType;
import uk.co.mistyknives.kick4j.events.impl.chatroom.ChatMessageEvent;
import uk.co.mistyknives.kick4j.util.Logger;

import java.time.LocalDateTime;

public class Kick4JChatReader implements StreamReaderI {
    private Kick4J client;
    private String channel;

    public Kick4JChatReader(String channel, QueueFilterWrapper queueFilterWrapper) {

        this.channel = channel;

        client = Kick4JBuilder.builder()
                .logType(Logger.DEFAULT)
                .join(Integer.parseInt((this.channel)))
                .on(EventType.CHAT_MESSAGE, (ChatMessageEvent chatMessageEvent) -> onChannelMessage(chatMessageEvent, queueFilterWrapper))
                .build();
    }

    public void onChannelMessage(ChatMessageEvent chatMessageEvent, QueueFilterWrapper queueFilterWrapper) {
        boolean wasAdded = queueFilterWrapper.offer(ChatMessage.builder().streamingSite("Kick").message(chatMessageEvent.getMessage())
                .userName(chatMessageEvent.getSender().getUsername()).timeCreated(LocalDateTime.now()).build());
        // TODO handle wasAdded when false, log?
    }

    public void start() {
        client.login();
    }
}


