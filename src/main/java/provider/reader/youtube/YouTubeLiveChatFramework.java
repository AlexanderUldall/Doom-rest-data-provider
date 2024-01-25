package provider.reader.youtube;

import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.IdType;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;
import lombok.extern.slf4j.Slf4j;
import provider.Enums.StreamingSite;
import provider.filter.QueueFilterWrapper;
import provider.reader.base.StreamReader;

import java.io.IOException;

@Slf4j
public class YouTubeLiveChatFramework extends StreamReader {
    private YouTubeLiveChat youTubeLiveChat;

    public YouTubeLiveChatFramework(String channelId, QueueFilterWrapper queueFilterWrapper) throws IOException {
        super(queueFilterWrapper, channelId);
        youTubeLiveChat = new YouTubeLiveChat(channelId, true, IdType.VIDEO);
    }

    public void start() {
        new Thread(() -> {
            try {
                while (true) {
                    int liveStatusCheckCycle = 0;

                    youTubeLiveChat.update();
                    for (ChatItem item : youTubeLiveChat.getChatItems()) {
//                        System.out.println("PRINTEDLINE "+ item.getMessage());
                        onChannelMessage(StreamingSite.YOUTUBE, item.getAuthorName(), item.getMessage());
                    }
                    liveStatusCheckCycle++;
                    if (liveStatusCheckCycle >= 10) {
                        // Calling this method frequently, cpu usage and network usage become higher because this method requests a http request.
                        if (!youTubeLiveChat.getBroadcastInfo().isLiveNow) {
                            break;
                        }
                        liveStatusCheckCycle = 0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("YouTubeLiveChatFramework started");
            } catch (Exception ex) {
                log.error("An error occurred in YouTubeLiveChatFramework");
            }
        }).start();
    }
}
