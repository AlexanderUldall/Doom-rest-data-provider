package provider.reader.kick;

import provider.Enums.StreamingSite;
import provider.filter.QueueFilterWrapper;
import provider.reader.base.StreamReader;
import uk.co.mistyknives.kick4j.Kick4J;
import uk.co.mistyknives.kick4j.Kick4JBuilder;
import uk.co.mistyknives.kick4j.events.EventType;
import uk.co.mistyknives.kick4j.events.impl.chatroom.ChatMessageEvent;
import uk.co.mistyknives.kick4j.util.Logger;

public class Kick4JChatReader extends StreamReader {
    private Kick4J client;

    public Kick4JChatReader(String channelId, QueueFilterWrapper queueFilterWrapper) {
        super(queueFilterWrapper, channelId);

        client = Kick4JBuilder.builder()
                .logType(Logger.DEFAULT)
                .join(Integer.parseInt((getChannelId())))
                .on(EventType.CHAT_MESSAGE, (ChatMessageEvent chatMessageEvent) -> onChannelMessage(StreamingSite.KICK, chatMessageEvent.getSender().getUsername(), chatMessageEvent.getMessage()))
                .build();
    }

    public void start() {
        client.login();
    }
}
