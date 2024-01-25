package provider.reader.twitch;

import lombok.extern.slf4j.Slf4j;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ThreadedListenerManager;
import provider.Enums.StreamingSite;
import provider.filter.QueueFilterWrapper;
import provider.reader.base.StreamReader;

@Slf4j
public class TwitchPircBotXReaderFramework extends StreamReader {

    private PircBotX bot;

    public TwitchPircBotXReaderFramework(String channelId, QueueFilterWrapper queueFilterWrapper) {
        super(queueFilterWrapper, channelId);
        try {

            Configuration configuration = new Configuration.Builder()
                    .setAutoNickChange(false)
                    .setOnJoinWhoEnabled(false)
                    .setCapEnabled(true)
                    .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                    .addServer("irc.chat.twitch.tv", 6667)
                    .setName("justinfan1234")
                    .addAutoJoinChannel("#" + channelId)
                    .setListenerManager(new ThreadedListenerManager())
                    .addListener(new TwitchChatListener())
                    .buildConfiguration();


            bot = new PircBotX(configuration);
        } catch (Exception e) {
            log.error("An error occurred when creating TwitchIRCReader");
        }
    }

    @Override
    public void start() {
        try {
            new Thread(() -> {
                try {
                    bot.startBot();
                } catch (Exception ex) {
                    log.error("TwitchIRCReader encountered an error");
                }
            }).start();

        } catch (Exception e) {
            log.error("An error occurred when creating TwitchIRCReader");
        }
        log.info("TwitchIRCReader started");
    }

    public class TwitchChatListener extends ListenerAdapter {
        @Override
        public void onMessage(MessageEvent event) {
            onChannelMessage(StreamingSite.TWITCH, event.getUser().getNick(), event.getMessage());
        }
    }
}
