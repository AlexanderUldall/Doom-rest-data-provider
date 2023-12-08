package provider.filter;

import provider.model.ChatMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueFilterWrapper {

    private ConcurrentLinkedQueue<ChatMessage> queue = new ConcurrentLinkedQueue<>();
    private HashMap<String, LocalDateTime> latestPlayerMonsterSpawnMap = new HashMap();

    public QueueFilterWrapper() {
    }

    public boolean hasElements() {
        return !queue.isEmpty();
    }

    public ChatMessage poll() {
        return queue.poll();
    }

    public boolean offer(ChatMessage chatMessage) {
        // TODO do syntax filter here eg MANCUBUS X Y
        boolean validSyntax = chatMessage.getMessage().contains("a"); // placeholder

        LocalDateTime timeCreated = latestPlayerMonsterSpawnMap.get(chatMessage.getUserName());
        boolean allowMonsterSpawn = timeCreated == null || timeCreated.isAfter(LocalDateTime.now().plusMinutes(1));

        if (validSyntax && allowMonsterSpawn) {
            latestPlayerMonsterSpawnMap.put(chatMessage.getUserName(), LocalDateTime.now());
            return queue.offer(chatMessage);
        }
        return true;
    }
}
