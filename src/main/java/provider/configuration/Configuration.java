package provider.configuration;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
public class Configuration {

    private String kickChannelId;
    private String youtubeChannelId;
    private String youtubeFrameworkChannelId;
    private String twitchChannelId;
    private String twitchIRCChannelId;

    public Configuration() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";

        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));

        this.kickChannelId = appProps.getProperty("kickChannelId");
        this.youtubeChannelId = appProps.getProperty("youtubeChannelId");
        this.youtubeFrameworkChannelId = appProps.getProperty("youtubeFrameworkChannelId");
        this.twitchChannelId = appProps.getProperty("twitchChannelId");
        this.twitchIRCChannelId = appProps.getProperty("twitchIRCChannelId");

    }
}
