package provider.filter;

import provider.model.ChatMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueFilterWrapper {

    ConcurrentLinkedQueue<ChatMessage> queue = new ConcurrentLinkedQueue<>();

    public QueueFilterWrapper() {
    }

    public boolean hasElements() {
        return !queue.isEmpty();
    }

    public ChatMessage poll() {
        return (ChatMessage) queue.poll();
    }

    public boolean offer(ChatMessage chatMessage) {
        // TODO do syntax filter here eg MANCUBUS X Y
        boolean validSyntax = chatMessage.getMessage().contains("a"); // palceholder

        // TODO Add check to ensure one user doesnt spam spawn moster
        boolean notToRecentMonsterSpawn = true;

        if (validSyntax && notToRecentMonsterSpawn) {
            return queue.offer(chatMessage);
        }
        return true;
    }

}
