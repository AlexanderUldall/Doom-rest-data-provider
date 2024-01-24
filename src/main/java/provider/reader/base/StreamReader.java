package provider.reader.base;

import lombok.AllArgsConstructor;
import provider.Enums.StreamingSite;
import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;

import java.time.LocalDateTime;

@AllArgsConstructor
public abstract class StreamReader implements StreamReaderI {

    private QueueFilterWrapper queueFilterWrapper;

    @Override
    public void onChannelMessage(StreamingSite streamingSite, String userName, String chatMessage) {
        boolean wasAdded = this.queueFilterWrapper.offer(ChatMessage.builder().streamingSite(streamingSite.name()).message(chatMessage)
                .userName(userName).timeCreated(LocalDateTime.now()).build());
//        TODO handle wasAdded when false
    }
}
