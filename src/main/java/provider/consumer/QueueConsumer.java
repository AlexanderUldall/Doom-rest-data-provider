package provider.consumer;

import provider.filter.QueueFilterWrapper;
import provider.model.ChatMessage;

public class QueueConsumer {

    QueueFilterWrapper queueFilterWrapper;

    public QueueConsumer(QueueFilterWrapper queueFilterWrapper) {
        this.queueFilterWrapper = queueFilterWrapper;
    }

    public void start() {
        while (true) {
            if (queueFilterWrapper.hasElements()) {
                ChatMessage chatMessage = queueFilterWrapper.poll();
                System.out.println(chatMessage.getStreamingSite() + " " + chatMessage.getMessage());
            }
        }
    }
}
